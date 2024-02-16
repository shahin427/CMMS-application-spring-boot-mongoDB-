package org.sayar.net.Dao.NewDao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.Part.Part;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PartDao extends GeneralDao<Part> {


//    List<Part> getPartsByAssetIdList(List<String> partListId);

    Part postPart(Part part);

    Part getPrivateSideOfPart(String partId);

    List<Part> getAllPrivateSideOfPart();

    List<Part> getAllGeneralSideOfPart();

    Part getOneGeneralSideOfPart(String partId);

    boolean checkIfCodeExists(String partCode);

    Part postPartInventory(PartDTO partDTO, String PartId);

    boolean checkIfInventoryCodeExists(String inventoryCode);

    List<Part> getAllPartsByFilterAndPagination(String term, Pageable pageable, Integer totalElement);

    long getAllCount(String term);

    List<Part> getAllPart(Pageable pageable, Integer totalElement);

    List<Part> getAllPartsById(List<String> parts);

    Part getOnePart(String partId);

    UpdateResult updateUpSide(PartPrivateDTO partPrivateDTO, String partId);

    UpdateResult updateDownSide(PartGeneralDTO partGeneralDTO, String partId);

    UpdateResult updateUserListByPartId(List<String> user, String partId);

    Part getUsersListByPartId(String partId);

    long getAllCountWithOutTerm();

    List<Part> getPartsByPartIdList(List<String> bomPartIdList);

    List<PartGetAllDTO> getAllAssignedUsersOfPartByUserId(PartDtoForSearch partDtoForSearch, Pageable pageable, Integer totalElementInteger);

    List<Part> getAssignedPartListsOfUser(String userId);

    List<PartGetAllDTO> getAllPartsWithOutInventory(PartDtoForSearch partDtoForSearch, Pageable pageable, Integer element);

    boolean checkIfUserExistsInPart(String userId);

    boolean ifCompanyExistsInPart(String companyId);

    boolean ifWareHouseExistsInPart(String storageId);

    long numberOfParts(PartDtoForSearch partDtoForSearch);

    List<Part> getAllParts();

    long numberOfUserParts(PartDtoForSearch partDtoForSearch);

    void decreaseNumberOfInventoryInPart(String partId);

    List<Part> getAllBOMPart(Pageable pageable);

    long countBOMPart();

    Part getPersonsOfPart(String partId);

    Part getGroupPersonnelOfPart(String partId);

    boolean addPersonTypePersonnel(String partId, List<AssignedToPerson> assignedToPersonList);

    boolean addGroupTypePersonnel(String partId, List<AssignedToGroup> assignedToGroupList);

    List<PartGetAllDTO> getAllAssignedPartsOfGroup(PartDtoForSearch partDtoForSearch, Pageable pageable, Integer totalElement);

    long countAllAssignedPartsOfGroup(PartDtoForSearch partDtoForSearch);

    List<Part> getUsedPartInWorkOrder(List<String> partIdList);
}