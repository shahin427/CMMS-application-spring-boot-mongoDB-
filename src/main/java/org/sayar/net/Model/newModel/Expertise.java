package org.sayar.net.Model.newModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.UserType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.Timestamp;

//import javax.persistence.Column;
//import javax.persistence.Entity;

//@Entity
@Data
@Document
@NoArgsConstructor
public class Expertise {
    @Id
    private String id;
    private String name;
    private String description;
    @JsonIgnore
    public String organId;
    private Expertise expertise;
    private Timestamp creationTimestamp;
    @DBRef
    private UserType userType;
}
