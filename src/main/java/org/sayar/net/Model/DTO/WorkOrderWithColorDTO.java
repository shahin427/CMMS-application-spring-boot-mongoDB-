package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class WorkOrderWithColorDTO {
    private String workOrderId;
    private String workOrderTitle;
    private String workOrderCode;
    private Date workOrderCompletionDate;
    private boolean late;

    public static List<WorkOrderWithColorDTO> map(List<WorkOrder> workOrderList) {
        List<WorkOrderWithColorDTO> workOrderWithColorDTOList = new ArrayList<>();
        workOrderList.forEach(workOrder -> {
            WorkOrderWithColorDTO workOrderWithColorDTO = new WorkOrderWithColorDTO();
            workOrderWithColorDTO.setWorkOrderCode(workOrder.getCode());
            workOrderWithColorDTO.setWorkOrderId(workOrder.getId());
            workOrderWithColorDTO.setWorkOrderTitle(workOrder.getTitle());
            workOrderWithColorDTO.setWorkOrderCompletionDate(workOrder.getRequiredCompletionDate());
            if (workOrder.getRequiredCompletionDate().after(new Date())) {
                workOrderWithColorDTO.setLate(false);
            } else {
                workOrderWithColorDTO.setLate(true);
            }
            workOrderWithColorDTOList.add(workOrderWithColorDTO);
        });
        return workOrderWithColorDTOList;

    }
}
