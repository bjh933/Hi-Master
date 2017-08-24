package com.example.a1.himaster;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1.himaster.SKPlanet.WeatherThread;
import com.example.a1.himaster.SKPlanet.WeatherTodayThread;
import com.example.a1.himaster.SKPlanet.WeatherWeekThread;

import java.util.Calendar;

/**
 * Created by a1 on 2017. 8. 16..
 */

public class WeekWeather extends AppCompatActivity {

    String str="none";

    TextView oneTv, twoTv, threeTv, fourTv, fiveTv, sixTv, sevenTv, oneHighTv, oneLowTv, twoHighTv, twoLowTv, threeHighTv, threeLowTv, fourHighTv, fourLowTv,
    fiveHighTv, fiveLowTv, sixHighTv, sixLowTv, sevenHighTv, sevenLowTv;

    ImageView oneIv, twoIv, threeIv, fourIv, fiveIv, sixIv, sevenIv;
    String[] weatherStr = new String[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekweather);

        oneTv = (TextView)findViewById(R.id.oneTv);
        twoTv = (TextView)findViewById(R.id.twoTv);
        threeTv = (TextView)findViewById(R.id.threeTv);
        fourTv = (TextView)findViewById(R.id.fourTv);
        fiveTv = (TextView)findViewById(R.id.fiveTv);
        sixTv = (TextView)findViewById(R.id.sixTv);
        sevenTv = (TextView)findViewById(R.id.sevenTv);
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
        oneIv = (ImageView) findViewById(R.id.oneIv);
        twoIv = (ImageView) findViewById(R.id.twoIv);
        threeIv = (ImageView) findViewById(R.id.threeIv);
        fourIv = (ImageView) findViewById(R.id.fourIv);
        fiveIv = (ImageView) findViewById(R.id.fiveIv);
        sixIv = (ImageView) findViewById(R.id.sixIv);
        sevenIv = (ImageView) findViewById(R.id.sevenIv);

        Context mContext = getApplicationContext();
        Calendar cal = Calendar.getInstance();
        int dayFlag = cal.get(Calendar.DAY_OF_WEEK);


        oneTv.setText(getToday(dayFlag));
        twoTv.setText(getToday(dayFlag+1));
        threeTv.setText(getToday(dayFlag+2));
        fourTv.setText(getToday(dayFlag+3));
        fiveTv.setText(getToday(dayFlag+4));
        sixTv.setText(getToday(dayFlag+5));
        sevenTv.setText(getToday(dayFlag+6));

        Handler handler = new Handler(){
            public void handleMessage(Message msg)
            {
                str = msg.getData().getString("weatherToday");    /// 번들에 들어있는 값 꺼냄
                Log.d("weathertoday", str);
                weatherStr = str.split("-");
                setSky(weatherStr[0], oneIv);
                String tMax = weatherStr[1].replace(".00", "");
                String tMin = weatherStr[2].replace(".00", "");
                oneHighTv.setText(tMax);
                oneLowTv.setText(tMin);

            }};

        WeatherTodayThread wtt = new WeatherTodayThread(handler, mContext, 37.5714000000, 126.9658000000);
        wtt.run();  //  오늘 날씨

        handler = new Handler(){
            public void handleMessage(Message msg)
        {
            str = msg.getData().getString("weather");    /// 번들에 들어있는 값 꺼냄
            Log.d("weather3days", str);
            weatherStr = str.split("-");
            setSky(weatherStr[0], twoIv);
            String tMax = weatherStr[1].replace(".00", "");
            String tMin = weatherStr[2].replace(".00", "");
            twoHighTv.setText(tMax);
            twoLowTv.setText(tMin);
        }};


        WeatherThread wt = new WeatherThread(handler, mContext, 37.5714000000, 126.9658000000);
        wt.run();   //  3일치 날씨

        handler = new Handler(){
            public void handleMessage(Message msg)
            {
                str = msg.getData().getString("WeekWeather");    /// 번들에 들어있는 값 꺼냄
                Log.d("weather7days", str);
                weatherStr = str.split("-");
                setSky(weatherStr[0], threeIv);
                threeHighTv.setText(weatherStr[1]);
                threeLowTv.setText(weatherStr[2]);
                setSky(weatherStr[3], fourIv);
                fourHighTv.setText(weatherStr[4]);
                fourLowTv.setText(weatherStr[5]);
                setSky(weatherStr[6], fiveIv);
                fiveHighTv.setText(weatherStr[7]);
                fiveLowTv.setText(weatherStr[8]);
                setSky(weatherStr[9], sixIv);
                sixHighTv.setText(weatherStr[10]);
                sixLowTv.setText(weatherStr[11]);
                setSky(weatherStr[12], sevenIv);
                sevenHighTv.setText(weatherStr[13]);
                sevenLowTv.setText(weatherStr[14]);


            }};

        WeatherWeekThread wwt = new WeatherWeekThread(handler, mContext, 37.5714000000, 126.9658000000);
        wwt.run();   //  7일치 날씨


    }

    public String getToday(int dayFlag) {
        String day = "";
        int extraDay = dayFlag;

        if(dayFlag > 7)
            extraDay = dayFlag - 7;

        if(extraDay == 1)
            day = "일요일";
        else if(extraDay == 2)
            day = "월요일";
        else if(extraDay == 3)
            day = "화요일";
        else if(extraDay == 4)
            day = "수요일";
        else if(extraDay == 5)
            day = "목요일";
        else if(extraDay == 6)
            day = "금요일";
        else if(extraDay == 7)
            day = "토요일";

        return day;
    }

    public void setSky(String skyStatus, ImageView iv) {

        if(skyStatus.equals("맑음"))
            iv.setImageResource(R.drawable.sunny);
        else if(skyStatus.equals("구름조금") || skyStatus.equals("구름많음") || skyStatus.equals("흐림"))
            iv.setImageResource(R.drawable.cloudy);
        else
            iv.setImageResource(R.drawable.rainy);
    }
}




