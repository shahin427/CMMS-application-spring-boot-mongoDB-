package org.sayar.net.Model.newModel;

import org.sayar.net.Model.newModel.Enum.BusinessType;
import org.springframework.data.annotation.Id;

//import javax.persistence.*;

//@Entity
public class Business {
    @Id
    private String id;
    private boolean suggest;
    private BusinessType type;
    private Company company;
    public long organId;

    public Business() {
    }

}

