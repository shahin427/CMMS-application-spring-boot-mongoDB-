package org.sayar.net.Service.newService;


import org.sayar.net.Dao.NewDao.PropertyDTO;
import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.Asset.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PropertyService extends GeneralService<Property> {

    boolean propertyKeyCheck(String key);

    Property saveProperty(Property property);

    Property updateProperty(Property property);

    boolean deleteProperty(String propertyId);

    Page<PropertyDTO> getAllByPagination(String term, String propertyCategoryId, Pageable pageable, Integer totalElement);

    List<Property> getAllProperty();

    boolean CheckIfPropertyKeyExist(String propertyKey);

    boolean checkIfPropertyCategoryExistsInProperty(String id);

    List<Property> getPropertyByPropertyCategoryId(String propertyCategoryId);

    Property getOneView(String propertyId);
}
