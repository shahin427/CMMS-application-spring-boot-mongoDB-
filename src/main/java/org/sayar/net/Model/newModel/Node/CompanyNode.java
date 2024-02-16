package org.sayar.net.Model.newModel.Node;

public class CompanyNode {

    private long id;
    private String name;
    private String code;
    private String phoneNumber;
    private String email;
    private String webSite;
    private String fax;
    private String description;
//    private List<CompanyNode> children = new ArrayList<>();

    public CompanyNode() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public List<CompanyNode> getChildren() {
//        return children;
//    }
//
//    public void setChildren(List<CompanyNode> children) {
//        this.children = children;
//    }
}
