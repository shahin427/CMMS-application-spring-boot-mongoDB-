package org.sayar.net.Model.Test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;


public class CityD{

    @Id
    private String id;
    private String googleLongNameFA;
    private String googleLongNameEN;
    private GoogleMap googleMap;
    private String googleShortNameEN;
    private String googleShortNameFA;
    private String title;
    @JsonIgnoreProperties(value = {"cityList"})
    private ProvinceD province;

    public CityD() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoogleLongNameFA() {
        return googleLongNameFA;
    }

    public void setGoogleLongNameFA(String googleLongNameFA) {
        this.googleLongNameFA = googleLongNameFA;
    }

    public String getGoogleLongNameEN() {
        return googleLongNameEN;
    }

    public void setGoogleLongNameEN(String googleLongNameEN) {
        this.googleLongNameEN = googleLongNameEN;
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

    public ProvinceD getProvince() {
        return province;
    }

    public void setProvince(ProvinceD province) {
        this.province = province;
    }
}
