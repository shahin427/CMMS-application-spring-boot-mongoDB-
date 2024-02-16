package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewGetOneٔWorkOrderDTO {
    private String id;
    private String title;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;                                                  //تاریخ وقوع خرابی
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;                                                    //تاریخ راه اندازی
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date requestedDate;                                              //تاریخ تحویل به تعمیرات
    private String requestDescription;                                       //شرح درخواست
    private String failureReason;                                            //علت خرابی
    private String solution;                                                 //شرح اقدامات انجام شده
    private List<String> userIdList = new ArrayList<>();                     //ایدی تعمیرکار
    //    private List<UserWithUserTypeNameDTO> UserWithUserTypeNameDTO;     //تعمیرکار
    private String assetId;                                                  //ایدی دستگاه
    private String assetName;                                                //نام دستگاه
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date repairDate;                                                 //تاریخ شروع تعمیر
    private List<GetOneUsedPart> getOneUsedPartList;
    private long partSupply;
    private String workRequestId;
    private String pmSheetCode;                                              //شماره برگه pm

    public static NewGetOneٔWorkOrderDTO map(WorkOrderPrimaryDTO workOrderPrimaryDTO, List<GetOneUsedPart> getOneUsedPartList) {
        NewGetOneٔWorkOrderDTO newGetOneٔWorkOrderDTO = new NewGetOneٔWorkOrderDTO();
        newGetOneٔWorkOrderDTO.setId(workOrderPrimaryDTO.getId());
        newGetOneٔWorkOrderDTO.setWorkRequestId(workOrderPrimaryDTO.getWorkRequestId());
        newGetOneٔWorkOrderDTO.setTitle(workOrderPrimaryDTO.getTitle());
        newGetOneٔWorkOrderDTO.setStartDate(workOrderPrimaryDTO.getStartDate());
        newGetOneٔWorkOrderDTO.setRepairDate(workOrderPrimaryDTO.getRepairDate());
        newGetOneٔWorkOrderDTO.setEndDate(workOrderPrimaryDTO.getEndDate());
        newGetOneٔWorkOrderDTO.setRequestedDate(workOrderPrimaryDTO.getRequestedDate());
        newGetOneٔWorkOrderDTO.setRequestDescription(workOrderPrimaryDTO.getRequestDescription());
        newGetOneٔWorkOrderDTO.setFailureReason(workOrderPrimaryDTO.getFailureReason());
        newGetOneٔWorkOrderDTO.setSolution(workOrderPrimaryDTO.getSolution());
        newGetOneٔWorkOrderDTO.setPartSupply(workOrderPrimaryDTO.getPartSupply());
        newGetOneٔWorkOrderDTO.setUserIdList(workOrderPrimaryDTO.getUserIdList());
        newGetOneٔWorkOrderDTO.setAssetId(workOrderPrimaryDTO.getAssetId());
        newGetOneٔWorkOrderDTO.setAssetName(workOrderPrimaryDTO.getAssetName());
        newGetOneٔWorkOrderDTO.setGetOneUsedPartList(getOneUsedPartList);
        newGetOneٔWorkOrderDTO.setPmSheetCode(workOrderPrimaryDTO.getPmSheetCode());
        return newGetOneٔWorkOrderDTO;
    }

//    public NewGetOneٔWorkOrderDTO map(NewGetOneٔWorkOrderDTO newGetOneٔWorkOrderDTO, List<User> userList, List<UserType> userTypeList) {
//        List<UserWithUserTypeNameDTO> userWithUserTypeNameDTOList = new ArrayList<>();
//        userList.forEach(user -> {
//            UserWithUserTypeNameDTO userWithUserTypeNameDTO = new UserWithUserTypeNameDTO();
//            userWithUserTypeNameDTO.setId(user.getId());
//            userWithUserTypeNameDTO.setName(user.getName());
//            userWithUserTypeNameDTO.setFamily(user.getFamily());
//            userWithUserTypeNameDTO.setUserTypeId(user.getUserTypeId());
//            userTypeList.forEach(userType -> {
//                if (user.getUserTypeId().equals(userType.getId())) {
//                    userWithUserTypeNameDTO.setUserTypeName(userType.getName());
//                }
//            });
//            userWithUserTypeNameDTOList.add(userWithUserTypeNameDTO);
//        });
//        newGetOneٔWorkOrderDTO.setUserWithUserTypeNameDTO(userWithUserTypeNameDTOList);
//        return newGetOneٔWorkOrderDTO;
//    }
}
