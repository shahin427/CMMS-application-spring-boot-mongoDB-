package org.sayar.net.General.db.util;

import org.bson.types.ObjectId;
import org.sayar.net.General.enums.FieldName;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Date;
import java.util.List;

/**
 * I write common query into this class for increasing usability of code.
 *
 * @param <T>
 * @author meghdad
 */
public class CommonQuery<T> extends BasicQuery<T> {
    /**
     * @param mongoDbFactory
     */
    public CommonQuery(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    /**
     * @param id
     * @return
     */
    public CommonQuery<T> isId(String id) {
        super.is(FieldName.id, id);
        return this;
    }

    public CommonQuery<T> isObjectId(String id) {
        super.is(FieldName.id, new ObjectId(id));
        return this;
    }

    public CommonQuery<T> isObjectId(Object fieldName, String id) {
        super.is(fieldName.toString(), new ObjectId(id));
        return this;
    }

    /**
     * @param idList
     * @return
     */
    public CommonQuery<T> inId(List<String> idList) {
        super.in(FieldName.id, idList);
        return this;
    }

    /**
     * @param parentId
     * @return
     */
    public CommonQuery<T> isParentId(String parentId) {
        super.is(FieldName.parentId, parentId);
        return this;
    }

    /**
     * @param parentIdList
     * @return
     */
    public CommonQuery<T> inParentId(List<String> parentIdList) {
        super.in(FieldName.parentId, parentIdList);
        return this;
    }

    /**
     * @param creatorId
     * @return
     */
    public CommonQuery<T> isCreatorId(String creatorId) {
        super.is(FieldName.creatorId, creatorId);
        return this;
    }

    public CommonQuery<T> isCreatorIdLoginUser() {
        super.is(FieldName.creatorId, SecurityUtils.getLoggedInUserId());
        return this;
    }

    /**
     * @return
     */
    public CommonQuery<T> isOwnerLoginUser() {
        super.is(FieldName.owner, SecurityUtils.getLoggedInUserId());
        return this;
    }

    /**
     * @return
     */
    public CommonQuery<T> setLogicDelete() {
        super.set(FieldName.deleted, true);
        super.set(FieldName.deleteDate, new Date());
        super.set(FieldName.deleteBy, SecurityUtils.getLoggedInUserId());
        return this;
    }

    public CommonQuery<T> setLogicDelete(Criteria criteria) {
        super.addCriteria(criteria);
        super.set(FieldName.deleted, true);
        super.set(FieldName.deleteDate, new Date());
        super.set(FieldName.deleteBy, SecurityUtils.getLoggedInUserId());
        return this;
    }


    /**
     * @return
     */
    public CommonQuery<T> noLogicDeleted() {
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where(FieldName.deleted.toString()).exists(false),
                Criteria.where(FieldName.deleted.toString()).is(false)
        );

        super.ne(FieldName.deleted, true);
        return this;
    }

    /**
     * @param deleteBy
     * @return
     */
    public CommonQuery<T> setLogicDelete(String deleteBy) {
        super.set(FieldName.deleted, true);
        super.set(FieldName.deleteDate, new Date());
        super.set(FieldName.deleteBy, deleteBy);
        return this;
    }

    /**
     * @return
     */
    public CommonQuery<T> excludeId() {
        super.exclude(FieldName.id);
        return this;
    }

    /**
     * @return
     */
    public CommonQuery<T> includeId() {
        super.include(FieldName.id);
        return this;
    }

    /**
     * @return
     */
    public CommonQuery<T> includeCreatorId() {
        super.include(FieldName.creatorId);
        return this;
    }

    /**
     * @return
     */
    public CommonQuery<T> includeParentId() {
        super.include(FieldName.parentId);
        return this;
    }

    /**
     * @param key
     * @return
     */
    public CommonQuery<T> isKey(String key) {
        super.is(FieldName.key, key);
        return this;
    }


    /**
     * @param parentId
     * @return
     */
    public CommonQuery<T> setParentId(String parentId) {
        super.set(FieldName.parentId, parentId);
        return this;
    }

    /**
     * @param keyList
     * @return
     */
    public CommonQuery<T> inKey(List<String> keyList) {
        super.in(FieldName.key, keyList);
        return this;
    }


//    /**
//     * @return
//     */
//
//    public BulkQuery<T> bulkOrdered() {
//        return new BulkQuery<T>( super.getMongoDataBaseFactory(), super.getEntityClass(), BulkOperations.BulkMode.ORDERED);
//    }
//
//
//    /**
//     * @return
//     */
//
//    public BulkQuery<T> bulkUnOrdered() {
//        return new BulkQuery<T>( super.getMongoDataBaseFactory(), super.getEntityClass(), BulkOperations.BulkMode.UNORDERED);
    }


//}
