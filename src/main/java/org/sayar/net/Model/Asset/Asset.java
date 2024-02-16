package org.sayar.net.Model.Asset;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Enumes.AssetPriority;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Location.Address.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Asset {
    //مدل دارایی
    @Id
    private String id;
    private String name;    //نام
    private String description;    //توضیحات
    private Boolean status;      //وضعیت
    public CategoryType categoryType;      //نوع دارایی (نوع)
    private String code;        //کد
    private AssetPriority assetPriority;   //اولویت دارایی (اولویت)
    private String rootId;        //آیدی اولین پرنت دارایی اگه root نوشته شده باشه یعنی خودش اولین پرنت هست
    private String isPartOfAsset;    //این تجهیز بخشی از تجهیز دیگر است (ایدی پرنت نوشته میشه)
    private List<String> activityIdList;    //ایدی فرایند تعمیر
    private DocumentFile image;     //تصویر دارایی
    private Boolean hasChild;    //دارایی فرزند دارد یا نه
    private String categoryId;    //آیدی دسته بند دارایی
    private String budgetId;     //آیدی بودجه تخصیص یافته
    private String chargeDepartmentId;   //آیدی دپارتمان مسئول
    private List<String> unitIdList;     //آیدی واحد اندازه گیری
    private List<Property> propertyList;   //مشخصات
    private List<DocumentFile> documentList;  //مستندات
    private Address address;    //آدرس محل دارایی
    private String note;         //یادداشت
    private List<String> parts;   //قطعات
    private List<AssignedToPerson> assignedToPersonList;   //پرسنل تخصیص به شخص
    private List<AssignedToGroup> assignedToGroupList;    //پرسنل تخصیص به گروه


    private String assetTemplateId;
    private List<String> companyList;
    private List<String> users;
    private String termsOfUse;
    private String maintenance;
    private String parent;
    private List<String> assets;
    private List<String> warranties;
    private List<String> purchases;
    private List<String> calibrations;
    private List<String> meterings;
    @Indexed(direction = IndexDirection.DESCENDING)
    private Long minQuantity;
    private Long quantity;
    private Long creationTime;
    private Long meterAmount;
    private Long lastWorkOrderDate;
    private String orgId;
    private Boolean deleted;
    private List<String> storageIdList;
    private String firstParentBuilding;


    public enum ME {
        id,
        title,
        assetTemplateId,
        description,
        assetTemplate,
        properties,
        parentCategoryId,
        subCategoryId,
        orgId,
        status,
        images, chargeDepartmentId,
        budgetId,
        budgetUsed,
        companyList,
        backupCompanies,
        users,
        code,
        note,
        termsOfUse,
        maintenance,
        parent,
        assets,
        warranties, purchases,
        calibrations,
        meterings,
        repairSchedulingLogs,
        consumableLogs,
        onOffLogs, openWorkOrderLogs, workOrderDateLogs,
        meteReadingLogs,
        scheduleMaintananceLogs,
        smallFileList,
        minQuantity,
        quantity,
        deleted,
        creationTime,
        meterAmount,
        lastWorkOrderDate,
        unitIdList,
        isPartOfAsset,
        assetPriority
    }
}
