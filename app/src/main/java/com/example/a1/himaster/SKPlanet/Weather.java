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
    private String day4Tmax = "";
    private String day5Tmax = "";
    private String day6Tmax = "";
    private String day7Tmax = "";
    private String day4Tmin = "";
    private String day5Tmin = "";
    private String day6Tmin = "";
    private String day7Tmin = "";
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
    private String day4Status = "";
    private String day5Status = "";
    private String day6Status = "";
    private String day7Status = "";



    public String getTemperature() {
        return temp;
    }
    public void setTemperature(String temp) {
        this.temp = temp;
    }

    public String getDayTomorrowStatus() {return dayTomorrowStatus;}
    public void setDayTomorrowStatus(String dayTomorrowStatus) { this.dayTomorrowStatus = dayTomorrowStatus; }

    public String getTomorrowTmax() {return tomorrowTmax;}
    public void setTomorrowTmax(String tomorrowTmax) {
        this.tomorrowTmax = tomorrowTmax;
    }

    public String getDayAfterStatus() {return dayAfterStatus;}

    public void setDayAfterStatus(String dayAfterStatus) { this.dayAfterStatus = dayAfterStatus; }

    public String getDayAfterTmax() {return dayAfterTmax;}
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

    public String getDay4Tmax() {
        return day4Tmax;
    }

    public void setDay4Tmax(String day4Tmax) {
        this.day4Tmax = day4Tmax;
    }

    public String getDay5Tmax() {
        return day5Tmax;
    }

    public void setDay5Tmax(String day5Tmax) {
        this.day5Tmax = day5Tmax;
    }

    public String getDay6Tmax() {
        return day6Tmax;
    }

    public void setDay6Tmax(String day6Tmax) {
        this.day6Tmax = day6Tmax;
    }

    public String getDay7Tmax() {
        return day7Tmax;
    }

    public void setDay7Tmax(String day7Tmax) {
        this.day7Tmax = day7Tmax;
    }

    public String getDay4Tmin() {
        return day4Tmin;
    }

    public void setDay4Tmin(String day4Tmin) {
        this.day4Tmin = day4Tmin;
    }

    public String getDay5Tmin() {
        return day5Tmin;
    }

    public void setDay5Tmin(String day5Tmin) {
        this.day5Tmin = day5Tmin;
    }

    public String getDay6Tmin() {
        return day6Tmin;
    }

    public void setDay6Tmin(String day6Tmin) {
        this.day6Tmin = day6Tmin;
    }

    public String getDay7Tmin() {
        return day7Tmin;
    }

    public void setDay7Tmin(String day7Tmin) {
        this.day7Tmin = day7Tmin;
    }

    public String getDay4Status() {
        return day4Status;
    }

    public void setDay4Status(String day4Status) {
        this.day4Status = day4Status;
    }

    public String getDay5Status() {
        return day5Status;
    }

    public void setDay5Status(String day5Status) {
        this.day5Status = day5Status;
    }

    public String getDay6Status() {
        return day6Status;
    }

    public void setDay6Status(String day6Status) {
        this.day6Status = day6Status;
    }

    public String getDay7Status() {
        return day7Status;
    }

    public void setDay7Status(String day7Status) {
        this.day7Status = day7Status;
    }

    private static Weather instance;
    private Weather(){
    }

    public static Weather getInstance(){
        if (instance == null)
            instance = new Weather();
        return instance;
    }
}
