package org.sayar.net.Controller.newController;

import org.sayar.net.Model.PartACR;
import org.sayar.net.Service.newService.PartACRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@Controller
@RequestMapping("part-ACR")
public class PartACRController {
    @Autowired
    private PartACRService partACRService;

    @PostMapping("save")
    public ResponseEntity<?> savePartACR(@RequestBody PartACR partACR) {
        if (partACRService.checkIfPartACRRegistered(partACR)) {
            return ResponseEntity.ok().body("\"این دارایی یکبار ذخیره شده است\"");
        } else {
            return ResponseEntity.ok().body(partACRService.savePartACR(partACR));
        }
    }

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllPartACR(String partId, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(partACRService.getAllPartACR(partId, pageable, totalElement));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteACRId(@PathParam("ACRId") String ACRId) {
        return ResponseEntity.ok().body(partACRService.logicDeleteById(ACRId, PartACR.class));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOnePartACR(@PathParam("ACRId") String ACRId) {
        return ResponseEntity.ok().body(partACRService.getOnePartACR(ACRId));
    }

    @PutMapping("update")
    public ResponseEntity<?> updatePartACR(@RequestBody PartACR partACR) {
        return ResponseEntity.ok().body(partACRService.updatePartACR(partACR));
    }
}
