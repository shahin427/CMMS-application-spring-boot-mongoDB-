package org.sayar.net.Dao.category;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Dao.category.aggrigation.CountResults;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.Category;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Model.newModel.Enum.NewCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Repository("categoryDaoImpl")
@Transactional
public class CategoryDaoImpl extends GeneralDaoImpl<Category> implements CategoryDao {

    @Autowired
    private MongoOperations mongoOperations;

    public CategoryDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


    @Override
    public Category add(Category category) {
        try {
//            category.setOrganId(null);
//            getCurrentSession().save(category);
        } catch (Exception e) {
            return null;
        }
        return category;
    }

    @Override
    public Category getParentCategory() {
//        return (Category) getCurrentSession()
//                .createCriteria(Category.class)
//                .add(Restrictions.isNull("parentCategory"))
//                .uniqueResult();
        return null;
    }

    @Override
    public Category getACategoryParentById(String categoryId) {
        return null;
    }

    @Override
    public List<Category> getChildren(Category category) {
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("parentId").is(category.getParentId())
//                Criteria.where("organId").is(tokenPrinciple.getOrganCode())
        );
        Query query = new Query();
        query.addCriteria(criteria);
        return mongoOperations.find(query, Category.class);
    }

    @Override
    public List<Category> getParentChildren(String parentCatId) {
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("parentId").is(parentCatId)
//                Criteria.where("organId").is(tokenPrinciple.getOrganCode())
        );
        Query query = new Query();
        query.addCriteria(criteria);
        return mongoOperations.find(query, Category.class);
    }

    @Override
    public List<CountResults> getCategoryCount(String organId) {
        ProjectionOperation projection = project()
                .andExpression("title").as("title")
                .andExpression("category._id").as("categoryId")
                .andExpression("category.title").as("categoryTitle");

        Aggregation aggregation =
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where(Asset.ME.orgId.toString()).is(organId)),
                        Aggregation.lookup("category", "parentCategoryId", "_id", "category"),
                        Aggregation.unwind("category"),
                        projection,
                        Aggregation.group("categoryId").count().as("value")
                                .addToSet("categoryId").as("categoryId")
                                .addToSet("categoryTitle").as("categoryTitle")
                );


        AggregationResults<CountResults> results = this.aggregate(aggregation, "asset", CountResults.class);
        return results.getMappedResults();
    }


    @Override
    public boolean haveCategoryWithTitle(String title, String id) {
//        return getCurrentSession().createCriteria(Category.class)
//                .add(Restrictions.eq("id", id))
//                .add(Restrictions.eq("name", title))
//                .uniqueResult() != null;
        return false;
    }

    @Override
    public boolean detachCategoryAssets(String categoryId) {
//
        return true;
    }

    @Override
    public void incrementAssetCount(String categoryId, String organId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("organId").is(organId));
        query.addCriteria(Criteria.where("id").is(categoryId));
        Update update = new Update();
        update.inc("assetCount", 1);
        this.updateFirst(query, update, Category.class);

    }

    @Override
    public List<Category> getAllSortByAssetCount(String organId) {
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("parentId").exists(false),
                Criteria.where("parentId").is(null),
                Criteria.where("parentId").is("")
        );
        Query query = new Query();
        query.addCriteria(Criteria.where("organId").is(organId));
        query.addCriteria(criteria);

        query.with(Sort.by(Sort.Direction.DESC, "assetCount"));
        return this.find(query, Category.class);
    }

    @Override
    public List<Category> getAllParents(String organId, boolean isPart) {
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where(Category.MN.parentId.toString()).exists(false),
                Criteria.where(Category.MN.parentId.toString()).is(null),
                Criteria.where("deleted").ne(true)
        );
        if (isPart)
            criteria.and(Category.MN.isPart.toString()).is(true);
        else criteria.and(Category.MN.isPart.toString()).ne(true);
        Query query = new Query();
        query.addCriteria(criteria);
        query.addCriteria(Criteria.where(Category.MN.organId.toString()).is(organId));
        return this.find(query, Category.class);
    }

    @Override
    public boolean existenceByCategory(String categoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("parentId").is(categoryId));
        return this.exists(query, Category.class);
    }

    @Override
    public Category updateCategory(Category category) {
        return mongoOperations.save(category);
    }

    @Override
    public Category postCategory(Category category) {
        return mongoOperations.save(category);
    }

    @Override
    public boolean CheckIfCategoryNotDuplicated(Category category) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(category.getTitle())
                .andOperator(Criteria.where("deleted").ne(true)));
        return mongoOperations.exists(query, Category.class);
    }

    @Override
    public boolean ifPropertyExistsInCategory(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("properties").is(id));
        return mongoOperations.exists(query, Category.class);
    }

    @Override
    public List<Category> getAllListPageable(String term, String parentId, CategoryType categoryType, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("title").regex(term);
        } else {
            criteria.and("title").ne(null);
        }

        if (categoryType != null) {
            criteria.and("categoryType").is(categoryType);
        }

        if (parentId != null && !parentId.equals("")) {
            criteria.and("parentId").is(parentId);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("title").as("title")
                        .and("properties").as("properties")
                        .and("parentId").as("parentId")
                        .and("categoryType").as("categoryType"),

//                        .and(ConvertOperators.ToObjectId.toObjectId("$parentId")).as("parentId"),
//                lookup("category", "parentId", "_id", "parentCategory"),
//                unwind("parentCategory"),
//                project()
//                        .and("id").as("id")
//                        .and("title").as("title")
//                        .and("properties").as("properties")
//                        .and("parentCategory._id").as("parentCategoryId")
//                        .and("parentCategory.title").as("parentCategoryTitle"),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, Category.class, Category.class).getMappedResults();
    }

    @Override
    public List<Category> getAllIdAndTitle() {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("deleted").ne(true),
                        Criteria.where("parentId").is("ROOT")
                )
        );
        query.fields()
                .include("id")
                .include("title")
                .include("categoryType")
                .include("properties");
        return mongoOperations.find(query, Category.class);
    }

    @Override
    public Category getOneCategory(String categoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(categoryId)
                .andOperator(Criteria.where("deleted").ne(true)));
        return mongoOperations.findOne(query, Category.class);
    }

    @Override
    public List<Category> getChildrenByParentId(String parentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("ParentId").is(parentId));
        return mongoOperations.find(query, Category.class);
    }

    @Override
    public List<Category> getAllEnumType(CategoryType categoryType) {
        Query query = new Query();
        query.fields().include("categoryType");
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("categoryType").is(categoryType));
        return mongoOperations.find(query, Category.class);

    }

    @Override
    public Category getCategoryTypeByCategoryId(String parentCategoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(parentCategoryId));
        query.fields().include("categoryType");
        return mongoOperations.findOne(query, Category.class);
    }

    @Override
    public boolean checkIfTitleIsUnique(String title) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("title").is(title));
        return mongoOperations.exists(query, Category.class);
    }

    @Override
    public long countAllCategory(String term, String parentId, CategoryType categoryType) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("title").regex(term);
        } else {
            criteria.and("title").ne(null);
        }

        if (categoryType != null) {
            criteria.and("categoryType").is(categoryType);
        }

        if (parentId != null && !parentId.equals("")) {
            criteria.and("parentId").is(parentId);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Category.class, Category.class).getMappedResults().size();
    }

    @Override
    public List<Category> getParentCategories(List<String> parentIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(parentIdList));
        query.fields()
                .include("id")
                .include("title")
                .include("categoryType");
        return mongoOperations.find(query, Category.class);
    }

    @Override
    public boolean checkIfCategoryIsParentCategory(String categoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("parentId").is(categoryId));
        return mongoOperations.exists(query, Category.class);
    }

    @Override
    public Category getPropertyOfCategory(String categoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(categoryId));
        query.fields()
                .include("id")
                .include("properties");
        return mongoOperations.findOne(query, Category.class);
    }

    @Override
    public boolean checkIfCategoryHasSubCategory(String categoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("parentId").is(categoryId));
        return mongoOperations.exists(query, Category.class);
    }

    @Override
    public Category getCategoryType(String parentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(parentId));
        query.fields()
                .include("categoryType");
        return mongoOperations.findOne(query, Category.class);
    }

    @Override
    public boolean newSaveCategory(Category category) {
        Category newCategory = mongoOperations.save(category);
        if (newCategory.getId() != null)
            return true;
        else
            return false;
    }

    @Override
    public Category newGetOneCategory(String categoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(categoryId));
        query.fields()
                .include("id")
                .include("title")
                .include("description");
        return mongoOperations.findOne(query, Category.class);
    }

    @Override
    public List<Category> getAllNewCategoryWithPagination(NewCategory newCategory, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (newCategory.getTitle() != null && !newCategory.getTitle().equals("")) {
            criteria.and("title").regex(newCategory.getTitle());
        }
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("title").as("title"),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, Category.class, Category.class).getMappedResults();
    }

    @Override
    public long countNewCategory(NewCategory newCategory) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (newCategory.getTitle() != null && !newCategory.getTitle().equals("")) {
            criteria.and("title").regex(newCategory.getTitle());
        }
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Category.class, Category.class).getMappedResults().size();
    }

    @Override
    public boolean newUpdateCategory(Category category) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(category.getId()));
        Update update = new Update();
        update.set("title", category.getTitle());
        update.set("description", category.getDescription());
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, Category.class);
        if (updateResult.getModifiedCount() > 0)
            return true;
        else
            return false;
    }

    @Override
    public List<Category> getCategoryListByCategoryIdList(List<String> categoryIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(categoryIdList));
        query.fields()
                .include("id")
                .include("title");
        return mongoOperations.find(query, Category.class);
    }


}




