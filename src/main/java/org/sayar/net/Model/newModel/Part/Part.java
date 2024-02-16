package org.sayar.net.Model.newModel.Part;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.newModel.Image;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Part {
    //مدل قطعات
    @Id
    private String id;
    private String name;   //نام قطعه
    private String description;   //توضیحات
    private String partCode;   //کد قطعه
    private String currentQuantity;
    private String minQuantity;
    //    @DBRef
    private String chargeDepartmentId;
    //    @DBRef
    private String budgetId;
    private String corridor;
    private String row;
    private String partModel;
    //    @DBRef
    private List<String> usersIdList;
    private String warehouse;
    //    @DBRef
    private List<String> warrantyId;
    //    @DBRef
    private String meteringId;
    //    private UploadAndDownloadFile uploadFile;
    //    @DBRef
    private String companyId;
    private Integer numberOfInventory = 0;
    private Image image;
    private List<AssignedToPerson> assignedToPersonList;
    private List<AssignedToGroup> assignedToGroupList;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date registrationDate;
}

