package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class WorkOrderAccessDTO {
    private String workOrderAccessId;

    public static Object map(String workOrderAccessId) {
        WorkOrderAccessDTO workOrderAccessDTO = new WorkOrderAccessDTO();
        workOrderAccessDTO.setWorkOrderAccessId(workOrderAccessId);
        return workOrderAccessDTO;
    }
}
