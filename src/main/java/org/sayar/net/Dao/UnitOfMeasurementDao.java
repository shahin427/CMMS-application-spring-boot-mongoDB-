package org.sayar.net.Dao;


import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.DTO.UnitOfMeasurementDTO;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UnitOfMeasurementDao extends GeneralDao<UnitOfMeasurement> {

    UnitOfMeasurement saveMeasure(UnitOfMeasurement unit);

    UnitOfMeasurement updateUnit(UnitOfMeasurement unit);

    List<UnitOfMeasurement> getAllUnitOfMeasurementById(List<String> unitIdList);

    UnitOfMeasurement getUnitOfMeasurementNameById(String unitOfMeasurementId);

    UnitOfMeasurement getUnitOfMeasurementByScheduleMaintenanceMeasurementId(String unitOfMeasurementId);

    boolean checkIfUnitOfMeasurementIsUnique(String title);

    List<UnitOfMeasurement> getAllUnitOfMeasurementOfTheAsset(List<String> assetUnitIdList);

    List<UnitOfMeasurement> getUnitOfMeasurementTitle(List<String> unitIdList);

    List<UnitOfMeasurement> getAllByPagination(UnitOfMeasurementDTO unitOfMeasurementDTO, Pageable pageable, Integer totalElement);

    long getAllCount(UnitOfMeasurementDTO unitOfMeasurementDTO);

    boolean checkIfUnitAndTitleExist(UnitOfMeasurementDTO unitOfMeasurementDTO);
}
