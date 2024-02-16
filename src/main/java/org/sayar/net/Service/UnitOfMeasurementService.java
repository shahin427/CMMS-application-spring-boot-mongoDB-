package org.sayar.net.Service;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.UnitOfMeasurementDTO;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UnitOfMeasurementService extends GeneralService<UnitOfMeasurement> {

    UnitOfMeasurement saveMeasure(UnitOfMeasurement unit);

    UnitOfMeasurement updateUnit(UnitOfMeasurement unit);

    List<UnitOfMeasurement> getAllUnitOfMeasurementById(List<String> unitIdList);

    UnitOfMeasurement getUnitOfMeasurementNameById(String unitOfMeasurementId);

    UnitOfMeasurement getUnitOfMeasurementByScheduleMaintenanceMeasurementId(String unitOfMeasurementId);

    boolean checkIfUnitOfMeasurementIsUnique(String title);

    List<UnitOfMeasurement> getAllUnitOfMeasurementOfTheAsset(List<String> assetUnitIdList);

    List<UnitOfMeasurement> getUnitOfMeasurementTitle(List<String> unitIdList);

    Page<UnitOfMeasurement> getAllByPagination(UnitOfMeasurementDTO unitOfMeasurementDTO, Pageable pageable, Integer totalElement);

    boolean checkIfUnitAndTitleExist(UnitOfMeasurementDTO unitOfMeasurementDTO);
}
