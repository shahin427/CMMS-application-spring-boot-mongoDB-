package org.sayar.net.Dao.NewDao;

import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.Asset.Property;
import org.sayar.net.Tools.Print;
import org.sayar.net.exceptionHandling.ApiRepetitiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Repository("propertyDaoImpl")
@Transactional
public class PropertyDaoImpl extends GeneralDaoImpl<Property> implements PropertyDao {
    @Autowired
    private MongoOperations mongoOperations;

    public PropertyDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public boolean propertyKeyCheck(String key) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Property.ME.key.toString()).is(key));
        this.addNotLogicalDeleteToQuery(query);
        return this.exists(query, Property.class);
    }

    @Override
    public Property saveProperty(Property property) throws ApiRepetitiveException {
        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(property.getKey()).andOperator(Criteria.where("deleted").ne(true)));
        Property property1 = mongoOperations.findOne(query, Property.class);
        if (property1 == null) {
            System.out.println("Not-exist");
            mongoOperations.save(property);
            return property;
        } else {
            System.out.println("Exist");
            throw new ApiRepetitiveException();
        }
    }

    public Property updateProperty(Property property) {
        Print.print(property);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(property.getId()));
        Print.print("BBB", query);
        Update update = new Update();
        update.set("key", property.getKey());
        update.set("repository", property.getRepository());
        update.set("type", property.getType());
        update.set("data", property.getData());
        update.set("propertyCategoryId", property.getPropertyCategoryId());
        FindAndModifyOptions options = FindAndModifyOptions.options();
        options.returnNew(true);
        return mongoOperations.findAndModify(query, update, options, Property.class);
    }

    @Override
    public List<PropertyDTO> getAllByPagination(String term, String propertyCategoryId, Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        ProjectionOperation projectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$propertyCategoryId"))).as("propertyCategoryObjId")
                .and("id").as("id")
                .and("key").as("key")
                .and("repository").as("repository")
                .and("type").as("type")
                .and("data").as("data");

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("propertyCategory")
                .localField("propertyCategoryObjId")
                .foreignField("_id")
                .as("propertyCategory");

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("key").regex(term);
        }
        if (propertyCategoryId != null && !propertyCategoryId.equals("")) {
            criteria.and("propertyCategoryId").is(propertyCategoryId);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                projectionOperation,
                lookupOperation,
                skipOperation,
                limitOperation,
                project("key", "type", "propertyCategory")
        );

        return this.aggregate(aggregation, Property.class, PropertyDTO.class).getMappedResults();
    }

    @Override
    public List<Property> getAllProperty(Pageable pageable, Integer totalElement) {
        System.out.println("3333333");
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("deleted").ne(true))
                , skipOperation
        );
        Print.print(mongoOperations.aggregate(agg, Property.class, Property.class).getMappedResults());
        return mongoOperations.aggregate(agg, Property.class, Property.class).getMappedResults();
    }

    public long getAllCount(String term, String propertyCategoryId) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("key").regex(term);
        }
        if (propertyCategoryId != null && !propertyCategoryId.equals("")) {
            criteria.and("propertyCategoryId").is(propertyCategoryId);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return this.aggregate(aggregation, Property.class, PropertyDTO.class).getMappedResults().size();
    }

    @Override
    public boolean CheckIfPropertyKeyExist(String propertyKey) {

        try {
            Criteria criteria = new Criteria();
            criteria.andOperator(
                    Criteria.where("key").is(propertyKey),
                    Criteria.where("deleted").ne(true)
            );
            Query query = new Query();
            query = query.addCriteria(criteria);
            Property property = mongoOperations.findOne(query, Property.class);

            if (property == null) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean checkIfPropertyCategoryExistsInProperty(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("propertyCategoryId").is(id));
        return mongoOperations.exists(query, Property.class);
    }

    @Override
    public List<Property> getAllSimpleProperty() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, Property.class);
    }

    @Override
    public List<Property> getPropertyByPropertyCategoryId(String propertyCategoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("propertyCategoryId").is(propertyCategoryId));
        return mongoOperations.find(query, Property.class);
    }

    @Override
    public Property getOneView(String propertyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(propertyId));
        return mongoOperations.findOne(query, Property.class);
    }
}
