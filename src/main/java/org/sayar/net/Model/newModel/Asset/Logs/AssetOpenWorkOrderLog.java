package org.sayar.net.Model.newModel.Asset.Logs;

import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.persistence.*;

@Data
@NoArgsConstructor
//@Entity
public class AssetOpenWorkOrderLog {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    @Column(columnDefinition = "TEXT COLLATE utf8_persian_ci")
    private String description;

//    private String date;
}
