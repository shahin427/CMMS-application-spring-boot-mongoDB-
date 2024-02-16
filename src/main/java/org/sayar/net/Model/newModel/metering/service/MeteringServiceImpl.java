
package org.sayar.net.Model.newModel.metering.service;


import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.AssetMeteringDTO;
import org.sayar.net.Model.DTO.MeteringDTO;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintenanceBackupService;
import org.sayar.net.Model.newModel.metering.dao.MeteringDao;
import org.sayar.net.Model.newModel.metering.model.Metering;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.newService.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeteringServiceImpl extends GeneralServiceImpl<Metering> implements MeteringService {

    @Autowired
    private MeteringDao meteringDao;

    @Autowired
    private AssetService assetService;

    @Autowired
    private ScheduleMaintenanceBackupService scheduleMaintenanceBackupService;

    @Autowired
    private UserService userService;

    @Override
    public Metering postOneMetering(Metering metering) {
        Metering metering1 = meteringDao.postOneMetering(metering);
        scheduleMaintenanceBackupService.checkIfAmountArrivedToNextTriggerThresholdOfScheduleMaintenance
                (metering1.getReferenceId(), metering1.getAmount(), metering1.getUnitOfMeasurement());
        return metering1;
    }

    @Override
    public MeteringDTO getOneMetering(String meteringId) {
        Metering metering = meteringDao.getOneMetering(meteringId);
        Asset asset = assetService.getAssetOfMetering(metering.getReferenceId());
        User user = userService.getUserOfTheMetering(meteringId);
        return MeteringDTO.UserMap(metering, user, asset);
    }

    @Override
    public Page<MeteringDTO> getAllMetering(Pageable pageable, Integer totalElement) {
        List<Metering> meteringList = meteringDao.getAllMetering();
        List<String> assetIdList = new ArrayList<>();
        List<String> userIdList = new ArrayList<>();
        meteringList.forEach(metering -> {
                    assetIdList.add(metering.getReferenceId());
                }
        );
        meteringList.forEach(metering -> {
            userIdList.add(metering.getUserId());
        });
        List<User> userList = userService.getAllUsersOfMeteringList(userIdList);
        List<Asset> assetList = assetService.getAssetByAssetIdList(assetIdList);
        return MeteringDTO.map(meteringList, assetList, userList, pageable);
    }

    @Override
    public boolean updateMetering(Metering metering) {
        return meteringDao.updateMetering(metering);
    }

    @Override
    public Page<Metering> getAllMeteringByAssetId(String assetId, Pageable pageable, Integer totalCount) {
        return meteringDao.getAllMeteringByAssetId(assetId, pageable, totalCount);
    }

    @Override
    public List<Metering> getMeteringAssetId(String partId) {
        return meteringDao.getMeteringAssetI(partId);
    }

    @Override
    public Page<AssetMeteringDTO> getMeteringListOfMeasurementUnitOfAsset(String assetId, String unitId, Pageable pageable) {
        return new PageImpl<>(
                meteringDao.getMeteringListOfMeasurementUnitOfAsset(assetId, unitId, pageable),
                pageable,
                meteringDao.countNumberOfMetering(assetId, unitId)
        );
    }

    @Override
    public MeteringDTO getOneMeteringWithAssociatedUser(String meteringId) {
        Metering metering = meteringDao.getOneMeteringWithAssociatedUser(meteringId);
        User user = userService.getUserOfTheMetering(metering.getUserId());
        Asset asset = assetService.getOneAsset(metering.getReferenceId());
        return MeteringDTO.UserMap(metering, user, asset);
    }

    @Override
    public boolean ifUnitExistsInMetering(String unitOfMeasurementId) {
        return meteringDao.ifUnitExistsInMetering(unitOfMeasurementId);
    }

    @Override
    public long getMaxMeteringOfAsset(String assetId, String unitOfMeasurementId) {
        return meteringDao.getMaxMeteringOfAsset(assetId, unitOfMeasurementId);
    }
}
