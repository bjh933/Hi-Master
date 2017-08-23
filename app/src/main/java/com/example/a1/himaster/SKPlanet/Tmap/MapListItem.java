package com.example.a1.himaster.SKPlanet.Tmap;

import java.io.Serializable;

public class MapListItem implements Serializable {
    private String name;  // 주소
    private String address;   // 상세주소
    private Double lat;
    private Double lon;

    public MapListItem(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public MapListItem(String name, String address, Double lat, Double lon) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }



}

