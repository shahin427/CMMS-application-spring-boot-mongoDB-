package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.DTO.CompanyDTO;
import org.sayar.net.Model.DTO.CompanyGetOneDTO;
import org.sayar.net.Model.DTO.CompanySearchDTO;
import org.sayar.net.Model.newModel.Company;
import org.sayar.net.Model.newModel.Node.Node;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyDao extends GeneralDao<Company> {


    List<Node> getCompanyTreeNode();

    List<CompanyDTO> getCompressAll();

    List<Company> getSupplier(String organId);

    List<CompanyDTO> getByIdList(List<String> companyIdList);

    Company postOne(Company company);

    Company updateCompany(Company company);

    List<Node> getCompanyTreeNodeSearch(String searchTerm);

    boolean ifProvinceExistsInCompany(String provinceId);

    boolean ifCityExistsInCompany(String cityId);

    List<CompanyGetOneDTO> getAllByTermAndPagination(CompanySearchDTO companySearchDTO, Pageable pageable);

    List<Company> getCompanyByIdList(List<String> companyIdList);

    List<Company> getAllCompany();

    boolean chickIfCurrencyExistsInCompany(String currencyId);

    long numberOfCompanies(CompanySearchDTO companySearchDTO);

    boolean checkCodeIsUnique(String code);

    CompanyGetOneDTO getOne(String companyId);

    Company getCompanyName(String companyId);
}
