package org.sayar.net.Service;


import org.sayar.net.Dao.NewDao.CityDao;
import org.sayar.net.Dao.ProvinceDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Model.newModel.Location.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("provinceServiceImpl")
public class ProvinceServiceImpl extends GeneralServiceImpl<Province> implements ProvinceService {

    private final ProvinceDao dao;
    private final CityDao cityDao;

    @Autowired
    public ProvinceServiceImpl(ProvinceDao dao, CityDao cityDao) {
        this.dao = dao;
        this.cityDao = cityDao;
    }

    @Override
    public List<City> getCities(String provinceId) {
        return dao.getCities(provinceId);
    }

    @Override
    public Province getProvinceByOrganId(String id) {
        return dao.getProvinceByOrganId(id);
    }

    @Override
    public List<Province> getAllCityNameIdAndLocation() {
        return dao.getAllCityNameIdAndLocation();
    }

    @Override
    public boolean saveOne(Province province) {
        return dao.saveOne(province);
    }

    @Override
    public Province updateProvince(Province province) {

        return dao.updateProvince(province);
    }

    @Override
    public List<Province> getAllProvince(String term) {
        return dao.getAllProvince(term);
    }

    @Override
    public List<Province> getProvinceList(List<String> provinceIdList) {
        return dao.getProvinceList(provinceIdList);
    }

    @Override
    public Page<Province> getAllByPagination(String term, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllByPagination(term, pageable, totalElement)
                , pageable
                , dao.getAllCount(term)
        );
    }
}
