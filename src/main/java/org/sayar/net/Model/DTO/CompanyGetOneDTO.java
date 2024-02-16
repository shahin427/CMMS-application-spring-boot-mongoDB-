package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Document.DocumentFile;

import java.util.List;

@Data
public class CompanyGetOneDTO {
    private String id;
    private String name;
    private String code;
    private String phoneNumber;
    private String email;
    private String webSite;
    private String fax;
    private String description;
    private String type;
    private AddressDTO address;
    private String currencyId;
    private String currencyName;
    private List<DocumentFile> documents;
}
