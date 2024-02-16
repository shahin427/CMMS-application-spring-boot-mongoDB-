package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Task.model.Task;

import java.util.List;

@Data
public class TaskGroupDTO {
    private String id;
    private String code;
    private String name;
    private List<TaskTitleDescriptionDTO> taskList;
}
