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
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        checkPermission1();

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

    public void checkPermission1() {

        int pCheck1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (pCheck1 == PackageManager.PERMISSION_DENIED) {
            //권한 추가
            ActivityCompat.requestPermissions(IntroActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);

        }

        int pCheck2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (pCheck2 == PackageManager.PERMISSION_DENIED) {
            //권한 추가
            ActivityCompat.requestPermissions(IntroActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {
                case REQUEST_WRITE_EXTERNAL_STORAGE:
                    for (int i = 0; i < permissions.length; i++) {
                        String permission = permissions[i];
                        int grantResult = grantResults[i];
                        if (permission.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            if(grantResult == PackageManager.PERMISSION_GRANTED) {
                                Log.d("ssss","success");

                            } else {
                                Log.d("ssss","fail");
                            }
                        }
                    }
                    break;

                case REQUEST_ACCESS_FINE_LOCATION:
                    for (int i = 0; i < permissions.length; i++) {
                        String permission = permissions[i];
                        int grantResult = grantResults[i];
                        if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            if(grantResult == PackageManager.PERMISSION_GRANTED) {
                                Log.d("ssss","success2");
                            } else {
                                Log.d("ssss","fail2");
                            }
                        }
                    }
                    break;
            }
        }
}