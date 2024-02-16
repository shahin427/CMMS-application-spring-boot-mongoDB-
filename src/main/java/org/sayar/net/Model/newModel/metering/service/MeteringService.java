package org.sayar.net.Model.newModel.metering.service;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.AssetMeteringDTO;
import org.sayar.net.Model.DTO.MeteringDTO;
import org.sayar.net.Model.newModel.metering.model.Metering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MeteringService extends GeneralService<Metering> {

    Metering postOneMetering(Metering metering);

    MeteringDTO getOneMetering(String meteringId);

    Page<MeteringDTO> getAllMetering(Pageable pageable, Integer totalElement);

    boolean updateMetering(Metering metering);

    Page<Metering> getAllMeteringByAssetId(String assetId, Pageable pageable, Integer totalCount);

    List<Metering> getMeteringAssetId(String partId);

    Page<AssetMeteringDTO> getMeteringListOfMeasurementUnitOfAsset(String assetId, String unitId, Pageable pageable);

    MeteringDTO getOneMeteringWithAssociatedUser(String meteringId);

    boolean ifUnitExistsInMetering(String unitOfMeasurementId);

    long getMaxMeteringOfAsset(String assetId,String unitOfMeasurementId);
}
