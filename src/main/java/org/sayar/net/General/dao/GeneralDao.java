package org.sayar.net.General.dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.enums.QueryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author meghdad
 */
public interface GeneralDao<T> extends MongoOperations {

    T findOneById(String id, Class<T> entityClass);

    T findOneById(String id, Class<T> entityClass, Object... includeFieldNames);

    T findOneById(String id, Class<T> className, QueryStatus queryStatus, Object... fieldNames);


    List<T> findOneByCreatorId(String CreatorId, Class<T> entityClass, Object... includeFieldNames);

    List<T> findOneByCreatorIdLoginUser(Class<T> entityClass, Object... includeFieldNames);


    T findOne(T object, Class<T> className);

    T findOne(String fieldKey, String fieldValue, Class<T> className);

    T findOne(String fieldKey, String fieldValue, Class<T> className, Object... includeFieldNames);

    T findOne(String fieldKey, String fieldValue, Class<T> className, QueryStatus queryStatus, Object... fieldNames);

    List<T> findAll(Class<T> className, Object... includeFieldNames);

    List<T> findAll(Class<T> className, QueryStatus queryStatus, Object... fieldNames);


    Integer getListSize(String id, String fieldName, Class<T> className);


    UpdateResult updateFirst(T object);

    UpdateResult updateInnerObject(String id, String fieldName, Object innerObject, Class<T> className);

    UpdateResult pushToInnerObject(String id, String fieldName, Object innerObject, Class<T> className);


    UpdateResult logicDeleteById(String id, Class<T> entityClass);

    UpdateResult logicDeleteByIdAndCreatorId(String id, String creatorId, Class<T> entityClass);

    DeleteResult delete(T object);

    DeleteResult deleteById(String id, Class<T> className);

    DeleteResult deleteAll(Class<T> className);


    Boolean exists(String id, Class<T> className);

    List<T> findAllNotLogicDeleted(Class<T> className, Object... includeFieldList);

    List<T> findListByIds(Set<String> idList, Class<T> className);

    List<T> findListByIds(Set<String> idList, Class<T> className, Object... includeFieldList);

    UpdateResult logicDelete(Criteria criteria, Class<T> className);

    Page<T> finListPageable(Pageable pageable, Class<T> serviceInfoClass);

    List<T> findListByKey(String fieldName, Object value, Class<T> className);

    List<T> findListByKeyList(String fieldName, Collection<?> value, Class<T> className);

    Page<T> finListPageable(Pageable pageable, Number totalElements, Class<T> className);
    void applyDateToQuery(Query query, String fieldName, Long from, Long until);

    void addNotLogicalDeleteToQuery(Query query);
    void applyFilerToCriteria(Criteria  criteria, Object o, String[] exclusions);

    void applyFieldsToUpdate(Update update, Object o, String[] exclusions);

    List<T> getByIdList(List<String> idList, Class<T> className);
    public Criteria getDateCriteria(Criteria criteria,String fieldName, Long from, Long until);

    boolean checkExistenceByOrgId(String fieldName, Object value, String organFieldName,String organIdValue, Class<T> className);

}
