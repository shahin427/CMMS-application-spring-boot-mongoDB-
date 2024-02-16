package org.sayar.net.Service.newService.Purchasing;


import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.newModel.Purchasing.PurchaseOrder;
import org.springframework.stereotype.Service;

@Service("purchaseOrderServiceImpl")
public class PurchaseOrderServiceImpl extends GeneralServiceImpl<PurchaseOrder> implements PurchaseOrderService {

}
