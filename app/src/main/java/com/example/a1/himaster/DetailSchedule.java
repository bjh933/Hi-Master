package com.example.a1.himaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by a1 on 2017. 8. 17..
 */

public class DetailSchedule extends AppCompatActivity implements View.OnClickListener{

    JSONArray posts = null;
    TextView dDateTv, dTimeTv, dTitleTv, dDepartTv, dArriveTv;
    Spinner trptSpin;
    String dTitle, dDate, dTime, dDest = "";
    ImageView trptImg, swapBtn;
    String serviceKey = "Zfm%2Fjv9CdbeGPRRkBK3wjUOpqFhc5gONSyZbz0P4p13fH1s0CHO25CzN8Sf8VNPrjjfhIhdAh1SQagI6bQeTkw%3D%3D";
    String subwayKey = "4e4b704a51646a673733724c474d6e";
    String departLat = "37.54931915";
    String departLon = "127.08117882";  //  어린이대공원
    String destLat = "37.49799082";
    String destLon = "127.02779625";    //  강남
    String subDepartLat = "37.54901353";
    String subDepartLon = "127.07542936";   //  어린이대공원역
    String subDestLat = "37.49810194";
    String subDestLon = "127.02876838"; //  강남역 2호선
    String temp = "";
    int spinFlag = 0;
    String subwayUrl = "";
    String busUrl = "";
    String walkTotalTime = "";
    String carTotalTime = "";

    String departureSt = "강변";  //  출발역 명
    String destinationSt = "어린이대공원";    // 도착역 명명
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailshcedule);

        subwayUrl = "http://swopenapi.seoul.go.kr/api/subway/" +
                subwayKey + "/json/shortestRoute/0/5/"+ departureSt + "/" + destinationSt;  //  지하철 최단소요시간

        busUrl = "http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getCrdntPrxmtSttnList?serviceKey="
                + serviceKey + "&gpsLati="+ departLat + "&gpsLong=" + departLon + "&_type=json";

        trptImg = (ImageView)findViewById(R.id.transportImg);
        trptSpin = (Spinner)findViewById(R.id.transportSpinner);

        trptSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    trptImg.setImageResource(R.drawable.bus);
                    getBusStopData(busUrl);    //  출발지 근처 버스정류장을 가져옴
                    spinFlag = 0;
                }
                else{
                    trptImg.setImageResource(R.drawable.train);
                    getSubwayStData(subwayUrl);
                    spinFlag = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        swapBtn = (ImageView)findViewById(R.id.swapBtn);
        swapBtn.setOnClickListener(this);
        dTitleTv = (TextView)findViewById(R.id.dtitle);
        dDateTv = (TextView)findViewById(R.id.ddate);
        dTimeTv = (TextView)findViewById(R.id.dtime);
        dDepartTv = (TextView)findViewById(R.id.depart);
        dArriveTv = (TextView)findViewById(R.id.arrive);


        Intent data = getIntent();
        dTitle = data.getExtras().getString("DTITLE");
        dDate = data.getExtras().getString("DDATE");
        dTime = data.getExtras().getString("DTIME");
        dDest = data.getExtras().getString("DDEST");
        String[] dateArr = dDate.split(" ");

        dTitleTv.setText(dTitle);
        dDateTv.setText(dateArr[0]);
        dTimeTv.setText(dTime);
        dArriveTv.setText(dDest);

        String startX = "127.08117882";
        String startY = "37.54931915";
        String endX = "127.02779625";
        String endY = "37.49799082";
        String startName = "어린이대공원";
        String endName = "강남역";

        String walkingUrl = "https://apis.skplanetx.com/tmap/routes/pedestrian?version=1" +
                "&appKey=b42a1814-4abc-36c2-a743-43c5f81cd73d&" +
                "startX="+startX+"&startY="+startY+"&endX="+endX+"&endY="+endY+"&startName="
                +startName+"&endName="+endName+"&reqCoordType=WGS84GEO&resCoordType=WGS84GEO";

        String carUrl = "https://apis.skplanetx.com/tmap/routes?version=1" +
                "&appKey=b42a1814-4abc-36c2-a743-43c5f81cd73d&" +
                "startX="+startX+"&startY="+startY+"&endX="+endX+"&endY="+endY+"&startName="
                +startName+"&endName="+endName+"&reqCoordType=WGS84GEO&resCoordType=WGS84GEO";


        getWalkTotalTimeData(walkingUrl);   //  초단위 보행자 소요 시간
        getCarTotalTimeData(carUrl);   //  초단위 자동차 소요 시간

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

    public void getBusStopData(String url) {
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
                    Log.d("receiveJsonBusInfo", String.valueOf(sb));
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                //Log.d("my", myJSON);
                makeBusStopList(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void makeBusStopList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);

            posts = jsonObj.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
            for(int i=0; i<posts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = posts.getJSONObject(i);
                String gpsLati = c.getString("gpslati");    //  정류소 X 좌표
                String gpsLong = c.getString("gpslong");    //  정류소 Y 좌표
                String nodeNm = c.getString("nodenm");    //  정류소 이름
                //String nodeNo = c.getString("nodeno");    //  정류소 번호
                String nodeId = c.getString("nodeid");    //  정류소 ID

                Log.d("BusInfo", gpsLati + ", " + gpsLong + ", " + nodeNm + ", " + nodeId);

            }

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSubwayStData(String url) {
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
                    Log.d("receiveJsonSubwayInfo", String.valueOf(sb));
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                //Log.d("my", myJSON);
                makeSubwayStList(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void makeSubwayStList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            posts = jsonObj.getJSONArray("shortestRouteList");

            for(int i=0; i<posts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = posts.getJSONObject(i);
                String departStation = c.getString("statnFnm");    //  출발역 명
                String destStation = c.getString("statnTnm");    //  도착역 명
                String stimeInfo = c.getString("shtTravelMsg");    //  최단 시간 도착예정 안내
                String stationCnt = c.getString("shtStatnCnt");    //  지나는 역 수
                String sTime = c.getString("shtTravelTm");    //  도착까지 걸리는 시간(분)


                Log.d("SubwayInfo", departStation + ", " + destStation + ", " + stimeInfo + ", " +
                        stationCnt + ", " + sTime);

            }

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void getWalkTotalTimeData(String url) {
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
                    Log.d("receiveTotalTime", String.valueOf(sb));
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                //Log.d("my", myJSON);
                makeWalkTotalList(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void makeWalkTotalList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            posts = jsonObj.getJSONArray("features");

            JSONObject c = posts.getJSONObject(0).getJSONObject("properties");
            walkTotalTime = c.getString("totalTime");

            Log.d("walkTotalTime", walkTotalTime);


        }catch(JSONException e) {
            e.printStackTrace();
        }
    }


    public void getCarTotalTimeData(String url) {
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
                    Log.d("receiveTotalTime", String.valueOf(sb));
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                //Log.d("my", myJSON);
                makeCarTotalList(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void makeCarTotalList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            posts = jsonObj.getJSONArray("features");

            JSONObject c = posts.getJSONObject(0).getJSONObject("properties");
            carTotalTime = c.getString("totalTime");

            Log.d("carTotalTime", carTotalTime);


        }catch(JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

    }

    @Override
    public void onClick(View v) {
        temp = dDepartTv.getText().toString();
        dDepartTv.setText(dArriveTv.getText().toString());
        dArriveTv.setText(temp);

        temp = departLat;
        departLat = destLat;
        destLat = temp;

        temp = departLon;
        departLon = destLon;
        destLon = temp;

        subwayUrl = "http://swopenapi.seoul.go.kr/api/subway/" + subwayKey + "/json/shortestRoute/0/5/"+ departureSt + "/" + destinationSt;

        busUrl = "http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getCrdntPrxmtSttnList?serviceKey="
                + serviceKey + "&gpsLati="+ departLat + "&gpsLong=" + departLon + "&_type=json";

        if(spinFlag == 0)
        {
            getBusStopData(busUrl);
        }
        else if(spinFlag == 1)
        {
            getSubwayStData(subwayUrl);
        }
    }

}