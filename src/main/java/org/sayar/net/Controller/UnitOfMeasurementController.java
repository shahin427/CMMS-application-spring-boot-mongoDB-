package org.sayar.net.Controller;

import org.sayar.net.Model.DTO.UnitOfMeasurementDTO;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.metering.service.MeteringService;
import org.sayar.net.Service.UnitOfMeasurementService;
import org.sayar.net.Service.newService.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/unit-of-measurement")
public class UnitOfMeasurementController {

    private final UnitOfMeasurementService service;
    private final AssetService assetService;
    private final MeteringService meteringService;

    @Autowired
    public UnitOfMeasurementController(UnitOfMeasurementService service, AssetService assetService, MeteringService meteringService) {
        this.service = service;
        this.assetService = assetService;
        this.meteringService = meteringService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "get-one")
    public ResponseEntity<?> findOne(@PathParam("unitOfMeasurementId") String unitOfMeasurementId) {
        return ResponseEntity.ok().body(service.findOneById(unitOfMeasurementId, UnitOfMeasurement.class));
    }

    @RequestMapping(method = RequestMethod.GET, value = "get-all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(service.findAllNotLogicDeleted(UnitOfMeasurement.class));
    }

    @RequestMapping(method = RequestMethod.POST, value = "save")
    public ResponseEntity<?> saveMeasure(@RequestBody UnitOfMeasurement unit) {
        return ResponseEntity.ok().body(service.saveMeasure(unit));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "update")
    public ResponseEntity<?> update(@RequestBody UnitOfMeasurement unit) {
        return ResponseEntity.ok().body(service.updateUnit(unit));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathParam("unitOfMeasurementId") String unitOfMeasurementId) {
        if (assetService.ifUnitExistsInAsset(unitOfMeasurementId)) {
            return ResponseEntity.ok().body("\"برای خذف این واحد اندازه گیری ابتدا آن را از بخش دارایی ها پاک کنید\"");
        } else if (meteringService.ifUnitExistsInMetering(unitOfMeasurementId)) {
            return ResponseEntity.ok().body("\"برای خذف این واحد اندازه گیری ابتدا آن را از بخش متراژها پاک کنید\"");
        } else {
            return ResponseEntity.ok().body(service.logicDeleteById(unitOfMeasurementId, UnitOfMeasurement.class));
        }
    }

    @GetMapping("check-if-unit-of-measurement-is-unique")
    public ResponseEntity<?> checkIfUnitOfMeasurementIsUnique(@PathParam("title") String title) {
        return ResponseEntity.ok().body(service.checkIfUnitOfMeasurementIsUnique(title));
    }

    @PostMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllByPagination(@RequestBody UnitOfMeasurementDTO unitOfMeasurementDTO, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllByPagination(unitOfMeasurementDTO, pageable, totalElement));
    }

    @PostMapping("check-if-unit-and-title-exist")
    public ResponseEntity<?> checkIfUnitAndTitleExist(@RequestBody UnitOfMeasurementDTO unitOfMeasurementDTO) {
        return ResponseEntity.ok().body(service.checkIfUnitAndTitleExist(unitOfMeasurementDTO));
    }

    @GetMapping("update-check")
    public ResponseEntity<?> updateCheck(@PathParam("unitOfMeasurementId") String unitOfMeasurementId) {
        if (assetService.ifUnitExistsInAsset(unitOfMeasurementId)) {
            return ResponseEntity.ok().body("\"برای ویرایش این واحد اندازه گیری ابتدا آن را از قسمت درایی ها حذف نمایید\"");
        } else if (meteringService.ifUnitExistsInMetering(unitOfMeasurementId)) {
            return ResponseEntity.ok().body("\"برای ویرایش این واحد اندازه گیری ابتدا آن را از قسمت متراژخوانی حذف نمایید\"");
        } else {
            return ResponseEntity.ok().body("\"مجاز به ویرایش میباشد \"");
        }
    }


}
