package org.sayar.net.Model.DTO;

import org.sayar.net.Controller.UploadAndDownloadFile;
import org.sayar.net.Model.Asset.Budget;
import org.sayar.net.Model.Asset.ChargeDepartment;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Company;
import org.sayar.net.Model.newModel.Part.Part;
import org.sayar.net.Model.newModel.metering.model.Metering;

import java.util.ArrayList;
import java.util.List;

public class PartGeneralDTO {
    private String id;
    private String quantity;
    private String minQuantity;
    private ChargeDepartment chargeDepartment;
    private Budget budget;
    private String corridor;
    private String row;
    private String warehouse;
    private String price;
    private String inventoryCode;
    private User user;
    private Metering metering;
    private UploadAndDownloadFile uploadFile;
    private Company company;

    public PartGeneralDTO() {
    }

    public PartGeneralDTO(String id, String quantity, String minQuantity, ChargeDepartment chargeDepartment, Budget budget, String corridor, String row, String warehouse, String price, String inventoryCode, User user, Metering metering, UploadAndDownloadFile uploadFile, Company company) {
        this.id = id;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.chargeDepartment = chargeDepartment;
        this.budget = budget;
        this.corridor = corridor;
        this.row = row;
        this.warehouse = warehouse;
        this.price = price;
        this.inventoryCode = inventoryCode;
        this.user = user;
        this.metering = metering;
        this.uploadFile = uploadFile;
        this.company = company;
    }

    public static List<PartGeneralDTO> map(List<Part> entityList) {
        List<PartGeneralDTO> result=new ArrayList<>();
        entityList.forEach(entity->{
            PartGeneralDTO partGeneralDTO = new PartGeneralDTO();
            partGeneralDTO.setBudget(partGeneralDTO.getBudget());
            partGeneralDTO.setChargeDepartment(partGeneralDTO.getChargeDepartment());
            partGeneralDTO.setCompany(partGeneralDTO.getCompany());
            partGeneralDTO.setCorridor(partGeneralDTO.getCorridor());
            partGeneralDTO.setInventoryCode(partGeneralDTO.getInventoryCode());
            partGeneralDTO.setId(partGeneralDTO.getId());
            partGeneralDTO.setMetering(partGeneralDTO.getMetering());
            partGeneralDTO.setPrice(partGeneralDTO.getPrice());
            partGeneralDTO.setQuantity(partGeneralDTO.getQuantity());
            partGeneralDTO.setMinQuantity(partGeneralDTO.getMinQuantity());
            partGeneralDTO.setRow(partGeneralDTO.getRow());
            partGeneralDTO.setWarehouse(partGeneralDTO.getWarehouse());
            partGeneralDTO.setUploadFile(partGeneralDTO.getUploadFile());
            partGeneralDTO.setUser(partGeneralDTO.getUser());
              result.add(partGeneralDTO);
        });
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(String minQuantity) {
        this.minQuantity = minQuantity;
    }

    public ChargeDepartment getChargeDepartment() {
        return chargeDepartment;
    }

    public void setChargeDepartment(ChargeDepartment chargeDepartment) {
        this.chargeDepartment = chargeDepartment;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public String getCorridor() {
        return corridor;
    }

    public void setCorridor(String corridor) {
        this.corridor = corridor;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Metering getMetering() {
        return metering;
    }

    public void setMetering(Metering metering) {
        this.metering = metering;
    }

    public UploadAndDownloadFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadAndDownloadFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
