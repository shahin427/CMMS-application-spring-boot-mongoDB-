package org.sayar.net.Model.newModel.Purchasing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.BaseOne.BaseModel1;
import org.sayar.net.Model.newModel.Enum.PurchaseOrderType;

//import javax.persistence.*;

//@Entity
@Data
@NoArgsConstructor
public class ActivityLog extends BaseModel1 {

//    @Column(columnDefinition = "TEXT  COLLATE utf8_persian_ci")
    private String comment;
//    @Column(columnDefinition = "VARCHAR(20)")
    private String date;

    @JsonIgnoreProperties(value = {
            "nationalCode",
            "organization",
            "workOrder",
            "notificationToken",
            "projects",
            "assets",
            "userCategories",
            "image",
            "userContact",
            "userType",
            "resetPasswordCode",
            "fatherName",
            "startWork",
            "birthDay",
            "nationalCode"},ignoreUnknown = true)
//    @OneToOne
//    @JoinTable(
//            name = "middle_activitylog_user",
//            joinColumns = {@JoinColumn(name = "activitylog_id")},
//            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private User user;
//    @Enumerated(EnumType.STRING)
    private PurchaseOrderType fromStatus;
//    @Enumerated(EnumType.STRING)
    private PurchaseOrderType toStatus;
}
