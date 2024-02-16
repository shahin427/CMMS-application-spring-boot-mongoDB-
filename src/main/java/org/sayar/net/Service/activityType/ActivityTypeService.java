package org.sayar.net.Service.activityType;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityTypeService extends GeneralService<ActivityType> {


    ActivityType createActivityType(ActivityType entity);

    ActivityType getOneActivityType(String id);

    boolean updateActivityType(ActivityType entity);

    Boolean deleteActivityType(String id);
    Boolean checkUsed(String id);

    List<ActivityType> getAllType();

     Page<ActivityType> getPage(String term, Pageable pageable, Integer total);

}
