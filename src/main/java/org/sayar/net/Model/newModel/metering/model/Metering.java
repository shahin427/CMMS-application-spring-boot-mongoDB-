package org.sayar.net.Model.newModel.metering.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Metering {
    @Id
    private String id;
    private long amount;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date creationDate;
    private String partId;
    private UnitOfMeasurement unitOfMeasurement;
    private String referenceId;
    private boolean deleted;
    private String userId;

    public Metering() {
        this.creationDate = new Date();
    }
}
