package org.sayar.net.Model.DTO;

import org.springframework.data.annotation.Id;

import java.util.List;

public class purchaseAssetDTO {
    @Id
    private String id;
    private String title;
    private String code;
    private String chargeDepartmentId;
    private List<String> users;

    public purchaseAssetDTO(String id, String title, String code, String chargeDepartmentId, List<String> users) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.chargeDepartmentId = chargeDepartmentId;
        this.users = users;
    }
    public purchaseAssetDTO(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChargeDepartmentId() {
        return chargeDepartmentId;
    }

    public void setChargeDepartmentId(String chargeDepartmentId) {
        this.chargeDepartmentId = chargeDepartmentId;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
