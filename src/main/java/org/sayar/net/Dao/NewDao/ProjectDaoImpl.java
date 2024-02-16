package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.DTO.ProjectDTO;
import org.sayar.net.Model.DTO.ProjectDTOCodeAndName;
import org.sayar.net.Model.newModel.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Repository("projectDaoImpl")
public class ProjectDaoImpl extends GeneralDaoImpl<Project> implements ProjectDao {
    @Autowired
    private MongoOperations mongoOperations;

    public ProjectDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


    @Override
    public boolean postOneProject(Project project) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("name").is(true));
        Project project1 = mongoOperations.findOne(query, Project.class);

        if (project1 == null) {
            System.out.println("نام تکراری نیست");
            mongoOperations.save(project);
            return true;
        } else {
            System.out.println("نام پ‍روژه تکراری است");
            return false;
        }
    }

    @Override
    public List<ProjectDTO> getAllFilterAndPagination(String term, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("deleted").ne(true)
                        .andOperator(Criteria.where("name").regex(term)))
                , skipOperation
                , limitOperation
        );
        return this.aggregate(aggregation, Project.class, ProjectDTO.class).getMappedResults();

    }

    @Override
    public long countProject(ProjectDTOCodeAndName projectDTOCodeAndName) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (projectDTOCodeAndName.getName() != null && !projectDTOCodeAndName.getName().equals(""))
            criteria.and("name").regex(projectDTOCodeAndName.getName());

        if (projectDTOCodeAndName.getCode() != null && !projectDTOCodeAndName.getCode().equals(""))
            criteria.and("code").regex(projectDTOCodeAndName.getCode());

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return this.aggregate(aggregation, Project.class, ProjectDTO.class).getMappedResults().size();
    }

    @Override
    public List<ProjectDTO> getAllProject(ProjectDTOCodeAndName projectDTOCodeAndName, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (projectDTOCodeAndName.getName() != null && !projectDTOCodeAndName.getName().equals(""))
            criteria.and("name").regex(projectDTOCodeAndName.getName());

        if (projectDTOCodeAndName.getCode() != null && !projectDTOCodeAndName.getCode().equals(""))
            criteria.and("code").regex(projectDTOCodeAndName.getCode());

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
                , skipOperation
                , limitOperation
                , project("code", "name", "requiredCompletionDate", "endDate", "startDate")
        );
        return this.aggregate(aggregation, Project.class, ProjectDTO.class).getMappedResults();
    }

    @Override
    public boolean checkProjectCode(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(code));
        query.addCriteria(Criteria.where("deleted").ne(true));
        Project project = mongoOperations.findOne(query, Project.class);
        return project != null;
    }

    @Override
    public Project getOne(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(projectId));
        query.fields()
                .exclude("documents")
                .exclude("users");
        return mongoOperations.findOne(query, Project.class);
    }

    @Override
    public List<Project> getAllProjectByProjectIdList(List<String> projectIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(projectIdList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, Project.class);
    }

    @Override
    public Project getAssociatedProjectToTheSchedule(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(projectId));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.findOne(query, Project.class);
    }

    @Override
    public UpdateResult updateProject(Project project, String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(projectId));
        Update update = new Update();
        update.set("name", project.getName());
        update.set("code", project.getCode());
        update.set("startDate", project.getStartDate());
        update.set("endDate", project.getEndDate());
        update.set("actualStartDate", project.getActualStartDate());
        update.set("actualEndDate", project.getActualEndDate());
        update.set("description", project.getDescription());
        update.set("requiredCompletionDate", project.getRequiredCompletionDate());
        return mongoOperations.updateFirst(query, update, Project.class);
    }

    @Override
    public boolean checkIfUserExistsInProject(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("users").is(userId));
        return mongoOperations.exists(query, Project.class);
    }

    @Override
    public Project getPersonnelOfProject(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(projectId));
        query.fields()
                .include("id")
                .include("assignedToPersonList");
        return mongoOperations.findOne(query, Project.class);
    }

    @Override
    public boolean addPersonnelToProject(String projectId, List<AssignedToPerson> assignedToPersonList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(projectId));
        Update update = new Update();
        update.set("assignedToPersonList", assignedToPersonList);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, Project.class);
        if (updateResult.getModifiedCount() > 0) {
            return true;
        } else
            return false;
    }

    @Override
    public boolean addGroupPersonnelToProject(String projectId, List<AssignedToGroup> assignedToGroupList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(projectId));
        Update update = new Update();
        update.set("assignedToGroupList", assignedToGroupList);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, Project.class);
        if (updateResult.getModifiedCount() > 0) {
            return true;
        } else
            return false;
    }

    @Override
    public Project getPersonnelGroupOfProject(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(projectId));
        query.fields()
                .include("id")
                .include("assignedToGroupList");
        return mongoOperations.findOne(query, Project.class);
    }

    @Override
    public List<Project> getAllProjectNameAndCode() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().include("code").include("name").include("id");
        return mongoOperations.find(query, Project.class);
    }

}
