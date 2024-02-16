package org.sayar.net.Service.Mongo.activityServices.activity;




import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.FormAndFormDataDTO;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.Mongo.poll.model.form.Form;
import org.sayar.net.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by meghdad on 5/22/17.
 */
//@Service("activityService")
public interface ActivityService extends GeneralService<Activity> {

    List<Activity> getAllLight(String organCode);

    Page<Activity> findAllActivity(String term,Pageable pageable, Integer totalElement);

    List<Activity> getAllLimit();

    List<User> getAllUsersByUserTypeIdAndOrganId(String userTypeId, String organizationId);

    ConvertIdToObject getUserNameOrganisationNameAndUserTypeName(String userId, String userTypeId, String organisationId);

    List<Activity> getAllActivity();

    Form getFormByActivityId(String activityId);

    List<Activity> getAssociatedActivityOfWorkRequest(List<String> activityIdList);

    List<Activity> getActivityOfPendingUser(List<String> activityIdList);

    Form getEachActivityLevelForm(String activityId, String activityLevelId);

    void changeMentionedActivityFirsStepStatus(String activityId);

    Activity getActivityByActivityId(String activityId);

    FormAndFormDataDTO getFormAndFormDataOfTheActivityLevel(String formId, String formDataId);

    boolean saveWorkOrderIdInActivity(String activityInstanceId, String workOrderId);

    List<Activity> getListOfActivityForArrivedTimeScheduleMaintenance(List<String> activityId);

    List<Activity> getActivitiesOfAsset(List<String> activityIdList);

    boolean checkIfUserExistsActivity(String userId);

    Activity getWorkRequestActivity(String activityId);

    Activity getActivityTitle(String id);
}

