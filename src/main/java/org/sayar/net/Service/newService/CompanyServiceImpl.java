package org.sayar.net.Service.newService;

import org.sayar.net.Dao.NewDao.CompanyDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.CompanyDTO;
import org.sayar.net.Model.DTO.CompanyGetOneDTO;
import org.sayar.net.Model.DTO.CompanySearchDTO;
import org.sayar.net.Model.newModel.Company;
import org.sayar.net.Model.newModel.Node.Node;
import org.sayar.net.Service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("companyServiceImpl")
public class CompanyServiceImpl extends GeneralServiceImpl<Company> implements CompanyService {


    private final CompanyDao companyDao;
    @Autowired
    private CityService cityService;
    @Autowired
    private ProvinceService provinceService;

    @Autowired
    public CompanyServiceImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }


    @Override
    public List<Node> getCompanyTreeNode() {
        return companyDao.getCompanyTreeNode();
    }

    @Override
    public List<CompanyDTO> getCompressAll() {
        return companyDao.getCompressAll();
    }

    @Override
    public List<Node> getCompanyTreeNodeSearch(String searchTerm) {
        return companyDao.getCompanyTreeNodeSearch(searchTerm);
    }

    @Override
    public List<Company> getSupplier(String organId) {
        return companyDao.getSupplier(organId);

    }

    @Override
    public List<CompanyDTO> getByIdList(List<String> companyIdList) {

        return companyDao.getByIdList(companyIdList);
    }

    @Override
    public boolean codeUniqueCheck(String code) {
        return companyDao.checkCodeIsUnique(code);
    }

    @Override
    public Company postOne(Company company) {
        return companyDao.postOne(company);
    }

    @Override
    public Company updateCompany(Company company) {
        return companyDao.updateCompany(company);
    }

    @Override
    public Page<CompanyGetOneDTO> getAllByTermAndPagination(CompanySearchDTO companySearchDTO, Pageable pageable) {
        return new PageImpl<>(
                companyDao.getAllByTermAndPagination(companySearchDTO, pageable)
                , pageable
                , companyDao.numberOfCompanies(companySearchDTO)
        );
    }

    @Override
    public List<Company> getAllCompany() {
        return companyDao.getAllCompany();
    }

    @Override
    public boolean ifProvinceExistsInCompany(String provinceId) {
        return companyDao.ifProvinceExistsInCompany(provinceId);
    }

    @Override
    public boolean ifCityExistsInCompany(String cityId) {
        return companyDao.ifCityExistsInCompany(cityId);
    }

    @Override
    public boolean chickIfCurrencyExistsInCompany(String currencyId) {
        return companyDao.chickIfCurrencyExistsInCompany(currencyId);
    }

    @Override
    public CompanyGetOneDTO getOne(String companyId) {
        return companyDao.getOne(companyId);
    }

    @Override
    public Company getCompanyName(String companyId) {
        return companyDao.getCompanyName(companyId);
    }

}
