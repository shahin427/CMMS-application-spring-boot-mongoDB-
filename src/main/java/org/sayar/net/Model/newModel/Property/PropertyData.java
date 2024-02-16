package org.sayar.net.Model.newModel.Property;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.newModel.BaseOne.BaseModel1;

import java.util.List;

//import javax.persistence.*;

//@Entity
@Data
@NoArgsConstructor
public class PropertyData extends BaseModel1{

//    private long assetId;
    private long propertyId;
//    @ElementCollection
//    @CollectionTable(name="data", joinColumns=@JoinColumn(name="property_data_id"))
//    @Column(name="data_col",columnDefinition = "VARCHAR(256) COLLATE utf8_persian_ci")
    private List<String> data;

    public PropertyData(long propertyId, List<String> data) {
        this.propertyId = propertyId;
        this.data = data;
    }
}
