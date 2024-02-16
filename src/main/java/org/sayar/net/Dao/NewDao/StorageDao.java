package org.sayar.net.Dao.NewDao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Model.DTO.StorageGetAllDTO;
import org.sayar.net.Model.newModel.Storage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StorageDao {

    boolean postStorage(Storage storage);

    List<Storage> getAllStorage();

    Storage getOneStorage(String storageId);

    UpdateResult updateStorage(Storage storage, String storageId);

    List<StorageGetAllDTO> getAllByFilterAndPagination(String term, String code,String assetId, Pageable pageable, Integer totalElement);

    long getAllStorageCount(String term, String code, String assetId);

    List<Storage> getAllByPagination(String term, Pageable pageable, Integer totalElement);

    boolean checkIfStorageCodeExists(String storageCode);

    List<Storage> getAllStorageThatHasAsset();

    List<Storage> getAllStorageOfAssetHasParts(String assetId);

    boolean ifProvinceExistsInStorage(String provinceId);

    boolean ifCityExistsInStorage(String cityId);

    boolean ifAssetExistsInStorage(String assetId);

    boolean checkIfCodeIsUnique(String code);

    void addInventoryIdToStorage(String inventoryId, String storageId);

    Storage getStorageNameById(String inventoryLocationId);
}
