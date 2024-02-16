package org.sayar.net.Service;

import org.sayar.net.Controller.newController.BOM;
import org.sayar.net.Controller.newController.BOMPart;
import org.sayar.net.Dao.BOMDao;
import org.sayar.net.Dao.BOMPartDTO;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.AssetBOMDTO;
import org.sayar.net.Model.DTO.BOMAssetDTO;
import org.sayar.net.Model.DTO.BOMNameDTO;
import org.sayar.net.Model.newModel.Part.Part;
import org.sayar.net.Service.newService.AssetService;
import org.sayar.net.Service.newService.BOMService;
import org.sayar.net.Service.newService.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BOMServiceImp extends GeneralServiceImpl<BOM> implements BOMService {

    @Autowired
    private BOMDao bomDao;

    @Autowired
    private PartService partService;

    @Autowired
    private AssetService assetService;

    @Override
    public String createBOM(BOMNameDTO bomdto) {
        return bomDao.createBOM(bomdto);
    }

    @Override
    public Page<Part> getAllBOMPart(Pageable pageable) {
        return partService.getAllBOMPart(pageable);
    }

    @Override
    public Page<BOMPartDTO> getAllPartByPagination(String bomId, Pageable pageable) {
        return new PageImpl<>(
                bomDao.getAllPartByPagination(bomId, pageable),
                pageable,
                bomDao.countAllPartBom(bomId)
        );
    }

    @Override
    public Page<BOMAssetDTO> getAllAssetByPagination(String bomId, Pageable pageable) {
        return new PageImpl<>(
                bomDao.getAllAssetByPagination(bomId, pageable),
                pageable,
                bomDao.countAssetBom(bomId)
        );
    }

    @Override
    public boolean updateBOM(List<BOMPart> partBOMDTOList, String id) {
        return super.updateResultStatus(bomDao.updateBOM(partBOMDTOList, id));
    }

    @Override
    public BOM getOneBOM(String bomId) {
        return bomDao.getOneBOM(bomId);
    }

    @Override
    public Page<BOM> getAllBOMByPagination(String name, String code, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                bomDao.getAllBOMByPagination(name, code, pageable, totalElement)
                , pageable
                , bomDao.countAllBOM(name, code)
        );
    }

    @Override
    public boolean removePartOfBOM(String bomId, String bomPartId) {
        return super.updateResultStatus(bomDao.removePartOfBOM(bomId, bomPartId));
    }

    @Override
    public boolean removeAssetOfBOM(String bomId, String bomAssetId) {
        return super.updateResultStatus(bomDao.removeAssetOfBOM(bomId, bomAssetId));
    }

    @Override
    public boolean ifAssetExistsInBOM(String assetId) {
        return bomDao.ifAssetExistsInBOM(assetId);
    }

    @Override
    public boolean ifPartExistsInBOM(String partId) {
        return bomDao.ifPartExistsInBOM(partId);
    }

    @Override
    public boolean updateAssetBOM(List<AssetBOMDTO> assetBOMDTOList, String id) {
        return super.updateResultStatus(bomDao.updateAssetBOM(assetBOMDTOList, id));
    }

    @Override
    public boolean checkIfBomExists(String bomCode) {
        return bomDao.checkIfBomExists(bomCode);
    }

    @Override
    public boolean updateFirstPage(BOMNameDTO bomNameDTO) {
        return this.updateResultStatus(bomDao.updateFirstPage(bomNameDTO));
    }

}
