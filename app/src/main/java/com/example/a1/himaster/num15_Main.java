package com.example.a1.himaster;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import static android.view.LayoutInflater.from;

public class num15_Main extends AppCompatActivity {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num15);

        spinner = (Spinner) findViewById(R.id.alarm);
        spinner = (Spinner) findViewById(R.id.alarmSound);
        spinner = (Spinner) findViewById(R.id.repeti);
        okBtn = (Button) findViewById(R.id.configOk);

        adapter = ArrayAdapter.createFromResource(this, R.array.alarm, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.alarmSound, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.repeti, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num15_Main.this, MyInfo.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(num15_Main.this, MyInfo.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
    }

}
