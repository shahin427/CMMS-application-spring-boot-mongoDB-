package org.sayar.net.Service;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Model.newModel.Location.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProvinceService extends GeneralService<Province> {

    List<City> getCities(String provinceId);

    Province getProvinceByOrganId(String id);

    List<Province> getAllCityNameIdAndLocation();

    boolean saveOne(Province province);

    Province updateProvince(Province province);

    List<Province> getAllProvince(String term);

    List<Province> getProvinceList(List<String> provinceIdList);

    Page<Province> getAllByPagination(String term, Pageable pageable, Integer totalElement);
}
