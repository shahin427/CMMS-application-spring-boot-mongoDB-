package org.sayar.net.Model.newModel.Purchasing;

import lombok.Data;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Enum.PurchaseOrderType;
import org.sayar.net.Model.newModel.Messaging.Message;
import org.springframework.data.annotation.Id;
import java.util.List;

@Data
public class PurchaseOrder {
    @Id
    private String id;
    private String expectedDeliveringDate;
    private String commentToAttach;
    private PurchaseOrderType status;
    private ShippingInformation shippingInformation;
    private List<AdditionalCost> additionalCost;
    private List<ActivityLog> activityLogs;
    private List<PurchaseItem> purchaseItems;
    private List<DocumentFile> documents;
    private List<Message> messages;

    public PurchaseOrder() {
    }


}
