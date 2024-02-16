package org.sayar.net.Controller.newController;

import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintenanceBackupService;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Service.DocumentService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("document")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private ScheduleMaintenanceBackupService scheduleMaintenanceBackupService;

    @GetMapping("get-all-by-extra-id")
    public ResponseEntity<?> getAllByExtraId(@PathParam("extraId") String extraId) {
        return ResponseEntity.ok().body(documentService.getAllByExtraId(extraId));
    }

    @GetMapping("get-all-by-user-id")
    public ResponseEntity<?> getAllByUserId(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(documentService.getAllByUserId(userId));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteDocument(@PathParam("documentId") String documentId) {
        return ResponseEntity.ok().body(documentService.logicDeleteById(documentId, DocumentFile.class));
    }

    @GetMapping("get-all-documents-of-work-order-by-extra-id")
    public ResponseEntity<?> getAllDocumentsOfWorkOrderByExtraId(@PathParam("extraId") String extraId) {
        WorkOrder workOrder = workOrderService.getDocumentIdListOfWorkOrder(extraId);
        if (workOrder.getDocumentsList() != null) {
            return ResponseEntity.ok(documentService.getAllDocumentsOfWorkOrderByExtraId(workOrder.getDocumentsList()));
        } else {
            return null;
        }
    }

    @DeleteMapping("delete-schedule")
    public ResponseEntity<?> deleteSchedule(@PathParam("documentId") String documentId, @PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        scheduleMaintenanceBackupService.deleteDocumentFromBackupSchedule(documentId, scheduleMaintenanceId);
        return ResponseEntity.ok().body(documentService.logicDeleteById(documentId, DocumentFile.class));
    }

    public List<DocumentFile> getDocumentsOfRepository(List<String> documentsList) {
        return documentService.getDocumentsOfRepository(documentsList);
    }
}
