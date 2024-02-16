package org.sayar.net.General.service;

import org.sayar.net.General.enums.QueryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Meghdad Hajilo
 */

public interface GeneralService<T> {

    T findOne(T object, Class<T> className);

    T findOne(String fieldKey, String fieldValue, Class<T> className);

    T findOne(String fieldKey, String fieldValue, String[] fieldNames, Class<T> className);

//    T findOne(String fieldKey, String fieldValue, String[] fieldNames, QueryStatus queryStatus, Class<T> className);

    T findOne(String fieldKey, String fieldValue, String[] fieldNames, QueryStatus queryStatus, Class<T> className);

    T findOneById(String id, Class<T> className);

    T findOneById(String id, Class<T> entityClass, Object... includeFieldList);

//    T findOneById(String id, Class<T> entityClass, QueryStatus queryStatus, Object... includeFieldList);

    T findOneById(String id, Class<T> className, QueryStatus queryStatus, Object... fieldList);

    Integer getListSize(String fieldName, String id, Class<T> className);

//  List<T> findAll(Class<T> className);
//
//  List<T> findAll(String[] fieldNames, Class<T> className);
//
//  List<T> findAllPageable(Integer page, Integer pageSize, Class<T> className);
//
//  List<T> findAllPageable(Integer page, Integer pageSize, String[] fieldNames, Class<T> className);
//
//  List<T> findAll(String[] fieldNames, QueryStatus queryStatus, Class<T> className);

    T save(T object);

    Boolean updateFirst(T object);

//  WriteResult updateInnerObject(String id, String fieldName, Object innerObject, Class<T> className);
//
//  WriteResult pushToInnerObject(String id, String fieldName, Object innerObject, Class<T> className);

    Boolean exist(String id, Class<T> className);

    Boolean logicDelete(T object);

    Boolean logicDelete(Criteria criteria, Class<T> className);

    Boolean logicDeleteById(String id, Class<T> className);

    List<T> findAll(Class<T> className);

    List<T> findAll(String[] includeFieldNames, Class<T> className);

    List<T>     findAllNotLogicDeleted(Class<T> className, Object... includeFieldList);

    List<T> findListByIds(Set<String> idList, Class<T> className);

    List<T> findListByIds(Set<String> idList, Class<T> className, Object... includeFieldList);

    Page<T> findListPageable(Pageable pageable, Class<T> serviceInfoClass);

    List<T> findListByKey(String fieldName, Object value, Class<T> className);

    List<T> findListByKeyList(String fieldName, Collection<?> value, Class<T> className);

    Page<T> findListPageable(Pageable pageable, Number totalElements, Class<T> serviceInfoClass);

    String grtOrganId();

}
