package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.sayar.net.Model.newModel.Enum.ScheduledFixType;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.model.ScheduleType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ScheduleMaintenanceBackup {
    private String id;
    private String title;
    private String assetId;
    private MaintenanceType maintenanceType;
    private String statusId;
    private Integer per = 0;
    private Integer perTime = 0;
    private String scheduleMaintenanceId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;    //this start shows the start of child scheduleMaintenance of mainScheduleMaintenance
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;    //this endDate shows the end of main scheduleMaintenance
    private ScheduledFixType scheduledFixType;
    private TimeType cycle;
    private DocumentFile image;
    private Priority priority;
    private String projectId;
    private List<String> taskGroupList;
    private List<String> taskList;
    private DocumentFile documentFile;
    private int laborHour = 0;
    private int actualLaborHour = 0;
    private String workInstruction;
    private String note;
    private String problem;
    private String rootCause;
    private String solution;
    private String adminNote;
    private List<String> taskIdList;
    private boolean expired = false;
    private boolean withoutExpireDate;
    private long endDistance = 0;
    private long startDistance = 0;
    private FixType fixType;
    private String unitOfMeasurementId;
    private ScheduleType scheduleType;
    private long nextTriggerThreshold;
    private boolean doneForDistance = false;
    private String code;
    private String budgetId;
    private String chargeDepartmentId;
    private String userAssignedId;
    private String completedUserId;
    private String issueSummary;
    private boolean deleted;
    private long numberOfDayForEndingEachScheduleMaintenance = 0;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDateOfMainScheduleMaintenanceProject;  //this field shows the creation time of scheduleMaintenanceTime not scheduleMaintenance
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDateOfEachScheduleMaintenance; // it's not important
    private long spentDistance;
    private String activityId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date creationDate;
    private boolean active;
    List<String> usedParts;
    List<String> documentsList;
}
