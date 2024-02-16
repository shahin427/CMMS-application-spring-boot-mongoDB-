package org.sayar.net.Service.newService;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.WarrantyDTO;
import org.sayar.net.Model.DTO.WarrantyGetAllDTO;
import org.sayar.net.Model.DTO.WarrantyGetOneDTO;
import org.sayar.net.Model.newModel.Warranty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WarrantyService extends GeneralService<Warranty> {

    List<Warranty> getByIdList(List<String> idList);

    Page<Warranty> getAllWarrantyByPagination(Pageable pageable, Integer totalElement);

    WarrantyGetOneDTO postOneWarranty(Warranty warranty);

    WarrantyGetOneDTO getOneWarranty(String warrantyId);

    UpdateResult updateWarranty(Warranty warranty);

    List<Warranty> getAllCompany();

    List<Warranty> getAllWarrantyType();

    List<WarrantyDTO> getAllWarrantyMeasurement();

    Page<Warranty> getAllPaginationByAssetId(String assetId, Pageable pageable, Integer totalElement);

    Page<WarrantyGetAllDTO> getAllWarrantyByPartId(String partId, String assetId, Pageable pageable, Integer totalElement);

    boolean checkIfCodeExists(String code);

    boolean ifCompanyExistsInWarranty(String companyId);
}
