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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.a1.himaster.PopUp.Popup_emailchk;
import com.example.a1.himaster.SKPlanet.Tmap.MapActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoToBottom extends AppCompatActivity {

    public static final int REQUEST_CODE = 1003;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    EditText addressEt, nameEt, emailEt;
    Button okBtn, myAddressBtn;
    String residence_lat = "";
    String residence_lon = "";
    String departSubwayName = "";
    String departSubwayLat = "";
    String departSubwayLon = "";
    String url = "http://223.195.9.198:8080/saveinfo";
    String str="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoinit);
        okBtn = (Button) findViewById(R.id.okButton);
        myAddressBtn = (Button) findViewById(R.id.myaddress);

        nameEt = (EditText) findViewById(R.id.nameEdit);
        emailEt = (EditText) findViewById(R.id.emailEdit);
        addressEt = (EditText) findViewById(R.id.addressEdit);

        int color = Color.parseColor("#000000");
        emailEt.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        nameEt.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        addressEt.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        String email = "";

        myAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InfoToBottom.this, MapActivity.class);
                intent.putExtra("MAPFLAG", 3);
                startActivityForResult(intent, REQUEST_CODE);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                //finish();

            }

        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonOb = null;
                jsonOb = new JSONObject();


                SharedPreferences pref1 = getSharedPreferences("common_login", MODE_PRIVATE);
                SharedPreferences.Editor edit1 = pref1.edit();
                edit1.putString("CNAME", nameEt.getText().toString());
                edit1.putString("CEMAIL", emailEt.getText().toString());
                edit1.putString("RESIDENCE", addressEt.getText().toString());
                edit1.putString("RESIDENCE_LAT", residence_lat);
                edit1.putString("RESIDENCE_LON", residence_lon);
                edit1.putString("DEPART_SUBWAY_NAME", departSubwayName);
                edit1.putString("DEPART_SUBWAY_LAT", departSubwayLat);
                edit1.putString("DEPART_SUBWAY_LON", departSubwayLon);
                edit1.commit();

                if(emailEt.length() == 0 || checkEmail(emailEt.getText().toString()) != true)
                {
                    Intent intent = new Intent(InfoToBottom.this, Popup_emailchk.class);
                    startActivity(intent);
                    Log.d("emailchk", "wrong email format");
                }
                else {
                    Log.d("emailchk", "right email format");
                    SharedPreferences saveInfo = getSharedPreferences("loginFlag", MODE_PRIVATE);
                    SharedPreferences.Editor editor = saveInfo.edit();
                    editor.putString("FLAG", "4");
                    editor.putString("USERID", emailEt.getText().toString());
                    editor.putString("NAME", nameEt.getText().toString());
                    editor.putString("RESIDENCE", addressEt.getText().toString());
                    editor.putString("RESIDENCE_LAT", residence_lat);
                    editor.putString("RESIDENCE_LON", residence_lon);
                    editor.putString("DEPART_SUBWAY_NAME", departSubwayName);
                    editor.putString("DEPART_SUBWAY_LAT", departSubwayLat);
                    editor.putString("DEPART_SUBWAY_LON", departSubwayLon);
                    Log.d("subwaydd", departSubwayName + ", " + departSubwayLat + ", " + departSubwayLon);

                    editor.commit();

                    try {
                        jsonOb.put("userId", emailEt.getText().toString());
                        jsonOb.put("residence", addressEt.getText().toString());
                        jsonOb.put("nickname", nameEt.getText().toString());


                        Log.d("jsonEvent", jsonOb.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    str = jsonOb.toString();


                    PutDataJSON g = new PutDataJSON();
                    g.execute(url, str);
                    Intent intent = new Intent(InfoToBottom.this, BottombarActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                    finish();
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

        if (requestCode == REQUEST_CODE) {
            String residence = data.getExtras().getString("DEPARTURE");
            addressEt.setText(residence);
            residence_lat = data.getExtras().getString("DEPART_LAT");
            residence_lon = data.getExtras().getString("DEPART_LON");
            departSubwayName = data.getExtras().getString("DEPART_SUBWAY_NAME");
            departSubwayLat = data.getExtras().getString("DEPART_SUBWAY_LAT");
            departSubwayLon = data.getExtras().getString("DEPART_SUBWAY_LON");
        } else {
            Toast.makeText(InfoToBottom.this, "REQUEST_CODE가 아님", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 이메일 포맷 체크
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;

    }

    class PutDataJSON extends AsyncTask<String, Void, String> {
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
                con.setDoOutput(true);
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-type", "application/json");
                con.setConnectTimeout(5000);
                Log.d("sendContents", str);
                byte[] outputInBytes = params[1].getBytes("UTF-8");
                OutputStream os = con.getOutputStream();
                os.write(outputInBytes);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InfoToBottom.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
    }
}