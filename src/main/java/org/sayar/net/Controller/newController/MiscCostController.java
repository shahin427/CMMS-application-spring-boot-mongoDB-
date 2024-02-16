package org.sayar.net.Controller.newController;


import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.newModel.MiscCost;
import org.sayar.net.Service.newService.MiscCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("misc-cost")
public class MiscCostController {
    @Autowired
    private MiscCostService miscCostService;


    @GetMapping("get-one")
    public ResponseEntity<?> findOne(@PathParam("id") String id) {
        return ResponseEntity.ok().body(miscCostService.getOneById(id));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(miscCostService.findAllNotLogicDeleted(MiscCost.class));
    }

    @PostMapping("save")
    public ResponseEntity<?> postMiscCost(@RequestBody MiscCost miscCost) {
        return ResponseEntity.ok().body(miscCostService.postMiscCostController(miscCost));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateMiscCost(@RequestBody MiscCost miscCost) {
        return ResponseEntity.ok().body(miscCostService.updateMiscCost(miscCost));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> delete(@PathParam("miscCostId") String miscCostId) {
        return new ResponseContent().sendOkResponseEntity("", miscCostService.logicDeleteById(miscCostId, MiscCost.class));
    }

    @GetMapping("get-misc-cost-list-by-reference-id")
    public ResponseEntity<?> getMiscCostListByReferenceId(@PathParam("referenceId") String referenceId) {
        return ResponseEntity.ok().body(miscCostService.getMiscCostListByReferenceId(referenceId));
    }
}
