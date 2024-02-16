package org.sayar.net.Dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Controller.newController.BOM;
import org.sayar.net.Controller.newController.BOMAsset;
import org.sayar.net.Controller.newController.BOMPart;
import org.sayar.net.Model.DTO.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BOMDao {
    String createBOM(BOMNameDTO bomdto);

    List<BOMPartDTO> getAllPartByPagination(String bomId, Pageable pageable);

    List<BOMAssetDTO> getAllAssetByPagination(String bomId, Pageable pageable);

    UpdateResult updateBOM(List<BOMPart> partBOMDTOList, String id);

    BOM getOneBOM(String bomId);

    List<BOM> getAllBOMByPagination(String name, String code, Pageable pageable, Integer totalElement);

    UpdateResult removePartOfBOM(String bomId, String bomPartId);

    long countAllBOM(String name,String code);

    UpdateResult removeAssetOfBOM(String bomId, String bomAssetId);

    boolean ifAssetExistsInBOM(String assetId);

    boolean ifPartExistsInBOM(String partId);

    UpdateResult updateAssetBOM(List<AssetBOMDTO> assetBOMDTOList, String id);

    long countAllPartBom(String bomId);

    long countAssetBom(String bomId);

    boolean checkIfBomExists(String bomCode);

    UpdateResult updateFirstPage(BOMNameDTO bomNameDTO);
}
