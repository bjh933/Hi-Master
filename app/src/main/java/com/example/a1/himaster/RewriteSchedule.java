package com.example.a1.himaster;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1.himaster.PopUp.Popup_titlechk;
import com.example.a1.himaster.SKPlanet.Tmap.MapActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RewriteSchedule extends AppCompatActivity {

    Spinner spinner1, spinner2, spinner3, spinner4, spinner5, spinner6, spinner7, spinner8,
            spinner9, spinner10;
    ArrayAdapter<CharSequence> adapter;

    String urlPost = "http://192.168.21.213:8080/postschedule";
    //String urlPost = "http://233.195.9.198/postschedule";

    String subTitle = "";
    String str="";
    String fix = "false";
    String iDestination = "";
    String iDestinationLat = "";
    String iDestinationLon = "";

    String destSubwayLat = "";
    String destSubwayLon = "";
    String destSubwayName = "";

    String iDeparture = "";
    String iDepartLat = "";
    String iDepartLon = "";

    String departSubwayLat = "";
    String departSubwayLon = "";
    String departSubwayName = "";

    Button cancelBtn;
    Button okBtn, destBtn, departBtn;
    CheckBox resiChk;
    EditText todoTitle, destEdit, scheMemo, departEdit;
    RadioButton iljung, halil, hangsa;
    LinearLayout giganLay, giganLay2, siganLay, departLay, destLay, fixLay;
    public static final int REQUEST_CODE1 = 1000;
    public static final int REQUEST_CODE2 = 1001;
    int pos1, pos2, pos3, pos, flag;
    JSONArray posts = null;
    TextView dText;
    int chkFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makeschedule);

        int color = Color.parseColor("#000000");

        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        todoTitle = (EditText) findViewById(R.id.todoEt);
        iljung = (RadioButton) findViewById(R.id.radio0);
        halil = (RadioButton) findViewById(R.id.radio1);
        hangsa = (RadioButton) findViewById(R.id.radio2);
        giganLay = (LinearLayout) findViewById(R.id.giganLayout);
        giganLay2 = (LinearLayout) findViewById(R.id.giganLayout2);
        siganLay = (LinearLayout) findViewById(R.id.siganLayout);
        destLay = (LinearLayout) findViewById(R.id.destLayout);
        departLay = (LinearLayout) findViewById(R.id.departLayout);
        destEdit = (EditText) findViewById(R.id.destEdit);
        scheMemo = (EditText) findViewById(R.id.scheMemoText);
        dText = (TextView) findViewById(R.id.dText);
        destBtn = (Button) findViewById(R.id.destBtn);
        departBtn = (Button) findViewById(R.id.departBtn);
        departEdit = (EditText) findViewById(R.id.departEdit);
        resiChk = (CheckBox) findViewById(R.id.residencechk);
        todoTitle.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        departEdit.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        destEdit.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //체크된 라디오버튼id가 checkedId에 자동으로 들어가있음
                switch (checkedId) {
                    //선택된 라디오버튼에 해당하는 색상으로 글자색상바꾸기
                    case R.id.radio0:
                        giganLay.setVisibility(View.VISIBLE);
                        giganLay2.setVisibility(View.VISIBLE);
                        siganLay.setVisibility(View.VISIBLE);
                        departLay.setVisibility(View.VISIBLE);
                        destLay.setVisibility(View.VISIBLE);
                        flag = 0;
                        break;
                    case R.id.radio1:
                        giganLay2.setVisibility(View.GONE);
                        siganLay.setVisibility(View.GONE);
                        departLay.setVisibility(View.GONE);
                        destLay.setVisibility(View.GONE);
                        dText.setVisibility(View.GONE);
                        flag = 1;
                        break;
                    case R.id.radio2:
                        giganLay.setVisibility(View.VISIBLE);
                        giganLay2.setVisibility(View.VISIBLE);
                        siganLay.setVisibility(View.GONE);
                        destLay.setVisibility(View.GONE);
                        departLay.setVisibility(View.GONE);
                        flag = 2;
                        break;
                }
            }
        });
        spinner1 = (Spinner) findViewById(R.id.timeStartSpinner);
        spinner2 = (Spinner) findViewById(R.id.hourStartSpinner);
        spinner3 = (Spinner) findViewById(R.id.minStartSpinner);
        spinner5 = (Spinner) findViewById(R.id.yearStartSpinner);
        spinner6 = (Spinner) findViewById(R.id.monthStartSpinner);
        spinner7 = (Spinner) findViewById(R.id.dateStartSpinner);
        spinner8 = (Spinner) findViewById(R.id.yearEndSpinner);
        spinner9 = (Spinner) findViewById(R.id.monthEndSpinner);
        spinner10 = (Spinner) findViewById(R.id.dateEndSpinner);


        adapter = ArrayAdapter.createFromResource(this, R.array.timeSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.hourSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.minSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.yearStartSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.monthStartSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner6.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.dateStartSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner7.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.yearEndSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner8.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.monthEndSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner9.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.dateEndSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner10.setAdapter(adapter);

        SharedPreferences saveInfo = getSharedPreferences("loginFlag", MODE_PRIVATE);
        final String userId = saveInfo.getString("USERID", "");   //  userId 가져옴
        Log.d("uid", userId);

        Intent reData = getIntent();

        //String userId = reData.getExtras().getString("USERID");
        String title = reData.getExtras().getString("TITLE");
        subTitle = reData.getExtras().getString("SUBTITLE");
        String startDate = reData.getExtras().getString("DATE");
        String endDate = reData.getExtras().getString("ENDDATE");
        String startTime = reData.getExtras().getString("STARTTIME");
        String departure = reData.getExtras().getString("DEPARTURE");
        String depart_lat = reData.getExtras().getString("DEPART_LAT");
        String depart_lon = reData.getExtras().getString("DEPART_LON");
        String subway_name = reData.getExtras().getString("SUBWAY_NAME");
        String subway_lat = reData.getExtras().getString("SUBWAY_LAT");
        String subway_lon = reData.getExtras().getString("SUBWAY_LON");
        String destination = reData.getExtras().getString("DESTINATION");
        String memo = reData.getExtras().getString("MEMO");

        iDeparture = reData.getExtras().getString("DEPARTURE");
        iDepartLat = reData.getExtras().getString("DEPART_LAT");
        iDepartLon = reData.getExtras().getString("DEPART_LON");
        departSubwayName = reData.getExtras().getString("DEPART_SUBWAY_NAME");
        departSubwayLat = reData.getExtras().getString("DEPART_SUBWAY_LAT");
        departSubwayLon = reData.getExtras().getString("DEPART_SUBWAY_LON");

        iDestination = reData.getExtras().getString("DESTINATION");
        iDestinationLat = reData.getExtras().getString("DESTINATION_LAT");
        iDestinationLon = reData.getExtras().getString("DESTINATION_LON");
        destSubwayName = reData.getExtras().getString("DEST_SUBWAY_NAME");
        destSubwayLat = reData.getExtras().getString("DEST_SUBWAY_LAT");
        destSubwayLon = reData.getExtras().getString("DEST_SUBWAY_LON");

        todoTitle.setText(title);
        departEdit.setText(iDeparture);
        destEdit.setText(iDestination);
        scheMemo.setText(memo);

        ////

        String[] sTime = startTime.split(":");
        int startHour = Integer.valueOf(sTime[0]);
        int startMinute = Integer.valueOf(sTime[1]);

        if(startHour > 12)
        {
            spinner1.setSelection(1);
            startHour -= 12;
            spinner2.setSelection(startHour-1);
        }

        else if(startHour <= 12)
        {
            spinner1.setSelection(0);
            spinner2.setSelection(startHour-1);
        }

        for(int i=0;i<12;i++) {
            if (startMinute == i * 5)
            {
                spinner3.setSelection(i);
                break;
            }


        }

        ////받아온 시간 설정

        String[] syDate = startDate.split("년 ");
        pos1 = Integer.valueOf(syDate[0]);
        if (pos1 == 2017)
            pos = 0;
        else if (pos1 == 2018)
            pos = 1;
        else
            pos = 2;

        spinner5.setSelection(pos);

        String[] smDate = syDate[1].split("월 ");
        pos2 = Integer.valueOf(smDate[0]);
        spinner6.setSelection(pos2-1);

        String[] sdDate = smDate[1].split("일");
        pos3 = Integer.valueOf(sdDate[0]);
        spinner7.setSelection(pos3-1);
        //// 받아온 시작 날짜 설정

        String[] eyDate = endDate.split("년 ");
        pos1 = Integer.valueOf(eyDate[0]);
        if (pos1 == 2017)
            pos = 0;
        else if (pos1 == 2018)
            pos = 1;
        else
            pos = 2;

        spinner8.setSelection(pos);

        String[] emDate = eyDate[1].split("월 ");
        pos2 = Integer.valueOf(emDate[0]);
        spinner9.setSelection(pos2-1);

        String[] edDate = emDate[1].split("일");
        pos3 = Integer.valueOf(edDate[0]);
        spinner10.setSelection(pos3-1);

        //// 받아온 종료 날짜 설정


        //날짜 생성일 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        final String getTime = sdf.format(date);
        Log.d("today", getTime);
        //
        final Intent intent = new Intent(RewriteSchedule.this, BottombarActivity.class);

        final SharedPreferences resiInfo = getSharedPreferences("loginFlag", MODE_PRIVATE);

        resiChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkFlag == 0){
                    departEdit.setText(resiInfo.getString("RESIDENCE", ""));
                    iDepartLat = resiInfo.getString("RESIDENCE_LAT", "");
                    iDepartLon = resiInfo.getString("RESIDENCE_LON", "");
                    iDeparture = resiInfo.getString("RESIDENCE", "");
                    departSubwayName = resiInfo.getString("DEPART_SUBWAY_NAME", "");
                    departSubwayLat = resiInfo.getString("DEPART_SUBWAY_LAT", "");
                    departSubwayLon = resiInfo.getString("DEPART_SUBWAY_LON", "");
                    chkFlag = 1;
                    departBtn.setEnabled(false);
                }
                else
                {
                    departEdit.setText("");
                    iDepartLat = "";
                    iDepartLon = "";
                    iDeparture = "";
                    departSubwayName = "";
                    departSubwayLat = "";
                    departSubwayLon = "";
                    chkFlag = 0;
                    departBtn.setEnabled(true);
                }
            }
        });

        departBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewriteSchedule.this, MapActivity.class);
                intent.putExtra("MAPFLAG", 1);
                startActivityForResult(intent, REQUEST_CODE1);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }

        });

        destBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewriteSchedule.this, MapActivity.class);
                intent.putExtra("MAPFLAG", 2);
                startActivityForResult(intent, REQUEST_CODE2);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }

        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
            }

        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (todoTitle.getText().toString().equals("")) {
                    Intent intent = new Intent(RewriteSchedule.this, Popup_titlechk.class);
                    startActivity(intent);
                    Log.d("titlechk", "wrong format");
                }

                else if (!todoTitle.getText().toString().equals("")) {

                    JSONObject jsonOb = null;
                    jsonOb = new JSONObject();

                    final String doTitle = todoTitle.getText().toString();
                    String time = spinner1.getSelectedItem().toString() + " " + spinner2.getSelectedItem().toString() +
                            " : " + spinner3.getSelectedItem().toString();
                    listItem item = new listItem(time, doTitle);
                    String ampm = spinner1.getSelectedItem().toString();
                    String startHour = spinner2.getSelectedItem().toString();
                    String startMinute = spinner3.getSelectedItem().toString();

                    String yText = spinner5.getSelectedItem().toString();
                    String mText = spinner6.getSelectedItem().toString();
                    String dText = spinner7.getSelectedItem().toString();
                    String yEText = spinner8.getSelectedItem().toString();
                    String mEText = spinner9.getSelectedItem().toString();
                    String dEText = spinner10.getSelectedItem().toString();

                    String startDate = yText + "-" + mText + "-" + dText;
                    String dueDate = startDate;
                    String key = yEText + "-" + mEText + "-" + dEText;  //해쉬 Key
                    Log.d("KeyValue", key);
                    String endDate = key;

                    String destination = destEdit.getText().toString();
                    String memo = scheMemo.getText().toString();


                    intent.putExtra("todo", doTitle);
                    intent.putExtra("dayFrom", dText);
                    intent.putExtra("monthFrom", mText);
                    intent.putExtra("yearFrom", yText);
                    intent.putExtra("dayTo", dEText);
                    intent.putExtra("monthTo", mEText);
                    intent.putExtra("yearTo", yEText);
                    intent.putExtra("listItem", item);
                    intent.putExtra("hashKey", key);


                    if (ampm.equals("오후")) {
                        int sHour = Integer.parseInt(startHour);
                        sHour = sHour + 12;

                        startHour = String.valueOf(sHour);
                    }

                    String startTime = startHour + ":" + startMinute;

                    if (flag == 0) {

                        try {
                            jsonOb.put("userId", userId);
                            jsonOb.put("title", doTitle);
                            jsonOb.put("subTitle", subTitle);
                            jsonOb.put("startDate", startDate);
                            jsonOb.put("endDate", endDate);
                            jsonOb.put("startTime", startTime);

                            jsonOb.put("departure", iDeparture);
                            jsonOb.put("depart_lat", iDepartLat);
                            jsonOb.put("depart_lon", iDepartLon);
                            jsonOb.put("depart_subway_name", departSubwayName);
                            jsonOb.put("depart_subway_lat", departSubwayLat);
                            jsonOb.put("depart_subway_lon", departSubwayLon);

                            jsonOb.put("destination", iDestination);
                            jsonOb.put("destination_lat", iDestinationLat);
                            jsonOb.put("destination_lon", iDestinationLon);
                            jsonOb.put("dest_subway_name", destSubwayName);
                            jsonOb.put("dest_subway_lat", destSubwayLat);
                            jsonOb.put("dest_subway_lon", destSubwayLon);

                            jsonOb.put("memo", memo);
                            jsonOb.put("fix", fix);

                            Log.d("jsonSchedule", jsonOb.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        str = jsonOb.toString();

                    } else if (flag == 1) {

                        try {
                            jsonOb.put("userId", userId);
                            jsonOb.put("title", doTitle);
                            jsonOb.put("dueDate", dueDate);
                            jsonOb.put("memo", memo);
                            jsonOb.put("fix", fix);
                            destEdit.setText("");

                            Log.d("jsonTodo", jsonOb.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        str = jsonOb.toString();


                    } else if (flag == 2) {

                        try {
                            jsonOb.put("userId", userId);
                            jsonOb.put("title", doTitle);
                            jsonOb.put("startDate", startDate);
                            jsonOb.put("endDate", endDate);
                            jsonOb.put("memo", memo);

                            Log.d("jsonEvent", jsonOb.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        str = jsonOb.toString();

                    }

                    if (doTitle.length() != 0) {
                        PostDataJSON g = new PostDataJSON();
                        g.execute(urlPost, str);
                        setResult(RESULT_OK, intent);
                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                        finish();
                    } else {
                        setResult(RESULT_CANCELED, intent);
                        finish();
                    }

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        else if (requestCode == REQUEST_CODE1) {
            iDeparture = data.getExtras().getString("DEPARTURE");
            iDepartLat = data.getExtras().getString("DEPART_LAT");
            iDepartLon = data.getExtras().getString("DEPART_LON");
            departSubwayName = data.getExtras().getString("DEPART_SUBWAY_NAME");
            departSubwayLat = data.getExtras().getString("DEPART_SUBWAY_LAT");
            departSubwayLon = data.getExtras().getString("DEPART_SUBWAY_LON");
            departEdit.setText(iDeparture);
        }

        else if (requestCode == REQUEST_CODE2) {
            iDestination = data.getExtras().getString("DESTINATION");
            iDestinationLat = data.getExtras().getString("DESTINATION_LAT");
            iDestinationLon = data.getExtras().getString("DESTINATION_LON");
            destSubwayName = data.getExtras().getString("DEST_SUBWAY_NAME");
            destSubwayLat = data.getExtras().getString("DEST_SUBWAY_LAT");
            destSubwayLon = data.getExtras().getString("DEST_SUBWAY_LON");
            destEdit.setText(iDestination);

            } else {
                Toast.makeText(RewriteSchedule.this, "REQUEST_CODE가 아님", Toast.LENGTH_SHORT).show();
            }
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }


    class PostDataJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            //JSON 받아온다.
            String uri = params[0];
            String str = params[1];
            Log.d("url", uri);

            BufferedWriter bw = null;
            try {
                JSONObject jsonO = null;
                jsonO = new JSONObject(str);
                URL url = new URL(uri);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);  // Enable writing
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-type", "application/json");
                con.setConnectTimeout(5000);
                Log.d("sendContents", str);
                byte[] outputInBytes = params[1].getBytes("UTF-8");
                OutputStream os = con.getOutputStream();
                os.write( outputInBytes );
                os.flush();
                os.close();

                //--------------------------
                //   서버에서 전송받기
                //--------------------------
                InputStreamReader tmp = new InputStreamReader(con.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str1;
                while ((str1 = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                    builder.append(str1 + "\n");                     // View에 표시하기 위해 라인 구분자 추가
                }
                String result = builder.toString();                       // 전송결과를 전역 변수에 저장
                Log.d("receiveString", result);

                Log.d("sendResult", "success");
                return null;
            } catch (Exception e) {
                Log.d("sendResult", "fail");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String myJSON) {
            //Log.d("my", myJSON);
        }

    }


}
