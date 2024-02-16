package org.sayar.net.Dao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Filter.UserFilter;
import org.sayar.net.Model.ResetPasswordToken;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.Notification;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.sayar.net.Security.Hash;
import org.sayar.net.Security.TokenPrinciple;
import org.sayar.net.Tools.Print;
import org.sayar.net.exceptionHandling.ApiRepetitiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
@Transactional
public class UserDaoImp extends GeneralDaoImpl<User> implements UserDao {


    private final TokenPrinciple tokenPrinciple;
    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    public UserDaoImp(MongoDatabaseFactory mongoDbFactory, TokenPrinciple tokenPrinciple) {
        super(mongoDbFactory);
        this.tokenPrinciple = tokenPrinciple;
    }

    @Override
    public boolean isUserSignUp(String userName) {

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("username").is(userName));
        return this.exists(query, User.class);

    }

    @Override
    public boolean haveUserNationalCode(User user) {
        Print.print("user", user);
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("nationalCode").is(user.getNationalCode()),
                Criteria.where("deleted").ne(true)
        );
        Query query = new Query();
        query = query.addCriteria(criteria);
        return mongoOperations.exists(query, User.class);
    }

    @Override
    public boolean update(User user) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("nationalCode").is(user.getNationalCode()),
                        Criteria.where("deleted").ne(true),
                        Criteria.where("id").ne(user.getId())
                )
        );
        User user1 = mongoOperations.findOne(query, User.class);
        if (user1 == null) {
            System.out.println("Not_exist");
            mongoOperations.save(user);
            return true;
        } else {
            System.out.println("Exist");
            throw new ApiRepetitiveException();
        }
    }

    @Override
    public List<User> filter(UserFilter filter) {
        Query query = new Query();
        super.applyDateToQuery(query, User.FN.creationTimestamp.toString(), filter.getFrom(), filter.getUntil());
        if (filter.getName() != null && !filter.getName().equals(""))
            query.addCriteria(Criteria.where(User.FN.name.toString()).regex(filter.getName()));

        if (filter.getFamily() != null && !filter.getFamily().equals(""))
            query.addCriteria(Criteria.where(User.FN.family.toString()).regex(filter.getFamily()));

        if (filter.getUsername() != null && !filter.getUsername().equals(""))
            query.addCriteria(Criteria.where(User.FN.username.toString()).regex(filter.getUsername()));

        if (filter.getUserTypeId() != null && !filter.getUserTypeId().equals(""))
            query.addCriteria(Criteria.where(User.FN.userTypeId.toString()).is(filter.getUserTypeId()));

        return this.find(query, User.class);
    }

    @Override
    public User getUserForToken(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("username").is(username));
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public List<UserDTO> getByIdList(List<String> idList) {

        ProjectionOperation projectionOperation =
                project()
                        .andExpression("id").as("id")
                        .andExpression("username").as("username")
                        .andExpression("family").as("family")
                        .andExpression("name").as("name")
                        .andExpression("userType").as("userType");
        Aggregation aggregation = newAggregation(
                match(Criteria.where("id").in(idList)),
                projectionOperation
        );
        AggregationResults<UserDTO> results = this.aggregate(
                aggregation, "user", UserDTO.class
        );
        return results.getMappedResults();
    }
    @Override
    public List<UserDTO> getByIdList(Set<String> idList) {

        Aggregation aggregation = Aggregation.newAggregation(
                match(Criteria.where("id").in(idList)),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
             );
        AggregationResults<UserDTO> results = this.aggregate(
                aggregation, User.class, UserDTO.class
        );
        return results.getMappedResults();
    }

    public List<UserFilterDTO> getAllByFilter(UserFilter userFilter, org.springframework.data.domain.Pageable pageable, Integer totalElement) {
        long skip = pageable.getPageSize() * pageable.getPageNumber();
        if (skip < 0) {
            skip = 0L;
        }

        ProjectionOperation po = project()
                .and("id").as("id")
                .and("family").as("family")
                .and("name").as("name")
                .and("username").as("username")
                .and("registerDate").as("registerDate")
                .and(ConvertOperators.ToObjectId.toObjectId("$userTypeId")).as("uTId");

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("userType")
                .localField("uTId")
                .foreignField("_id")
                .as("userType");

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (userFilter.getName() != null && !userFilter.getName().equals(""))
            criteria.and(User.FN.name.toString()).regex(userFilter.getName());

        if (userFilter.getFamily() != null && !userFilter.getFamily().equals(""))
            criteria.and(User.FN.family.toString()).regex(userFilter.getFamily());

        if (userFilter.getUsername() != null && !userFilter.getUsername().equals(""))
            criteria.and(User.FN.username.toString()).regex(userFilter.getUsername());

        if (userFilter.getUserTypeId() != null && !userFilter.getUserTypeId().equals(""))
            criteria.and(User.FN.userTypeId.toString()).is(userFilter.getUserTypeId());

        Aggregation aggregation = newAggregation(
                match(criteria)
                , Aggregation.sort(Sort.Direction.DESC, "registerDate")
                , Aggregation.skip(skip)
                , Aggregation.limit(pageable.getPageSize())
                , po
                , lookupOperation
                , unwind("userType", true)
                , project()
                        .and("id").as("id")
                        .and("family").as("family")
                        .and("name").as("name")
                        .and("username").as("username")
                        .and("registerDate").as("registerDate")
                        .and("userType.name").as("userTypeName")
                        .and("userType._id").as("userTypeId")
        );
        return this.aggregate(aggregation, "user", UserFilterDTO.class).getMappedResults();
    }

    public long getAllByFilterCount(UserFilter userFilter) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (userFilter.getName() != null && !userFilter.getName().equals(""))
            criteria.and(User.FN.name.toString()).regex(userFilter.getName());

        if (userFilter.getFamily() != null && !userFilter.getFamily().equals(""))
            criteria.and(User.FN.family.toString()).regex(userFilter.getFamily());

        if (userFilter.getUsername() != null && !userFilter.getUsername().equals(""))
            criteria.and(User.FN.username.toString()).regex(userFilter.getUsername());

        if (userFilter.getUserTypeId() != null && !userFilter.getUserTypeId().equals(""))
            criteria.and(User.FN.userTypeId.toString()).is(userFilter.getUserTypeId());

        Aggregation aggregation = newAggregation(
                match(criteria)
        );
        return this.aggregate(aggregation, "user", User.class).getMappedResults().size();
    }

    @Override
    public List<User> getAllIdAndTitle() {

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("id")
                .include("name")
                .include("family")
                .include("userName")
                .include("orgAndUserTypeList");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public User createMainInformationOfUser(UserMainInformationStringDTO userMainInformationStringDTO) {
        User user = new User();
        if (userMainInformationStringDTO.getName() != null)
            user.setName(userMainInformationStringDTO.getName());
        if (userMainInformationStringDTO.getFamily() != null)
            user.setFamily(userMainInformationStringDTO.getFamily());
        if (userMainInformationStringDTO.getImage() != null)
            user.setImage(userMainInformationStringDTO.getImage());
        if (userMainInformationStringDTO.getNationalCode() != null)
            user.setNationalCode(userMainInformationStringDTO.getNationalCode());
        if (userMainInformationStringDTO.getUsername() != null)
            user.setUsername(userMainInformationStringDTO.getUsername());
        if (userMainInformationStringDTO.getPassword() != null)
            user.setPassword(Hash.hash(userMainInformationStringDTO.getPassword()));
        if (userMainInformationStringDTO.getResetPasswordCode() != null)
            user.setResetPasswordCode(userMainInformationStringDTO.getResetPasswordCode());
        if (userMainInformationStringDTO.getActive() != null)
            user.setActive(userMainInformationStringDTO.getActive());
        if (userMainInformationStringDTO.getUserTypeId() != null)
            user.setUserTypeId(userMainInformationStringDTO.getUserTypeId());
        user.setRegisterDate(new Date());
        return mongoOperations.save(user);
    }

    @Override
    public UpdateResult updateSecondaryInformationOfUser(UserSecondaryInformationDTO userSecondaryInformationDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userSecondaryInformationDTO.getUserId()));
        Update update = new Update();
        update.set("birthDay", userSecondaryInformationDTO.getBirthDay());
        update.set("startWork", userSecondaryInformationDTO.getStartWork());
        update.set("fatherName", userSecondaryInformationDTO.getFatherName());
        update.set("userContact", userSecondaryInformationDTO.getUserContact());
        return mongoOperations.updateFirst(query, update, User.class);
    }

    @Override
    public UserChildUsersDTO updateChildUsersForReport(UserChildUsersDTO userChildUsersDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userChildUsersDTO.getUserId()));
        Update update = new Update();
        update.set("parentUserId", userChildUsersDTO.getParentUserId());
        mongoOperations.updateFirst(query, update, User.class);
        return userChildUsersDTO;
    }

    @Override
    public User getAllChildUsersIdOfUserByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        query.fields()
                .include("id")
                .include("parentUserId");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public List<User> getInformationOfChildUsers(List<String> childUserStringList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("childUsersId").in(childUserStringList));
        return mongoOperations.find(query, User.class);
    }

    @Override
    public User getUserChildrenByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));
//        query.fields().include("parentUserId");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public UserMainInformationStringDTO getMainInformationOfUser(String userId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(userId);
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
                        .and("userTypeId").as("userTypeId")
                        .and("nationalCode").as("nationalCode")
                        .and("image").as("image")
                        .and("username").as("username")
                        .and("active").as("active")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userTypeId")).as("userTypeId"),
                lookup("userType", "userTypeId", "_id", "userType"),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
                        .and("nationalCode").as("nationalCode")
                        .and("image").as("image")
                        .and("active").as("active")
                        .and("username").as("username")
                        .and("userTypeId").as("userTypeId")
                        .and("userType.name").as("userTypeName"));
        return mongoOperations.aggregate(aggregation, User.class, UserMainInformationStringDTO.class).getUniqueMappedResult();
    }

    @Override
    public UpdateResult updateMainInformation(UserMainInformationStringDTO userMainInformationDTO, String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        Update update = new Update();
        update.set("name", userMainInformationDTO.getName());
        update.set("family", userMainInformationDTO.getFamily());
        update.set("nationalCode", userMainInformationDTO.getNationalCode());
        update.set("image", userMainInformationDTO.getImage());
        update.set("username", userMainInformationDTO.getUsername());
        update.set("userTypeId", userMainInformationDTO.getUserTypeId());
        update.set("resetPasswordCode", userMainInformationDTO.getResetPasswordCode());
        update.set("active", userMainInformationDTO.getActive());
        return mongoOperations.updateFirst(query, update, User.class);
    }

    @Override
    public Page<User> getAllUsersByTermAndPagination(String term, Pageable pageable, Integer totalElement) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        if (term != null && !term.equals("")) {
            query.addCriteria(Criteria.where("name").regex(term));
            query.with(pageable);
            query.fields()
                    .include("username")
                    .include("family")
                    .include("name")
                    .include("userType");

            List<User> users = mongoOperations.find(query, User.class);
            long count = users.size();
            return new PageImpl<>(users, pageable, count);
        } else {
            query.with(pageable);
            query.fields()
                    .include("username")
                    .include("family")
                    .include("name")
                    .include("userType");
            List<User> userList = mongoOperations.find(query, User.class);
            long count = userList.size();
            return new PageImpl<>(userList, pageable, count);
        }
    }

    @Override
    public List<User> getChildrenUsersByChildrenIdList(List<String> childrenUserIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(childrenUserIdList));
        query.fields().include("id")
                .include("name")
                .include("username")
                .include("family")
                .include("userTypeId");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public User getSecondaryInformationOfUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public List<User> getAllRecipientUserInformation(List<String> stringList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(stringList));
        return mongoOperations.find(query, User.class);
    }

    @Override
    public List<Notification> getAllCreationDateOfNotification() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().include("creationDate");
        return mongoOperations.find(query, Notification.class);
    }

    @Override
    public User getSenderById(String senderUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(senderUserId));
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public List<User> getAllParentUsersByUserIdList(List<String> userIdListForSendingNotification) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userIdListForSendingNotification));
        return mongoOperations.find(query, User.class);
    }

    @Override
    public User getOneByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public List<User> getAllPersonnelOfAsset(List<String> users) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(users));
        return mongoOperations.find(query, User.class);
    }

    @Override
    public List<UserDTOWithUserTypeDTO> getAllUsersExceptOne(String userId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        Aggregation aggregation = newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("family").as("family")
                        .and("name").as("name")
                        .and("userTypeId").as("userTypeId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userTypeId")).as("userTypeId"),
                lookup("userType", "userTypeId", "_id", "userType"),
                project()
                        .and("id").as("id")
                        .and("family").as("family")
                        .and("name").as("name")
                        .and("userTypeId").as("userTypeId")
                        .and("userType.name").as("userTypeName")
        );
        return mongoOperations.aggregate(aggregation, User.class, UserDTOWithUserTypeDTO.class).getMappedResults();
    }

    @Override
    public List<User> getAllUsersOfMeteringList(List<String> userIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userIdList));
        query.fields().include("id")
                .include("username");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public User getUserOfTheMetering(String meteringId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(meteringId));
        query.fields()
                .include("id")
                .include("username");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public List<User> getAllSenderUsersOfNotifications(List<String> senderUserListId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(senderUserListId));
        return mongoOperations.find(query, User.class);
    }

    @Override
    public List<User> getAllUsersByUserTypeIdAndOrganId(String userTypeId, String organizationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("orgAndUserTypeList").elemMatch(
                Criteria.where("organizationId").is(organizationId)
                        .andOperator(Criteria.where("userTypeList").is(userTypeId))
        ));
        query.fields()
                .include("id")
                .include("name")
                .include("family");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public boolean checkIfUsernameIsRepetitive(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("username").is(username));
        return mongoOperations.exists(query, User.class);
    }

    @Override
    public List<User> getAllOrganizationOfTheUser(List<String> userIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userIdList));
        return mongoOperations.find(query, User.class);
    }

    @Override
    public User getOrganizationsByAUserId(String userId) {
        Print.print("userId22", userId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public UserDTOWithUserTypeDTO getParentUserByParentId(String parentUserId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(parentUserId);

        Aggregation aggregation = newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("family").as("family")
                        .and("name").as("name")
                        .and("userTypeId").as("userTypeId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userTypeId")).as("userTypeId"),
                lookup("userType", "userTypeId", "_id", "userType"),
                project()
                        .and("id").as("id")
                        .and("family").as("family")
                        .and("name").as("name")
                        .and("userTypeId").as("userTypeId")
                        .and("userType.name").as("userTypeName")
        );
        return mongoOperations.aggregate(aggregation, User.class, UserDTOWithUserTypeDTO.class).getUniqueMappedResult();
    }

    @Override
    public User getTheUserNationalCode(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        query.fields().include("nationalCode");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public boolean checkIfNationalCodeExistForUpdate(String code, String userId) {
        Print.print(userId);
        Print.print("code", code);
        Query query = new Query();
        query.addCriteria(Criteria.where("nationalCode").is(code).andOperator(Criteria.where("id").ne(userId)));
        return mongoOperations.exists(query, User.class);
    }

    @Override
    public User getTheUserUsername(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        query.fields().include("username");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public boolean checkIfUsernameExistForUpdate(String username, User userUsername) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("username").is(username),
                Criteria.where("username").is(userUsername)
        ));
        return mongoOperations.exists(query, User.class);
    }

    @Override
    public List<User> getChildUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("parentUserId").is(userId));
        return mongoOperations.find(query, User.class);
    }

    @Override
    public List<User> getUserOfWorkRequest(List<String> userIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userIdList));
        query.fields()
                .include("username")
                .include("id");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public List<User> getUsersOfActivity(List<String> userIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userIdList));
        query.fields()
                .include("name")
                .include("id")
                .include("family");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public User getRequesterDetails(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        query.fields()
                .include("name")
                .include("family")
                .include("userTypeId");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public User getParentUserOfConsideredUser(String parentUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(parentUserId));
        query.fields()
                .include("name")
                .include("family")
                .include("userTypeId");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public User findRelatedTechnicianOfTheFaultyAsset(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        query.fields()
                .include("username")
                .include("id");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public boolean ifUserTypeExistsInUser(String userTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userTypeId").is(userTypeId));
        return mongoOperations.exists(query, User.class);
    }

    @Override
    public boolean ifOrganisationExistsInUser(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("organizationIdList").is(id));
        return mongoOperations.exists(query, User.class);
    }

    @Override
    public User getUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public User getParentOfTheUser(String parentUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(parentUserId));
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public User getAncestorOfTheUser(String parentUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(parentUserId));
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public List<User> getUserListForSendingNotification(List<String> usersListForSendingNotification) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(usersListForSendingNotification));
        return mongoOperations.find(query, User.class);
    }

    @Override
    public User getUserParenByParentId(String parentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(parentId));
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public boolean checkIfUserIsParentOfAnotherUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("parentUserId").is(userId));
        return mongoOperations.exists(query, User.class);
    }

    @Override
    public List<SubUserDTO> getSubUsers(String userId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("parentUserId").is(userId);
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userTypeId")).as("userTypeId"),
                lookup("userType", "userTypeId", "_id", "userType"),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
                        .and("userType._id").as("userTypeId")
                        .and("userType.name").as("userTypeName")
        );
        return mongoOperations.aggregate(aggregation, User.class, SubUserDTO.class).getMappedResults();
    }

    @Override
    public boolean checkIfUsernameIsUnique(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("username").is(username));
        return mongoOperations.exists(query, User.class);
    }

    @Override
    public List<User> getAllUsersWithRelevantUserType() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("id")
                .include("name")
                .include("family")
                .include("userTypeId");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public UserDetailAndUserTypeDTO getRequesterUser(String userId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(userId);
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("family").as("family")
                        .and("name").as("name")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userTypeId")).as("userTypeId"),
                lookup("userType", "userTypeId", "_id", "userType"),
                project()
                        .and("id").as("id")
                        .and("family").as("family")
                        .and("name").as("name")
                        .and("userType.name").as("userTypeName")
        );
        return mongoOperations.aggregate(aggregation, User.class, UserDetailAndUserTypeDTO.class).getUniqueMappedResult();
    }

    @Override
    public boolean checkIfNationalCodeExist(String nationalCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("nationalCode").is(nationalCode));
        return mongoOperations.exists(query, User.class);
    }

    @Override
    public List<User> getChildUsers(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("parentUserId").is(id));
        query.fields()
                .include("id")
                .include("username")
                .include("name")
                .include("family");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public List<User> getUsersName(List<String> userIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userIdList));
        query.fields()
                .include("id")
                .include("username");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public User getNameAndFamilyNameOfUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        query.fields()
                .include("id")
                .include("name")
                .include("family");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public UserType getUserTypeTitle(String userTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userTypeId));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.findOne(query, UserType.class);
    }

    @Override
    public List<User> getTaskUserList(List<String> userIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userIdList));
        query.fields()
                .include("id")
                .include("name")
                .include("family")
                .include("userTypeId");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public User getWorkRequestUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        query.fields()
                .include("id")
                .include("name")
                .include("family")
                .include("userTypeId")
                .include("orgAndUserTypeList");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public User createPrimaryUser(Organization organization, UserType userType) {
        User user = new User();
        user.setPassword("123456");
        user.setUsername("admin");
        user.setRegisterDate(new Date());
        user.setStartWork(new Date());
//        user.setUserTypeId(userType.getId());
        List<String> organizationIdList = new ArrayList<>();
        organizationIdList.add(organization.getId());
//        user.setOrganizationIdList(organizationIdList);
        return mongoOperations.save(user);
    }

    @Override
    public boolean checkIfUserTypeOfOrganizationUsedInUser(String organizationId, String userTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("organizationIdList").is(organizationId));
        query.addCriteria(Criteria.where("userTypeId").is(userTypeId));
        return mongoOperations.exists(query, User.class);
    }

    @Override
    public SubUserDTO getSubUsersInUserGetAll(String userId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("parentUserId").is(userId);
        Aggregation aggregation = newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("family").as("family")
                        .and("name").as("name")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userTypeId")).as("userTypeId"),
                lookup("userType", "$userTypeId", "_id", "userType"),
                unwind("userType"),
                project().
                        and("id").as("id")
                        .and("family").as("family")
                        .and("name").as("name")
                        .and("userType.name").as("userTypeName")
        );
        return mongoOperations.aggregate(aggregation, User.class, SubUserDTO.class).getUniqueMappedResult();
    }

    @Override
    public List<PartUserDTO> getUsersOfPart(List<String> usersIdList) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").in(usersIdList);
        Aggregation aggregation = newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userTypeId")).as("userTypeId"),
                lookup("userType", "$userTypeId", "_id", "userType"),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
                        .and("userType.name").as("userTypeName")
        );
        return mongoOperations.aggregate(aggregation, User.class, PartUserDTO.class).getMappedResults();
    }

    @Override
    public List<OrgAndUserType> getOrgAndUserTypeListByUserId(String userId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(userId);
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("orgAndUserTypeList").as("orgAndUserType"),
                unwind("orgAndUserType", true),
                replaceRoot("orgAndUserType")
        );
        return mongoOperations.aggregate(aggregation, User.class, OrgAndUserType.class).getMappedResults();
    }

    @Override
    public User getUserForResetPassword(String usernameOrEmail) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("username").is(usernameOrEmail),
                Criteria.where("userContact.email").is(usernameOrEmail)
        ));
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public void addTokenToResetPassword(User user, String token) {
        long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date afterAddingOneMinutes = new Date(5 * (t + ONE_MINUTE_IN_MILLIS));
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setToken(token);
        resetPasswordToken.setTokenExpireDate(afterAddingOneMinutes);
        user.setResetPasswordToken(resetPasswordToken);
        Print.print("changedUser", user);
        mongoOperations.save(user);
    }

    @Override
    public User getUserMailBeforeChange(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        query.fields()
                .include("userContact");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public User getUserUserTypeIds(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        query.fields()
                .include("id")
                .include("userTypeId");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public User getActivityLevelUser(String assignedUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assignedUserId));
        query.fields()
                .include("id")
                .include("name")
                .include("family");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public List<User> getUsersOfActivityByUserTypeId(String userTypeId, String organizationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("orgAndUserTypeList").elemMatch(Criteria.where("userTypeList").is(userTypeId)
                .andOperator(Criteria.where("organizationId").is(organizationId))));
        query.fields()
                .include("id")
                .include("name")
                .include("family");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public List<User> getCandidateUsersOfActivityByUserIdList(List<String> candidateUserIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(candidateUserIdList));
        query.fields()
                .include("id")
                .include("name")
                .include("family");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public List<User> getNameAndFamilyNameOfUsers(List<String> userIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userIdList));
        query.fields()
                .include("id")
                .include("name")
                .include("family")
                .include("userTypeId");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public List<UserWithUserTypeNameDTO> getAllUsersOfAUserType(String userTypeId, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("userTypeId").is(userTypeId);

        Aggregation aggregation = newAggregation(
                match(criteria),
                skipOperation,
                limitOperation,
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userTypeId")).as("userTypeId"),
                lookup("userType", "userTypeId", "_id", "userType"),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
                        .and("userType._id").as("userTypeId")
                        .and("userType.name").as("userTypeName")
        );
        return mongoOperations.aggregate(aggregation, User.class, UserWithUserTypeNameDTO.class).getMappedResults();
    }

    @Override
    public long countAllUsersOfAUserType(String userTypeId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("userTypeId").is(userTypeId);
        Aggregation aggregation = newAggregation(
                match(criteria)
        );
        return mongoOperations.aggregate(aggregation, User.class, UserWithUserTypeNameDTO.class).getMappedResults().size();
    }

    @Override
    public List<User> getAssetTemplatePersonnel(List<String> userIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userIdList));
        query.fields()
                .include("id")
                .include("name")
                .include("family")
                .include("userTypeId");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public List<User> getAllUsersOfActivity(String userTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userTypeId").is(userTypeId));
        query.fields()
                .include("id")
                .include("name")
                .include("family")
                .include("userTypeId");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public User getUserReportTo(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        query.fields()
                .include("parentUserId");
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public List<UserWithUserTypeNameDTO> getPersonnelOfProject(List<String> users) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").in(users);

        Aggregation aggregation = newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
                        .and("username").as("username")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userTypeId")).as("userTypeId"),
                lookup("userType", "userTypeId", "_id", "userType"),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
                        .and("username").as("username")
                        .and("userType._id").as("userTypeId")
                        .and("userType.name").as("userTypeName")
        );
        return mongoOperations.aggregate(aggregation, User.class, UserWithUserTypeNameDTO.class).getMappedResults();
    }

    @Override
    public List<UserWithUserTypeNameDTO> getUserWithUserType() {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
                        .and("userTypeId").as("userTypeId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userTypeId")).as("userTypeId"),
                lookup("userType", "userTypeId", "_id", "userType"),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("family").as("family")
                        .and("userTypeId").as("userTypeId")
                        .and("userType.name").as("userTypeName")
        );
        return mongoOperations.aggregate(aggregation, User.class, UserWithUserTypeNameDTO.class).getMappedResults();
    }

    @Override
    public List<User> getUserLIstOfNewWorkOrder(List<String> userTypeIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("id")
                .include("name")
                .include("family")
                .include("userTypeId");
        return mongoOperations.find(query, User.class);
    }

    @Override
    public List<User> getTechnicians(List<String> userIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userIdList));
        query.fields()
                .include("name")
                .include("family")
                .include("id");
        return mongoOperations.find(query, User.class);
    }
}
