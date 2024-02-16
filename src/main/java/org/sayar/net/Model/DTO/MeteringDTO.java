package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.metering.model.Metering;
import org.sayar.net.Tools.Print;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MeteringDTO {
    private String id;
    private long amount;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date creationDate;
    private String partId;
    private UnitOfMeasurement unitOfMeasurement;
    private Asset asset;
    private String userId;
    private String userName;

    public static Page<MeteringDTO> map(List<Metering> meteringList, List<Asset> assetList, List<User> userList, Pageable pageable) {
        List<MeteringDTO> meteringDTOList = new ArrayList<>();
        meteringList.forEach(metering -> {
            MeteringDTO meteringDTO = new MeteringDTO();
            meteringDTO.setId(metering.getId());
            meteringDTO.setAmount(metering.getAmount());
            meteringDTO.setDescription(metering.getDescription());
            meteringDTO.setCreationDate(metering.getCreationDate());
            meteringDTO.setUnitOfMeasurement(metering.getUnitOfMeasurement());
            assetList.forEach(asset -> {
                if (asset.getId().equals(metering.getReferenceId())) {
                    meteringDTO.setAsset(asset);
                }
            });
            userList.forEach(user -> {
                if (user.getId().equals(metering.getUserId())) {
                    meteringDTO.setUserId(user.getId());
                    meteringDTO.setUserName(user.getUsername());
                }
            });
            meteringDTOList.add(meteringDTO);
        });
        int count = meteringDTOList.size();
        return new PageImpl<>(meteringDTOList, pageable, count);
    }

    public static MeteringDTO UserMap(Metering metering, User user, Asset asset) {

        MeteringDTO meteringDTO = new MeteringDTO();
        if (asset != null)
            meteringDTO.setAsset(asset);
        if (metering != null) {
            meteringDTO.setId(metering.getId());
            if (metering.getUnitOfMeasurement() != null)
                meteringDTO.setUnitOfMeasurement(metering.getUnitOfMeasurement());
            if (metering.getCreationDate() != null)
                meteringDTO.setCreationDate(metering.getCreationDate());
            if (metering.getDescription() != null)
                meteringDTO.setDescription(metering.getDescription());
            meteringDTO.setAmount(metering.getAmount());
            if (metering.getPartId() != null)
                meteringDTO.setPartId(metering.getPartId());
        }
        if (user != null && user.getUsername() != null) {
            meteringDTO.setUserId(user.getId());
            meteringDTO.setUserName(user.getUsername());
        }
        return meteringDTO;
    }
}
