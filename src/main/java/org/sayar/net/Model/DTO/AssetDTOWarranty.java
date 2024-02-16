package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.Warranty;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssetDTOWarranty {
    private String id;
    private List<Warranty> warrantyList;

    public static List<AssetDTOWarranty> map(List<Asset> entityList, List<Warranty> warrantyList) {
        List<AssetDTOWarranty> result = new ArrayList<>();
        entityList.forEach(asset -> {
            AssetDTOWarranty assetDTOWarranty = new AssetDTOWarranty();
            List<Warranty> warrantyList1 = new ArrayList<>();
            warrantyList.forEach(warranty -> {
                if (warranty.getAssetId().equals(asset.getId()))
                    warrantyList1.add(warranty);
            });
            assetDTOWarranty.setWarrantyList(warrantyList1);
            result.add(assetDTOWarranty);
        });
        return result;

//        warrantyList.forEach(warranty -> {
//            AssetDTOWarranty assetDTOWarranty=new AssetDTOWarranty();
//            assetDTOWarranty.setId();
//        });


    }
}
