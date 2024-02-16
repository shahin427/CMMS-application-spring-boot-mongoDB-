package org.sayar.net.Model.ResponseModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;


public class BaseCountModel implements Serializable{

    @JsonIgnore
    private String id;

    private String name;

    private long value;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }


}
