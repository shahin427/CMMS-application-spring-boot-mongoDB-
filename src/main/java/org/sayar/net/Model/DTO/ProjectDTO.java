package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO implements Serializable {
    private String id;
    private String name;
    private String code;
    private Date startDate;
    private Date endDate;
    private Date actualStartDate;
    private Date actualEndDate;
    private Date requiredCompletionDate;
}
