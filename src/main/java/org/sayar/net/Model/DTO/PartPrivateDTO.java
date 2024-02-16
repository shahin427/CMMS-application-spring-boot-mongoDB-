package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Part.Part;
import org.sayar.net.Tools.Print;

import java.util.ArrayList;
import java.util.List;

@Data
public class PartPrivateDTO {
    private String id;
    private String name;
    private String description;
    private String partCode;
    private DocumentFile image;
    private int numberOfInventory;

    public static List<PartPrivateDTO> map(List<Part> entityList) {

        List<PartPrivateDTO> result = new ArrayList<>();
        entityList.forEach(entity -> {
            PartPrivateDTO partPrivateDTO = new PartPrivateDTO();
            partPrivateDTO.setNumberOfInventory(entity.getNumberOfInventory());
            partPrivateDTO.setId(entity.getId());
            partPrivateDTO.setName(entity.getName());
//            partPrivateDTO.setDescription(entity.getPartCode());
//            partPrivateDTO.setImage(entity.getImage());
            partPrivateDTO.setPartCode(entity.getPartCode());
//            System.out.println(entity.getPartCode());
            result.add(partPrivateDTO);
        });
        Print.print(result);
        return result;
    }


}
