package org.sayar.net.Controller.newController;

import org.sayar.net.Model.newModel.Notify;
import org.sayar.net.Service.newService.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("notify")
public class NotifyController {
    @Autowired
    private NotifyService notifyService;

    @GetMapping("get-one")
    public ResponseEntity<?> findOne(@PathParam("notifyId") String notifyId) {
        return ResponseEntity.ok().body(notifyService.getOneById(notifyId));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(notifyService.findAllNotLogicDeleted(Notify.class));
    }

    @PostMapping("save")
    public ResponseEntity<?> postNotify(@RequestBody Notify notify) {
        return ResponseEntity.ok().body(notifyService.postNotify(notify));
    }

    @PutMapping("update")
    public ResponseEntity<?> update(@RequestBody Notify notify, @PathParam("notifyId") String notifyId) {
        return ResponseEntity.ok().body(notifyService.updateNotify(notify, notifyId));
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@PathParam("notifyId") String notifyId) {
        return ResponseEntity.ok().body(notifyService.logicDeleteById(notifyId, Notify.class));
    }

    @GetMapping("get-notify-list-by-reference-id")
    public ResponseEntity<?> getNotifyListByReferenceId(@PathParam("referenceId") String referenceId) {
        return ResponseEntity.ok().body(notifyService.getNotifyListByReferenceId(referenceId));
    }

    @PutMapping("update-notify-of-schedule-maintenance")
    public ResponseEntity<?> updateNotifyOfScheduleMaintenance(@RequestBody Notify notify) {
        return ResponseEntity.ok().body(notifyService.updateNotifyOfScheduleMaintenance(notify));
    }

    @PostMapping("save-schedule-maintenance-notify")
    public ResponseEntity<?> saveScheduleMaintenanceNotify(@RequestBody Notify notify) {
        return ResponseEntity.ok().body(notifyService.saveScheduleMaintenanceNotify(notify));
    }

}
