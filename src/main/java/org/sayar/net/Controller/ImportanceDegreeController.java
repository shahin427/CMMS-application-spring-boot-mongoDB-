package org.sayar.net.Controller;

import org.sayar.net.Model.ImportanceDegree;
import org.sayar.net.Service.ImportanceDegree.ImportanceDegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/importanceDegree")
public class ImportanceDegreeController {

    @Autowired
    private ImportanceDegreeService entityService;


    @PostMapping("save")
    public ResponseEntity<?> create(@RequestBody ImportanceDegree entity) {
        return ResponseEntity.ok().body(entityService.createImportanceDegree(entity));
    }

    @GetMapping("check-used")
    public ResponseEntity<?> checkUsed(@PathParam("id") String id) {
        return ResponseEntity.ok().body(entityService.checkedUsed(id));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOne(@PathParam("id") String id) {
        return ResponseEntity.ok().body(entityService.getOneImportanceDegree(id));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> delete(@PathParam("id") String id) {
        return ResponseEntity.ok().body(entityService.deleteImportanceDegree(id));

    }

    @PutMapping("update")
    public ResponseEntity<?> update(@RequestBody ImportanceDegree entity) {
        return ResponseEntity.ok().body(entityService.updateImportanceDegree(entity));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllType() {
        return ResponseEntity.ok().body(entityService.getAllType());
    }



    @GetMapping("get-page")
    public ResponseEntity<?> getPage(@RequestParam(value = "term",required = false) String term, Pageable pageable, @RequestParam("totalElements")Integer totalElements) {
        return ResponseEntity.ok().body(entityService.getPage(term,pageable, totalElements));
    }
}
