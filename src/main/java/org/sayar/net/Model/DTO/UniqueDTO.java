package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Storage;

import java.util.ArrayList;
import java.util.List;

@Data
public class UniqueDTO {
    private String id;
    private String code;
    private String title;

    public static List<UniqueDTO> map(List<Storage> storageList) {
        List<UniqueDTO> uniqueDTOList = new ArrayList<>();
        storageList.forEach(storage -> {
            UniqueDTO uniqueDTO = new UniqueDTO();
            if (storage != null) {
                if (storage.getTitle() != null) {
                    uniqueDTO.setTitle(storage.getTitle());
                }
                if (storage.getId() != null) {
                    uniqueDTO.setId(storage.getId());
                }
                if (storage.getCode() != null) {
                    uniqueDTO.setCode(storage.getCode());
                }
                uniqueDTOList.add(uniqueDTO);
            }
        });
        return uniqueDTOList;
    }
}
