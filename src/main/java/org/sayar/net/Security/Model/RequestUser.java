package org.sayar.net.Security.Model;

import org.sayar.net.Model.newModel.OrganManagment.Organization;

//import javax.persistence.*;

//@Entity
public class RequestUser {

//    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
//    @NotNull
    private String name;
//    @NotNull
    private String family;
//    @NotNull
    private String username;
//    @NotNull
    private String email;
//    @NotNull
    private String phone;

//    @NotNull
//    @OneToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "organ_id")
    private Organization organization;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
