package com.example.a1.himaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by a1 on 2017. 8. 17..
 */

public class DetailSchedule extends AppCompatActivity {

    TextView dDateTv, dTimeTv, dTitleTv, dDepartTv, dArriveTv;
    String dTitle, dDate, dTime, dDest = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailshcedule);

        dTitleTv = (TextView)findViewById(R.id.dtitle);
        dDateTv = (TextView)findViewById(R.id.ddate);
        dTimeTv = (TextView)findViewById(R.id.dtime);
        dDepartTv = (TextView)findViewById(R.id.depart);
        dArriveTv = (TextView)findViewById(R.id.arrive);

        Intent data = getIntent();
        dTitle = data.getExtras().getString("DTITLE");
        dDate = data.getExtras().getString("DDATE");
        dDest = data.getExtras().getString("DDEST");
        String[] dateArr = dDate.split(" ");

        String[] timeArr = dateArr[1].split(":");
        int time = Integer.parseInt(timeArr[0]);
        checkAmPm(time, timeArr);


        dTitleTv.setText(dTitle);
        dDateTv.setText(dateArr[0]);
        dTimeTv.setText(dTime);
        dArriveTv.setText(dDest);

    }

    public String checkAmPm(int time, String[] timeArr){
        if(time > 12)
        {
            time = time - 12;
            dTime = "PM " + time + " : " + timeArr[1];
        }
        else
        {
            dTime = "AM " + time + " : " + timeArr[1];
        }

        return dTime;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

    }

}