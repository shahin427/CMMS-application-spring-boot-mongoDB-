package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class PartGetAllDTO {
    private String currentQuantity;
    private String partId;
    private String partCode;
    private String partName;

//    public static List<PartGetAllDTO> map(List<Part> partList) {
//        List<PartGetAllDTO> partGetAllDTOS = new ArrayList<>();
//        partList.forEach(part -> {
//            PartGetAllDTO partGetAllDTO = new PartGetAllDTO();
//            partGetAllDTO.setCurrentQuantity(part.getCurrentQuantity());
//            partGetAllDTO.setPartId(part.getId());
//            partGetAllDTO.setPartCode(part.getPartCode());
//            partGetAllDTO.setPartName(part.getName());
//            partGetAllDTOS.add(partGetAllDTO);
//        });
//        return partGetAllDTOS;
//    }
}
