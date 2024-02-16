package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityDao extends GeneralDao<City> {

    List<Organization> getCityOrganization(String cityId, String parentOrgan);

    List<City> getAllCityNameIdAndLocation(long provinceId);

    boolean existenceByProvinceId(String provinceId);

    boolean postOne(City city);

    UpdateResult updateCity(City city);

    List<City> getAllCityByProvinceId(String provinceId);

    City findOneById(String cityId);

    boolean ifProvinceExistsInCity(String provinceId);

    List<City> getCityList(List<String> cityIdList);

    List<City> getAllByPaginationByProvinceId(String provinceId, String term, Pageable pageable, Integer totalElement);

    long getAllCount(String provinceId, String term);
}
