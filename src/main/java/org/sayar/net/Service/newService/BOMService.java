package org.sayar.net.Service.newService;

import org.sayar.net.Controller.newController.BOM;
import org.sayar.net.Controller.newController.BOMAsset;
import org.sayar.net.Controller.newController.BOMPart;
import org.sayar.net.Dao.BOMPartDTO;
import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.Part.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BOMService extends GeneralService<BOM> {

    String createBOM(BOMNameDTO bomdto);

    Page<Part> getAllBOMPart(Pageable pageable);

    Page<BOMPartDTO> getAllPartByPagination(String bomId, Pageable pageable);

    Page<BOMAssetDTO> getAllAssetByPagination(String bomId, Pageable pageable);

    boolean updateBOM(List<BOMPart> partBOMDTOList, String id);

    BOM getOneBOM(String bomId);

    Page<BOM> getAllBOMByPagination(String name,String code, Pageable pageable, Integer totalElement);

    boolean removePartOfBOM(String bomId, String bomPartId);

    boolean removeAssetOfBOM(String bomId, String bomAssetId);

    boolean ifAssetExistsInBOM(String assetId);

    boolean ifPartExistsInBOM(String partId);

    boolean updateAssetBOM(List<AssetBOMDTO> assetBOMDTOList, String id);

    boolean checkIfBomExists(String bomCode);

    boolean updateFirstPage(BOMNameDTO bomNameDTO);
}
