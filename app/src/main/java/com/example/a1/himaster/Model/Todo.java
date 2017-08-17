package com.example.a1.himaster.Model;

import java.sql.Timestamp;

/**
 * Created by a1 on 2017. 8. 10..
 */

public class Todo {
    private String title = "";
    private Timestamp startDate = new Timestamp(System.currentTimeMillis());
    private String memo = "";
    private Boolean fix = false;

    public Timestamp getStartDate() {
        return startDate;
    }
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public Boolean getFix() {
        return fix;
    }
    public void setFix(Boolean fix) {
        this.fix = fix;
    }


}
