package org.sayar.net.General.db.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.DBRef;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.sayar.net.General.db.configuration.CustomMongoTemplate;
import org.sayar.net.General.enums.QueryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * This class is top layer for MongoDB query. I rewrite Spring data common
 * function for MongoDB because this way reduce query code size and it cause we
 * focus on program logic.
 *
 * @author meghdad
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Repository
public class BasicQuery<T> extends CustomMongoTemplate<T> {
    private Query query = new Query();
    private Update update = new Update();
    private Class<T> entityClass;

    public BasicQuery(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


    /**
     * @param updateResult
     * @return
     */
    private Boolean updateResultBoolean(UpdateResult updateResult) {
        return updateResult.getModifiedCount() > 0;
    }

    /**
     * @param deleteResult
     * @return
     */
    private Boolean deleteResultBoolean(DeleteResult deleteResult) {
        return deleteResult.getDeletedCount() > 0;
    }

    /**
     * Find method that take a Query to express the query and that return a single
     * entity.
     *
     * @return A single entity matched with query.
     */
    public T findOne() {
        return this.findOne(query, entityClass);
    }

    /**
     * @return
     */
    public T findOneAndModify() {
        return this.findAndModify(query, update, new FindAndModifyOptions().returnNew(false), entityClass);
    }

    /**
     * @return
     */
    public T modifyAndfindOne() {
        return this.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), entityClass);
    }

    /**
     * Find method that take a Query to express the query and that return a list of
     * entities.
     *
     * @return List of entities matched with query.
     */
    public List<T> find() {
        return this.find(query, entityClass);
    }

    /**
     * Find method that take a Query to express the query and that return a page of
     * entities.
     *
     * @param pageable : For pagination
     * @return Page of entities matched with query.
     */
    public Page<T> find(Pageable pageable) {
        Query queryWithPageable = this.query;
        queryWithPageable.with(pageable);
        return PageableExecutionUtils.getPage(this.find(queryWithPageable, entityClass), pageable,
                () -> this.count(query, entityClass));
    }

    /**
     * Find method that take a Query to express the query and that return a page of
     * entities.
     *
     * @param pageable:      For pagination
     * @param totalElements: When client has total page of query can send total page and
     *                       increase performance of query.
     * @return Page of entities matched with query.
     */
//    public Page<T> find(Pageable pageable, Number totalElements) {
//
//        Query queryWithPageable = this.query;
//        queryWithPageable.with(pageable);
//
//        if (totalElements != null && totalElements.intValue() > 0)
//            return PageableExecutionUtils.getPage(this.find(queryWithPageable, entityClass), pageable,
//                    () -> totalElements.longValue());
//
//        return PageableExecutionUtils.getPage(
//                this.find(queryWithPageable, entityClass),
//                pageable,
//                () -> this.count(query, entityClass));
//    }
    public Page<T> find(Pageable pageable, Number totalElements) {

        Query queryWithPageable = this.query;
        queryWithPageable.with(pageable);

        long count = 0;
        if (totalElements == null ||
                totalElements.intValue() < (
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize()
                                * pageable.getPageNumber())
                || totalElements.intValue() < 1)
            count = this.count(queryWithPageable, entityClass);

        return new PageImpl<>(
                this.find(queryWithPageable, entityClass),
                pageable,
                count);
    }

    /**
     * Are any entities matched with query.
     *
     * @return If matched return true otherwise return false.
     */
    public Boolean exists() {
        return this.exists(query, entityClass);
    }

    /**
     * Return number of entities are matched with query.
     *
     * @return Number of entities.
     */
    public Long count() {
        return this.count(query, entityClass);
    }

    /**
     * Update first entity is matched with query.
     *
     * @return If entity update was complete return true otherwise return false.
     */
    public UpdateResult updateFirst() {
        return this.updateFirst(query, update, entityClass);
    }

    /**
     * @return
     */
    public UpdateResult upsert() {
        return this.upsert(query, update, entityClass);
    }

    /**
     * Update all entities are matched with query.
     *
     * @return If entities update were complete return true otherwise return false.
     */
    public Boolean updateMulti() {
        return updateResultBoolean(this.updateMulti(query, update, entityClass));
    }

    /**
     * Delete entities are matched with query.
     *
     * @return If minimal one entity was deleted return true otherwise return false.
     */
    public DeleteResult delete() {
        return this.remove(query, entityClass);
    }

    public Boolean deleteResultBoolean() {
        return deleteResultBoolean(this.remove(query, entityClass));
    }

    /**
     * Find entities are matched with query and remove those.
     *
     * @return Entities are matched with query
     */
    public List<T> findAllAndRemove() {
        return this.findAllAndRemove(query, entityClass);
    }

    /**
     * Find first entity is matched with query and remove that.
     *
     * @return Entity is matched with query
     */
    public T findAndRemove() {
        return this.findAndRemove(query, entityClass);
    }

    /**
     * Find first entity is matched with query and update that.
     *
     * @return Entity is matched with query before update.
     */
    public T findAndModify() {
        return this.findAndModify(query, update, entityClass);
    }

    /**
     * Add pagination into query.
     *
     * @param pageable
     * @return
     */
    public BasicQuery<T> with(Pageable pageable) {
        this.query.with(pageable);
        return this;
    }

    /**
     * Add sorting into query.
     *
     * @param field:     field name to sorting.
     * @param direction: direction of sorting.
     * @return
     */
    public BasicQuery<T> withSort(Object field, Direction direction) {
        this.query.with(Sort.by(direction, field.toString()));
        return this;
    }

    /**
     * Is query.
     *
     * @param field: field name to query.
     * @param value: value of query.
     * @return
     */
    public BasicQuery<T> is(Object field, Object value) {
        query.addCriteria(Criteria.where(field.toString()).is(value));
        return this;
    }

    /**
     * Element match for list with is query.
     *
     * @param listFieldName
     * @param fieldToQuery
     * @param value
     * @return
     */
    public BasicQuery<T> isElemMatch(Object listFieldName, Object fieldToQuery, Object value) {
        query.addCriteria(
                Criteria.where(listFieldName.toString()).elemMatch(Criteria.where(fieldToQuery.toString()).is(value)));
        return this;
    }

    /**
     * Add exist query of field.
     *
     * @param field
     * @param value
     * @return
     */
    public BasicQuery<T> exists(Object field, Boolean value) {
        query.addCriteria(Criteria.where(field.toString()).exists(value));
        return this;
    }

    /**
     * @param field
     * @param value
     * @return
     */
    public BasicQuery<T> ne(Object field, Object value) {
        query.addCriteria(Criteria.where(field.toString()).ne(value));
        return this;
    }

    /**
     * @param field
     * @param value
     * @return
     */
    public BasicQuery<T> in(Object field, Collection<?> value) {
        query.addCriteria(Criteria.where(field.toString()).in(value));
        return this;
    }

    /**
     * Is query for DBRef field.
     *
     * @param field:      Field name
     * @param id:         Id of entity to finding.
     * @param entityName: Collection name of DBRef field.
     * @return
     */
    public BasicQuery<T> isDBRef(Object field, String id, String entityName) {
        query.addCriteria(Criteria.where(field.toString()).is(new DBRef(entityName, new ObjectId(id))));
        return this;
    }

    /**
     * In query for DBRef field.
     *
     * @param field:      Field name
     * @param idList:     Id list of entities to finding.
     * @param entityName: Collection name of DBRef field.
     * @return
     */
    public BasicQuery<T> inDBRef(Object field, List<String> idList, String entityName) {
        List<DBRef> dbRefList = new ArrayList<>();
        Optional.ofNullable(idList).ifPresent(l -> l.forEach(id -> {
            dbRefList.add(new DBRef(entityName, new ObjectId(id)));
        }));
        query.addCriteria(Criteria.where(field.toString()).in(dbRefList));
        return this;
    }

    /**
     * @param field
     * @param value
     * @return
     */
    public BasicQuery<T> set(Object field, Object value) {
        update.set(field.toString(), value);
        return this;
    }

    public BasicQuery<T> unset(Object field) {
        update.unset(field.toString());
        return this;
    }

    /**
     * @param field
     * @param inc
     * @return
     */
    public BasicQuery<T> inc(Object field, Number inc) {
        update.inc(field.toString(), inc);
        return this;
    }

    /**
     * @param field
     * @param value
     * @return
     */
    public BasicQuery<T> pull(Object field, Object value) {
        update.pull(field.toString(), value);
        return this;
    }

    /**
     * @param field
     * @param valueList
     * @return
     */
    public BasicQuery<T> pullAll(Object field, List<?> valueList) {
        update.pullAll(field.toString(), valueList.toArray());
        return this;
    }

    /**
     * @param field
     * @param idList
     * @param entityName
     * @return
     */
    public BasicQuery<T> pullAllDBRef(Object field, List<String> idList, String entityName) {
        List<DBRef> dbRefList = new ArrayList<>();
        Optional.ofNullable(idList).ifPresent(l -> l.forEach(id -> {
            dbRefList.add(new DBRef(entityName, new ObjectId(id)));
        }));
        update.pullAll(field.toString(), dbRefList.toArray());
        return this;
    }

    /**
     * @param field
     * @param value
     * @return
     */
    public BasicQuery<T> push(Object field, Object value) {
        update.push(field.toString(), value);
        return this;
    }

    /**
     * @param field
     * @param value
     * @param position
     * @return
     */
    public BasicQuery<T> pushToPosition(Object field, Object value, Integer position) {
        update.push(field.toString()).atPosition(position).value(value);
        return this;
    }

    /**
     * @param field
     * @param id
     * @param entityName
     * @return
     */
    public BasicQuery<T> pushDBRef(Object field, String id, String entityName) {
        update.push(field.toString(), new DBRef(entityName, new ObjectId(id)));
        return this;
    }

    /**
     * @param field
     * @param idList
     * @param entityName
     * @return
     */
    public BasicQuery<T> pushAllDBRef(Object field, List<String> idList, String entityName) {
        List<DBRef> dbRefList = new ArrayList<>();
        Optional.ofNullable(idList).ifPresent(l -> l.forEach(id -> {
            dbRefList.add(new DBRef(entityName, new ObjectId(id)));
        }));
        update.push(field.toString()).each(dbRefList);
        return this;
    }

    /**
     * @param field
     * @param values
     * @return
     */
    public BasicQuery<T> pushAll(Object field, List<?> values) {
        update.push(field.toString()).each(values);
        return this;
    }

    /**
     * @param field
     * @param value
     * @return
     */
    public BasicQuery<T> addToSet(Object field, Object value) {
        update.addToSet(field.toString(), value);
        return this;
    }

    /**
     * @param field
     * @param id
     * @param entityCollectionName
     * @return
     */
    public BasicQuery<T> addToSetDBRef(Object field, String id, String entityCollectionName) {
        update.addToSet(field.toString(), new DBRef(entityCollectionName, new ObjectId(id)));
        return this;
    }

    /**
     * Including fields time to getting.
     *
     * @param fieldList
     * @return
     */
    public BasicQuery<T> include(Object... fieldList) {
        Optional.ofNullable(Arrays.asList(fieldList)).ifPresent(l -> l.forEach(field -> {
            query.fields().include(field.toString());
        }));
        return this;
    }

    public BasicQuery<T> setFields(Object[] fieldNames, QueryStatus queryStatus) {
        switch (queryStatus) {
            case EXCLUDE:
                Optional.ofNullable(Arrays.asList(fieldNames)).ifPresent(l -> l.forEach(field -> {
                    query.fields().exclude(field.toString());
                }));
                break;
            case INCLUDE:
                Optional.ofNullable(Arrays.asList(fieldNames)).ifPresent(l -> l.forEach(field -> {
                    query.fields().include(field.toString());
                }));
                break;
        }
        return this;
    }

    /**
     * Excluding fields time to getting.
     *
     * @param fieldList
     * @return
     */
    public BasicQuery<T> exclude(Object... fieldList) {
        Optional.ofNullable(Arrays.asList(fieldList)).ifPresent(l -> l.forEach(field -> {
            query.fields().exclude(field.toString());
        }));
        return this;
    }

    /**
     * Add any query with criteria object into query.
     *
     * @param criteriaList
     * @return
     */
    public BasicQuery<T> addCriteria(Criteria... criteriaList) {
        Optional.ofNullable(Arrays.asList(criteriaList)).ifPresent(l -> l.forEach(criteria -> {
            query.addCriteria(criteria);
        }));
        return this;
    }

    public BasicQuery<T> andOperator(Criteria... criteriaList) {
        Criteria criteria = new Criteria();
        criteria.andOperator(criteriaList);
        query.addCriteria(criteria);
        return this;
    }

    public BasicQuery<T> orOperator(Criteria... criteriaList) {

        Criteria criteria = new Criteria();
        criteria.orOperator(criteriaList);
        query.addCriteria(criteria);
        return this;
    }


}
