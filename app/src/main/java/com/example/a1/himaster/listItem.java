package com.example.a1.himaster;

import java.io.Serializable;

/**
 * Created by Dong on 2015-03-15.
 */
public class listItem implements Serializable{
    private String doTitle;  //일정
    private String time;  //시간


    public listItem(String time, String doTitle) {
        this.time = time;
        this.doTitle = doTitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlan() {
        return doTitle;
    }

    public void setPlan(String doTitle) {
        this.doTitle = doTitle;
    }
}

