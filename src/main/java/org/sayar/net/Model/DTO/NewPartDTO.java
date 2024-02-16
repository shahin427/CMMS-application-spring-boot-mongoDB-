package org.sayar.net.Model.DTO;
import lombok.Data;
import org.sayar.net.Controller.UploadAndDownloadFile;
import org.sayar.net.Model.Asset.Budget;
import org.sayar.net.Model.Asset.ChargeDepartment;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Company;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Warranty;
import org.sayar.net.Model.newModel.metering.model.Metering;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
public class NewPartDTO {
    private String id;
    private String name;
    private String description;
    private String code;
    private DocumentFile image;
    private String quantity;
    private String minQuantity;
    private ChargeDepartment chargeDepartment;
    private Budget budget;
    private String corridor;
    private String row;
    private String partModel;
    private String price;
    private String inventoryCode;
    private List<User> users;
    private String warehouse;
    private List<Warranty> warranty;
    private Metering metering;
    private UploadAndDownloadFile uploadFile;
    private Company company;
    private int numberOfInventory;
}
