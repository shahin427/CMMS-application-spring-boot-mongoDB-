package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.WorkRequest;

import java.util.Date;

@Data
public class WorkRequesterSpecificationDTO {
    private String workRequestId;
    private Date workRequestDate;
    private String userId;
    private String name;
    private String family;
    private String userTypeId;
    private String userTypeName;

    public static WorkRequesterSpecificationDTO map(WorkRequest workRequest, User requesterUser, UserType userType) {
        WorkRequesterSpecificationDTO workRequesterSpecificationDTO = new WorkRequesterSpecificationDTO();
            if (requesterUser.getId() != null)
                workRequesterSpecificationDTO.setUserId(requesterUser.getId());
            if (requesterUser.getName() != null)
                workRequesterSpecificationDTO.setName(requesterUser.getName());
            if (requesterUser.getFamily() != null)
                workRequesterSpecificationDTO.setFamily(requesterUser.getFamily());
        if (userType != null) {
            if (userType.getId() != null)
                workRequesterSpecificationDTO.setUserTypeId(userType.getId());
            if (userType.getName() != null)
                workRequesterSpecificationDTO.setUserTypeName(userType.getName());
        }
        if (workRequest != null) {
            if (workRequest.getId() != null)
                workRequesterSpecificationDTO.setWorkRequestId(workRequest.getId());
            if (workRequest.getRequestDate() != null)
                workRequesterSpecificationDTO.setWorkRequestDate(workRequest.getRequestDate());
        }
        return workRequesterSpecificationDTO;
    }
}
