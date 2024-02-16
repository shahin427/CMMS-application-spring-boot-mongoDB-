package org.sayar.net.Service.newService;


import org.sayar.net.Dao.NewDao.PropertyDTO;
import org.sayar.net.Dao.NewDao.PropertyDao;
import org.sayar.net.Dao.category.CategoryDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.Property;
import org.sayar.net.exceptionHandling.ApiExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyServiceImpl extends GeneralServiceImpl<Property> implements PropertyService {

    @Autowired
    private PropertyDao dao;
    @Autowired
    private CategoryDao categoryDao;

    @Override
    public boolean propertyKeyCheck(String key) {
        return dao.propertyKeyCheck(key);
    }

    @Override
    public Property saveProperty(Property property) {
        return dao.saveProperty(property);
    }

    @Override
    public Property updateProperty(Property property) {
        return dao.updateProperty(property);
    }

    @Override
    public boolean deleteProperty(String propertyId) {
        if (categoryDao.ifPropertyExistsInCategory(propertyId)) {
            System.out.println("Property_Exists_In_Category");
            throw new ApiExistsException();
        } else {
            System.out.println("Property_Not_Exists_In_Category");
            dao.logicDeleteById(propertyId, Property.class);
            return true;
        }
    }

    @Override
    public Page<PropertyDTO> getAllByPagination(String term, String propertyCategoryId, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllByPagination(term, propertyCategoryId, pageable, totalElement)
                , pageable
                , dao.getAllCount(term, propertyCategoryId)
        );
    }

    @Override
    public List<Property> getAllProperty() {
        return dao.getAllSimpleProperty();
    }

    @Override
    public boolean CheckIfPropertyKeyExist(String propertyKey) {
        return dao.CheckIfPropertyKeyExist(propertyKey);
    }

    @Override
    public boolean checkIfPropertyCategoryExistsInProperty(String id) {
        return dao.checkIfPropertyCategoryExistsInProperty(id);
    }

    @Override
    public List<Property> getPropertyByPropertyCategoryId(String propertyCategoryId) {
        return dao.getPropertyByPropertyCategoryId(propertyCategoryId);
    }

    @Override
    public Property getOneView(String propertyId) {
        return dao.getOneView(propertyId);
    }
}
