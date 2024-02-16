package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityService extends GeneralService<City> {


    List<Organization> getCityOrganization(String cityId, String parentOrgan);

    List<City> getAllCityNameIdAndLocation(long provinceId);

    boolean postOne(City city);

    boolean updateCity(City city);

    boolean deleteOneCity(String cityId);

    List<City> getAllByProvinceId(String provinceId);

    City findOneById(String cityId);

    boolean ifProvinceExistsInCity(String provinceId);

    List<City> getCityList(List<String> cityIdList);

    Page<City> getAllByPaginationByProvinceId(String provinceId,String term, Pageable pageable, Integer totalElement);
}
