package org.sayar.net.Model.newModel.Asset.Logs;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Tools.PersianCalendar;

import java.util.Date;

//import javax.persistence.*;

//@Entity
@Data
@NoArgsConstructor
public class AssetWorkOrderDateLog {
//    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
//    @Column(columnDefinition = "TEXT COLLATE utf8_persian_ci")
    private String description;
//    @Column(columnDefinition = "VARCHAR(17) COLLATE utf8_persian_ci")
    private String completionDate = PersianCalendar.getPersianDate(new Date());

    public AssetWorkOrderDateLog(String description) {
        this.description = description;
    }
}
