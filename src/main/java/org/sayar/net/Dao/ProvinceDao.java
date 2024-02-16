package org.sayar.net.Dao;


import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Model.newModel.Location.Province;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProvinceDao extends GeneralDao<Province> {

    List<City> getCities(String provinceId);

    Province getProvinceByOrganId(String id);

    List<Province> getAllCityNameIdAndLocation();

    String deleteOne(String provinceId);

    boolean saveOne(Province province);

    Province updateProvince(Province province);

    List<Province> getAllProvince(String term);

    List<Province> getProvinceList(List<String> provinceIdList);

    List<Province> getAllByPagination(String term, Pageable pageable, Integer totalElement);

    long getAllCount(String term);
}
