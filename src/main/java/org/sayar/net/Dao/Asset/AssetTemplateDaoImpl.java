package org.sayar.net.Dao.Asset;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.Asset.AssetTemplate;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.Asset.Property;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class AssetTemplateDaoImpl extends GeneralDaoImpl<AssetTemplate> implements AssetTemplateDao {

    @Autowired
    private MongoOperations mongoOperations;

    public AssetTemplateDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    public AssetTemplate getOneAssetTemplate(String assetTemplateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetTemplateId));
        query.fields()
                .include("id")
                .include("name")
                .include("description")
                .include("image")
                .include("parentCategoryId")
                .include("parentCategoryTitle")
                .include("subCategoryId")
                .include("subCategoryTitle")
                .include("parentCategoryType")
                .include("note");
        return mongoOperations.findOne(query, AssetTemplate.class);
    }

    public String postOneAssetTemplate(AssetTemplate assetTemplate) {
        AssetTemplate assetTemplate1 = mongoOperations.save(assetTemplate);
        return assetTemplate1.getId();
    }

    @Override
    public List<AssetTemplate> getAllAssetTemplate() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));

        return mongoOperations.find(query, AssetTemplate.class);
    }

    @Override
    public UpdateResult updateAssetTemplate(AssetTemplate assetTemplate) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("id").is(assetTemplate.getId()),
                        Criteria.where("deleted").ne(true)
                )
        );
        Update update = new Update();
        update.set("name", assetTemplate.getName());
        update.set("description", assetTemplate.getDescription());
        update.set("image", assetTemplate.getImage());
        update.set("assignedToPersonList", assetTemplate.getAssignedToPersonList());
        update.set("assignedToGroupList", assetTemplate.getAssignedToGroupList());
        update.set("note", assetTemplate.getNote());
        update.set("parentCategoryTitle", assetTemplate.getParentCategoryTitle());
        update.set("parentCategoryId", assetTemplate.getParentCategoryId());
        update.set("subCategoryTitle", assetTemplate.getSubCategoryTitle());
        update.set("subCategoryId", assetTemplate.getSubCategoryId());
        return mongoOperations.updateFirst(query, update, AssetTemplate.class);
    }

    @Override
    public List<AssetTemplate> getAllFilter(String term, String parentCategoryId, String subCategoryId, Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("name").regex(term);
        } else {
            criteria.and("name").ne(null);
        }

        if (parentCategoryId != null) {
            criteria.and("parentCategoryId").regex(parentCategoryId);
        }

        if (subCategoryId != null) {
            criteria.and("subCategoryId").regex(subCategoryId);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("subCategoryId").as("subCategoryId")
                        .and("subCategoryTitle").as("subCategoryTitle")
                        .and(ConvertOperators.ToObjectId.toObjectId("$parentCategoryId")).as("parentCategoryId"),
                lookup("category", "parentCategoryId", "_id", "category"),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("subCategoryId").as("subCategoryId")
                        .and("subCategoryTitle").as("subCategoryTitle")
                        .and("category._id").as("parentCategoryId")
                        .and("category.title").as("parentCategoryTitle"),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, AssetTemplate.class, AssetTemplate.class).getMappedResults();
    }

    @Override
    public List<AssetTemplate> getAllAssetTemplateName() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("id")
                .include("name")
                .include("properties");
        return mongoOperations.find(query, AssetTemplate.class);
    }

    @Override
    public AssetTemplate getCategoryTypeByAssetId(String assetTemplateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(assetTemplateId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.findOne(query, AssetTemplate.class);
    }

    @Override
    public List<AssetTemplate> getAllAssetTemplateByIdList(List<String> assetTemplateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(assetTemplateId));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, AssetTemplate.class);
    }

    @Override
    public boolean checkIfAssetTemplateNameIsUnique(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("name").is(name));
        return mongoOperations.exists(query, AssetTemplate.class);
    }

    @Override
    public boolean checkIfUserExistsAssetTemplate(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("properties").elemMatch(Criteria.where("id").is(userId)));
        return mongoOperations.exists(query, AssetTemplate.class);
    }

    @Override
    public boolean ifPropertyExistsInAssetTemplate(String propertyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("properties").is(propertyId));
        return mongoOperations.exists(query, AssetTemplate.class);
    }

    @Override
    public boolean ifCategoryExistsInAssetTemplate(String categoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("parentCategoryId").is(categoryId),
                Criteria.where("subCategoryId").is(categoryId)
        ));
        return mongoOperations.exists(query, AssetTemplate.class);
    }

    @Override
    public List<AssetTemplate> getAllAssetTemplatesWithPagination(String term, String parentCategory, String subCategory,
                                                                  Pageable pageable, Integer totalElement) {

        long skip = pageable.getPageSize() * pageable.getPageNumber();
        if (skip < 0) {
            skip = 0L;
        }

        ProjectionOperation po = project()
                .andExpression("_id").as("id")
                .andExpression("parentCategoryTitle").as("parentCategoryTitle")
                .andExpression("subCategoryTitle").as("subCategoryTitle")
                .andExpression("name").as("name");

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (term != null && !term.equals("")) {
            criteria.and("name").ne(true);
        }
        if (parentCategory != null && !parentCategory.equals("")) {
            criteria.and("parentCategoryTitle").regex(parentCategory);
        }
        if (subCategory != null && !subCategory.equals("")) {
            criteria.and("subCategoryTitle").regex(subCategory);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                po,
                Aggregation.skip(skip),
                Aggregation.limit(Pageable.unpaged().getPageSize())
        );
        return mongoOperations.aggregate(aggregation, AssetTemplate.class, AssetTemplate.class).getMappedResults();
    }

    @Override
    public long countAllAssetTemplate(String term, String parentCategory, String subCategory) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (term != null && !term.equals("")) {
            criteria.and("name").regex(term);
        }
        if (parentCategory != null && !parentCategory.equals("")) {
            criteria.and("parentCategoryId").regex(parentCategory);
        }
        if (subCategory != null && !subCategory.equals("")) {
            criteria.and("subCategoryId").regex(subCategory);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, AssetTemplate.class, AssetTemplate.class).getMappedResults().size();
    }

    @Override
    public long countAll(String term, String parentCategoryId, String subCategoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));

        if (term != null && !term.equals("")) {
            query.addCriteria(Criteria.where("name").regex(term));
        } else {
            query.addCriteria(Criteria.where("name").ne(null));
        }

        if (parentCategoryId != null) {
            query.addCriteria(Criteria.where("parentCategoryId").is(parentCategoryId));
        }

        if (subCategoryId != null) {
            query.addCriteria(Criteria.where("subCategoryId").is(subCategoryId));
        }
        return mongoOperations.count(query, AssetTemplate.class);
    }

    @Override
    public boolean addPersonTypePersonnelOfAssetTemplate(String assetTemplateId, List<AssignedToPerson> assignedToPersonList) {
        System.out.println(assetTemplateId);
        Print.print(assignedToPersonList);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetTemplateId));
        Update update = new Update();
        update.set("assignedToGroupList", assignedToPersonList);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, AssetTemplate.class);
        if (updateResult.getModifiedCount() > 0) {
            return true;
        } else
            return false;
    }

    @Override
    public AssetTemplate getPersonnelOfAssetTemplate(String assetTemplateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetTemplateId));
        query.fields()
                .include("assignedToPersonList");
        return mongoOperations.findOne(query, AssetTemplate.class);
    }

    @Override
    public boolean checkIfCategoryIsAssetTemplateParent(String categoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("parentCategoryId").is(categoryId),
                Criteria.where("subCategoryId").is(categoryId)
        ));
        return mongoOperations.exists(query, AssetTemplate.class);
    }

    @Override
    public boolean addGroupPersonnelToAssetTemplate(String assetTemplateId, List<AssignedToGroup> assignedToGroupList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetTemplateId));
        Update update = new Update();
        update.set("assignedToGroupList", assignedToGroupList);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, AssetTemplate.class);
        if (updateResult.getModifiedCount() > 0) {
            return true;
        } else
            return false;
    }

    @Override
    public AssetTemplate getGroupPersonnelOfAssetTemplate(String assetTemplateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetTemplateId));
        query.fields()
                .include("id")
                .include("assignedToGroupList");
        return mongoOperations.findOne(query, AssetTemplate.class);
    }

    @Override
    public AssetTemplate getPropertyOfAssetTemplate(String assetTemplateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetTemplateId));
        query.fields()
                .include("properties");
        return mongoOperations.findOne(query, AssetTemplate.class);
    }

    @Override
    public boolean updateAssetTemplateProperties(String assetTemplateId, List<Property> properties) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetTemplateId));
        Update update = new Update();
        update.set("properties", properties);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, AssetTemplate.class);
        return updateResult.getModifiedCount() > 0;
    }

    @Override
    public List<AssetTemplate> getAllAssetTemplateOfAsset(CategoryType categoryType) {
        Print.print("AAAA",categoryType);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("parentCategoryType").is(categoryType));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, AssetTemplate.class);
    }
}
