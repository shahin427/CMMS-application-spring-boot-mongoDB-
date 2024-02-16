package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.WorkRequest;
import org.sayar.net.Model.WorkRequestStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkRequestDTO {

    private String id;
    private String assetId;
    private String assetName;
    private String activityId;
    private String activityName;
    private String workRequestTitle;
    private Date workRequestTime;
    private WorkRequestStatus workRequestStatus;
    private boolean hasAssessment;


    public static List<WorkRequestDTO> map(List<WorkRequest> workRequestPage,
                                           List<Asset> assetList,
                                           List<Activity> activityList) {

        List<WorkRequestDTO> workRequestDTOList = new ArrayList<>();
        workRequestPage.forEach(singleWorkRequest -> {
            WorkRequestDTO workRequest = new WorkRequestDTO();
            workRequest.setId(singleWorkRequest.getId());
            workRequest.setWorkRequestTitle(singleWorkRequest.getTitle());
            workRequest.setWorkRequestTime(singleWorkRequest.getRequestDate());
            workRequest.setHasAssessment(singleWorkRequest.isHasAssessment());
            workRequest.setWorkRequestStatus(singleWorkRequest.getWorkRequestStatus());
            assetList.forEach(asset -> {
                if (asset.getId().equals(singleWorkRequest.getAssetId())) {
                    workRequest.setAssetName(asset.getName());
                }
            });
            activityList.forEach(activity -> {
                if (activity.getId().equals(singleWorkRequest.getActivityId())) {
                    workRequest.setActivityName(activity.getTitle());
                }
            });

            workRequestDTOList.add(workRequest);
        });
        return workRequestDTOList;
    }
}
