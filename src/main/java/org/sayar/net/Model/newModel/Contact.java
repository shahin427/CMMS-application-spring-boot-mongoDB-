package org.sayar.net.Model.newModel;

import com.fasterxml.jackson.annotation.JsonInclude;

//import javax.persistence.*;

//@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contact{

    private String phoneNumber;
    private String email;
    private String address;

    public Contact() {
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


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
