package com.example.a1.himaster.SKPlanet.Tmap;

/**
 * Created by a1 on 2017. 8. 22..
 */

public class MapPoint {
    private String name;
    private double lat;
    private double lon;

    public MapPoint() {
        super();
    }

    public MapPoint(String name, double lat, double lon) {

        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

}
