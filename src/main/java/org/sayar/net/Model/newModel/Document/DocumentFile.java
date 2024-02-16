package org.sayar.net.Model.newModel.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentFile {
    @Id
    private String id;
    private String extraId;
    private byte[] fileByte;
    private String fileName;
    private String fileContentType;
    private String companyId;
    private String userId;
    private String showName;
    private boolean forSchedule;
}
