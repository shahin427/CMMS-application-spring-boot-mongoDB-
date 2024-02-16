package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;

import java.util.ArrayList;
import java.util.List;

@Data
public class TechnicianIdAndNameDTO {
    private String userId;
    private String userName;
    private String userFamily;
    private String workOrderId;

    public static List<TechnicianIdAndNameDTO> map(WorkOrder workOrder, List<User> technicians) {
        List<TechnicianIdAndNameDTO> technicianIdAndNameDTOS = new ArrayList<>();
        technicians.forEach(tec -> workOrder.getUserIdList().forEach(user -> {
            if (tec.getId().equals(user)) {
                TechnicianIdAndNameDTO technicianIdAndNameDTO = new TechnicianIdAndNameDTO();
                technicianIdAndNameDTO.setUserId(tec.getId());
                technicianIdAndNameDTO.setUserName(tec.getName());
                technicianIdAndNameDTO.setUserFamily(tec.getFamily());
                technicianIdAndNameDTO.setWorkOrderId(workOrder.getId());
                technicianIdAndNameDTOS.add(technicianIdAndNameDTO);
            }
        }));
        return technicianIdAndNameDTOS;
    }
}
