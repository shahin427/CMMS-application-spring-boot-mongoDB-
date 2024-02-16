package org.sayar.net.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Certification {
    @Id
    private String id;
    private String userId;
    private String name;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date validFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date validFor;
    private List<DocumentFile> certificateDocumentList;
}
