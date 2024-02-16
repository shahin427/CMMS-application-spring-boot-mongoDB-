package org.sayar.net.Dao;

import org.sayar.net.Controller.newController.NewWorkRequestDTO;
import org.sayar.net.Model.DTO.WorkRequestSearchDTO;
import org.sayar.net.Model.WorkRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkRequestDao {

    WorkRequest createWorkRequest(WorkRequest workRequest, String activityInstanceId);

    NewWorkRequestDTO getOneWorkRequest(String workRequestId);

    List<WorkRequest> getAllWorkRequest(Pageable pageable,Integer element,WorkRequestSearchDTO workRequestSearchDTO, String userId);

    long getNumberOfWorkRequestPage(WorkRequestSearchDTO workRequestSearchDTO, String userId);

    WorkRequest getWorkRequestForActivitySampleValidation(String workRequestId);

    WorkRequest getWorkRequesterSpecification(String instanceId);

    List<WorkRequest> getRelevantWorkRequests(List<String> relevantWorkRequestId);

    List<WorkRequest> getWorkRequestOfTheUser(String userId);

    boolean ifAssetExistsInWorkRequest(String assetId);

    WorkRequest getRequesterUser(String requestId);

    void changeWorkRequestInProcessStatusToFalse(String workRequestId);

    void changeWorkRequestStatus(String workRequestId);

    void workRequestStatusIsInProcess(String workRequestId);

    void setAssessmentTrue(String workRequestId);

    void setAssessmentFalse(String workRequestId);
}
