package org.sayar.net.Controller;

import org.sayar.net.Model.ActivityType;
import org.sayar.net.Service.activityType.ActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/activityType")
public class ActivityTypeController {

    @Autowired
    private ActivityTypeService activityTypeService;


    @PostMapping("save")
    public ResponseEntity<?> create(@RequestBody ActivityType entity) {
        return ResponseEntity.ok().body(activityTypeService.createActivityType(entity));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOne(@PathParam("id") String id) {
        return ResponseEntity.ok().body(activityTypeService.getOneActivityType(id));
    }
    @GetMapping("check-used")
    public ResponseEntity<?> checkUsed(@PathParam("id") String id) {
        return ResponseEntity.ok().body(activityTypeService.checkUsed(id));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> delete(@PathParam("id") String id) {
        return ResponseEntity.ok().body(activityTypeService.deleteActivityType(id));

    }

    @PutMapping("update")
    public ResponseEntity<?> update(@RequestBody ActivityType entity) {
        return ResponseEntity.ok().body(activityTypeService.updateActivityType(entity));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllType() {
        return ResponseEntity.ok().body(activityTypeService.getAllType());
    }

    @GetMapping("get-page")
    public ResponseEntity<?> getPage(@RequestParam(value = "term",required = false) String term, Pageable pageable, @RequestParam("totalElements")Integer totalElements) {
        return ResponseEntity.ok().body(activityTypeService.getPage(term,pageable, totalElements));
    }
}
