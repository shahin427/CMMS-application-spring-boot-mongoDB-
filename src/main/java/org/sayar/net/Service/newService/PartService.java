package org.sayar.net.Service.newService;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.PartPersonnelDTO;
import org.sayar.net.Model.newModel.Part.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

//import org.sayar.net.General.service.GeneralService;

public interface PartService extends GeneralService<Part> {

    Part postPart(Part part);

    Part getPrivateSideOfPart(String partId);

    List<Part> getAllPrivateSideOfPart();

    List<PartGeneralDTO> getAllGeneralSideOfPart();

    Part getOneGeneralSideOfPart(String partId);

    boolean checkIfCodeExist(String partCode);

    Part postPartInventory(PartDTO part, String PartId);

    boolean checkIfInventoryCodeExists(String inventoryCode);

    Page<Part> getAllPartsByFilterAndPagination(String term, Pageable pageable, Integer totalElement);

    Part getOnePart(String partId);

    UpdateResult updateUpSide(PartPrivateDTO partPrivateDTO, String partId);

    UpdateResult updateDownSide(PartGeneralDTO partGeneralDTO, String partId);

    boolean updateUserListByPartId(List<String> user, String partId);

    List<PartUserDTO> getUsersListByPartId(String partId);

    List<Part> getPartsByPartIdList(List<String> bomPartIdList);

    Page<PartGetAllDTO> getAllAssignedUsersOfPartByUserId(PartDtoForSearch partDtoForSearch, Pageable pageable, Integer totalElement);

    List<Part> getAssignedPartListsOfUser(String userId);

    Page<PartGetAllDTO> getAllPartsWithOutInventoryAndLoadedInventories(PartDtoForSearch partDtoForSearch, Pageable pageable, Integer element);

    boolean checkIfUserExistsInPart(String userId);

    boolean ifCompanyExistsInPart(String companyId);

    boolean ifWareHouseExistsInPart(String storageId);

    SimpleStorageAndPartsDTO getAllParts();

    void addOneToNumberOfInventoryOfPart(String partId);

    void decreaseNumberOfInventoryInPart(String partId);

    Page<Part> getAllBOMPart(Pageable pageable);

    Part getPartCode(String partId);

    List<PartPersonnelDTO> getPersonsOfPart(String partId);

    List<PartGroupPersonnelDTO>  getGroupPersonnelOfPart(String partId);

    boolean addPersonTypePersonnel(String partId, List<AssignedToPerson> assignedToPersonList);

    boolean addGroupTypePersonnel(String partId, List<AssignedToGroup> assignedToGroupList);

    Page<PartGetAllDTO> getAllAssignedPartsOfGroup(PartDtoForSearch partDtoForSearch, Pageable pageable, Integer totalElement);

    List<Part> getUsedPartInWorkOrder(List<String> partIdList);

    List<Part> getAllByIdList(List<String> idList);
}