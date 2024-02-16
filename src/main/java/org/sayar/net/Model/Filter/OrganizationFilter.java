package org.sayar.net.Model.Filter;

import lombok.Data;

@Data
public class OrganizationFilter {
    private String provinceId;
    private String cityId;
    private String parentOrganId;
    private String organizationCode;
    private String organizationName;

    public enum FN {
        PROVINCEID, CITYID, PARENTORGANID, ORGANIZATIONCODE, ORGANIZATIONNAME ,USERTYPE
    }
}
