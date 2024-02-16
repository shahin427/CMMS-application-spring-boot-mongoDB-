package org.sayar.net.Model.newModel;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Location.Address.Address;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company  {
    @Id
    private String id;
    private String name;
    private String code;
    private String phoneNumber;
    private String email;
    private String webSite;
    private String fax;
    private String description;
    private String type;
    private Address address;
    private String currencyId;
    private List<DocumentFile> documents;
//    @DBRef
//    private Currency currency;
//    private List<String> assets;
//    private List<String> users;
//    public String organId;
//    private List<NotificationUpload> companyUploadList;

    public Company() {
    }
}
