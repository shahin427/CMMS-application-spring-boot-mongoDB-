package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service;

import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduledTime;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.dao.ScheduleMaintenanceCalender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static org.sayar.net.Model.newModel.Enum.TimeCycle.*;

@RestController
public class ScheduleMaintenanceCalenderController {
    @Autowired
    private MongoOperations mongoOperations;

    public List<ScheduleMaintenanceCalender> getTriggeredScheduleMaintenanceInSpecifiedTime(Date dueDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("scheduleMaintenanceTriggerDateList").is(dueDate));
        query.fields().include("scheduleMaintenanceId");
        return mongoOperations.find(query, ScheduleMaintenanceCalender.class);
    }

    public void changeScheduleMaintenanceCalendarDate(ScheduledTime scheduledTime, String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        boolean status = mongoOperations.exists(query, ScheduleMaintenanceCalender.class);

        List<Date> datesInRange = new ArrayList<>();
        ScheduleMaintenanceCalender scheduleMaintenanceCalender = new ScheduleMaintenanceCalender();
        if (scheduledTime.getCycle().equals(DAILY)) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(scheduledTime.getStartOn());

            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(scheduledTime.getEndOn());
            while (calendar.before(endCalendar)) {
                datesInRange.add(calendar.getTime());
                calendar.add(Calendar.DATE, scheduledTime.getPer());
            }
            scheduleMaintenanceCalender.setScheduleMaintenanceId(scheduleMaintenanceId);
            scheduleMaintenanceCalender.setScheduleMaintenanceTriggerDateList(datesInRange);
        }
        if (scheduledTime.getCycle().equals(WEEKLY)) {
            Calendar calendar = new GregorianCalendar();
            int per = 7 * scheduledTime.getPer();
            calendar.setTime(scheduledTime.getStartOn());

            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(scheduledTime.getEndOn());
            while (calendar.before(endCalendar)) {
                datesInRange.add(calendar.getTime());
                calendar.add(Calendar.DATE, per);
            }
            scheduleMaintenanceCalender.setScheduleMaintenanceId(scheduleMaintenanceId);
            scheduleMaintenanceCalender.setScheduleMaintenanceTriggerDateList(datesInRange);
        }

        if (scheduledTime.getCycle().equals(MONTHLY)) {
            System.out.println("entered monthly");
            int per = 30 * scheduledTime.getPer();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(scheduledTime.getStartOn());

            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(scheduledTime.getEndOn());
            while (calendar.before(endCalendar)) {
                datesInRange.add(calendar.getTime());
                calendar.add(Calendar.DATE, per);
            }
            scheduleMaintenanceCalender.setScheduleMaintenanceId(scheduleMaintenanceId);
            scheduleMaintenanceCalender.setScheduleMaintenanceTriggerDateList(datesInRange);
        }

        if (scheduledTime.getCycle().equals(YEARLY)) {
            System.out.println("entered yearly");

            int per = 365 * scheduledTime.getPer();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(scheduledTime.getStartOn());

            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(scheduledTime.getEndOn());
            while (calendar.before(endCalendar)) {
                datesInRange.add(calendar.getTime());
                calendar.add(Calendar.DATE, per);
            }
            scheduleMaintenanceCalender.setScheduleMaintenanceId(scheduleMaintenanceId);
            scheduleMaintenanceCalender.setScheduleMaintenanceTriggerDateList(datesInRange);
        }
        if (!status) {
            mongoOperations.save(scheduleMaintenanceCalender);
        } else {
            Update update = new Update();
            update.set("scheduleMaintenanceTriggerDateList", datesInRange);
            mongoOperations.updateFirst(query, update, ScheduleMaintenanceCalender.class);
        }
    }

    public void deleteScheduleMaintenanceCalendar(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        mongoOperations.remove(query, ScheduleMaintenanceCalender.class);
    }

    public List<Date> getFutureDatesOfScheduleMaintenance(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        ScheduleMaintenanceCalender scheduleMaintenanceCalender = mongoOperations.findOne(query, ScheduleMaintenanceCalender.class);
        if (scheduleMaintenanceCalender != null)
            return scheduleMaintenanceCalender.getScheduleMaintenanceTriggerDateList();
        else
            return null;
    }
}
