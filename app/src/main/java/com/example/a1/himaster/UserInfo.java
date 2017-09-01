package com.example.a1.himaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a1.himaster.SKPlanet.Tmap.MapActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInfo extends AppCompatActivity {
    public static final int REQUEST_CODE = 1003;
    ArrayAdapter<CharSequence> adapter;
    EditText addressEt, nameEt, emailEt;
    Button okBtn, myAddressBtn;
    String residence_lat = "";
    String residence_lon = "";
    String departSubwayName = "";
    String departSubwayLat = "";
    String departSubwayLon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);
        okBtn = (Button)findViewById(R.id.okButton);
        myAddressBtn = (Button)findViewById(R.id.myaddress);
        addressEt = (EditText)findViewById(R.id.addressEdit);
        nameEt = (EditText)findViewById(R.id.nameEdit);
        emailEt = (EditText)findViewById(R.id.emailEdit);
        addressEt.setFocusable(false);
        addressEt.setClickable(false);

        final SharedPreferences pref = getSharedPreferences("loginFlag", MODE_PRIVATE);
        final String loginFlag = pref.getString("FLAG", "4");
        String email = "";

        if(loginFlag.equals("1"))
        {
            SharedPreferences guestInfo = getSharedPreferences("fb_login", MODE_PRIVATE);
            email = guestInfo.getString("EMAIL", "");
            String name = guestInfo.getString("NAME", "");
            String residence = guestInfo.getString("RESIDENCE", "");
            addressEt.setText(residence);
            nameEt.setText(name);
            emailEt.setText(email);

        }
        else if(loginFlag.equals("2"))
        {
            SharedPreferences guestInfo = getSharedPreferences("gg_login", MODE_PRIVATE);
            String name = guestInfo.getString("GNAME", "");
            email = guestInfo.getString("GMAIL", "");
            String residence = guestInfo.getString("RESIDENCE", "");
            addressEt.setText(residence);
            nameEt.setText(name);
            emailEt.setText(email);
            emailEt.setFocusable(false);
            emailEt.setClickable(false);
        }
        else if(loginFlag.equals("3"))
        {
            SharedPreferences guestInfo = getSharedPreferences("ka_login", MODE_PRIVATE);
            String name = guestInfo.getString("KNAME", "");
            email = guestInfo.getString("KEMAIL", "");
            String residence = guestInfo.getString("RESIDENCE", "");
            addressEt.setText(residence);
            nameEt.setText(name);
            nameEt.setFocusable(false);
            nameEt.setClickable(false);
            emailEt.setText(email);
        }
        else
        {
            nameEt.setFocusable(true);
            nameEt.setClickable(true);
            emailEt.setFocusable(true);
            emailEt.setClickable(true);

            SharedPreferences guestInfo = getSharedPreferences("common_login", MODE_PRIVATE);
            String name = guestInfo.getString("CNAME", "");
            email = guestInfo.getString("CEMAIL", "");
            String residence = guestInfo.getString("RESIDENCE", "");
            addressEt.setText(residence);
            nameEt.setText(name);
            emailEt.setText(email);
        }

        myAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserInfo.this, MapActivity.class);
                intent.putExtra("MAPFLAG", 3);
                startActivityForResult(intent, REQUEST_CODE);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                //finish();

            }

        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(loginFlag.equals("1")){
                SharedPreferences pref = getSharedPreferences("fb_login", MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("RESIDENCE", addressEt.getText().toString());
                edit.putString("RESIDENCE_LAT", residence_lat);
                edit.putString("RESIDENCE_LON", residence_lon);
                edit.putString("DEPART_SUBWAY_NAME", departSubwayName);
                edit.putString("DEPART_SUBWAY_LAT", departSubwayLat);
                edit.putString("DEPART_SUBWAY_LON", departSubwayLon);
                edit.commit();
            }

            else if(loginFlag.equals("2")){
                SharedPreferences pref = getSharedPreferences("gg_login", MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("GNAME", nameEt.getText().toString());
                edit.putString("RESIDENCE", addressEt.getText().toString());
                edit.putString("RESIDENCE_LAT", residence_lat);
                edit.putString("RESIDENCE_LON", residence_lon);
                edit.putString("DEPART_SUBWAY_NAME", departSubwayName);
                edit.putString("DEPART_SUBWAY_LAT", departSubwayLat);
                edit.putString("DEPART_SUBWAY_LON", departSubwayLon);
                edit.commit();
            }

            else if(loginFlag.equals("3")){
                SharedPreferences pref = getSharedPreferences("ka_login", MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("KEMAIL", emailEt.getText().toString());
                edit.putString("RESIDENCE", addressEt.getText().toString());
                edit.putString("RESIDENCE_LAT", residence_lat);
                edit.putString("RESIDENCE_LON", residence_lon);
                edit.putString("DEPART_SUBWAY_NAME", departSubwayName);
                edit.putString("DEPART_SUBWAY_LAT", departSubwayLat);
                edit.putString("DEPART_SUBWAY_LON", departSubwayLon);
                edit.commit();
            }

            else if(loginFlag.equals("4")) {
                SharedPreferences pref = getSharedPreferences("common_login", MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("CNAME", nameEt.getText().toString());
                edit.putString("CEMAIL", emailEt.getText().toString());
                edit.putString("RESIDENCE", addressEt.getText().toString());
                edit.putString("RESIDENCE_LAT", residence_lat);
                edit.putString("RESIDENCE_LON", residence_lon);
                edit.putString("DEPART_SUBWAY_NAME", departSubwayName);
                edit.putString("DEPART_SUBWAY_LAT", departSubwayLat);
                edit.putString("DEPART_SUBWAY_LON", departSubwayLon);
                edit.commit();
            }
                if(emailEt.length() == 0 || checkEmail(emailEt.getText().toString()) != true)
                {
                    Intent intent = new Intent(UserInfo.this, Popup_emailchk.class);
                    startActivity(intent);
                    Log.d("emailchk", "wrong email format");
                }
                else {
                    Log.d("emailchk", "right email format");
                    SharedPreferences saveInfo = getSharedPreferences("loginFlag", MODE_PRIVATE);
                    SharedPreferences.Editor editor = saveInfo.edit();
                    editor.putString("FLAG", loginFlag);
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
                    Intent intent = new Intent(UserInfo.this, MyInfo.class);
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

        if (requestCode == REQUEST_CODE ) {
            String residence = data.getExtras().getString("DEPARTURE");
            addressEt.setText(residence);
            residence_lat  = data.getExtras().getString("DEPART_LAT");
            residence_lon  = data.getExtras().getString("DEPART_LON");
            departSubwayName = data.getExtras().getString("DEPART_SUBWAY_NAME");
            departSubwayLat = data.getExtras().getString("DEPART_SUBWAY_LAT");
            departSubwayLon = data.getExtras().getString("DEPART_SUBWAY_LON");
        } else {
            Toast.makeText(UserInfo.this, "REQUEST_CODE가 아님", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 이메일 포맷 체크
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserInfo.this, MyInfo.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
    }
}