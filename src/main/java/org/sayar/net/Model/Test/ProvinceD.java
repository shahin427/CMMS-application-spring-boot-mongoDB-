package org.sayar.net.Model.Test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

import java.util.List;

//import javax.persistence.Entity;
//import javax.persistence.Table;


public class ProvinceD  {

    @Id
    private String id;

    @JsonIgnoreProperties(value = {"province"})
    private List<CityD> cityList;
    private String googleLongNameEN;
    private String googleLongNameFA;
    private GoogleMap googleMap;
    private String googleShortNameEN;
    private String googleShortNameFA;
    private String title;

    public ProvinceD() {
    }

    public ProvinceD(String id, List<CityD> cityList, String googleLongNameEN, String googleLongNameFA, GoogleMap googleMap, String googleShortNameEN, String googleShortNameFA, String title) {
        this.id = id;
        this.cityList = cityList;
        this.googleLongNameEN = googleLongNameEN;
        this.googleLongNameFA = googleLongNameFA;
        this.googleMap = googleMap;
        this.googleShortNameEN = googleShortNameEN;
        this.googleShortNameFA = googleShortNameFA;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CityD> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityD> cityList) {
        this.cityList = cityList;
    }

    public String getGoogleLongNameEN() {
        return googleLongNameEN;
    }

    public void setGoogleLongNameEN(String googleLongNameEN) {
        this.googleLongNameEN = googleLongNameEN;
    }

    public String getGoogleLongNameFA() {
        return googleLongNameFA;
    }

    public void setGoogleLongNameFA(String googleLongNameFA) {
        this.googleLongNameFA = googleLongNameFA;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public String getGoogleShortNameEN() {
        return googleShortNameEN;
    }

    public void setGoogleShortNameEN(String googleShortNameEN) {
        this.googleShortNameEN = googleShortNameEN;
    }

    public String getGoogleShortNameFA() {
        return googleShortNameFA;
    }

    public void setGoogleShortNameFA(String googleShortNameFA) {
        this.googleShortNameFA = googleShortNameFA;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
