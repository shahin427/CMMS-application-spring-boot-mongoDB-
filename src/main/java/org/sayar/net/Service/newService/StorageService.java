package org.sayar.net.Service.newService;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.StorageDTO;
import org.sayar.net.Model.DTO.StorageGetAllDTO;
import org.sayar.net.Model.newModel.Storage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StorageService extends GeneralService {

    boolean postStorage(Storage storage);

    List<Storage> getAllStorage();

    StorageDTO getOneStorage(String storageId);

    boolean updateStorage(Storage storage, String storageId);

    Page<StorageGetAllDTO> getAllByFilterAndPagination(String term, String code, String assetId, Pageable pageable, Integer totalElement);

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
