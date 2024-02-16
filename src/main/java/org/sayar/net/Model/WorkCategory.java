package org.sayar.net.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkCategory {

    @Id
    private String id;
    private String name;
}
