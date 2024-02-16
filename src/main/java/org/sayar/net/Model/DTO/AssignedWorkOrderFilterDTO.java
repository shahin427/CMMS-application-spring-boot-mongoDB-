package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignedWorkOrderFilterDTO {
    private String id;
    private String title;
    private String code;
    private String statusId;
    private String statusName;
    private Priority priority;
    private String projectId;
    private String projectName;
    private String assetId;
    private String assetName;
    private Date startDate;
    private Date endDate;
    private MaintenanceType maintenanceType;
    private boolean fromSchedule;

}
