package org.sayar.net.Model.newModel.Menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.newModel.BaseOne.BaseModel1;

//import javax.persistence.Column;
//import javax.persistence.Entity;

@Data
@NoArgsConstructor
//@Entity
@JsonIgnoreProperties({"id"})
public class SubItem extends BaseModel1{
//    @Column(columnDefinition = "VARCHAR(256) COLLATE utf8_persian_ci")
    private String title;
    private String link;

    public SubItem(String title, String link) {
        this.title = title;
        this.link = link;
    }
}
