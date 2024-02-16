package org.sayar.net.Model.newModel.Asset.Logs;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Tools.PersianCalendar;

import java.util.Date;

//import javax.persistence.*;

//@Entity
@Data
@NoArgsConstructor
public class AssetMeteReadingLog {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long amount;
    private long meteringId;

//    @OneToOne(cascade = CascadeType.MERGE)
    private UnitOfMeasurement unit;
//    @Column(columnDefinition = "VARCHAR(17) COLLATE utf8_persian_ci")
    private String date = PersianCalendar.getPersianDate(new Date());


}
