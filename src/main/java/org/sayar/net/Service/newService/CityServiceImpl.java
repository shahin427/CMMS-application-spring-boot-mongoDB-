package org.sayar.net.Service.newService;

import org.sayar.net.Dao.NewDao.CityDao;
import org.sayar.net.Dao.NewDao.CompanyDao;
import org.sayar.net.Dao.NewDao.OrganDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.sayar.net.exceptionHandling.ApiOkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("cityServiceImpl")
public class CityServiceImpl extends GeneralServiceImpl<City> implements CityService {
    @Autowired
    private CityDao dao;
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private OrganDao organDao;

    @Override
    public List<Organization> getCityOrganization(String cityId, String parentOrgan) {
        return dao.getCityOrganization(cityId, parentOrgan);
    }

    @Override
    public List<City> getAllCityNameIdAndLocation(long provinceId) {
        return dao.getAllCityNameIdAndLocation(provinceId);
    }

    @Override
    public boolean postOne(City city) {
        return dao.postOne(city);
    }

    @Override
    public boolean updateCity(City city) {
        return this.updateResultStatus(dao.updateCity(city));
    }

    @Override
    public boolean deleteOneCity(String cityId) throws ApiOkException {

        if (organDao.ifCityExistInOrganization(cityId)) {
            System.out.println("Found_in_organization");
            throw new ApiOkException("این شهر در داخل سازمان های ثبت شده وجود دارد و قابل حذف نمی باشد");
        }
        if (companyDao.ifCityExistsInCompany(cityId)) {
            System.out.println("Found_In_Company");
            throw new ApiOkException("این شهر در داخل شرکت های ثبت شده وجود دارد و قابل حذف نمی باشد");
        } else {
            System.out.println("The_City_Not_Found_In_Company");
            dao.logicDeleteById(cityId, City.class);
            return true;
        }
    }

    @Override
    public List<City> getAllByProvinceId(String provinceId) {
        return dao.getAllCityByProvinceId(provinceId);
    }

    @Override
    public City findOneById(String cityId) {
        return dao.findOneById(cityId);
    }

    @Override
    public boolean ifProvinceExistsInCity(String provinceId) {
        return dao.ifProvinceExistsInCity(provinceId);
    }

    @Override
    public List<City> getCityList(List<String> cityIdList) {
        return dao.getCityList(cityIdList);
    }

    @Override
    public Page<City> getAllByPaginationByProvinceId(String provinceId, String term, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllByPaginationByProvinceId(provinceId, term, pageable, totalElement)
                , pageable
                , dao.getAllCount(provinceId, term)
        );
    }
}
