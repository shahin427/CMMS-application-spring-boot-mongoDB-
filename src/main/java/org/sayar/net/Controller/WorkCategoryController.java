package org.sayar.net.Controller;

import org.sayar.net.Model.WorkCategory;
import org.sayar.net.Service.WorkCategory.WorkCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/workCategory")
public class WorkCategoryController {

    @Autowired
    private WorkCategoryService activityTypeService;


    @PostMapping("save")
    public ResponseEntity<?> create(@RequestBody WorkCategory entity) {
        return ResponseEntity.ok().body(activityTypeService.createWorkCategory(entity));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOne(@PathParam("id") String id) {
        return ResponseEntity.ok().body(activityTypeService.getOneWorkCategory(id));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> delete(@PathParam("id") String id) {
        return ResponseEntity.ok().body(activityTypeService.deleteWorkCategory(id));

    }
    @GetMapping("check-used")
    public ResponseEntity<?> checkUsed(@PathParam("id") String id) {
        return ResponseEntity.ok().body(activityTypeService.checkUsed(id));
    }

    @PutMapping("update")
    public ResponseEntity<?> update(@RequestBody WorkCategory entity) {
        return ResponseEntity.ok().body(activityTypeService.updateWorkCategory(entity));
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
