package org.sayar.net.Model.newModel.BaseOne;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

//import javax.persistence.*;
//
//@MappedSuperclass

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id",
//        scope = Long.class
//)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public abstract class BaseModel extends BaseModel1{


//    @Column(columnDefinition = "VARCHAR(256) COLLATE utf8_persian_ci")
    protected String name;

    public BaseModel() {
    }

    public BaseModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
