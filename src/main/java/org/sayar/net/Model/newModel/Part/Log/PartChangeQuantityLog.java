package org.sayar.net.Model.newModel.Part.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.User;
import org.sayar.net.Tools.PersianCalendar;

import java.util.Date;

//import javax.persistence.*;

@Data
@NoArgsConstructor
//@Entity
public class PartChangeQuantityLog {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
            "nationalCode"
    },ignoreUnknown = true)
//    @OneToOne(cascade = CascadeType.MERGE)
    private User user;
    private long previousCount;
    private long newCount;
//    @Column(columnDefinition = "VARCHAR(17) COLLATE utf8_persian_ci")
    private String date= PersianCalendar.getPersianDate(new Date());

}

