package org.sayar.net.Service.newService;

import org.sayar.net.Dao.NewDao.StorageDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.StorageDTO;
import org.sayar.net.Model.DTO.StorageGetAllDTO;
import org.sayar.net.Model.newModel.Inventory;
import org.sayar.net.Model.newModel.Storage;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StorageServiceImp extends GeneralServiceImpl implements StorageService {
    @Autowired
    private StorageDao storageDao;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private AssetService assetService;

    @Override
    public boolean postStorage(Storage storage) {
        return storageDao.postStorage(storage);
    }

    @Override
    public List<Storage> getAllStorage() {
        return storageDao.getAllStorage();
    }

    @Override
    public StorageDTO getOneStorage(String storageId) {
        Storage storage = storageDao.getOneStorage(storageId);
        Asset asset = assetService.getOneAsset(storage.getAssetId());
        Print.print("asset", asset);
        return StorageDTO.map(storage, asset);
    }

    @Override
    public boolean updateStorage(Storage storage, String storageId) {
        return this.updateResultStatus(storageDao.updateStorage(storage, storageId));
    }

    @Override
    public Page<StorageGetAllDTO> getAllByFilterAndPagination(String term, String code, String assetId, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                storageDao.getAllByFilterAndPagination(term, code, assetId, pageable, totalElement),
                pageable,
                storageDao.getAllStorageCount(term, code, assetId)
        );
    }

    @Override
    public boolean checkIfStorageCodeExists(String storageCode) {
        return storageDao.checkIfStorageCodeExists(storageCode);
    }

    @Override
    public List<Storage> getAllStorageThatHasAsset() {
        return storageDao.getAllStorageThatHasAsset();
    }

    @Override
    public List<Storage> getAllStorageOfAssetHasParts(String assetId) {
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        List<Storage> storageList = new ArrayList<>();
        inventoryList.forEach(inventory -> {
//            storageList.add(inventory.getInventoryLocation());
        });
        List<Storage> storageOfAsset = new ArrayList<>();
        storageList.forEach(storage -> {
            if (storage.getAssetId().equals(assetId)) {
                storageOfAsset.add(storage);
            }
        });
        return storageOfAsset;
    }

    @Override
    public boolean ifProvinceExistsInStorage(String provinceId) {
        return storageDao.ifProvinceExistsInStorage(provinceId);
    }

    @Override
    public boolean ifCityExistsInStorage(String cityId) {
        return storageDao.ifCityExistsInStorage(cityId);
    }

    @Override
    public boolean ifAssetExistsInStorage(String assetId) {
        return storageDao.ifAssetExistsInStorage(assetId);
    }

    @Override
    public boolean checkIfCodeIsUnique(String code) {
        return storageDao.checkIfCodeIsUnique(code);
    }

    @Override
    public void addInventoryIdToStorage(String inventoryId, String storageId) {
        storageDao.addInventoryIdToStorage(inventoryId, storageId);
    }

    @Override
    public Storage getStorageNameById(String inventoryLocationId) {
        return storageDao.getStorageNameById(inventoryLocationId);
    }

}
