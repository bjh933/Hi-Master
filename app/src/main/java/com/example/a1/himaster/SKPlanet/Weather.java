package com.example.a1.himaster.SKPlanet;

/**
 * Created by a1 on 2017. 8. 20..
 */

public class Weather {

    private String temp = "0";
    private String tomorrowTmax = "0";
    private String tomorrowTmin = "0";
    private String dayAfterTmax = "0";
    private String dayAfterTmin = "0";
    private String tmax = "0";
    private String tmin = "0";
    private String cloud = "none";
    private String sky = "none";
    private String name10hour = "";
    private String name31hour = "";
    private String name46hour = "";
    private String dayStatus = "";
    private String dayTomorrowStatus = "";
    private String dayAfterStatus = "";

    private static Weather instance;
    private Weather(){
    }

    public static Weather getInstance(){
        if (instance == null)
            instance = new Weather();
        return instance;
    }

    public String getTemperature() {
        return temp;
    }
    public void setTemperature(String temp) {
        this.temp = temp;
    }

    public String getDayTomorrowStatus() {return dayTomorrowStatus;}
    public void setDayTomorrowStatus(String dayTomorrowStatus) { this.dayTomorrowStatus = dayTomorrowStatus; }

    public String getDayAfterStatus() {return dayAfterStatus;}
    public void setDayAfterStatus(String dayAfterStatus) { this.dayAfterStatus = dayAfterStatus; }

    public String getTomorrowTmax() {return tomorrowTmax;}
    public void setTomorrowTmax(String tomorrowTmax) {
        this.tomorrowTmax = tomorrowTmax;
    }

    public String getDayAfterTmax() {
        return dayAfterTmax;
    }
    public void setDayAfterTmax(String dayAfterTmax) {
        this.dayAfterTmax = dayAfterTmax;
    }

    public String getTomorrowTmin() {return tomorrowTmin;}
    public void setTomorrowTmin(String tomorrowTmin) {
        this.tomorrowTmin = tomorrowTmin;
    }

    public String getDayAfterTmin() {return dayAfterTmin;}
    public void setDayAfterTmin(String dayAfterTmin) {
        this.dayAfterTmin = dayAfterTmin;
    }

    public String getDayStatus() {
        return dayStatus;
    }
    public void setDayStatus(String dayStatus) {
        this.dayStatus = dayStatus;
    }

    public String getTodayTmax() {
        return tmax;
    }
    public void setTodayTmax(String tmax) {
        this.tmax = tmax;
    }

    public String getTodayTmin() {
        return tmin;
    }
    public void setTodayTmin(String tmin) {
        this.tmin = tmin;
    }

    public String getCloud() {
        return cloud;
    }
    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getSky() { return sky; }
    public void setSky(String sky) {
        this.sky = sky;
    }


}
