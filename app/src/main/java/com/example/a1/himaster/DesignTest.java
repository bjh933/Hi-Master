package com.example.a1.himaster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

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
 * Created by a1 on 2017. 8. 27..
 */

public class DesignTest extends AppCompatActivity {

    String departLat = "37.54931915";
    String departLon = "127.08117882";  //  어린이대공원
    String destLat = "37.49799082";
    String destLon = "127.02779625";    //  강남
    XmlPullParser xpp;

    String serviceKey = "Zfm%2Fjv9CdbeGPRRkBK3wjUOpqFhc5gONSyZbz0P4p13fH1s0CHO25CzN8Sf8VNPrjjfhIhdAh1SQagI6bQeTkw%3D%3D";

    String busStUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?serviceKey=" +
            serviceKey + "&tmX=" + departLon + "&tmY=" + departLat + "&radius=" + "400";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void getBusStopXMLData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                StringBuffer buffer = new StringBuffer();
                try {

                    URL url = new URL(busStUrl); //문자열로 된 요청 url을 URL 객체로 생성.

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
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                            if(isItemTag && tag.equals("gpsX")) {
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                            if(isItemTag && tag.equals("gpsY")) {
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                        }else if(eventType == XmlPullParser.END_TAG){ //닫는 태그를 만났을때
                            //태그명을 저장
                            tag=xpp.getName();

                            if(tag.equals("itemList")){

                                isItemTag = false; //초기화

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
            protected void onPostExecute(String myJSON) {
                //Log.d("my", myJSON);
                makeBusStopList(myJSON); //리스트를 보여줌
            }
        }

            GetDataJSON g = new GetDataJSON();
            g.execute(url);
    }

    public void makeBusStopList(String myJSON) {

    }
}
