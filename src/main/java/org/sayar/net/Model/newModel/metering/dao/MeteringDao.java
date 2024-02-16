package org.sayar.net.Model.newModel.metering.dao;


import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.DTO.AssetMeteringDTO;
import org.sayar.net.Model.newModel.metering.model.Metering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MeteringDao extends GeneralDao<Metering> {

    Metering postOneMetering(Metering metering);

    Metering getOneMetering(String meteringId);

    List<Metering> getAllMetering();

    boolean updateMetering(Metering metering);

    List<Metering> getMeteringByAssetId(List<String> meterings);

    Page<Metering> getAllMeteringByAssetId(String assetId, Pageable pageable, Integer totalCount);

    List<Metering> getMeteringAssetI(String partId);

    List<AssetMeteringDTO> getMeteringListOfMeasurementUnitOfAsset(String assetId, String unitId, Pageable pageable);

    Metering getOneMeteringWithAssociatedUser(String meteringId);

    long countNumberOfMetering(String assetId, String unitId);

    boolean ifUnitExistsInMetering(String unitOfMeasurementId);

    long getMaxMeteringOfAsset(String assetId,String unitOfMeasurementId);
}
