package com.example.a1.himaster;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.LayoutInflater.from;

public class num03_Main extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private TextView rain_Activity;

    ImageView backBtn;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num03);

        spinner = (Spinner) findViewById(R.id.spiner);
        button = (Button) findViewById(R.id.btn_movePage);
        button.setOnClickListener(this);
        setCustomActionbar();
        backBtn = (ImageView)findViewById(R.id.menu_back);

        adapter = ArrayAdapter.createFromResource(this, R.array.city_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num03_Main.this, num04_Main.class);
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
                Intent i = new Intent(this, num03_ViewActivity.class);
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