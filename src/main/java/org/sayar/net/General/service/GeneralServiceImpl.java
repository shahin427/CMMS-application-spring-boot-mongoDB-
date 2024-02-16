package org.sayar.net.General.service;


import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.General.enums.QueryStatus;
import org.sayar.net.Security.TokenPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Meghdad Hajilo
 */

@Primary
@Service
public class GeneralServiceImpl<T> implements GeneralService<T> {


    @Autowired
    private  GeneralDao<T> generalDao;
    @Autowired
    private  TokenPrinciple tokenPrinciple;


    @Override
    public T findOne(T object, Class<T> className) {
        return generalDao.findOne(object, className);
    }

    @Override
    public T findOne(String fieldKey, String fieldValue, Class<T> className) {
        return generalDao.findOne(fieldKey, fieldValue, className);
    }

    @Override
    public T findOne(String fieldKey, String fieldValue, String[] includeFieldNames, Class<T> className) {
        return generalDao.findOne(fieldKey, fieldValue, className, includeFieldNames);
    }

    @Override
    public T findOne(String fieldKey, String fieldValue, String[] fieldNames, QueryStatus queryStatus, Class<T> className) {
        return generalDao.findOne(fieldKey, fieldValue, className, queryStatus, fieldNames);
    }

    @Override
    public T findOneById(String id, Class<T> className) {
        return generalDao.findOneById(id, className);
    }

    @Override
    public T findOneById(String id, Class<T> entityClass, Object... includeFieldList) {
        return generalDao.findOneById(id, entityClass, includeFieldList);
    }

    @Override
    public T findOneById(String id, Class<T> className, QueryStatus queryStatus, Object... fieldList) {
        return generalDao.findOneById(id, className, queryStatus, fieldList);
    }

    @Override
    public Integer getListSize(String fieldName, String id, Class<T> className) {
        return generalDao.getListSize(fieldName, id, className);
    }

//  @Override
//  public List<T> findAll(String[] fieldNames, Class<T> className) {
//    return generalDao.findAll(fieldNames, className);
//  }

//  @Override
//  public List<T> findAllPageable(Integer page, Integer pageSize, Class<T> className) {
//    return generalDao.findAllPageable(page, pageSize, className);
//  }

//  @Override
//  public List<T> findAllPageable(Integer page, Integer pageSize, String[] fieldNames, Class<T> className) {
//    return generalDao.findAllPageable(page, pageSize, fieldNames, className);
//  }

//  @Override
//  public List<T> findAll(String[] fieldNames, QueryStatus queryStatus, Class<T> className) {
//    return generalDao.findAll(fieldNames, queryStatus, className);
//  }

//  @Override
//  public List<T> findAll(Class<T> className) {
//    return generalDao.findAll(className);
//  }

    @Override
    public T save(T object) {
        generalDao.save(object);
        return object;
    }

    @Override
    public Boolean updateFirst(T object) {
        UpdateResult result = generalDao.updateFirst(object);
        return this.updateResultStatus(result);
    }

//  @Override
//  public WriteResult updateInnerObject(String id, String fieldName, Object innerObject, Class<T> className) {
//    return generalDao.updateInnerObject(id,fieldName,innerObject,className);
//  }

//  @Override
//  public WriteResult pushToInnerObject(String id, String fieldName, Object innerObject, Class<T> className) {
//    return generalDao.pushToInnerObject(id,fieldName,innerObject,className);
//  }

    @Override
    public Boolean exist(String id, Class<T> className) {
        return generalDao.exists(id, className);
    }

    @Override
    public Boolean logicDelete(T object) {
        DeleteResult result = generalDao.delete(object);
        return this.deleteResultStatus(result);
    }

    @Override
    public Boolean logicDelete(Criteria criteria, Class<T> className) {
        UpdateResult result = generalDao.logicDelete(criteria, className);
        return this.updateResultStatus(result);
    }

    @Override
    public Boolean logicDeleteById(String id, Class<T> className) {

        UpdateResult result = generalDao.logicDeleteById(id, className);
        return this.updateResultStatus(result);
    }

    @Override
    public List<T> findAll(Class<T> className) {
        return generalDao.findAll(className);
    }

    @Override
    public List<T> findAll(String[] includeFieldNames, Class<T> className) {
        return generalDao.findAll(className, includeFieldNames);
    }

    @Override
    public List<T> findAllNotLogicDeleted(Class<T> className, Object... includeFieldList) {
        return generalDao.findAllNotLogicDeleted(className, includeFieldList);
    }

    @Override
    public List<T> findListByIds(Set<String> idList, Class<T> className) {
        return generalDao.findListByIds(idList, className);
    }

    @Override
    public List<T> findListByIds(Set<String> idList, Class<T> className, Object... includeFieldList) {
        return generalDao.findListByIds(idList, className, includeFieldList);
    }

    @Override
    public Page<T> findListPageable(Pageable pageable, Class<T> className) {
        return generalDao.finListPageable(pageable, className);
    }

    @Override
    public List<T> findListByKey(String fieldName, Object value, Class<T> className) {
        return generalDao.findListByKey(fieldName, value, className);
    }

    @Override
    public List<T> findListByKeyList(String fieldName, Collection<?> value, Class<T> className) {
        return generalDao.findListByKeyList(fieldName, value, className);
    }

    @Override
    public Page<T> findListPageable(Pageable pageable, Number totalElements, Class<T> className) {
        if (totalElements == null)
            totalElements = 0;
        return generalDao.finListPageable(pageable, totalElements, className);
    }

    @Override
    public String grtOrganId() {
        return null;
    }

//    @Override
    public void addNotLogicalDeleteToQuery(Query query) {
        generalDao.addNotLogicalDeleteToQuery(query);
    }

    protected Boolean deleteResultStatus(DeleteResult result) {
        return (result.getDeletedCount() > 0 && result.wasAcknowledged());
    }

    protected Boolean updateResultStatus(UpdateResult result) {
        return result.getModifiedCount() > 0;
    }

//  @Override
//  public Boolean deleteAll(Class<T> className) {
//    DeleteResult result = generalDao.deleteAll(className);
//    return this.deleteResultStatus(result);
//  }

//  @Override
//  public Integer getListSize(String fieldName, String id, Class<T> className) {
//    return generalDao.getListSize(fieldName, id, className);
//  }

//  @Override
//  public long count(Query query) {
//    return generalDao.count(query, (Class<T>) getClass());
//  }

//  @Override
//  public long countOfAllItem(Class<T> className) {
//    return generalDao.countOfAllItem(className);
//  }

}
