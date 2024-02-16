package org.sayar.net.General.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.db.configuration.CustomMongoTemplate;
import org.sayar.net.General.domain.Count;
import org.sayar.net.General.domain.GeneralDomain;
import org.sayar.net.General.enums.FieldName;
import org.sayar.net.General.enums.QueryStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @author yaqub
 */

@Primary
@Service
public class GeneralDaoImpl<T> extends CustomMongoTemplate<T> implements GeneralDao<T> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public GeneralDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public T findOneById(String id, Class<T> entityClass) {

        return mongoQuery(entityClass)
                .isId(id)
                .findOne();
    }

    @Override
    public T findOneById(String id, Class<T> entityClass, Object... includeFieldNames) {
        return mongoQuery(entityClass)
                .isObjectId(id)
                .include(includeFieldNames)
                .findOne();
    }

    @Override
    public T findOneById(String id, Class<T> className, QueryStatus queryStatus, Object... fieldNames) {
        return mongoQuery(className)
                .isId(id)
                .setFields(fieldNames, queryStatus)
                .findOne();
    }


    @Override
    public List<T> findOneByCreatorId(String CreatorId, Class<T> entityClass, Object... includeFieldNames) {
        return mongoQuery(entityClass) //
                .isCreatorId(CreatorId) //
                .include(includeFieldNames) //
                .find();
    }

    @Override
    public List<T> findOneByCreatorIdLoginUser(Class<T> entityClass, Object... includeFieldNames) {
        return mongoQuery(entityClass) //
                .isCreatorIdLoginUser() //
                .include(includeFieldNames) //
                .find();
    }


    @Override
    public T findOne(T object, Class<T> className) {
        Query query = getIsQuery(object);
        return this.findOne(query, className);
    }

    @Override
    public T findOne(String fieldKey, String fieldValue, Class<T> className) {
        return mongoQuery(className)
                .is(fieldKey, fieldValue)
                .findOne();
    }

    @Override
    public T findOne(String fieldKey, String fieldValue, Class<T> className, Object... includeFieldNames) {
        return mongoQuery(className)
                .is(fieldKey, fieldValue)
                .include(includeFieldNames)
                .findOne();
    }

    @Override
    public T findOne(String fieldKey, String fieldValue, Class<T> className, QueryStatus queryStatus, Object... fieldNames) {
        Query query = this.fields(fieldNames, queryStatus);
        query.addCriteria(Criteria.where(fieldKey).is(fieldValue));
        return super.findOne(query, className);
    }


    @Override
    public List<T> findAll(Class<T> className, Object... includeFieldNames) {
        return mongoQuery(className)
                .include(includeFieldNames)
                .find();
    }

    @Override
    public List<T> findAll(Class<T> className, QueryStatus queryStatus, Object... fieldNames) {
        return super.find(fields(fieldNames, queryStatus), className);
    }


    @Override
    public Integer getListSize(String id, String fieldName, Class<T> className) {
        Aggregation aggregation = newAggregation(
                className,
                match(

                        Criteria.where("id").is(id)
                ),
                project()
                        .and(fieldName)
                        .size()
                        .as("count")
        );

        AggregationResults<Count> counts = this.aggregate(aggregation, className, Count.class);
        List<Count> result = counts.getMappedResults();
        return result.get(0).getCount();
    }

    @Override
    public UpdateResult updateFirst(T object) {
        Query query = this.getQueryForId(object);
        Update update = this.update(object);
        return this.updateFirst(query, update, object.getClass());
    }

    @Override
    public UpdateResult updateInnerObject(String id, String fieldName, Object innerObject, Class<T> className) {
        Query query = new Query();
        Update update = new Update();

        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("id").is(id),
                        Criteria.where(fieldName).elemMatch(
                                Criteria.where("id").is(getFieldValue("id", innerObject))
                        )
                )
        );

        update.set(fieldName + ".$", innerObject);

        return this.updateFirst(query, update, className);
    }

    @Override
    public UpdateResult pushToInnerObject(String id, String fieldName, Object innerObject, Class<T> className) {

        Query query = new Query();
        Update update = new Update();

        query.addCriteria(Criteria.where("id").is(id));

        update.push(fieldName, innerObject);
        return this.updateFirst(query, update, className);
    }

    @Override
    public UpdateResult logicDelete(Criteria criteria, Class<T> className) {
        return mongoQuery(className) //
                .setLogicDelete() //
                .addCriteria(criteria) //
                .updateFirst();
    }

    @Override
    public Page<T> finListPageable(Pageable pageable, Class<T> entityClass) {
        return mongoQuery(entityClass) //
                .noLogicDeleted()
                .find(pageable);
    }

    @Override
    public List<T> findListByKey(String fieldName, Object value, Class<T> className) {
        return mongoQuery(className)
                .is(fieldName, value)
                .find();
    }

    @Override
    public List<T> findListByKeyList(String fieldName, Collection<?> value, Class<T> className) {
        return mongoQuery(className)
                .in(fieldName, value)
                .find();
    }

    @Override
    public Page<T> finListPageable(Pageable pageable, Number totalElements, Class<T> className) {
        return mongoQuery(className) //
                .noLogicDeleted()
                .find(pageable, totalElements.longValue());
    }

    @Override
    public void applyDateToQuery(Query query, String fieldName, Long from, Long until) {
        if (from == null && until == null) return;
        if (until == null)
            query.addCriteria(Criteria.where(fieldName).gte(from));

        else if (from == null)
            query.addCriteria(Criteria.where(fieldName).lte(until));

        else query.addCriteria(Criteria.where(fieldName).gte(from).lte(until));

    }

    @Override
    public Criteria getDateCriteria(Criteria criteria, String fieldName, Long from, Long until) {


        if (from == null && until == null) return null;
        if (until == null)
            return criteria.and(fieldName).gte(from);

        else if (from == null)
            return criteria.and(fieldName).lte(until);

        else return criteria.and(fieldName).gte(from).lte(until);
    }

    @Override
    public boolean checkExistenceByOrgId(String fieldName, Object value, String organFieldName,String organIdValue, Class<T> className) {
        Query query = new Query();
        query.addCriteria(Criteria.where(fieldName).is(value));
        query.addCriteria(Criteria.where(fieldName).is(value));
        query.addCriteria(Criteria.where(organFieldName).is(organIdValue));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return this.exists(query,className);
    }

    @Override
    public void addNotLogicalDeleteToQuery(Query query) {

        query.addCriteria(Criteria.where(FieldName.deleted.toString()).ne(true));
    }

    @Override
    public void applyFilerToCriteria(Criteria  criteria, Object o, String[] exclusions) {
//        Print.print(o);
        ObjectMapper oMapper = new ObjectMapper();
        Map map = oMapper.convertValue(o, Map.class);
        List<String> ex = null;
        if (exclusions != null)
            ex = Arrays.asList(exclusions);

        for (Field field : o.getClass().getDeclaredFields()) {

            field.setAccessible(true);
            String fieldName = field.getName();
            if (ex != null && ex.contains(fieldName)) continue;
            if (map.get(fieldName) != null && !map.get(fieldName).equals(""))
            criteria.and(fieldName).is(map.get(fieldName));

        }
        criteria.and("deleted").ne(true);

    }
    @Override
    public void applyFieldsToUpdate(Update update, Object o, String[] exclusions) {
        ObjectMapper oMapper = new ObjectMapper();
        Map map = oMapper.convertValue(o, Map.class);
        List<String> ex = null;
        if (exclusions != null)
            ex = Arrays.asList(exclusions);

        for (Field field : o.getClass().getDeclaredFields()) {

            field.setAccessible(true);
            String fieldName = field.getName();
            if (ex != null && ex.contains(fieldName)) continue;
            if (map.get(fieldName) != null && !map.get(fieldName).equals(""))
                update.set(fieldName,map.get(fieldName));

        }


    }

    @Override
    public List<T> getByIdList(List<String> idList, Class<T> className) {

        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(idList));
        return this.find(query, className);
    }


    public UpdateResult logicDeleteById(String id, Class<T> entityClass) {
        return mongoQuery(entityClass)
                .isId(id)
                .setLogicDelete()
                .updateFirst();
    }

    public UpdateResult logicDeleteByIdAndCreatorId(String id, String creatorId, Class<T> entityClass) {
        return mongoQuery(entityClass)
                .isId(id)
                .isCreatorId(creatorId)
                .setLogicDelete()
                .updateFirst();
    }


    @Override
    public DeleteResult deleteById(String id, Class<T> entityClass) {
        return mongoQuery(entityClass) //
                .isId(id) //
                .delete();
    }

    @Override
    public DeleteResult delete(T object) {
        return super.remove(object);
    }

    @Override
    public DeleteResult deleteAll(Class<T> className) {
        return super.remove(new Query(), className);
    }


    @Override
    public Boolean exists(String id, Class<T> className) {
        return mongoQuery(className)
                .isId(id)
                .exists();
    }

    @Override
    public List<T> findAllNotLogicDeleted(Class<T> className, Object... includeFieldList) {
        return mongoQuery(className)
                .noLogicDeleted()
                .include(includeFieldList)
                .find();
    }

    @Override
    public List<T> findListByIds(Set<String> idList, Class<T> className) {
        return mongoQuery(className)
                .is(GeneralDomain.GFN.id, idList)
                .find();
    }

    @Override
    public List<T> findListByIds(Set<String> idList, Class<T> className, Object... includeFieldList) {
        return mongoQuery(className)
                .is(GeneralDomain.GFN.id, idList)
                .include()
                .find();
    }


    private Object getFieldValue(String fieldName, Object object) {

        Object fieldValue = null;
        if (object == null)
            return null;

        ObjectMapper oMapper = new ObjectMapper();
        // object -> Map
        Map<String, Object> map = oMapper.convertValue(object, Map.class);

        Query query = new Query();
        for (Field field : object.getClass().getDeclaredFields()) {

            if (field.getName() == fieldName) {
                return map.get(field.getName());
            }
        }
        return fieldValue;

    }

    private Query getQueryForId(T object) {
        if (object == null)
            return new Query();

        ObjectMapper oMapper = new ObjectMapper();
        // object -> Map
        Map<String, Object> map = oMapper.convertValue(object, Map.class);

        List<Field> fieldList = new ArrayList<>();
        fieldList.addAll(Arrays.asList(object.getClass().getSuperclass().getDeclaredFields()));
        fieldList.addAll(Arrays.asList(object.getClass().getDeclaredFields()));

        Query query = new Query();
        for (Field field : fieldList) {
            if (field.getName() == "id") {
                query.addCriteria(Criteria.where(field.getName()).is(map.get(field.getName())));
            }
        }
        return query;

    }

    private Update update(T object) {

        ObjectMapper oMapper = new ObjectMapper();

        // object -> Map
        Map<String, Object> map = oMapper.convertValue(object, Map.class);

        Update update = new Update();
        for (Field field : object.getClass().getDeclaredFields()) {

            if (field.getName() != "id" &&
                    map.get(field.getName()) != null &&
                    !(map.get(field.getName()).toString() == "{}") &&
                    !(map.get(field.getName()).toString() == "[]")
                    ) {
                update.set(field.getName(), map.get(field.getName()));
            }
        }
        return update;
    }

    private Query getIsQuery(T object) {
        if (object == null)
            return new Query();

        ObjectMapper oMapper = new ObjectMapper();
        // object -> Map
        Map<String, Object> map = oMapper.convertValue(object, Map.class);

        Query query = new Query();
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getName() != "credit" && map.get(field.getName()) != null
                    && !(map.get(field.getName()) instanceof List)) {
                query.addCriteria(Criteria.where(field.getName()).is(map.get(field.getName())));
            }
        }

        return query;
    }

    private Query fields(Object[] fieldNames, QueryStatus queryStatus) {
        Query query = new Query();
        if (queryStatus.equals(QueryStatus.INCLUDE)) {
            for (String fieldName : (String[]) fieldNames) {
                query.fields().include(fieldName);
            }
        } else {
            for (String fieldName : (String[]) fieldNames) {
                query.fields().exclude(fieldName);
            }
        }
        return query;
    }

    private Query fields(String[] fieldNames) {
        Query query = new Query();
        for (String fieldName : fieldNames) {
            query.fields().include(fieldName);
        }
        return query;
    }


}
