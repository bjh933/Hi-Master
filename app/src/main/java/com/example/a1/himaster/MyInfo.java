package com.example.a1.himaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by a1 on 2017. 8. 15..
 */

public class MyInfo extends AppCompatActivity {

    TextView userEmailTv, myInfoTv, appConfigTv, helpTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);

        SharedPreferences saveInfo = getSharedPreferences("loginFlag", MODE_PRIVATE);
        String email = saveInfo.getString("USERID","");
        userEmailTv = (TextView)findViewById(R.id.userEmail);
        userEmailTv.setText(email);

        myInfoTv = (TextView)findViewById(R.id.myInfo);
        appConfigTv = (TextView)findViewById(R.id.appConfig);
        helpTv = (TextView)findViewById(R.id.help);

        myInfoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyInfo.this, num20_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        appConfigTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyInfo.this, num15_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        helpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyInfo.this, num16_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyInfo.this, BottombarActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
        return;
    }
}
