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

public class DetailScheduleTest extends AppCompatActivity {

    JSONArray posts = null;
    TextView dDateTv, dTimeTv, dTitleTv, dDepartTv, dArriveTv;
    Spinner trptSpin;
    String dTitle, dDate, dTime, dDest = "";
    ImageView trptImg;
    String serviceKey = "Zfm%2Fjv9CdbeGPRRkBK3wjUOpqFhc5gONSyZbz0P4p13fH1s0CHO25CzN8Sf8VNPrjjfhIhdAh1SQagI6bQeTkw%3D%3D";
    String destLat = "37.49799082";
    String destLon = "127.02779625";    //  강남
    String subLat = "37.49810194";
    String subLon = "127.02876838"; //  강남역 2호선

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailshcedule);

        trptImg = (ImageView)findViewById(R.id.transportImg);
        trptSpin = (Spinner)findViewById(R.id.transportSpinner);

        trptSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    trptImg.setImageResource(R.drawable.bus);
                }
                else{
                    trptImg.setImageResource(R.drawable.train);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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


        String url = "http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getCrdntPrxmtSttnList?serviceKey="
                + serviceKey + "&gpsLati="+ destLat + "&gpsLong=" + destLon + "&_type=json";

        getBusStopData(url);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

    }

}