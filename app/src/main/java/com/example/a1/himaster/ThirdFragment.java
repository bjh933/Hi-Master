package com.example.a1.himaster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class ThirdFragment extends Fragment {
    private static final String TAG_CAT = "category";
    private static final String TAG_TIME = "fcstTime";
    private static final String TAG_VALUE = "fcstValue";
    ImageView weatherIv, optMenu;
    JSONArray posts = null;
    TextView dateTv, tempTv, rainTv, skyTv, dustValTv, dustTv, bulTv, bulExTv, ultTv, ultExTv, sickTv, sickExTv;

    public static ThirdFragment newInstance() {
        return new ThirdFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thirdfragment, container, false);
        dateTv = (TextView)view.findViewById(R.id.dateTv);
        tempTv = (TextView)view.findViewById(R.id.tempTv);
        skyTv = (TextView)view.findViewById(R.id.skyTv);
        rainTv = (TextView)view.findViewById(R.id.perTv);
        weatherIv = (ImageView)view.findViewById(R.id.weatherIv);
        dustValTv = (TextView)view.findViewById(R.id.dustValTv);
        dustTv = (TextView)view.findViewById(R.id.dustTv);

        bulTv = (TextView)view.findViewById(R.id.bulTv);
        bulExTv = (TextView)view.findViewById(R.id.bulExTv);
        ultTv = (TextView)view.findViewById(R.id.ultTv);
        ultExTv = (TextView)view.findViewById(R.id.ultExTv);
        sickTv = (TextView)view.findViewById(R.id.sickTv);
        sickExTv = (TextView)view.findViewById(R.id.sickExTv);

        optMenu = (ImageView)view.findViewById(R.id.optMenuBtn);
        optMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyInfo.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                //finish();
            }

        });

        /*
        Drawable d = weatherIv.getDrawable();
        if (d instanceof BitmapDrawable) {
            Bitmap bm = ((BitmapDrawable) d).getBitmap();
            if (bm != null)
                bm.recycle();
        }
        weatherIv.setImageDrawable(null);
        */
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat toDate = new SimpleDateFormat("yyyy년 M월 d일");
        String todayDate = toDate.format(date);
        dateTv.setText(todayDate);

        SimpleDateFormat conDate = new SimpleDateFormat("yyyyMMdd");
        String baseDate = conDate.format(date);

        SimpleDateFormat conTime = new SimpleDateFormat("HHmm");
        String baseTime = conTime.format(date);
        int compTime = Integer.valueOf(baseTime);

        if(compTime >= 200 && compTime < 500)
            baseTime = "0200";
        else if(compTime >= 500 && compTime < 800)
            baseTime = "0500";
        else if(compTime >= 800 && compTime < 1100)
            baseTime = "0800";
        else if(compTime >= 1100 && compTime < 1400)
            baseTime = "1100";
        else if(compTime >= 1400 && compTime < 1700)
            baseTime = "1400";
        else if(compTime >= 1700 && compTime < 2000)
            baseTime = "1700";
        else if(compTime >= 2000 && compTime < 2300)
            baseTime = "2000";
        else if((compTime >= 2300 && compTime < 2459))
        {
            baseTime = "2300";
        }
        else if(compTime < 200)
        {
            SimpleDateFormat yday = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = new GregorianCalendar();
            cal.add(Calendar.DATE, -1);
            baseDate = yday.format(cal.getTime());
            baseTime = "2300";}

        String serviceKey = "Zfm%2Fjv9CdbeGPRRkBK3wjUOpqFhc5gONSyZbz0P4p13fH1s0CHO25CzN8Sf8VNPrjjfhIhdAh1SQagI6bQeTkw%3D%3D";
        String url = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?serviceKey="
                +serviceKey+"&base_date="+baseDate+"&base_time="+baseTime+
                "&nx=60&ny=127&numOfRows=8&pageSize=11&pageNo=1&startPage=1&_type=json";
        // 기온 02 05 08 11 14 17 20 23시 예보

        String url2 = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=%EA%B0%95%EB%82%A8%EA%B5%AC&dataTerm=month&pageNo=1&numOfRows=1&ServiceKey="+serviceKey+"&ver=1.3&_returnType=json";
        //  미세먼지 예보

        SimpleDateFormat dday = new SimpleDateFormat("yyyyMMdd06");
        String today = dday.format(date);

        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, -1);

        String jisuUrl1 = "http://newsky2.kma.go.kr/iros/RetrieveLifeIndexService2/getDsplsLifeList?serviceKey="+
                serviceKey+"&areaNo=1100000000&time="+today+"&type=json";   // 불쾌지수

        String yesterday = dday.format(cal.getTime());
        String jisuUrl2 = "http://newsky2.kma.go.kr/iros/RetrieveLifeIndexService2/getUltrvLifeList?serviceKey="+
                serviceKey+"&areaNo=1100000000&time="+yesterday+"&type=json";   // 자외선지수
        String jisuUrl3 = "http://newsky2.kma.go.kr/iros/RetrieveLifeIndexService2/getFsnLifeList?serviceKey="+
                serviceKey+"&areaNo=1100000000&time="+yesterday+"&type=json";   // 식중독지수

        getData(url);
        getDustData(url2);
        getBulData(jisuUrl1);
        getUltData(jisuUrl2);
        getSickData(jisuUrl3);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params) {
                //JSON 받아온다.
                String uri = params[0];
                BufferedReader br = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();

                    StringBuilder sb = new StringBuilder();

                    br = new BufferedReader(new InputStreamReader(conn1.getInputStream()));

                    String json;
                    while((json = br.readLine()) != null) {
                        sb.append(json+"\n");
                    }
                    //Log.d("ssss", String.valueOf(sb));
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                //Log.d("my", myJSON);
                makeList(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }


    public void makeList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);

            posts = jsonObj.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
            for(int i=0; i<posts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = posts.getJSONObject(i);
                String category = c.getString(TAG_CAT);
                String time = c.getString(TAG_TIME);
                String value = c.getString(TAG_VALUE);
                String skyStatus = "";
                String rain = "";
                String temp = "";
                int rainPer = 0;
                //Log.d("cate", category);
                //Log.d("time", time);
                //Log.d("value", value);

                if(category.equals("POP"))
                {
                    rainTv.setText(value);
                    rain = rainTv.getText().toString();
                    rainPer = Integer.parseInt(rain);
                    rainTv.append(" %");

                    if(rainPer >= 70)
                    {
                        weatherIv.setImageResource(R.drawable.rainy);
                    }
                    else if(rainPer >= 30 && rainPer < 70)
                    {
                        weatherIv.setImageResource(R.drawable.cloudy);
                    }
                    else if(rainPer < 30)
                    {
                        weatherIv.setImageResource(R.drawable.sunny);
                    }

                }

                else if(category.equals("T3H"))
                {
                    tempTv.setText(value);
                    temp = tempTv.getText().toString();
                    tempTv.append(" ℃");
                }

                else if(category.equals("SKY"))
                {
                    if(value.equals("1"))
                        skyStatus="맑음";
                    else if(value.equals("2"))
                        skyStatus="구름 조금";
                    else if(value.equals("3"))
                        skyStatus="구름 많음";
                    else if(value.equals("4"))
                        skyStatus="매우 흐림";

                    skyTv.setText(skyStatus);
                    // 1:맑음 2:구름조금 3:구름많음 4:흐림
                }

            }

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDustData(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params) {
                //JSON 받아온다.
                String uri = params[0];
                BufferedReader br = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();

                    StringBuilder sb = new StringBuilder();

                    br = new BufferedReader(new InputStreamReader(conn1.getInputStream()));

                    String json;
                    while((json = br.readLine()) != null) {
                        sb.append(json+"\n");
                    }
                    //Log.d("aaaa", String.valueOf(sb));
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                //Log.d("my", myJSON);
                makeDustList(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void makeDustList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);

            posts = jsonObj.getJSONArray("list");
            for(int i=0; i<posts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = posts.getJSONObject(i);
                String khaiVal = c.getString("khaiValue");
                String khaiGrade = c.getString("khaiGrade");
                String dustVal = c.getString("pm10Value");
                String dust = c.getString("pm10Grade1h");
                // Log.d("miseval", khaiVal);
                // Log.d("misegra", khaiGrade);
                // Log.d("dustval", dustVal);
                // Log.d("dustt", dust);

                dustValTv.setText(dustVal);
                dustValTv.append("  ㎍/㎥");

                if(dust.equals("1"))
                    dustTv.setText(", 좋음");
                else if(dust.equals("2"))
                    dustTv.setText(", 보통");
                else if(dust.equals("3"))
                    dustTv.setText(", 나쁨");
                else if(dust.equals("4"))
                    dustTv.setText(", 매우 나쁨");

            }

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void getBulData(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params) {
                //JSON 받아온다.
                String uri = params[0];
                BufferedReader br = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();

                    StringBuilder sb = new StringBuilder();

                    br = new BufferedReader(new InputStreamReader(conn1.getInputStream()));

                    String json;
                    while((json = br.readLine()) != null) {
                        sb.append(json+"\n");
                    }
                    //Log.d("bulcae", String.valueOf(sb));
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                //Log.d("my", myJSON);
                makeBulList(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void makeBulList(String myJSON) {
        try {
            JSONObject c = null;
            JSONObject jsonObj = new JSONObject(myJSON);
            c = jsonObj.getJSONObject("Response").getJSONObject("body").getJSONObject("indexModel");

            //JSON에서 각각의 요소를 뽑아옴

            String bulValue = c.getString("h9");
            int bul = Integer.valueOf(bulValue);
            //Log.d("hhh9", bulValue);

            bulTv.setText(bulValue);

            if(bul >= 80) {
                bulTv.setText(bulValue);
                bulExTv.setText(", 매우 높음");
            }
            else if(bul >= 75 && bul < 80){
                bulTv.setText(bulValue);
                bulExTv.setText(", 높음");
            }
            else if(bul >= 68 && bul < 75){
                bulTv.setText(bulValue);
                bulExTv.setText(", 보통");
            }
            else if(bul < 68){
                bulTv.setText(bulValue);
                bulExTv.setText(", 낮음");
            }


        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUltData(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params) {
                //JSON 받아온다.
                String uri = params[0];
                BufferedReader br = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();

                    StringBuilder sb = new StringBuilder();

                    br = new BufferedReader(new InputStreamReader(conn1.getInputStream()));

                    String json;
                    while((json = br.readLine()) != null) {
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                makeUltList(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void makeUltList(String myJSON) {
        try {
            JSONObject c = null;
            JSONObject jsonObj = new JSONObject(myJSON);
            c = jsonObj.getJSONObject("Response").getJSONObject("body").getJSONObject("indexModel");

            //JSON에서 각각의 요소를 뽑아옴

            String ultValue = c.getString("today");
            int ult = Integer.valueOf(ultValue);

            if(ult >= 11) {
                ultTv.setText(ultValue);
                ultExTv.setText(", 위험");
            }
            else if(ult >= 8 && ult < 11){
                ultTv.setText(ultValue);
                ultExTv.setText(", 매우 높음");
            }
            else if(ult >= 6 && ult < 8){
                ultTv.setText(ultValue);
                ultExTv.setText(", 높음");
            }
            else if(ult >= 3 && ult < 6){
                ultTv.setText(ultValue);
                ultExTv.setText(", 보통");
            }
            else if(ult >= 0 && ult < 3){
                ultTv.setText(ultValue);
                ultExTv.setText(", 낮음");
            }


        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSickData(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params) {
                //JSON 받아온다.
                String uri = params[0];
                BufferedReader br = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();

                    StringBuilder sb = new StringBuilder();

                    br = new BufferedReader(new InputStreamReader(conn1.getInputStream()));

                    String json;
                    while((json = br.readLine()) != null) {
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                makeSickList(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void makeSickList(String myJSON) {
        try {
            JSONObject c = null;
            JSONObject jsonObj = new JSONObject(myJSON);
            c = jsonObj.getJSONObject("Response").getJSONObject("body").getJSONObject("indexModel");

            //JSON에서 각각의 요소를 뽑아옴

            String ultValue = c.getString("today");
            int ult = Integer.valueOf(ultValue);

            if(ult >= 95) {
                sickTv.setText(ultValue);
                sickExTv.setText(", 위험");
            }
            else if(ult >= 70 && ult < 95){
                sickTv.setText(ultValue);
                sickExTv.setText(", 경고");
            }
            else if(ult >= 35 && ult < 70){
                sickTv.setText(ultValue);
                sickExTv.setText(", 주의");
            }
            else if(ult >= 0 && ult < 35){
                sickTv.setText(ultValue);
                sickExTv.setText(", 관심");
            }


        }catch(JSONException e) {
            e.printStackTrace();
        }
    }
}