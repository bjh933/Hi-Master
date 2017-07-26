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

import static android.view.LayoutInflater.from;

public class num02_RecommandLocationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_movePage;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num02_recommand_location);

        btn_movePage = (Button) findViewById(R.id.btn_movePage);
        btn_movePage.setOnClickListener(this);

        setCustomActionbar();
        backBtn = (ImageView)findViewById(R.id.menu_back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num02_RecommandLocationActivity.this, num02_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_movePage:
                Intent i = new Intent(this, num02_RecommandLocationActivity.class);
                startActivity(i);
                break;
        }
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