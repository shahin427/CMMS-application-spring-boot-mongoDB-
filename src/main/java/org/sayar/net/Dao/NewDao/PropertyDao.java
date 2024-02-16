package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.Asset.Property;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PropertyDao extends GeneralDao<Property> {


    boolean propertyKeyCheck(String key);

    Property saveProperty(Property property);

    Property updateProperty(Property property);

    List<PropertyDTO> getAllByPagination(String term, String propertyCategoryId, Pageable pageable, Integer totalElement);

    List<Property> getAllProperty(Pageable pageable, Integer totalElement);

    long getAllCount(String term, String propertyCategoryId);

    boolean CheckIfPropertyKeyExist(String propertyKey);

    boolean checkIfPropertyCategoryExistsInProperty(String id);

    List<Property> getAllSimpleProperty();

    List<Property> getPropertyByPropertyCategoryId(String propertyCategoryId);

    Property getOneView(String propertyId);
}
