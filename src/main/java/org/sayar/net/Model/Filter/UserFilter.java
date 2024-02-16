package org.sayar.net.Model.Filter;

import lombok.Data;

@Data
public class UserFilter {

    protected String username;
    private String family;
    private String name;
    private String userTypeId;
    private Long from;
    private Long until;

    public enum FN {
        USERNAME, FAMILY, NAME, USERTYPEID, FROM, UNTIL
    }

}
