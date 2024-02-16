package org.sayar.net.Dao.NewDao.asset;

import lombok.Data;

@Data
public class UserAssetFilterDTO {
    private String id;
    private String name;
    private String code;
    private boolean status;
    private String assetTemplateId;
    private String assetTemplateName;
}
