package org.sayar.net.Service;

import org.sayar.net.Dao.UnitOfMeasurementDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.UnitOfMeasurementDTO;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("unitOfMeasurementServiceImpl")
public class UnitOfMeasurementServiceImpl extends GeneralServiceImpl<UnitOfMeasurement> implements UnitOfMeasurementService {
    @Autowired
    private UnitOfMeasurementDao dao;

    public UnitOfMeasurement saveMeasure(UnitOfMeasurement unit) {
        return dao.saveMeasure(unit);
    }

    @Override
    public UnitOfMeasurement updateUnit(UnitOfMeasurement unit) {
        return dao.updateUnit(unit);
    }

    @Override
    public List<UnitOfMeasurement> getAllUnitOfMeasurementById(List<String> unitIdList) {
        return dao.getAllUnitOfMeasurementById(unitIdList);
    }

    @Override
    public UnitOfMeasurement getUnitOfMeasurementNameById(String unitOfMeasurementId) {
        return dao.getUnitOfMeasurementNameById(unitOfMeasurementId);
    }

    @Override
    public UnitOfMeasurement getUnitOfMeasurementByScheduleMaintenanceMeasurementId(String unitOfMeasurementId) {
        return dao.getUnitOfMeasurementByScheduleMaintenanceMeasurementId(unitOfMeasurementId);
    }

    @Override
    public boolean checkIfUnitOfMeasurementIsUnique(String title) {
        return dao.checkIfUnitOfMeasurementIsUnique(title);
    }

    @Override
    public List<UnitOfMeasurement> getAllUnitOfMeasurementOfTheAsset(List<String> assetUnitIdList) {
        return dao.getAllUnitOfMeasurementOfTheAsset(assetUnitIdList);
    }

    @Override
    public List<UnitOfMeasurement> getUnitOfMeasurementTitle(List<String> unitIdList) {
        return dao.getUnitOfMeasurementTitle(unitIdList);
    }

    @Override
    public Page<UnitOfMeasurement> getAllByPagination(UnitOfMeasurementDTO unitOfMeasurementDTO, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllByPagination(unitOfMeasurementDTO, pageable, totalElement)
                ,pageable
                ,dao.getAllCount(unitOfMeasurementDTO)
        );
    }

    @Override
    public boolean checkIfUnitAndTitleExist(UnitOfMeasurementDTO unitOfMeasurementDTO) {
        return dao.checkIfUnitAndTitleExist(unitOfMeasurementDTO);
    }
}
