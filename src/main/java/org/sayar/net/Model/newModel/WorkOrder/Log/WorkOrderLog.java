package org.sayar.net.Model.newModel.WorkOrder.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Enum.WorkOrderStatusEnum;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Tools.PersianCalendar;

import java.util.Date;

//import javax.persistence.*;

@Data
@NoArgsConstructor
//@Entity
public class WorkOrderLog {

    // log model for schedule maintanance and work_order

//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonIgnoreProperties(value = {
            "projects",
            "assets",
            "userCategories",
            "image",
            "userContact",
            "userType",
            "resetPasswordCode",
            "password",
            "fatherName",
            "startWork",
            "birthDay",
            "organization"},ignoreUnknown = true)
//    @OneToOne(cascade = CascadeType.MERGE)
    private User user;
//    @Column(columnDefinition = "VARCHAR(256) COLLATE utf8_persian_ci")
    private String note;
    private String date = PersianCalendar.getPersianDate(new Date());
    private long timeUsed;

//    @Enumerated(EnumType.STRING)
    private WorkOrderStatusEnum status;

    @JsonIgnoreProperties(ignoreUnknown = true)
//    @ManyToOne(cascade = CascadeType.MERGE)
    private WorkOrder workOrder;

}
