package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.DTO.WarrantyDTO;
import org.sayar.net.Model.DTO.WarrantyGetAllDTO;
import org.sayar.net.Model.DTO.WarrantyGetOneDTO;
import org.sayar.net.Model.newModel.Warranty;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WarrantyDao extends GeneralDao<Warranty> {
    List<Warranty> getByIdList(List<String> idList);

    List<Warranty> getWarrantyByIdList(List<String> warrantyIdList);

    List<Warranty> getAllWarrantyByPagination(Pageable pageable, Integer totalElement);

    long getAllCount();

    Warranty postOneWarranty(Warranty warranty);

    WarrantyGetOneDTO getOneWarranty(String warrantyId);

    UpdateResult updateWarranty(Warranty warranty);

    List<Warranty> getAllCompany();

    List<Warranty> getAllWarrantyType();

    List<WarrantyDTO> getAllWarrantyMeasurement();

    List<Warranty> getAllPaginationByAssetId(String assetId, Pageable pageable, Integer totalElement);

    long getAllWarrantyCount();

    List<WarrantyGetAllDTO> getAllWarrantyByPartId(String partId, String assetId, Pageable pageable, Integer totalElement);

    long countAllWarrantyByPartId(String assetId, String partId);

    boolean checkIfCodeExists(String code);

    boolean ifCompanyExistsInWarranty(String companyId);
}
