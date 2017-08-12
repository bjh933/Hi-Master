package com.example.a1.himaster;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.LayoutInflater.from;

public class num19_Main extends AppCompatActivity {

    ImageView backBtn;
    Button okBtn;
    Button addBtn;
    public static final int REQUEST_CODE = 1001;    //  달력 날짜 데이터 전달

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num19);

        setCustomActionbar();
        backBtn = (ImageView)findViewById(R.id.menu_back);
        okBtn = (Button)findViewById(R.id.okButton);
        addBtn = (Button)findViewById(R.id.addButton);
        long now = System.currentTimeMillis();

        Date date = new Date(now);
        final SimpleDateFormat CurYear = new SimpleDateFormat("yyyy");
        final SimpleDateFormat CurMonth = new SimpleDateFormat("M");
        final SimpleDateFormat CurDay = new SimpleDateFormat("d");
        final String strCurYear = CurYear.format(date);
        final String strCurMonth = CurMonth.format(date);
        final String strCurDay = CurDay.format(date);
        final int curYear = Integer.valueOf(strCurYear);
        final int curMonth = Integer.valueOf(strCurMonth)-1;
        final int tomorrow = Integer.valueOf(strCurDay)+1;

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num19_Main.this, num20_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num19_Main.this, BottombarActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num19_Main.this, num09_Main.class);

                //startActivityForResult(intent, REQUEST_CODE);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });



    }

    private void setCustomActionbar()
    {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = from(this).inflate(R.layout.abs_layout, null);
        actionBar.setCustomView(mCustomView);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#999999")));


        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(mCustomView, params);

    }

}
