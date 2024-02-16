package org.sayar.net.Service.Mongo.activityServices.activity;


import org.sayar.net.Controller.newController.NewFormController;
import org.sayar.net.Dao.Mongo.activityDao.activity.ActivityDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.FormAndFormDataDTO;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.Mongo.MyModel.ActivityLevel;
import org.sayar.net.Model.Mongo.poll.controller.form.FormController;
import org.sayar.net.Model.Mongo.poll.controller.form.FormDataController;
import org.sayar.net.Model.Mongo.poll.model.form.Form;
import org.sayar.net.Model.Mongo.poll.model.form.FormData;
import org.sayar.net.Model.NewForm;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.UserTypeService;
import org.sayar.net.Service.newService.OrganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by meghdad on 5/22/17.
 */
@Service
public class ActivityServiceImpl extends GeneralServiceImpl<Activity> implements ActivityService {

    @Autowired
    final ActivityDao activityDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private OrganService organService;

    @Autowired
    private FormController formController;

    @Autowired
    private FormDataController formDataController;

    @Autowired
    private NewFormController newFormController;

    @Autowired
    public ActivityServiceImpl(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }


    @Override
    public List<Activity> getAllLight(String organCode) {

        return activityDao.getAllLight(organCode);
    }

    @Override
    public Page<Activity> findAllActivity(String term, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                activityDao.findAllActivity(term, pageable, totalElement),
                pageable,
                activityDao.countAllActivity(term)
        );
    }

    @Override
    public List<Activity> getAllLimit() {
        return activityDao.getAllLimit();
    }

    @Override
    public List<User> getAllUsersByUserTypeIdAndOrganId(String userTypeId, String organizationId) {
        return userService.getAllUsersByUserTypeIdAndOrganId(userTypeId, organizationId);
    }

    @Override
    public ConvertIdToObject getUserNameOrganisationNameAndUserTypeName(String userId, String userTypeId, String organisationId) {
        User user = userService.getOneByUserId(userId);
        UserType userType = userTypeService.getUserTypeByUSerTypeId(userTypeId);
        Organization organization = organService.getOneOrganisation(organisationId);
        return ConvertIdToObject.map(user, userType, organization);
    }

    @Override
    public List<Activity> getAllActivity() {
        return activityDao.getAllActivity();
    }

    @Override
    public Form getFormByActivityId(String activityId) {
        Activity activity = activityDao.geFormIdByActivityId(activityId);
        if (activity != null) {
            ActivityLevel firstActivityLevel = activity.getActivityLevelList().get(0);
            if (firstActivityLevel != null && firstActivityLevel.getFormId() != null) {
                String formId = firstActivityLevel.getFormId().toString();
                return formController.getFirstActivityLevelForm(formId);
            } else return null;
        } else
            return null;
    }

    @Override
    public List<Activity> getAssociatedActivityOfWorkRequest(List<String> activityIdList) {
        return activityDao.getAssociatedActivityOfWorkRequest(activityIdList);
    }

    @Override
    public List<Activity> getActivityOfPendingUser(List<String> activityIdList) {
        return activityDao.getActivityOfPendingUser(activityIdList);
    }

    @Override
    public Form getEachActivityLevelForm(String activityId, String activityLevelId) {
        List<ActivityLevel> activityLevelList = activityDao.getEachActivityLevelForm(activityId, activityLevelId);
        String formId = "";
        activityLevelList.forEach(activityLevel -> {
//              formId =activityLevel.getForm().
        });
        return formController.getFormOfActivityByFormId(formId);
    }

    @Override
    public void changeMentionedActivityFirsStepStatus(String activityId) {
        activityDao.changeMentionedActivityFirsStepStatus(activityId);
    }

    @Override
    public Activity getActivityByActivityId(String activityId) {
        return activityDao.getActivityByActivityId(activityId);
    }

    @Override
    public FormAndFormDataDTO getFormAndFormDataOfTheActivityLevel(String formId, String formDataId) {
//        NewForm form = formController.getFormForFormAndFormDataDTO(formId);
        NewForm form = newFormController.getFormForFormAndFormDataDTO(formId);
        FormData formData = formDataController.getFormDataForFormAndFormDataDTO(formDataId);
        return FormAndFormDataDTO.map(form, formData);
    }

    @Override
    public boolean saveWorkOrderIdInActivity(String activityInstanceId, String workOrderId) {
        return this.updateResultStatus(activityDao.saveWorkOrderIdInActivity(activityInstanceId, workOrderId));
    }

    @Override
    public List<Activity> getListOfActivityForArrivedTimeScheduleMaintenance(List<String> activityId) {
        return activityDao.getListOfActivityForArrivedTimeScheduleMaintenance(activityId);
    }

    @Override
    public List<Activity> getActivitiesOfAsset(List<String> activityIdList) {
        return activityDao.getActivitiesOfAsset(activityIdList);
    }

    @Override
    public boolean checkIfUserExistsActivity(String userId) {
        return activityDao.checkIfUserExistsActivity(userId);
    }

    @Override
    public Activity getWorkRequestActivity(String activityId) {
        return activityDao.getWorkRequestActivity(activityId);
    }

    @Override
    public Activity getActivityTitle(String id) {
        return activityDao.getActivityTitle(id);
    }
}

