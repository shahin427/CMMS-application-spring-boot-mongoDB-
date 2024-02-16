//package org.sayar.net.Controller;
//
//import org.sayar.net.General.service.GeneralServiceImpl;
//import org.sayar.net.Model.Plant;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.websocket.server.PathParam;
//
//@RestController
//@RequestMapping("plant")
//public class PlantController extends GeneralServiceImpl {
//    @Autowired
//    private PlantService plantService;
//
//    @PostMapping("save")
//    public ResponseEntity<?> savePlant(@RequestBody Plant plant) {
//        return ResponseEntity.ok().body(plantService.savePlant(plant));
//    }
//
//    @GetMapping("get-all")
//    public ResponseEntity<?> getAllPlants() {
//        return ResponseEntity.ok().body(plantService.getAllPlants());
//    }
//
//    @GetMapping("get-one")
//    public ResponseEntity<?> getOnePlant(@PathParam("id") String id) {
//        return ResponseEntity.ok().body(plantService.getOnePlant(id));
//    }
//
//    @DeleteMapping("delete")
//    public ResponseEntity<?> deletePlant(@PathParam("id") String id) {
//        return ResponseEntity.ok().body(plantService.logicDeleteById(id, Plant.class));
//    }
//
//    @PutMapping("update")
//    public ResponseEntity<?> updatePlant(@RequestBody Plant plant) {
//        return ResponseEntity.ok().body(plantService.updatePlant(plant));
//    }
//}
