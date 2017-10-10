package com.example.a1.himaster;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a1.himaster.Adapter.BusRouteAdapter;
import com.example.a1.himaster.Model.Bus;
import com.example.a1.himaster.SKPlanet.WeatherWeekThread;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by a1 on 2017. 8. 17..
 */

public class DetailSchedule extends AppCompatActivity {

    RecyclerView rv1;

    TextView dDateTv, dTimeTv, dTitleTv, dDepartTv, dArriveTv, transportNum, startTransport, firstEndTransport,
            totalTime, skyTv, tMaxTv, tMinTv, dateWeatherTv;
    Spinner trptSpin;
    String dTitle, dDate, dTime, dDest = "";
    ImageView trptImg, weatherIv;
    String serviceKey = "Zfm%2Fjv9CdbeGPRRkBK3wjUOpqFhc5gONSyZbz0P4p13fH1s0CHO25CzN8Sf8VNPrjjfhIhdAh1SQagI6bQeTkw%3D%3D";
    String subwayKey = "50496e7a64646a673235696b79684b";
    String walkingUrl = "";
    LinearLayout rvLinear;

    String firstDepartureLat = "37.643974";
    String firstDepartureLon = "127.0357636";
    String finalDestLat = "37.5505411";
    String finalDestLon = "127.07384612";

    String departure = "";
    String departLat = "37.643974";
    String departLon = "127.0357636";  //  강원학사
    String destLat = "37.55054112";
    String destLon = "127.07384612";    //  세종대
    String departSubLat = "37.64805684";
    String departSubLon = "127.03456915";   //  쌍문역
    String destSubLat = "37.54801363";
    String destSubLon = "127.07467945"; //  어린이대공원역

    int spinFlag = 0;

    String subwayUrl = "";
    String busRouteUrl = "";
    String subwayNumUrl;

    String[] routeNm = new String[5];
    String[] fname = new String[5];
    String[] tname = new String[5];
    String[] fx = new String[5];
    String[] fy = new String[5];
    String[] tx = new String[5];
    String[] ty = new String[5];

    String walkTotalTime = "";
    String busTotalTime = "";

    String departureSt = "쌍문역 4번출구";  //  출발역 명
    String destinationSt = "어린이대공원역 7번출구";    // 도착역 명
    ArrayList<Bus> busList;
    BusRouteAdapter adapter;
    int walkingTime = 0;
    int walkFlag = 0;

    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailshcedule);

        Context mContext = this;

        rvLinear = (LinearLayout)findViewById(R.id.rvLinear);
        totalTime = (TextView)findViewById(R.id.totalTime);
        busList = new ArrayList<Bus>();
        weatherIv = (ImageView)findViewById(R.id.weatherIv);
        tMaxTv = (TextView)findViewById(R.id.tMaxTv);
        tMinTv = (TextView)findViewById(R.id.tMinTv);
        skyTv = (TextView)findViewById(R.id.skyTv);
        dateWeatherTv = (TextView)findViewById(R.id.dateWeatherTv);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat toDate = new SimpleDateFormat("MMM . dd / EEE", Locale.ENGLISH);
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        String tmoDate = toDate.format(date);
        dateWeatherTv.setText(tmoDate);

        subwayUrl = "http://swopenapi.seoul.go.kr/api/subway/" +
                subwayKey + "/json/shortestRoute/0/5/"+ departureSt + "/" + destinationSt;  //  지하철 최단소요시간

        busRouteUrl = "http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoByBus?serviceKey=" +
                serviceKey +"&startX="+firstDepartureLon+"&startY="+firstDepartureLat+"&endX="+
                finalDestLon+"&endY="+finalDestLat;

        firstEndTransport = (TextView)findViewById(R.id.endBusTv);
        transportNum = (TextView)findViewById(R.id.transportNum);
        startTransport = (TextView)findViewById(R.id.startBusTv);
        trptImg = (ImageView)findViewById(R.id.transportImg);
        trptSpin = (Spinner)findViewById(R.id.transportSpinner);


        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv1 = (RecyclerView)findViewById(R.id.busRouteRv);
        rv1.setHasFixedSize(true);
        rv1.setLayoutManager(mLinearLayoutManager);

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
        firstDepartureLat = data.getExtras().getString("DEPART_LAT");
        firstDepartureLon = data.getExtras().getString("DEPART_LON");
        departure = data.getExtras().getString("DEPARTURE");
        departLat = data.getExtras().getString("DEPART_LAT");
        departLon = data.getExtras().getString("DEPART_LON");
        departureSt = data.getExtras().getString("DEPART_SUBWAY_NAME");
        departSubLat = data.getExtras().getString("DEPART_SUBWAY_LAT");
        departSubLon = data.getExtras().getString("DEPART_SUBWAY_LON");

        finalDestLat = data.getExtras().getString("DESTINATION_LAT");
        finalDestLon = data.getExtras().getString("DESTINATION_LON");

        destLat = data.getExtras().getString("DESTINATION_LAT");
        destLon = data.getExtras().getString("DESTINATION_LON");
        destinationSt = data.getExtras().getString("DEST_SUBWAY_NAME");
        destSubLat = data.getExtras().getString("DEST_SUBWAY_LAT");
        destSubLon = data.getExtras().getString("DEST_SUBWAY_LON");

        dTitleTv.setText(dTitle);
        dDateTv.setText(dateArr[0]);
        dTimeTv.setText(dTime);
        dDepartTv.setText(departure);
        dArriveTv.setText(dDest);


        departureSt = processSubwaySt(departureSt);
        destinationSt = processSubwaySt(destinationSt);


        trptSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {

                    rvLinear.setVisibility(View.VISIBLE);
                    trptImg.setImageResource(R.drawable.bus);
                    walkingTime = 0;
                    walkFlag = 0;

                    departLat = firstDepartureLat;
                    departLon = firstDepartureLon;
                    destLat = finalDestLat;
                    destLon = finalDestLon;

                    busRouteUrl = "http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoByBus?serviceKey=" +
                            serviceKey +"&startX="+departLon+"&startY="+departLat+"&endX="+
                            destLon+"&endY="+destLat;

                    getBusRouteXMLData(busRouteUrl);    //  출발지 근처 버스정류장을 가져옴(정류장명, arsId, gps)

                    spinFlag = 0;
                }
                else{
                    rvLinear.setVisibility(View.GONE);
                    trptImg.setImageResource(R.drawable.train);
                    walkingTime = 0;
                    walkFlag = 0;

                    transportNum.setText("");
                    startTransport.setText(departureSt + "역");
                    firstEndTransport.setText(destinationSt + "역");


                    subwayNumUrl = "http://swopenAPI.seoul.go.kr/api/subway/"+subwayKey+
                            "/json/stationTimetable/0/5/"+ departureSt; //  지하철 몇 호선인지 정보 가져옴

                    subwayUrl = "http://swopenapi.seoul.go.kr/api/subway/" +
                            subwayKey + "/json/shortestRoute/0/5/"+ departureSt + "/" + destinationSt;
                    Log.d("subway",departureSt);

                    getSubwayNumData(subwayNumUrl);
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



        Handler handler = new Handler(){
            public void handleMessage(Message msg)
            {
                String str = msg.getData().getString("WeekWeather");    /// 번들에 들어있는 값 꺼냄
                Log.d("weather7days", str);
                String[] weatherStr = str.split("-");
                skyTv.setText(weatherStr[0]);
                setSky(weatherStr[0], weatherIv);
                String tMax = weatherStr[1].replace(".00", "");
                String tMin = weatherStr[2].replace(".00", "");
                tMaxTv.setText(tMax);
                tMaxTv.append(" ℃");
                tMinTv.setText(tMin);
                tMinTv.append(" ℃");
            }};

        WeatherWeekThread wt = new WeatherWeekThread(handler, mContext, 37.5714000000, 126.9658000000);
        wt.run();   //  내일 날씨

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

    public void setSky(String skyStatus, ImageView iv) {

        if(skyStatus.equals("맑음"))
            iv.setImageResource(R.drawable.sunny);
        else if(skyStatus.equals("구름조금") || skyStatus.equals("구름많음") || skyStatus.equals("흐림"))
            iv.setImageResource(R.drawable.cloudy);
        else if(skyStatus.equals("구름많고 비") || skyStatus.equals("흐리고 비"))
            iv.setImageResource(R.drawable.rainy);
    }

    public void getBusRouteXMLData(String url) {
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
                    int i = 0;
                    Bus bus = new Bus();

                    while(eventType != XmlPullParser.END_DOCUMENT){


                        if(eventType == XmlPullParser.START_TAG){ //시작 태그를 만났을때.
                            //태그명을 저장
                            tag = xpp.getName();
                            if(tag.equals("itemList"))
                                isItemTag = true;

                        }

                        else if(eventType == XmlPullParser.TEXT){ //내용

                            if(isItemTag && tag.equals("routeNm")) {
                                routeNm[i] = xpp.getText();
                                if(i != 0) {
                                    bus.setBusNum(routeNm[i]);
                                }
                                Log.d("routeNm", routeNm[i]);
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                            if(isItemTag && tag.equals("fname")) {
                                fname[i] = xpp.getText();
                                if(i != 0) {
                                    bus.setFname(fname[i]);
                                }
                                Log.d("startPoint", fname[i]);
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                            if(isItemTag && tag.equals("tname")) {
                                tname[i] = xpp.getText();
                                if(i != 0) {
                                    bus.setTname(tname[i]);
                                    busList.add(bus);
                                    Log.d("whyyy0", busList.get(0).getBusNum());
                                }
                                Log.d("endPoint", tname[i]);
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                            if(isItemTag && tag.equals("fx")) {
                                fx[i] = xpp.getText();
                                if(i == 0)
                                    destLon = fx[i];
                                Log.d("startFx", fx[i]);
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                            if(isItemTag && tag.equals("fy")) {
                                fy[i] = xpp.getText();
                                if(i == 0)
                                    destLat = fy[i];
                                Log.d("startFy", fy[i]);
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                            if(isItemTag && tag.equals("tx")) {
                                tx[i] = xpp.getText();
                                Log.d("startTx", tx[i]);
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                            if(isItemTag && tag.equals("ty")) {
                                ty[i] = xpp.getText();
                                Log.d("startTy", ty[i]);
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                            if(isItemTag && tag.equals("time")) {
                                busTotalTime = xpp.getText();
                                int mTosTotalTime = Integer.valueOf(busTotalTime) * 60;
                                walkingTime += mTosTotalTime;

                                Log.d("busTotalTime", busTotalTime);
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                            if(i != 0) {

                            }
                        }else if(eventType == XmlPullParser.END_TAG){ //닫는 태그를 만났을때
                            //태그명을 저장
                            tag=xpp.getName();

                            if(tag.equals("pathList"))
                            {
                                i++;
                            }

                            if(tag.equals("itemList"))
                                break;

                        }

                        eventType = xpp.next(); //다음 이벤트 타입
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("Routeresult", buffer.toString());
                return buffer.toString();
            }

            @Override
            protected void onPostExecute(String buffer) {
                Log.d("startBusSt", departLat+ ", " +departLon +", "+ destLat+", "+destLon);
                Log.d("busListSize", String.valueOf(busList.size()));

                transportNum.setText(routeNm[0]);
                startTransport.setText(fname[0]);
                firstEndTransport.setText(tname[0]);

                walkingUrl = "https://apis.skplanetx.com/tmap/routes/pedestrian?version=1" +
                        "&appKey=b42a1814-4abc-36c2-a743-43c5f81cd73d&" +
                        "startX="+departLon+"&startY="+departLat+"&endX="+destLon+"&endY="+destLat+"&startName="
                        +"start"+"&endName="+"end"+"&reqCoordType=WGS84GEO&resCoordType=WGS84GEO";

                departLon = tx[busList.size()];
                departLat = ty[busList.size()];
                destLon = finalDestLon;
                destLat = finalDestLat;
                getWalkTotalTimeData(walkingUrl);   //  걸어서 정류장까지 걸리는 시간

                walkFlag = 1;
                makeBusRouteList(busList);


            }
        }

        GetDataXML g = new GetDataXML();
        g.execute(url);
    }

    public void makeBusRouteList(ArrayList<Bus> busList) {

        ArrayList<HashMap<String,String>> busRouteList = new ArrayList<HashMap<String, String>>();;

        for(int i=0;i<busList.size();i++)
        {
            HashMap<String,String> posts = new HashMap<String, String>();
            posts.put("busNum", busList.get(i).getBusNum().toString());
            posts.put("start", busList.get(i).getFname().toString());
            posts.put("end", busList.get(i).getTname().toString());

            //ArrayList에 HashMap 붙이기
            busRouteList.add(posts);

        }

        adapter = new BusRouteAdapter(this, busRouteList);
        rv1.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        int size = busList.size();
        for(int i=0;i<size;i++) {
            busList.remove(i);
        }

        walkingUrl = "https://apis.skplanetx.com/tmap/routes/pedestrian?version=1" +
                "&appKey=b42a1814-4abc-36c2-a743-43c5f81cd73d&" +
                "startX="+departLon+"&startY="+departLat+"&endX="+destLon+"&endY="+destLat+"&startName="
                +"start"+"&endName="+"end"+"&reqCoordType=WGS84GEO&resCoordType=WGS84GEO";

        getWalkTotalTimeData(walkingUrl);   //  하차지에서 목적지까지 걸어서 걸리는 시간
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
                departLat = firstDepartureLat;
                departLon = firstDepartureLon;
                destLat = departSubLat;
                destLon = departSubLon;

                walkingUrl = "https://apis.skplanetx.com/tmap/routes/pedestrian?version=1" +
                        "&appKey=b42a1814-4abc-36c2-a743-43c5f81cd73d&" +
                        "startX="+departLon+"&startY="+departLat+"&endX="+destLon+"&endY="+destLat+"&startName="
                        +"start"+"&endName="+"end"+"&reqCoordType=WGS84GEO&resCoordType=WGS84GEO";

                getWalkTotalTimeData(walkingUrl);   //  출발지부터 근처 역까지 걸어서 가는 시간
                walkFlag = 1;
                makeSubwayStList(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void makeSubwayStList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            JSONArray posts = jsonObj.getJSONArray("shortestRouteList");

            for(int i=0; i<posts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = posts.getJSONObject(i);
                String departStation = c.getString("statnFnm");    //  출발역 명
                String destStation = c.getString("statnTnm");    //  도착역 명
                String stimeInfo = c.getString("shtTravelMsg");    //  최단 시간 도착예정 안내
                String stationCnt = c.getString("shtStatnCnt");    //  지나는 역 수
                String sTime = c.getString("shtTravelTm");    //  도착까지 걸리는 시간(분)
                int mTosTotalTime = Integer.valueOf(sTime) * 60;
                walkingTime += mTosTotalTime;


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
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void makeWalkTotalList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            JSONArray posts = jsonObj.getJSONArray("features");

            JSONObject c = posts.getJSONObject(0).getJSONObject("properties");
            walkTotalTime = c.getString("totalTime");
            if(walkFlag == 1)
            {
                walkingTime += Integer.valueOf(walkTotalTime);
                walkingTime /= 60;
                String totalMinute = String.valueOf(walkingTime) + "분";
                totalTime.setText(totalMinute);
                walkFlag = 0;
            }
            else
            {
                walkingTime += Integer.valueOf(walkTotalTime);
                Log.d("walkingTime", String.valueOf(walkingTime));
            }
        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

    }

    public String processSubwaySt(String str)
    {
        int index = str.indexOf("역");
        String subwayName = str.substring(0, index);

        return subwayName;
    }

    public void getSubwayNumData(String url) {
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

                getSubwayNum(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void getSubwayNum(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            JSONArray posts = jsonObj.getJSONArray("timeTableList");

                JSONObject c = posts.getJSONObject(0);
                String subwayNm = c.getString("subwayNm");    //  출발역 호선 번호

            transportNum.setText(subwayNm);

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

}