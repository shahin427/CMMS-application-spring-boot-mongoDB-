package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.CompanyDTO;
import org.sayar.net.Model.DTO.CompanyGetOneDTO;
import org.sayar.net.Model.DTO.CompanySearchDTO;
import org.sayar.net.Model.newModel.Company;
import org.sayar.net.Model.newModel.Node.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService extends GeneralService<Company> {

    List<Node> getCompanyTreeNode();

    List<CompanyDTO> getCompressAll();

    List<Node> getCompanyTreeNodeSearch(String searchTerm);

    List<Company> getSupplier(String organId);

    List<CompanyDTO> getByIdList(List<String> companyIdList);

    boolean codeUniqueCheck(String code);

    Company postOne(Company company);

    Company updateCompany(Company company);

    Page<CompanyGetOneDTO> getAllByTermAndPagination(CompanySearchDTO companySearchDTO, Pageable pageable);

    List<Company> getAllCompany();

    boolean ifProvinceExistsInCompany(String provinceId);

    boolean ifCityExistsInCompany(String cityId);

    boolean chickIfCurrencyExistsInCompany(String currencyId);

    CompanyGetOneDTO getOne(String companyId);

    Company getCompanyName(String companyId);
}
