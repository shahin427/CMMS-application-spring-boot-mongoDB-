package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTOWithUserTypeDTO {
    private String id;
    private String name;
    private String family;
    private String userTypeId;
    private String userTypeName;
//    private String orgName;
//    private List<String> userTypeNameList;


//    public static UserDTOWithUserTypeDTO map(User parentOfTheUser, List<UserType> userTypeOfTheUser) {
//
//        UserDTOWithUserTypeDTO userDTOWithUserTypeDTO = new UserDTOWithUserTypeDTO();
//        if (parentOfTheUser.getId() != null)
//            userDTOWithUserTypeDTO.setId(parentOfTheUser.getId());
//        if (parentOfTheUser.getName() != null)
//            userDTOWithUserTypeDTO.setName(parentOfTheUser.getName());
//        if (parentOfTheUser.getFamily() != null)
//            userDTOWithUserTypeDTO.setFamily(parentOfTheUser.getFamily());
//        if (userTypeOfTheUser != null) {
//            List<String> uTypeNameList = new ArrayList<>();
//            userTypeOfTheUser.stream().filter(uTypeOfTheUser -> uTypeNameList.add(uTypeOfTheUser.getName())).findFirst().orElse(null);
//            userDTOWithUserTypeDTO.setUserTypeNameList(uTypeNameList);
//        }
//        return userDTOWithUserTypeDTO;
//    }

    public static List<UserDTOWithUserTypeDTO> map2(List<User> userList, List<UserType> userTypeList) {
        List<UserDTOWithUserTypeDTO> userDTOWithUserTypeDTOList = new ArrayList<>();
        userList.forEach(user -> {
            UserDTOWithUserTypeDTO userDTOWithUserTypeDTO = new UserDTOWithUserTypeDTO();
            userDTOWithUserTypeDTO.setId(user.getId());
            userDTOWithUserTypeDTO.setName(user.getName());
            userDTOWithUserTypeDTO.setFamily(user.getFamily());
            userTypeList.forEach(userType -> {
//                if (user.getUserTypeId() != null && user.getUserTypeId().equals(userType.getId())) {
//                    userDTOWithUserTypeDTO.setUserTypeName(userType.getName());
//                }
                //TODO commented part was red
            });
            userDTOWithUserTypeDTOList.add(userDTOWithUserTypeDTO);
        });
        return userDTOWithUserTypeDTOList;
    }
}
