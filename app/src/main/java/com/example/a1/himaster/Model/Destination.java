package com.example.a1.himaster.Model;

/**
 * Created by a1 on 2017. 8. 30..
 */

public class Destination {

    private String departLat;
    private String departLon;
    private String destLat;
    private String destLon;
    private String totalTimeStr;
    private int totalTime;

    public String getDepartLat() {
        return departLat;
    }

    public void setDepartLat(String departLat) {
        this.departLat = departLat;
    }

    public String getDepartLon() {
        return departLon;
    }

    public void setDepartLon(String departLon) {
        this.departLon = departLon;
    }

    public String getDestLat() {
        return destLat;
    }

    public void setDestLat(String destLat) {
        this.destLat = destLat;
    }

    public String getDestLon() {
        return destLon;
    }

    public void setDestLon(String destLon) {
        this.destLon = destLon;
    }

    public String getTotalTimeStr() {
        return totalTimeStr;
    }

    public void setTotalTimeStr(String totalTimeStr) {
        this.totalTimeStr = totalTimeStr;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTimeStr) {
        int totalTimeInt = Integer.valueOf(totalTimeStr);
        this.totalTime = totalTimeInt;
    }

}
