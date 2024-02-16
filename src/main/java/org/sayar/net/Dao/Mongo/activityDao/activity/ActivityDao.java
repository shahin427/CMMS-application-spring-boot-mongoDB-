package org.sayar.net.Dao.Mongo.activityDao.activity;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.DTO.FormAndFormDataDTO;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.Mongo.MyModel.ActivityLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by meghdad on 5/22/17.
 */
public interface ActivityDao extends GeneralDao<Activity> {
//    List<Trip> getAllRequestOfACompany(String companyId);
//    int countPlanTypeUsedActivity(String activityId);

    List<Activity> getAllLight(String organCode);

    List<Activity> findAllActivity(String term, Pageable pageable, Integer totalElement);

    List<Activity> getAllLimit();

    List<Activity> getAllActivity();

    Activity geFormIdByActivityId(String activityId);

    List<Activity> getAssociatedActivityOfWorkRequest(List<String> activityIdList);

    List<Activity> getActivityOfPendingUser(List<String> activityIdList);

    Activity getNeededActivityByActivityId(String activityId);

    List<ActivityLevel> getEachActivityLevelForm(String activityId, String activityLevelId);

    void changeMentionedActivityFirsStepStatus(String activityId);

    Activity getActivityByActivityId(String activityId);

    UpdateResult saveWorkOrderIdInActivity(String activityInstanceId, String workOrderId);

    List<Activity> getListOfActivityForArrivedTimeScheduleMaintenance(List<String> activityId);

    List<Activity> getActivitiesOfAsset(List<String> activityIdList);

    boolean checkIfUserExistsActivity(String userId);

    Activity getWorkRequestActivity(String activityId);

    Activity getActivityTitle(String id);

    long countAllActivity(String term);
}

