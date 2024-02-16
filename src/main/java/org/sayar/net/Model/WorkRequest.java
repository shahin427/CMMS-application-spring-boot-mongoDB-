package org.sayar.net.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkRequest {
    //مدل درخواست کار
    private String id;
    private String description;      //شرح درخواست
    private String assetId;          //نام دستگاه
    private Date requestDate;        //تاریخ تحویل به تعمیرات
    private Date failureDate;        //تاریخ و ساعت وقوع خرابی
    private String userId;           //آیدی کاربر درخواست دهنده
    private String activityId;       //آیدی فرایند
    private boolean hasAssessment;   //ارزیابی
    private String activityInstanceId;    //آیدی فرایند در کارتابل
    private String pmSheetCode;       //شماره برگه pm
    //-----------------------------------------------
    //از اینجا به پایین رو فعلا نیاز نداریم
    private String title;
    private Priority priority;
    private MaintenanceType maintenanceType;
    private String suggestionTime;
    private WorkRequestStatus workRequestStatus;
}
