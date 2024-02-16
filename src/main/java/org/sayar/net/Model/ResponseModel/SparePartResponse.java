package org.sayar.net.Model.ResponseModel;

import java.io.Serializable;
import java.util.List;

public class SparePartResponse implements Serializable {

    private long id;
    private String name;
    private String collector;
    private String purchesingRes;
    private double cost;
    private long collectorId;
    private long purchesingResId;

    private List<Long> documentId;

    public SparePartResponse() {
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

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getPurchesingRes() {
        return purchesingRes;
    }

    public void setPurchesingRes(String purchesingRes) {
        this.purchesingRes = purchesingRes;
    }

    public long getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(long collectorId) {
        this.collectorId = collectorId;
    }

    public long getPurchesingResId() {
        return purchesingResId;
    }

    public void setPurchesingResId(long purchesingResId) {
        this.purchesingResId = purchesingResId;
    }


    public List<Long> getDocumentId() {
        return documentId;
    }

    public void setDocumentId(List<Long> documentId) {
        this.documentId = documentId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
