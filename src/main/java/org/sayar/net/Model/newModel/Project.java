package org.sayar.net.Model.newModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

//import javax.persistence.*;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project {
    @Id
    private String id;
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date actualStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date actualEndDate;
    private Date requiredCompletionDate;
    private String description;
    private String code;
    private List<DocumentFile> documents;
    private List<String> users;
    private List<AssignedToPerson> assignedToPersonList;
    private List<AssignedToGroup> assignedToGroupList;
}
