package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Model.UsedPart;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewSaveDTO {
    private String id;
    private String title;                              //شرح درخواست خرابی
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;                           //تاریخ وقوع خرابی
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;                             //تاریخ راه اندازی
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date requestedDate;                       //تاریخ تحویل به تعمیرات
    private String requestDescription;                //شرح درخواست
    private String failureReason;                     //علت خرابی
    private String solution;                          //شرح اقدامات انجام شده
    private List<String> userIdList;                  //تعمیرکار
    private String assetId;                           //نام دستگاه
    private Date repairDate;                          //تاریخ شروع تعمیر
    private String activityId;
    private List<UsedPart> usedPartList;
    private long partSupply;                           //زمان تامین قطعه
    private Long failureDuration;
    private Long repairDuration;
    private String pmSheetCode;
}
