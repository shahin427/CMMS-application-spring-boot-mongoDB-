package org.sayar.net.Model.newModel.BaseOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;

//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.MappedSuperclass;
//
//@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public abstract class BaseModel1 {
    @Id
    public long id;
    public BaseModel1() {
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    protected BaseModel1 validation(BaseModel1 baseModel1) {

        if (baseModel1 == null) {
            return null;
        } else {
            if (baseModel1.getId() == 0) {
                return null;
            } else {
                return baseModel1;
            }
        }
    }
}
