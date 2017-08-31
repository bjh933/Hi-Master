package com.example.a1.himaster;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1.himaster.SKPlanet.WeatherThreeDayThread;
import com.example.a1.himaster.SKPlanet.WeatherTodayThread;
import com.example.a1.himaster.SKPlanet.WeatherWeekThread;

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
import java.util.Locale;


public class ThirdFragment extends Fragment {
    private static final String TAG_CAT = "category";
    private static final String TAG_TIME = "fcstTime";
    private static final String TAG_VALUE = "fcstValue";
    ImageView weatherIv, optMenu;
    JSONArray posts = null;
    TextView dateTv, tempTv, rainTv, skyTv, dustTv, bulTv, bulExTv, ultTv, ultExTv, sickTv, sickExTv;
    String str="none";

    TextView oneTv, twoTv, threeTv, fourTv, fiveTv, sixTv, sevenTv, oneHighTv, oneLowTv, twoHighTv, twoLowTv, threeHighTv, threeLowTv, fourHighTv, fourLowTv,
            fiveHighTv, fiveLowTv, sixHighTv, sixLowTv, sevenHighTv, sevenLowTv;

    ImageView oneIv, twoIv, threeIv, fourIv, fiveIv, sixIv, sevenIv;
    String[] weatherStr = new String[15];


    public static ThirdFragment newInstance() {
        return new ThirdFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thirdfragment, container, false);
        dateTv = (TextView) view.findViewById(R.id.dateTv);
        tempTv = (TextView) view.findViewById(R.id.tempTv);
        skyTv = (TextView) view.findViewById(R.id.skyTv);
        rainTv = (TextView) view.findViewById(R.id.perTv);
        weatherIv = (ImageView) view.findViewById(R.id.weatherIv);
        dustTv = (TextView) view.findViewById(R.id.dustTv);
        bulExTv = (TextView) view.findViewById(R.id.bulExTv);
        ultExTv = (TextView) view.findViewById(R.id.ultExTv);
        sickExTv = (TextView) view.findViewById(R.id.sickExTv);

        Context mContext = getContext();

        oneTv = (TextView)view.findViewById(R.id.oneTv);
        twoTv = (TextView)view.findViewById(R.id.twoTv);
        threeTv = (TextView)view.findViewById(R.id.threeTv);
        fourTv = (TextView)view.findViewById(R.id.fourTv);
        fiveTv = (TextView)view.findViewById(R.id.fiveTv);
        sixTv = (TextView)view.findViewById(R.id.sixTv);
        sevenTv = (TextView)view.findViewById(R.id.sevenTv);
        oneHighTv = (TextView)view.findViewById(R.id.oneHighTv);
        oneLowTv = (TextView)view.findViewById(R.id.oneLowTv);
        twoHighTv = (TextView)view.findViewById(R.id.twoHighTv);
        twoLowTv = (TextView)view.findViewById(R.id.twoLowTv);
        threeHighTv = (TextView)view.findViewById(R.id.threeHighTv);
        threeLowTv = (TextView)view.findViewById(R.id.threeLowTv);
        fourHighTv = (TextView)view.findViewById(R.id.fourHighTv);
        fourLowTv = (TextView)view.findViewById(R.id.fourLowTv);
        fiveHighTv = (TextView)view.findViewById(R.id.fiveHighTv);
        fiveLowTv = (TextView)view.findViewById(R.id.fiveLowTv);
        sixHighTv = (TextView)view.findViewById(R.id.sixHighTv);
        sixLowTv = (TextView)view.findViewById(R.id.sixLowTv);
        sevenHighTv = (TextView)view.findViewById(R.id.sevenHighTv);
        sevenLowTv = (TextView)view.findViewById(R.id.sevenLowTv);
        oneIv = (ImageView) view.findViewById(R.id.oneIv);
        twoIv = (ImageView) view.findViewById(R.id.twoIv);
        threeIv = (ImageView) view.findViewById(R.id.threeIv);
        fourIv = (ImageView) view.findViewById(R.id.fourIv);
        fiveIv = (ImageView) view.findViewById(R.id.fiveIv);
        sixIv = (ImageView) view.findViewById(R.id.sixIv);
        sevenIv = (ImageView) view.findViewById(R.id.sevenIv);
        Calendar cal = Calendar.getInstance();
        int dayFlag = cal.get(Calendar.DAY_OF_WEEK);


        oneTv.setText(getToday(dayFlag));
        twoTv.setText(getToday(dayFlag+1));
        threeTv.setText(getToday(dayFlag+2));
        fourTv.setText(getToday(dayFlag+3));
        fiveTv.setText(getToday(dayFlag+4));
        sixTv.setText(getToday(dayFlag+5));
        sevenTv.setText(getToday(dayFlag+6));

        optMenu = (ImageView) view.findViewById(R.id.optMenuBtn);
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
        java.sql.Date date = new java.sql.Date(now);
        SimpleDateFormat toDate = new SimpleDateFormat("MMM . dd / EEE", Locale.ENGLISH);
        String todayDate = toDate.format(date);
        dateTv.setText(todayDate);

        SimpleDateFormat conDate = new SimpleDateFormat("yyyyMMdd");
        String baseDate = conDate.format(date);

        SimpleDateFormat conTime = new SimpleDateFormat("HHmm");
        String baseTime = conTime.format(date);
        int compTime = Integer.valueOf(baseTime);

        if (compTime >= 200 && compTime < 500)
            baseTime = "0200";
        else if (compTime >= 500 && compTime < 800)
            baseTime = "0500";
        else if (compTime >= 800 && compTime < 1100)
            baseTime = "0800";
        else if (compTime >= 1100 && compTime < 1400)
            baseTime = "1100";
        else if (compTime >= 1400 && compTime < 1700)
            baseTime = "1400";
        else if (compTime >= 1700 && compTime < 2000)
            baseTime = "1700";
        else if (compTime >= 2000 && compTime < 2300)
            baseTime = "2000";
        else if ((compTime >= 2300 && compTime < 2459)) {
            baseTime = "2300";
        } else if (compTime < 200) {
            SimpleDateFormat yday = new SimpleDateFormat("yyyyMMdd");
            cal = new GregorianCalendar();
            cal.add(Calendar.DATE, -1);
            baseDate = yday.format(cal.getTime());
            baseTime = "2300";
        }

        String serviceKey = "Zfm%2Fjv9CdbeGPRRkBK3wjUOpqFhc5gONSyZbz0P4p13fH1s0CHO25CzN8Sf8VNPrjjfhIhdAh1SQagI6bQeTkw%3D%3D";
        String url = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?serviceKey="
                + serviceKey + "&base_date=" + baseDate + "&base_time=" + baseTime +
                "&nx=60&ny=127&numOfRows=8&pageSize=11&pageNo=1&startPage=1&_type=json";
        // 강수확 02 05 08 11 14 17 20 23시 예보

        String url2 = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=%EA%B0%95%EB%82%A8%EA%B5%AC&dataTerm=month&pageNo=1&numOfRows=1&ServiceKey=" + serviceKey + "&ver=1.3&_returnType=json";
        //  미세먼지 예보

        SimpleDateFormat dday = new SimpleDateFormat("yyyyMMdd06");
        String today = dday.format(date);

        cal = new GregorianCalendar();
        cal.add(Calendar.DATE, -1);

        String jisuUrl1 = "http://newsky2.kma.go.kr/iros/RetrieveLifeIndexService2/getDsplsLifeList?serviceKey=" +
                serviceKey + "&areaNo=1100000000&time=" + today + "&type=json";   // 불쾌지수

        String yesterday = dday.format(cal.getTime());
        String jisuUrl2 = "http://newsky2.kma.go.kr/iros/RetrieveLifeIndexService2/getUltrvLifeList?serviceKey=" +
                serviceKey + "&areaNo=1100000000&time=" + yesterday + "&type=json";   // 자외선지수
        String jisuUrl3 = "http://newsky2.kma.go.kr/iros/RetrieveLifeIndexService2/getFsnLifeList?serviceKey=" +
                serviceKey + "&areaNo=1100000000&time=" + yesterday + "&type=json";   // 식중독지수

        Handler handler = new Handler(){
            public void handleMessage(Message msg)
            {
                String str = msg.getData().getString("weatherToday");    /// 번들에 들어있는 값 꺼냄
                Log.d("weathertoday", str);
                String[] weatherStr = str.split("-");
                skyTv.setText(weatherStr[0]);
                setSky(weatherStr[0], weatherIv);
                String tMax = weatherStr[1].replace(".00", "");
                String tMin = weatherStr[2].replace(".00", "");
                oneHighTv.setText(tMax);
                oneLowTv.setText(tMin);
                String temp = weatherStr[3];
                int index = temp.indexOf('.');
                String tempNow = temp.substring(0, index);
                tempTv.setText(tempNow);
                tempTv.append(" ℃");
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


        WeatherThreeDayThread wt = new WeatherThreeDayThread(handler, mContext, 37.5714000000, 126.9658000000);
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

        getData(url);   //  강수 확률 얻기
        getDustData(url2);  //  미세먼지 얻기
        getBulData(jisuUrl1);   //  불쾌지수 얻기
        getUltData(jisuUrl2);   //  자외선지수 얻기
        getSickData(jisuUrl3);  //  식중독지수 얻기
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
        class GetDataJSON extends AsyncTask<String, Void, String> {
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
                    while ((json = br.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    //Log.d("ssss", String.valueOf(sb));
                    return sb.toString().trim();
                } catch (Exception e) {
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

    public void setSky(String skyStatus, ImageView iv) {

        if(skyStatus.equals("맑음"))
            iv.setImageResource(R.drawable.sunny);
        else if(skyStatus.equals("구름조금") || skyStatus.equals("구름많음") || skyStatus.equals("흐림"))
            iv.setImageResource(R.drawable.cloudy);
        else
            iv.setImageResource(R.drawable.rainy);
    }

    public void makeList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);

            posts = jsonObj.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
            for (int i = 0; i < posts.length(); i++) {
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

                if (category.equals("POP")) {
                    rainTv.setText(value);
                    rain = rainTv.getText().toString();
                    rainPer = Integer.parseInt(rain);
                    rainTv.append(" %");

                    if (rainPer >= 70) {
                        weatherIv.setImageResource(R.drawable.rainy);
                    } else if (rainPer >= 30 && rainPer < 70) {
                        weatherIv.setImageResource(R.drawable.cloudy);
                    } else if (rainPer < 30) {
                        weatherIv.setImageResource(R.drawable.sunny);
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDustData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
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
                    while ((json = br.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    //Log.d("aaaa", String.valueOf(sb));
                    return sb.toString().trim();
                } catch (Exception e) {
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
            for (int i = 0; i < posts.length(); i++) {
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

                if (dust.equals("1"))
                    dustTv.setText("좋음");
                else if (dust.equals("2"))
                    dustTv.setText("보통");
                else if (dust.equals("3"))
                    dustTv.setText("나쁨");
                else if (dust.equals("4"))
                    dustTv.setText("매우 나쁨");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getBulData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
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
                    while ((json = br.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    //Log.d("bulcae", String.valueOf(sb));
                    return sb.toString().trim();
                } catch (Exception e) {
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


            if (bul >= 80) {
                bulExTv.setText("매우 높음");
            } else if (bul >= 75 && bul < 80) {
                bulExTv.setText("높음");
            } else if (bul >= 68 && bul < 75) {
                bulExTv.setText("보통");
            } else if (bul < 68) {
                bulExTv.setText("낮음");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUltData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
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
                    while ((json = br.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
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

            if (ult >= 11) {
                ultExTv.setText("위험");
            } else if (ult >= 8 && ult < 11) {
                ultExTv.setText("매우 높음");
            } else if (ult >= 6 && ult < 8) {
                ultExTv.setText("높음");
            } else if (ult >= 3 && ult < 6) {
                ultExTv.setText("보통");
            } else if (ult >= 0 && ult < 3) {
                ultExTv.setText("낮음");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSickData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
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
                    while ((json = br.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
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

            if (ult >= 95) {
                sickExTv.setText("위험");
            } else if (ult >= 70 && ult < 95) {
                sickExTv.setText("경고");
            } else if (ult >= 35 && ult < 70) {
                sickExTv.setText("주의");
            } else if (ult >= 0 && ult < 35) {
                sickExTv.setText("관심");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
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

}