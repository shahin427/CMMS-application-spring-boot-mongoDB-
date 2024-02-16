package org.sayar.net.Service.newService;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Dao.NewDao.WarrantyDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.WarrantyDTO;
import org.sayar.net.Model.DTO.WarrantyGetAllDTO;
import org.sayar.net.Model.DTO.WarrantyGetOneDTO;
import org.sayar.net.Model.newModel.Company;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.Warranty;
import org.sayar.net.Service.UnitOfMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("warrantyServiceImpl")
public class WarrantyServiceImpl extends GeneralServiceImpl<Warranty> implements WarrantyService {

    private final WarrantyDao dao;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UnitOfMeasurementService unitOfMeasurementService;

    @Autowired
    public WarrantyServiceImpl(WarrantyDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Warranty> getByIdList(List<String> idList) {
        return dao.getByIdList(idList);
    }

    @Override
    public Page<Warranty> getAllWarrantyByPagination(Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllWarrantyByPagination(pageable, totalElement)
                , pageable
                , dao.getAllCount()
        );
    }

    @Override
    public List<Warranty> getAllWarrantyType() {
        return dao.getAllWarrantyType();
    }

    @Override
    public List<WarrantyDTO> getAllWarrantyMeasurement() {
        return dao.getAllWarrantyMeasurement();
    }

    @Override
    public Page<Warranty> getAllPaginationByAssetId(String assetId, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllPaginationByAssetId(assetId, pageable, totalElement)
                , pageable
                , dao.getAllWarrantyCount()
        );
    }

    @Override
    public Page<WarrantyGetAllDTO> getAllWarrantyByPartId(String partId, String assetId, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllWarrantyByPartId(partId, assetId, pageable, totalElement)
                , pageable
                , dao.countAllWarrantyByPartId(partId, assetId));
    }

    @Override
    public boolean checkIfCodeExists(String code) {
        return dao.checkIfCodeExists(code);
    }

    @Override
    public boolean ifCompanyExistsInWarranty(String companyId) {
        return dao.ifCompanyExistsInWarranty(companyId);
    }


    //___________________________________________________________________
    @Override
    public WarrantyGetOneDTO postOneWarranty(Warranty warranty) {
        Warranty savedWarranty = dao.postOneWarranty(warranty);
        UnitOfMeasurement unitOfMeasurement = unitOfMeasurementService.getUnitOfMeasurementNameById(savedWarranty.getUnitOfMeasurementId());
        Company company = companyService.getCompanyName(savedWarranty.getCompanyId());
        return WarrantyGetOneDTO.map(savedWarranty, company, unitOfMeasurement);

    }

    @Override
    public WarrantyGetOneDTO getOneWarranty(String warrantyId) {
        return dao.getOneWarranty(warrantyId);
    }

    @Override
    public UpdateResult updateWarranty(Warranty warranty) {
        return dao.updateWarranty(warranty);
    }

    @Override
    public List<Warranty> getAllCompany() {
        return dao.getAllCompany();
    }


}
