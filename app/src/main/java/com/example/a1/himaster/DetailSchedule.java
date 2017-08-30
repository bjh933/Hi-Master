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

import com.example.a1.himaster.Model.Destination;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by a1 on 2017. 8. 17..
 */

public class DetailSchedule extends AppCompatActivity implements View.OnClickListener{

    JSONArray posts = null;
    TextView dDateTv, dTimeTv, dTitleTv, dDepartTv, dArriveTv, transportNum, timeTerm, transportdTv;
    Spinner trptSpin;
    String dTitle, dDate, dTime, dDest = "";
    ImageView trptImg, swapBtn;
    String serviceKey = "Zfm%2Fjv9CdbeGPRRkBK3wjUOpqFhc5gONSyZbz0P4p13fH1s0CHO25CzN8Sf8VNPrjjfhIhdAh1SQagI6bQeTkw%3D%3D";
    String subwayKey = "4e4b704a51646a673733724c474d6e";
    String walkingUrl = "";
    String carUrl = "";

    String firstDepartureLat = "37.643974";
    String firstDepartureLon = "127.0357636";
    String finalDestLat = "37.5505411";
    String finalDestLon = "127.07384612";

    String departLat = "37.643974";
    String departLon = "127.0357636";  //  강원학사
    String destLat = "37.55054112";
    String destLon = "127.07384612";    //  세종대
    String departSubLat = "37.64805684";
    String departSubLon = "127.03456915";   //  쌍문역
    String destSubLat = "37.54801363";
    String destSubLon = "127.07467945"; //  어린이대공원역
    String temp = "";
    int spinFlag = 0;
    String subwayUrl = "";

    String busUrl = "";
    String busStUrl = "";
    String busStLat = "";
    String busStLon = "";

    String busStationNm = "";
    String busTerm = "";
    String busStNm = "";        //  arsId, gps정보 가져옴
    String getBusNumUrl = "";   //  arsId를 통해 버스번호(BusRouteNm) 제공
    String arsId = "";

    String walkTotalTime = "";
    String carTotalTime = "";

    String departureSt = "쌍문";  //  출발역 명
    String destinationSt = "어린이대공원";    // 도착역 명명

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailshcedule);

        subwayUrl = "http://swopenapi.seoul.go.kr/api/subway/" +
                subwayKey + "/json/shortestRoute/0/5/"+ departureSt + "/" + destinationSt;  //  지하철 최단소요시간

        busUrl = "http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getCrdntPrxmtSttnList?serviceKey="
                + serviceKey + "&gpsLati="+ departLat + "&gpsLong=" + departLon + "&_type=json";


        busStUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?serviceKey=" +
                serviceKey + "&tmX=" + departLon + "&tmY=" + departLat + "&radius=" + "400";

        transportdTv = (TextView)findViewById(R.id.transportDepartTv);
        transportNum = (TextView)findViewById(R.id.transportNum);
        timeTerm = (TextView)findViewById(R.id.leftTimeTv);
        trptImg = (ImageView)findViewById(R.id.transportImg);
        trptSpin = (Spinner)findViewById(R.id.transportSpinner);

        trptSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    trptImg.setImageResource(R.drawable.bus);

                    departLat = firstDepartureLat;
                    departLon = firstDepartureLon;
                    getBusStopXMLData(busStUrl);    //  출발지 근처 버스정류장을 가져옴(정류장명, arsId, gps)

                    spinFlag = 0;
                }
                else{
                    trptImg.setImageResource(R.drawable.train);

                    // 지하철이 몇분 뒤 도착하는지?

                    getSubwayStData(subwayUrl); //  출발역부터 도착역까지 걸리는 시간

                    departLat = destSubLat;
                    departLon = destSubLon;
                    destLat = finalDestLat;
                    destLon = finalDestLon;
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

        //getWalkTotalTimeData(walkingUrl);   //  초단위 보행자 소요 시간
        //getCarTotalTimeData(carUrl);   //  초단위 자동차 소요 시간

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

    public void getBusStopXMLData(String url) {
        class GetDataXML extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                StringBuffer buffer = new StringBuffer();
                try {
                    String uri = params[0];
                    URL url = new URL(uri); //문자열로 된 요청 url을 URL 객체로 생성.
                    InputStream is = url.openStream();  //url위치로 입력스트림 연결
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(new InputStreamReader(is, "UTF-8"));  //inputstream 으로부터 xml 입력받기
                    String tag = null;
                    xpp.next();

                    int eventType = xpp.getEventType();
                    boolean isItemTag = false; // <item> .영역에 인지 여부 체크

                    while(eventType != XmlPullParser.END_DOCUMENT){


                        if(eventType == XmlPullParser.START_TAG){ //시작 태그를 만났을때.
                            //태그명을 저장
                            tag = xpp.getName();
                            if(tag.equals("itemList"))
                                isItemTag = true;

                        }else if(eventType == XmlPullParser.TEXT){ //내용

                            if(isItemTag && tag.equals("arsId")) {
                                arsId = xpp.getText();
                                Log.d("arsId", arsId);
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                            if(isItemTag && tag.equals("stationNm")) {
                                busStationNm = xpp.getText();
                                Log.d("busstationNm", busStationNm);
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                            if(isItemTag && tag.equals("gpsX")) {
                                busStLon = xpp.getText();
                                destLon = busStLon;
                                Log.d("destlon", destLon);
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                            if(isItemTag && tag.equals("gpsY")) {
                                busStLat = xpp.getText();
                                destLat = busStLat;
                                Log.d("destlat", destLat);
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                        }else if(eventType == XmlPullParser.END_TAG){ //닫는 태그를 만났을때
                            //태그명을 저장
                            tag=xpp.getName();

                            if(tag.equals("itemList")){

                                break;

                            }
                        }

                        eventType = xpp.next(); //다음 이벤트 타입
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("receiveresult", buffer.toString());
                return buffer.toString();
            }

            @Override
            protected void onPostExecute(String buffer) {
                //Log.d("my", buffer);

                getBusNumUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getRouteByStation?ServiceKey="+serviceKey
                        +"&arsId="+arsId;
                getBusNumXMLData(getBusNumUrl);

                walkingUrl = "https://apis.skplanetx.com/tmap/routes/pedestrian?version=1" +
                        "&appKey=b42a1814-4abc-36c2-a743-43c5f81cd73d&" +
                        "startX="+departLon+"&startY="+departLat+"&endX="+destLon+"&endY="+destLat+"&startName="
                        +"start"+"&endName="+"end"+"&reqCoordType=WGS84GEO&resCoordType=WGS84GEO";
                getWalkTotalTimeData(walkingUrl);   //  걸어서 정류장까지 걸리는 시간
            }
        }

        GetDataXML g = new GetDataXML();
        g.execute(url);
    }

    public void getBusNumXMLData(String url) {
        class GetDataXML extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                StringBuffer buffer = new StringBuffer();
                try {
                    String uri = params[0];
                    URL url = new URL(uri); //문자열로 된 요청 url을 URL 객체로 생성.
                    InputStream is = url.openStream();  //url위치로 입력스트림 연결
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(new InputStreamReader(is, "UTF-8"));  //inputstream 으로부터 xml 입력받기
                    String tag = null;
                    xpp.next();

                    int eventType = xpp.getEventType();
                    boolean isItemTag = false; // <item> .영역에 인지 여부 체크

                    while(eventType != XmlPullParser.END_DOCUMENT){


                        if(eventType == XmlPullParser.START_TAG){ //시작 태그를 만났을때.
                            //태그명을 저장
                            tag = xpp.getName();
                            if(tag.equals("itemList"))
                                isItemTag = true;

                        }else if(eventType == XmlPullParser.TEXT){ //내용

                            if(isItemTag && tag.equals("busRouteNm")) {
                                busStNm = xpp.getText();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                            if(isItemTag && tag.equals("term")) {
                                busTerm = xpp.getText();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                        }else if(eventType == XmlPullParser.END_TAG){ //닫는 태그를 만났을때
                            //태그명을 저장
                            tag=xpp.getName();

                            if(tag.equals("itemList")){

                                break;

                            }
                        }

                        eventType = xpp.next(); //다음 이벤트 타입
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("BusNumresult", busStNm +", " + busTerm);
                return buffer.toString();
            }

            @Override
            protected void onPostExecute(String buffer) {
                transportNum.setText(busStNm);
                timeTerm.setText(busTerm+"분 간격");
                transportdTv.setText(busStationNm);
            }
        }

        GetDataXML g = new GetDataXML();
        g.execute(url);
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
                departLat = firstDepartureLat;
                departLon = firstDepartureLon;
                destLat = departSubLat;
                destLon = departSubLon;
                walkingUrl = "https://apis.skplanetx.com/tmap/routes/pedestrian?version=1" +
                        "&appKey=b42a1814-4abc-36c2-a743-43c5f81cd73d&" +
                        "startX="+departLon+"&startY="+departLat+"&endX="+destLon+"&endY="+destLat+"&startName="
                        +"start"+"&endName="+"end"+"&reqCoordType=WGS84GEO&resCoordType=WGS84GEO";

                getWalkTotalTimeData(walkingUrl);   //  출발지부터 근처 역까지 걸어서 가는 시간
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

            departLat = destSubLat;
            departLon = destSubLon;
            destLat = finalDestLat;
            destLon = finalDestLon;
            walkingUrl = "https://apis.skplanetx.com/tmap/routes/pedestrian?version=1" +
                    "&appKey=b42a1814-4abc-36c2-a743-43c5f81cd73d&" +
                    "startX="+departLon+"&startY="+departLat+"&endX="+destLon+"&endY="+destLat+"&startName="
                    +"start"+"&endName="+"end"+"&reqCoordType=WGS84GEO&resCoordType=WGS84GEO";

            getWalkTotalTimeData(walkingUrl);   //  도착역부터 목적지까지 걸어서 가는 시간

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
                    Log.d("receiveWalkTotalTime", String.valueOf(sb));
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                //Log.d("my", myJSON);
                makeWalkTotalList(myJSON); //리스트를 보여줌
            if(spinFlag == 0)
                {
                    departLat = busStLat;
                    departLon = busStLon;
                    destLat = finalDestLat;
                    destLon = finalDestLon;
                    carUrl = "https://apis.skplanetx.com/tmap/routes?version=1" +
                            "&appKey=b42a1814-4abc-36c2-a743-43c5f81cd73d&" +
                            "startX="+departLon+"&startY="+departLat+"&endX="+destLon+"&endY="+destLat+"&startName="
                            +"start"+"&endName="+"end"+"&reqCoordType=WGS84GEO&resCoordType=WGS84GEO";
                    getCarTotalTimeData(carUrl);
                }
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
                    Log.d("receiveCarTotalTime", String.valueOf(sb));
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

        busStUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?serviceKey=" +
                serviceKey + "&tmX=" + departLon + "&tmY=" + departLat + "&radius=" + "400";

        if(spinFlag == 0)
        {
            getBusStopXMLData(busStUrl);
        }
        else if(spinFlag == 1)
        {
            getSubwayStData(subwayUrl);
        }
    }

}