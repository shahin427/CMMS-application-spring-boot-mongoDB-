package org.sayar.net.Service;

import org.sayar.net.Controller.newController.NewWorkRequestDTO;
import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.WorkRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkRequestService extends GeneralService<WorkRequest> {

    void createWorkRequest(WorkRequest workRequest);

    NewWorkRequestDTO getOneWorkRequest(String workRequestId);

    Page<WorkRequestDTO> getAllWorkRequest(Pageable pageable, Integer element, WorkRequestSearchDTO workRequestSearchDTO, String userId);

    WorkRequest getWorkRequestForActivitySampleValidation(String workRequestId);

    WorkRequesterSpecificationDTO getWorkRequesterSpecification(String instanceId);

    List<Activity> getActivitiesOfAsset(String assetId);

    List<WorkRequest> getRelevantWorkRequests(List<String> relevantWorkRequestId);

    List<WorkRequest> getWorkRequestOfTheUser(String userId);

    boolean ifAssetExistsInWorkRequest(String assetId);

    UserDetailAndUserTypeDTO getRequesterUser(String requestId);

    void changeWorkRequestInProcessStatusToFalse(String workRequestId);

    void changeWorkRequestStatus(String workRequestId);

    void workRequestStatusIsInProcess(String workRequestId);

    void setAssessmentTrue(String workRequestId);

    List<TechnicianIdAndNameDTO> getWorkRequestTechnician(String workRequestId);

    void setAssessmentFalse(String workRequestId);
}
