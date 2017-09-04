package com.example.a1.himaster.PopUp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.a1.himaster.R;

/**
 * Created by a1 on 2017. 7. 29..
 */

public class Popup_titlechk extends Activity {

    Button popupBtn;
    TextView tvPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_title);
        this.setFinishOnTouchOutside(false);
        tvPop = (TextView)findViewById(R.id.popText);
        popupBtn = (Button)findViewById(R.id.popBtn);

        popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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