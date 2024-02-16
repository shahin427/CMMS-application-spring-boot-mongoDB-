package org.sayar.net.Dao;

import lombok.Data;
import org.sayar.net.Enumes.Type;

import java.util.List;
 @Data
public class UserTypeDTO {
    private String id;
    private String name;
    private Type type;
    private List<String> privilege;
}
