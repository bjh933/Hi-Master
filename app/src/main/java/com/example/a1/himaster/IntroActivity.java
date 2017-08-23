package com.example.a1.himaster;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;


public class IntroActivity extends Activity {
    Boolean loginChecked;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        pref = getSharedPreferences("loginFlag", MODE_PRIVATE);
        String flag = pref.getString("FLAG", "");
        boolean flagCheck = false;

        if(!flag.equals("")){
            flagCheck = true;
        }

        if(flagCheck)
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    Intent intent = new Intent(IntroActivity.this,
                            BottombarActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                    finish();
                }
            }, 2000);
        }

        else
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(IntroActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                    finish();
                }
            }, 2000);

        }


    }

}