package org.sayar.net.Model.newModel.OrganManagment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.sayar.net.Model.newModel.Location.Address.Address;
import org.springframework.data.annotation.Id;

//import javax.persistence.*;

//@Entity
public class RequestOrgan {
      @Id
    private String id;
    private String type;   // must be change an upload enum for this field
    private long userNumber;
    private String name;
    private String phone;
    private String email;

    @JsonIgnoreProperties(ignoreUnknown = true)
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "address_id")
    private Address address;

    public RequestOrgan() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(long userNumber) {
        this.userNumber = userNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
