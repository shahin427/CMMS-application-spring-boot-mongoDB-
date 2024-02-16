package org.sayar.net.Model.DTO;

//import org.hibernate.criterion.Projections;

public class CompanyDTO {
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String type;
    private String currencyTitle;
    public CompanyDTO() {
    }

    public CompanyDTO(String id, String currencyTitle, String name, String phoneNumber, String email, String type) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.type = type;
        this.currencyTitle=currencyTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrencyTitle() {
        return currencyTitle;
    }

    public void setCurrencyTitle(String currencyTitle) {
        this.currencyTitle = currencyTitle;
    }
}
