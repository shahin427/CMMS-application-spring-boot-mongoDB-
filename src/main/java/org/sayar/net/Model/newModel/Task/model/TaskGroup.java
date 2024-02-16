package org.sayar.net.Model.newModel.Task.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskGroup {
    @Id
    private String id;
    private String workOrderId;
    private String code;
    private String name;
    private List<DocumentFile> documents;
    private List<String> tasks;
}
