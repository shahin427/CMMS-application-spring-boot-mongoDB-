package org.sayar.net.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Enumes.Type;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserType {
    //مدل پست
    @Id
    private String id;
    private String name ;
    private Type type;
    private List<String> privilege;
}
