package com.example.a1.himaster;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.a1.himaster.SKPlanet.WeatherThread;
import com.example.a1.himaster.SKPlanet.WeatherTodayThread;

/**
 * Created by a1 on 2017. 8. 16..
 */

public class WeekWeather extends AppCompatActivity {

    String str="none";

    TextView oneHighTv, oneLowTv, twoHighTv, twoLowTv, threeHighTv, threeLowTv, fourHighTv, fourLowTv,
    fiveHighTv, fiveLowTv, sixHighTv, sixLowTv, sevenHighTv, sevenLowTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekweather);

        oneHighTv = (TextView)findViewById(R.id.oneHighTv);
        oneLowTv = (TextView)findViewById(R.id.oneLowTv);
        twoHighTv = (TextView)findViewById(R.id.twoHighTv);
        twoLowTv = (TextView)findViewById(R.id.twoLowTv);
        threeHighTv = (TextView)findViewById(R.id.threeHighTv);
        threeLowTv = (TextView)findViewById(R.id.threeLowTv);
        fourHighTv = (TextView)findViewById(R.id.fourHighTv);
        fourLowTv = (TextView)findViewById(R.id.fourLowTv);
        fiveHighTv = (TextView)findViewById(R.id.fiveHighTv);
        fiveLowTv = (TextView)findViewById(R.id.fiveLowTv);
        sixHighTv = (TextView)findViewById(R.id.sixHighTv);
        sixLowTv = (TextView)findViewById(R.id.sixLowTv);
        sevenHighTv = (TextView)findViewById(R.id.sevenHighTv);
        sevenLowTv = (TextView)findViewById(R.id.sevenLowTv);

        Handler handler = new Handler(){
            public void handleMessage(Message msg)
        {
            str = msg.getData().getString("weather");    /// 번들에 들어있는 값 꺼냄
            oneHighTv.setText(str);
            //Log.d("weather", msg.getData().getString("weather"));

        }};

        Context mContext = getApplicationContext();
        WeatherThread wt = new WeatherThread(handler, mContext, 37.5714000000, 126.9658000000);
        wt.run();

        handler = new Handler(){
            public void handleMessage(Message msg)
            {
                str = msg.getData().getString("weatherToday");    /// 번들에 들어있는 값 꺼냄
                twoHighTv.setText(str);
                //Log.d("weather", msg.getData().getString("weather"));

            }};

        WeatherTodayThread wtt = new WeatherTodayThread(handler, mContext, 37.5714000000, 126.9658000000);
        wtt.run();
        /// 3일치 날짜 정보 받기 완료

    }

}




