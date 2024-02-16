package org.sayar.net.Dao.activityType;


import org.sayar.net.Model.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityTypeDao {


    ActivityType createActivityType(ActivityType userType);

    ActivityType getOneActivityType(String userTypeId);

    Boolean updateActivityType(ActivityType userType);

    Boolean deleteActivityType(String userTypeId);
    Boolean checkUsed(String userTypeId);

    List<ActivityType> getAllType();

    Page<ActivityType> getPage(String term, Pageable pageable, Integer total);

}
