package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class WorkOrderMiscCostDTO {
    private String workOrderId;
    private List<String> miscCostList;
}
