package org.sayar.net.Model.newModel.Asset.Logs;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Tools.PersianCalendar;

import java.util.Date;

//import javax.persistence.*;

//@Entity
@Data
@NoArgsConstructor
public class AssetRepairSchedulingLog {

//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    @Column(columnDefinition = "VARCHAR(17) COLLATE utf8_persian_ci")
    private String date = PersianCalendar.getPersianDate(new Date());
//    @Column(columnDefinition = "TEXT COLLATE utf8_persian_ci")
    private String description;
}
//INSERT INTO `asset_repair_scheduling_log` ( `date`, `description`) VALUES ( '1396/11/20', 'sdfghj');

// INSERT INTO `asset_repair_logs` (asset_id,repair_logs_id) VALUES (1,1)