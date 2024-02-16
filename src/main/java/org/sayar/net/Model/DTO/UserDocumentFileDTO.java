package org.sayar.net.Model.DTO;

import lombok.Data;
import java.util.List;

@Data
public class UserDocumentFileDTO {
    private String userId;
    private List<String> documentIdList;
}
