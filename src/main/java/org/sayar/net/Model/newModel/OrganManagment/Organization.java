package org.sayar.net.Model.newModel.OrganManagment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.Location.Location;
import org.sayar.net.Model.newModel.Messaging.MessageBox;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Organization implements Serializable {
    @Id
    private String id;
    private String name;
    private String organCode;
    private String parentOrganId;
    private List<String> childrenOrgan;
    private Location organLocation;
    private List<String> organUsers;
    private String cityId;
    private String provinceId;
    private List<String> userTypeList;

    public enum ME {
        id,
        name,
        organCode,
        parentOrganId,
        childrenOrgan,
        organLocation,
        organUsers,
        cityId,
        provinceId,
    }
}
