package org.sayar.net.Model.newModel.purchase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.sayar.net.Model.DTO.CompanyDTO;
import org.sayar.net.Model.DTO.UserDTO;
import org.sayar.net.Model.DTO.purchaseAssetDTO;
import org.sayar.net.Model.newModel.Contact;
import org.sayar.net.Model.newModel.Enum.PurchaseOrderType;
import org.sayar.net.Model.newModel.Purchasing.AdditionalCost;
import org.sayar.net.Model.newModel.Purchasing.PurchaseShipping;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.File;
import java.util.Date;
import java.util.List;

//import javax.persistence.*;


//@Entity
@Data
@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Purchase {

    @Id
    private String id;
    private purchaseAssetDTO purchaseAssetDTO;
    private UserDTO requester;
    private List<CompanyDTO> supplier;
    private long requiredQuantity;
    private long unitPrice;
    private long totalCost;
    private Date expectedDeliverTime;
    private List<AdditionalCost> additionalCost;
    private String purchaseCode;
    private Contact contact;
    private PurchaseOrderType status;
    private PurchaseShipping shipping;
    private File file;
    private boolean deleted=false;
     public enum FN {
         id,
        purchaseAssetDTO,
         requester,
         supplier,
         requiredQuantity,
         unitPrice,
         totalCost,
         expectedDeliverTime,
         additionalCost,
         currency,
         purchaseCode,
         contact,
         status,
         shipping,
         file,
         deleted
    }
}
