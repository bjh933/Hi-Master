package com.example.a1.himaster.Model;

import java.sql.Timestamp;

/**
 * Created by a1 on 2017. 8. 10..
 */

public class Event {

    private String title = "";
    private Timestamp dueDate = new Timestamp(System.currentTimeMillis());
    private Timestamp startDate = new Timestamp(System.currentTimeMillis());
    private Timestamp endDate = new Timestamp(System.currentTimeMillis());
    private String destination = "";
    private String memo = "";

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Timestamp getDueDate() {
        return dueDate;
    }
    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }
    public Timestamp getStartDate() {
        return startDate;
    }
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
    public Timestamp getEndDate() {
        return endDate;
    }
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }


}
