package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.ActivitySample;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.WorkRequest;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ActivitySampleWithAssetNameDTO {
    private String assetId;
    private String assetCode;
    private String assetName;
    private String instanceId;
    private MaintenanceType maintenanceType;
    private Priority priority;
    private boolean fromSchedule;
    private Date workRequestTime;
    private String workRequestTitle;
    private Date replyDate;
    private Date deliveryDate;
}
