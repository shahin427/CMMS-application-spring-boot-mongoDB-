package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.CandidateMode;
import java.util.List;

@Data
public class UserIdAndUserTypeIdDTO {
    private List<UserIdAndUserNameDTO> userIdAndUserNameDTOList;
    private String organizationId;
    private String organizationName;
    private String userTypeId;
    private String userTypeName;
    private CandidateMode candidateMode;
    private boolean existRecipientOrderUser;
}
