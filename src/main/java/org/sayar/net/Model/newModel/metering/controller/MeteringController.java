package org.sayar.net.Model.newModel.metering.controller;

import org.sayar.net.Model.newModel.metering.model.Metering;
import org.sayar.net.Model.newModel.metering.service.MeteringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("metering")
public class MeteringController {
    @Autowired
    private MeteringService meteringService;

    @PostMapping("save")
    public ResponseEntity<?> postOneMetering(@RequestBody Metering metering) {
        return ResponseEntity.ok().body(meteringService.postOneMetering(metering));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneMetering(@PathParam("metering") String meteringId) {
        return ResponseEntity.ok().body(meteringService.getOneMetering(meteringId));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllMetering(Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(meteringService.getAllMetering(pageable, totalElement));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteMetering(@PathParam("meteringId") String meteringId) {
        return ResponseEntity.ok().body(meteringService.logicDeleteById(meteringId, Metering.class));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateMetering(@RequestBody Metering metering) {
        return ResponseEntity.ok().body(meteringService.updateMetering(metering));
    }

    @GetMapping("get-all-by-asset-id")
    public ResponseEntity<?> getAllMeteringByAssetId(@PathParam("assetId") String assetId, Pageable pageable, Integer totalCount) {
        return ResponseEntity.ok().body(meteringService.getAllMeteringByAssetId(assetId, pageable, totalCount));
    }

    @GetMapping("get-metering")
    public ResponseEntity<?> getMeteringAssetId(@PathParam("partId") String partId) {
        System.out.println(partId);
        return ResponseEntity.ok().body(meteringService.getMeteringAssetId(partId));
    }

    @GetMapping("get-metering-list-of-unit-of-asset-with-pagination")
    public ResponseEntity<?> getMeteringListOfMeasurementUnitOfAsset(@PathParam("assetId") String assetId,
                                                                     @PathParam("unitId") String unitId,
                                                                     Pageable pageable) {
        return ResponseEntity.ok().body(meteringService.getMeteringListOfMeasurementUnitOfAsset(assetId, unitId, pageable));
    }

    @GetMapping("get-one-with-associated-user")
    public ResponseEntity<?> getOneMeteringWithAssociatedUser(@PathParam("meteringId") String meteringId) {
        return ResponseEntity.ok().body(meteringService.getOneMeteringWithAssociatedUser(meteringId));
    }

    @DeleteMapping("delete")
        public ResponseEntity<?> deleteMeteringFromMainMeteringModule(@PathParam("meteringId") String meteringId) {
        return ResponseEntity.ok().body(meteringService.logicDeleteById(meteringId, Metering.class));
    }

    @GetMapping("get-max-metering-of-asset")
    public ResponseEntity<?> getMaxMeteringOfAsset(@PathParam("assetId") String assetId, @PathParam("unitOfMeasurementId") String unitOfMeasurementId) {
        return ResponseEntity.ok().body(meteringService.getMaxMeteringOfAsset(assetId, unitOfMeasurementId));
    }
}