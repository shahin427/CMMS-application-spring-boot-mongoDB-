package org.sayar.net.Model.newModel.Asset.Logs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;

//import javax.persistence.*;
//
//@Entity
@Data
@NoArgsConstructor
public class AssetConsumablePartLog {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonIgnoreProperties({
            "logs",
            "taskGroups",
            "laborTasks",
            "documents",
            "notifications",
            "miscCosts",
            "meterReadings",
            "workOrderParts",
            "workOrderTasks",
            "completedUser",
            "userAssigned",
            "chargeDepartment",
            "account",
            "project",
            "asset",
            "status",
            "image",
            "maintenanceType",
            "priority","adminNote","Solution","rootCause","problem","note","workInstruction",""})
//    @OneToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "work_order_id")
    private WorkOrder workOrder;

    @JsonIgnoreProperties({
            "businesses",
            "documents",
            "warranties",
            "meterings",
            "users",
            "parts",
            "parent","image","chargeDepartment","assetLocation","account","unit"})
//    @OneToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "part_id")
    private int count;
}
