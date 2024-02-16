package org.sayar.net.Controller.newController;

import org.sayar.net.Model.DTO.ActivityLevelStringDTO;
import org.sayar.net.Model.newModel.ActivityHistory;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("activity-history")
public class ActivityHistoryController {

    @Autowired
    private MongoOperations mongoOperations;


    public ActivityLevelStringDTO getPendingAndActiveActivityLevel(String activityId) {
        Print.print("wuwuwu", activityId);
        Query query = new Query();
        query.addCriteria(Criteria.where("active").is(true));
        query.addCriteria(Criteria.where("activityId").is(activityId));
        ActivityHistory activityHistory = mongoOperations.findOne(query, ActivityHistory.class);
        Print.print("activityhistoori", activityHistory);
        int numberOfPendingUser = 0;
        if (activityHistory != null) {
            Print.print("sizeee", activityHistory.getActivityLevelUserIdList().size());
            for (int i = 0; i < activityHistory.getActivityLevelUserIdList().size(); i++) {
                if (activityHistory.getActivityLevelUserIdList().get(i).equals(activityHistory.getPendingUserId())) {
                    numberOfPendingUser = i;
                    System.out.println("iiiiiiii" + i);
                }
            }
            System.out.println("ferffe");
            List<String> activeUserIdList = new ArrayList<>();
            if (numberOfPendingUser != 0) {
                for (int i = 0; i < numberOfPendingUser; i++) {
                    activeUserIdList.add(activityHistory.getActivityLevelUserIdList().get(i));
                }
                Print.print("idLList", activeUserIdList);
                return ActivityLevelStringDTO.map(activeUserIdList, activityHistory.getPendingUserId());
            } else {
                System.out.println("second");
                ActivityLevelStringDTO activityLevelStringDTO = new ActivityLevelStringDTO();
                activityLevelStringDTO.setPendingUserId(activityHistory.getPendingUserId());
                activityLevelStringDTO.setActiveUserIdList(null);
                Print.print("feferf", activityLevelStringDTO);
                return activityLevelStringDTO;
            }
        } else
            return null;
    }
}
