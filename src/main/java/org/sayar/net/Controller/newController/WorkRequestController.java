package org.sayar.net.Controller.newController;

import org.sayar.net.Controller.ActivitySampleController;
import org.sayar.net.Model.DTO.WorkRequestSearchDTO;
import org.sayar.net.Model.WorkRequest;
import org.sayar.net.Service.WorkRequestService;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("work-request")
public class WorkRequestController {

    @Autowired
    private WorkRequestService workRequestService;

    @Autowired
    private ActivitySampleController activitySampleController;

    @PostMapping("save")
    public void createWorkRequest(@RequestBody WorkRequest workRequest) throws ApiInputIsInComplete {
        workRequestService.createWorkRequest(workRequest);
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneWorkRequest(@PathParam("workRequestId") String workRequestId) {
        return ResponseEntity.ok().body(workRequestService.getOneWorkRequest(workRequestId));
    }

    @PostMapping("get-all")
    public ResponseEntity<?> getAllWorkRequest(Pageable pageable, Integer element,
                                               @RequestBody WorkRequestSearchDTO workRequestSearchDTO,
                                               @PathParam("userId") String userId) {
        return ResponseEntity.ok().body(workRequestService.getAllWorkRequest(pageable, element, workRequestSearchDTO, userId));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteWorkRequest(@PathParam("workRequestId") String workRequestId) {

        WorkRequest workRequest = workRequestService.getWorkRequestForActivitySampleValidation(workRequestId);
        boolean activitySampleExistence = activitySampleController.ifActivitySampleExist(workRequest.getId());
        if (!activitySampleExistence) {
            return ResponseEntity.ok().body(workRequestService.logicDeleteById(workRequestId, WorkRequest.class));
        } else {
            return ResponseEntity.ok().body("\"حذف این درخواست به دلیل قرار گرفتن در فرآیند درخواست امکان پذیر نمیباشد\"");
        }
    }

    @GetMapping("get-work-requester-specification")
    public ResponseEntity<?> getWorkRequesterSpecification(@PathParam("instanceId") String instanceId) {
        return ResponseEntity.ok().body(workRequestService.getWorkRequesterSpecification(instanceId));
    }

    @GetMapping("get-activities-of-asset")
    public ResponseEntity<?> getActivitiesOfAsset(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(workRequestService.getActivitiesOfAsset(assetId));
    }

    @GetMapping("get-work-request-of-the-user")
    public ResponseEntity<?> getWorkRequestOfTheUser(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(workRequestService.getWorkRequestOfTheUser(userId));
    }

    @GetMapping("get-requester-user")
    public ResponseEntity<?> getRequesterUser(@PathParam("requestId") String requestId) {
        return ResponseEntity.ok().body(workRequestService.getRequesterUser(requestId));
    }

    @GetMapping("get-work-request-technician")
    public ResponseEntity<?> getWorkRequestTechnician(@PathParam("workRequestId") String workRequestId) {
        return ResponseEntity.ok().body(workRequestService.getWorkRequestTechnician(workRequestId));
    }
}
