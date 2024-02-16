package org.sayar.net.Controller.newController;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;

import java.util.Date;

@Data
public class NewWorkRequestDTO {
    private String description;
    private String assetId;
    private String assetName;
    private Date failureDate;
    private String userName;
    private String userFamily;
    private String activityId;
    private String activityName;
//-----------------------------------

    private String id;
    private String assetCode;
    private Priority priority;
    private MaintenanceType maintenanceType;
    private String title;

//    public static NewWorkRequestDTO map(WorkRequest workRequest, Asset workRequestAsset, User workRequestUser,
//                                        List<UserType> userTypeListOfUser, Activity workRequestActivity) {
//
//        NewWorkRequestDTO newWorkRequestDTO = new NewWorkRequestDTO();
//        if (workRequest.getId() != null)
//            newWorkRequestDTO.setId(workRequest.getId());
//        if (workRequestAsset != null) {
//            if (workRequestAsset.getName() != null)
//                newWorkRequestDTO.setAssetName(workRequestAsset.getName());
//            if (workRequestAsset.getCode() != null)
//                newWorkRequestDTO.setAssetCode(workRequestAsset.getCode());
//            if (workRequestAsset.getId() != null)
//                newWorkRequestDTO.setAssetId(workRequest.getAssetId());
//        }
//        if (workRequest.getDescription() != null)
//            newWorkRequestDTO.setDescription(workRequest.getDescription());
//        if (workRequest.getMaintenanceType() != null)
//            newWorkRequestDTO.setMaintenanceType(workRequest.getMaintenanceType());
//        if (workRequest.getPriority() != null)
//            newWorkRequestDTO.setPriority(workRequest.getPriority());
//        if (workRequest.getTitle() != null)
//            newWorkRequestDTO.setTitle(workRequest.getTitle());
//        if (workRequestUser != null) {
//            if (workRequestUser.getName() != null)
//                newWorkRequestDTO.setUserName(workRequestUser.getName());
//            if (workRequestUser.getFamily() != null)
//                newWorkRequestDTO.setUserFamily(workRequestUser.getFamily());
//        }
//        if (workRequestActivity != null) {
//            if (workRequestActivity.getId() != null)
//                newWorkRequestDTO.setActivityId(workRequestActivity.getId());
//            if (workRequestActivity.getTitle() != null)
//                newWorkRequestDTO.setActivityName(workRequestActivity.getTitle());
//        }
//        List<String> userTypesName = new ArrayList<>();
//        userTypeListOfUser.forEach(uTy -> {
//            userTypesName.add(uTy.getName());
//        });
//        newWorkRequestDTO.setUserType(userTypesName);
//        return newWorkRequestDTO;
//    }
}
