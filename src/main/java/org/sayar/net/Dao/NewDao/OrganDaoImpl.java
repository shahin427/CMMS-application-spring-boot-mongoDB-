package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.DTO.OrganizationSearchDTO;
import org.sayar.net.Model.DTO.UserTypeNameAndIdDTO;
import org.sayar.net.Model.Filter.OrganizationFilter;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository("organDaoImpl")
@Transactional
public class OrganDaoImpl extends GeneralDaoImpl<Organization> implements OrganDao {
    public OrganDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Autowired
    private MongoOperations mongoOperations;


    @Override
    public List<User> getOrganUseres(String id) {
        return null;
    }

    @Override
    public boolean isUniqueOrganCode(String organCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("organCode").is(organCode));
        return mongoOperations.exists(query, Organization.class);
    }

    @Override
    public List<Organization> getParentChilderen(String parentOrganId) {

//        Organization organ=new Organization();
//        organ.setId(parentOrganId);
//
//        if(parentOrganId == 0){
//            return sessionFactory.getCurrentSession().createCriteria(Organization.class).
//                    add(Restrictions.isNull("parentOrganId")).list();
//        }else {
//
//            return sessionFactory.getCurrentSession().createCriteria(Organization.class).
//                    add(Restrictions.eq("parentOrganId",organ)).list();
//        }
        return null;

    }

    @Override
    public Organization getOrganByUserName(String userId) {
//        return (Organization) getCurrentSession().createCriteria(Organization.class,"org")
//
//                .createCriteria("organUsers")
//                .add(Restrictions.eq("id",userId))
//                .setProjection(Projections.projectionList()
//                        .add(Projections.property("org.id").as("id"))
//                        .add(Projections.property("org.name").as("name"))
//                )
//                .setResultTransformer(Transformers.aliasToBean(Organization.class))
//                .uniqueResult();
        return null;
    }

    @Override
    public List<Organization> getOrganNameAndId() {
        ProjectionOperation projectionOperation = project()
                .andExpression("id").as("id")
                .andExpression(Organization.ME.name.toString()).as("name");
        Aggregation aggregation = newAggregation(
                projectionOperation
        );
        AggregationResults<Organization> groupResults
                = this.aggregate(aggregation, Organization.class, Organization.class);

        return groupResults.getMappedResults();
    }

    @Override
    public List<Organization> getOrganNameAndIdByCityId(String cityId) {
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where(Organization.ME.cityId.toString()).is(cityId)
        );
        ProjectionOperation projectionOperation = project()
                .andExpression("id").as("id")
                .andExpression(Organization.ME.name.toString()).as("name");
        Aggregation aggregation = newAggregation(
                match(criteria),
                projectionOperation
        );
        return mongoOperations.aggregate(aggregation, Organization.class, Organization.class).getMappedResults();
    }

    @Override
    public List<Organization> getAllParentOrgan() {
        Query query = new Query();
        query.addCriteria(Criteria.where("parentOrganId").is("PARENT"));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, Organization.class);
    }

    @Override
    public List<OrganizationSearchDTO> getAllByFilterAndPagination(OrganizationFilter organizationFilter, Pageable pageable, Integer totalElement) {

        long skip = pageable.getPageSize() * pageable.getPageNumber();
        if (skip < 0) {
            skip = 0L;
        }
        ProjectionOperation projectionOperation = project()
                .andExpression("id").as("id")
                .andExpression("name").as("name")
                .andExpression("organCode").as("organCode")
                .andExpression("organLocation").as("organLocation")
                .andExpression("userTypeList").as("userTypeIdList")
                .andExpression("cityId").as("cityId")
                .andExpression("provinceId").as("provinceId")
                .andExpression("parentOrganId").as("parentOrganId");

        Criteria criteria;
        List<Criteria> criteriaList = new ArrayList<>();

        criteriaList.add(Criteria.where("deleted").ne(true));
        if (organizationFilter.getProvinceId() != null && !organizationFilter.getProvinceId().equals(""))
            criteriaList.add(Criteria.where(Organization.ME.provinceId.toString()).regex(organizationFilter.getProvinceId()));

        if (organizationFilter.getCityId() != null && !organizationFilter.getCityId().equals(""))
            criteriaList.add(Criteria.where(Organization.ME.cityId.toString()).regex(organizationFilter.getCityId()));

        if (organizationFilter.getParentOrganId() != null && !organizationFilter.getParentOrganId().equals(""))
            criteriaList.add(Criteria.where(Organization.ME.parentOrganId.toString()).regex(organizationFilter.getParentOrganId()));

        if (organizationFilter.getOrganizationName() != null && !organizationFilter.getOrganizationName().equals(""))
            criteriaList.add(Criteria.where(Organization.ME.name.toString()).regex(organizationFilter.getOrganizationName()));

        if (organizationFilter.getOrganizationCode() != null && !organizationFilter.getOrganizationCode().equals(""))
            criteriaList.add(Criteria.where(Organization.ME.organCode.toString()).regex(organizationFilter.getOrganizationCode()));

        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));

        Aggregation aggregation = newAggregation(
                Aggregation.match(criteria)
                , projectionOperation
                , Aggregation.skip(skip)
                , Aggregation.limit(pageable.getPageSize())
        );
        return this.aggregate(aggregation, Organization.class, OrganizationSearchDTO.class).getMappedResults();
    }

    @Override
    public Organization getOneOrganization(String orgId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(orgId));
        return mongoOperations.findOne(query, Organization.class);
    }

    @Override
    public long getAllByFilterCount(OrganizationFilter organizationFilter) {
        Criteria criteria;
        List<Criteria> criteriaList = new ArrayList<>();

        criteriaList.add(Criteria.where("deleted").ne(true));
        if (organizationFilter.getProvinceId() != null && !organizationFilter.getProvinceId().equals(""))
            criteriaList.add(Criteria.where(Organization.ME.provinceId.toString()).regex(organizationFilter.getProvinceId()));

        if (organizationFilter.getCityId() != null && !organizationFilter.getCityId().equals(""))
            criteriaList.add(Criteria.where(Organization.ME.cityId.toString()).regex(organizationFilter.getCityId()));

        if (organizationFilter.getParentOrganId() != null && !organizationFilter.getParentOrganId().equals(""))
            criteriaList.add(Criteria.where(Organization.ME.parentOrganId.toString()).regex(organizationFilter.getParentOrganId()));

        if (organizationFilter.getOrganizationName() != null && !organizationFilter.getOrganizationName().equals(""))
            criteriaList.add(Criteria.where(Organization.ME.name.toString()).regex(organizationFilter.getOrganizationName()));

        if (organizationFilter.getOrganizationCode() != null && !organizationFilter.getOrganizationCode().equals(""))
            criteriaList.add(Criteria.where(Organization.ME.organCode.toString()).regex(organizationFilter.getOrganizationCode()));

        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));

        Aggregation aggregation = newAggregation(
                Aggregation.match(criteria)
        );
        return this.aggregate(aggregation, Organization.class, OrganizationSearchDTO.class).getMappedResults().size();
    }

    @Override
    public boolean ifCityExistInOrganization(String cityId) {
        System.out.println("reading organ Api");
        Query query = new Query();
        query.addCriteria(Criteria.where("cityId").is(cityId));
        return mongoOperations.exists(query, Organization.class);
    }

    @Override
    public Organization getAllUserTypesOfThOrganization(String organizationId) {
        System.out.println("11111" + organizationId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(organizationId));
        query.fields()
                .include("userTypeList");
        return mongoOperations.findOne(query, Organization.class);
    }

    @Override
    public UpdateResult updateOrganization(Organization organization) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(organization.getId()));
        Update update = new Update();
        update.set("name", organization.getName());
        update.set("organCode", organization.getOrganCode());
        update.set("childrenOrgan", organization.getChildrenOrgan());
        update.set("parentOrganId", organization.getParentOrganId());
        update.set("organLocation", organization.getOrganLocation());
        update.set("organUsers", organization.getOrganUsers());
        update.set("cityId", organization.getCityId());
        update.set("provinceId", organization.getProvinceId());
        update.set("userTypeList", organization.getUserTypeList());
        return mongoOperations.updateFirst(query, update, Organization.class);
    }

    @Override
    public List<Organization> getOrganizationOfUser(List<String> organIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("Deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(organIdList));
        return mongoOperations.find(query, Organization.class);
    }

    @Override
    public List<Organization> getOrganizationOfUserType(List<String> organIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("Deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(organIdList));
        return mongoOperations.find(query, Organization.class);
    }

    @Override
    public List<Organization> getAllOrganizationsByUserTypeId(String userTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userTypeList").is(userTypeId));
        return mongoOperations.find(query, Organization.class);
    }

    @Override
    public List<Organization> getOrganizationListOfTheUser(List<String> organizationIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(organizationIdList));
        return mongoOperations.find(query, Organization.class);
    }

    @Override
    public boolean ifUserTypeExistsInOrgan(String userTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userTypeList").is(userTypeId));
        return mongoOperations.exists(query, Organization.class);
    }

    @Override
    public boolean ifProvinceExistsInOrganization(String provinceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("provinceId").is(provinceId));
        return mongoOperations.exists(query, Organization.class);
    }

    @Override
    public boolean ifCityExistsInOrganization(String cityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("cityId").is(cityId));
        return mongoOperations.exists(query, Organization.class);
    }

    @Override
    public List<Organization> getParentOrganizationList(List<String> parentOrganIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(parentOrganIdList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, Organization.class);
    }

    @Override
    public Organization getTheOrganizationUserType(String organizationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(organizationId));
        query.fields()
                .include("id")
                .include("userTypeList")
                .include("userTypeList");
        return mongoOperations.findOne(query, Organization.class);
    }

    @Override
    public Organization getAllUserTypesByTerm(String term, String organizationId) {
        Criteria criteria = new Criteria();
        ProjectionOperation projectionOperation = project()
                .andExpression("id").as("id")
                .andExpression("name").as("name");

        criteria.and("deleted").ne(true);
        if (term != null && !term.equals("")) {
            criteria.and("name").regex(term);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                projectionOperation
        );
        return mongoOperations.aggregate(aggregation, Organization.class, Organization.class).getUniqueMappedResult();
    }

    @Override
    public Organization createFirstOrganization(UserType userType) {
        Organization organization = new Organization();
        List<String> userTypeIdList = new ArrayList<>();
        userTypeIdList.add(userType.getId());
        organization.setUserTypeList(userTypeIdList);
        organization.setName("بهین سایار");
        return mongoOperations.save(organization);
    }

    @Override
    public boolean checkIfOrganHasChild(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("parentOrganId").is(id));
        return mongoOperations.exists(query, Organization.class);
    }

    @Override
    public boolean isOrganCodeUniqueInUpdate(String organCode, String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").ne(id));
        query.addCriteria(Criteria.where("organCode").is(organCode));
        return mongoOperations.exists(query, Organization.class);
    }

    @Override
    public List<Organization> getOrganizationName() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, Organization.class);
    }

    @Override
    public List<UserTypeNameAndIdDTO> getUserTypeListByOrganizationId(String orgId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(orgId);

        Aggregation aggregation = newAggregation(
                match(criteria),
                unwind("userTypeList"),
                project()
                        .and(ConvertOperators.ToObjectId.toObjectId("$userTypeList")).as("userTypeList"),
                lookup("userType", "userTypeList", "_id", "userType"),
                project()
                        .and("userType._id").as("userTypeId")
                        .and("userType.name").as("userTypeName")
        );
        return mongoOperations.aggregate(aggregation, Organization.class, UserTypeNameAndIdDTO.class).getMappedResults();
    }

    @Override
    public List<Organization> getOrganizationNameAndId(List<String> orgIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(orgIdList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, Organization.class);
    }

    @Override
    public Organization getRelatedOrganByOrganId(String organizationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(organizationId));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.findOne(query, Organization.class);
    }
}
