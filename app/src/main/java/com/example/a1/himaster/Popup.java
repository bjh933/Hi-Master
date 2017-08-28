package com.example.a1.himaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by a1 on 2017. 7. 29..
 */

public class Popup extends Activity {

    Button popupBtn;
    Button popupCloseBtn;
    TextView tvPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup);
        this.setFinishOnTouchOutside(false);
        tvPop = (TextView)findViewById(R.id.popText);
        popupBtn = (Button)findViewById(R.id.popBtn);
        popupCloseBtn = (Button)findViewById(R.id.popCloseBtn);

        popupCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Popup.this, BottombarActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });



    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }


}