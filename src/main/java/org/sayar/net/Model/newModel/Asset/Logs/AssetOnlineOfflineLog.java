package org.sayar.net.Model.newModel.Asset.Logs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.User;

//import javax.persistence.*;

@Data
@NoArgsConstructor
//@Entity
public class AssetOnlineOfflineLog {

//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    @OneToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "user_online_id")
    @JsonIgnoreProperties({
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
            "nationalCode","laborTasks"
    })
    private User userOnline;

//    @Column(columnDefinition = "VARCHAR(17) COLLATE utf8_persian_ci")
    private String dataOnline;
//    @OneToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties({
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
    })
//    @JoinColumn(name = "user_offline_id")
    private User userOffline;
//    @Column(columnDefinition = "VARCHAR(17) COLLATE utf8_persian_ci")
    private String dateOffline;

}
