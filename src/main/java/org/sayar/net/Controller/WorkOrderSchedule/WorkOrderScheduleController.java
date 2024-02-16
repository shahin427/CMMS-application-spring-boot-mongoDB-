package org.sayar.net.Controller.WorkOrderSchedule;

import org.sayar.net.Controller.WorkOrderSchedule.dto.ReqWorkOrderScheduleGetPageDto;
import org.sayar.net.Controller.WorkOrderSchedule.dto.WorkOrderScheduleCreateDto;
import org.sayar.net.Controller.newController.dto.ReqWorkOrderForCalendarGetListDTO;
import org.sayar.net.Model.WorkOrderSchedule;
import org.sayar.net.Service.WorkOrderSchedule.WorkOrderScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/workOrderSchedule")
public class WorkOrderScheduleController {

    @Autowired
    private WorkOrderScheduleService entityService;


    @PostMapping("save")
    public ResponseEntity<?> create(@RequestBody WorkOrderScheduleCreateDto entity) {
        return ResponseEntity.ok().body(entityService.createWorkOrderSchedule(entity));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOne(@PathParam("id") String id) {
        return ResponseEntity.ok().body(entityService.getOneWorkOrderSchedule(id));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> delete(@PathParam("id") String id) {
        return ResponseEntity.ok().body(entityService.deleteWorkOrderSchedule(id));
    }

    @PutMapping("update")
    public ResponseEntity<?> update(@RequestBody WorkOrderSchedule entity) {
        return ResponseEntity.ok().body(entityService.updateWorkOrderSchedule(entity));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllType() {
        return ResponseEntity.ok().body(entityService.getAllType());
    }


    @PostMapping("get-list/for-calendar")
    public ResponseEntity<?> getListWorkOrderScheduleForCalendar( @RequestBody ReqWorkOrderForCalendarGetListDTO entity) {
        return ResponseEntity.ok().body(entityService.getListWorkOrderScheduleForCalendar(entity));
    }

    @PostMapping("get-all-for-report")
    public ResponseEntity<?> getAllType(@RequestBody ReqWorkOrderScheduleGetPageDto entity) {
        return ResponseEntity.ok().body(entityService.getAll(entity));
    }


    @PostMapping("get-page")
    public ResponseEntity<?> getPage(@RequestBody ReqWorkOrderScheduleGetPageDto entity, Pageable pageable, @RequestParam("totalElements") Integer totalElements) {
        return ResponseEntity.ok().body(entityService.getPage(entity, pageable, totalElements));
    }
}
