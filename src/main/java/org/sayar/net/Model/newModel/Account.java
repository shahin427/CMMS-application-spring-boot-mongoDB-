package org.sayar.net.Model.newModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.BaseOne.BaseModel;

import java.io.Serializable;

//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.OneToOne;

//@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class Account extends BaseModel implements Serializable {

//    @Column(columnDefinition = "VARCHAR(20)")
    private String code;
//    @Column(columnDefinition = "TEXT COLLATE utf8_persian_ci")
    private String description;
    private double value;

    @JsonIgnore
    public long organId;

    public Account() {
    }

    public Account(long id, String name) {
        super(id, name);
    }


}
