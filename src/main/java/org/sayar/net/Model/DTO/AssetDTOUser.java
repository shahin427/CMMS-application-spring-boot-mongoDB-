package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;

import java.util.List;

@Data
public class AssetDTOUser {
    private List<User> userList;

    public static AssetDTOUser map(List<User> userList, List<UserType> userTypeTitle) {
        System.out.println("333333333333333");
        AssetDTOUser assetDTOUser = new AssetDTOUser();
        assetDTOUser.setUserList(userList);
        return assetDTOUser;
    }
}
