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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import static android.view.LayoutInflater.from;

public class num10_Main extends AppCompatActivity {

    Spinner spinner1, spinner2, spinner3, spinner4, spinner5, spinner6, spinner7, spinner8,
            spinner9, spinner10;
    ArrayAdapter<CharSequence> adapter;
    ImageView backBtn;
    Button cancelBtn;
    Button okBtn;
    EditText todoTitle;
    public static final int REQUEST_CODE = 1001;
    int pos1, pos2, pos3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num10);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        todoTitle = (EditText) findViewById(R.id.todoEt);

        spinner1 = (Spinner) findViewById(R.id.timeSpinner);
        spinner2 = (Spinner) findViewById(R.id.hourSpinner);
        spinner3 = (Spinner) findViewById(R.id.minSpinner);
        spinner4 = (Spinner) findViewById(R.id.reSpinner);
        spinner5 = (Spinner) findViewById(R.id.yearStartSpinner);
        spinner6 = (Spinner) findViewById(R.id.monthStartSpinner);
        spinner7 = (Spinner) findViewById(R.id.dateStartSpinner);
        spinner8 = (Spinner) findViewById(R.id.yearEndSpinner);
        spinner9 = (Spinner) findViewById(R.id.monthEndSpinner);
        spinner10 = (Spinner) findViewById(R.id.dateEndSpinner);

        adapter = ArrayAdapter.createFromResource(this, R.array.timeSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.hourSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.minSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.reSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.yearStartSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.monthStartSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner6.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.dateStartSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner7.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.yearEndSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner8.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.monthEndSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner9.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.dateEndSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner10.setAdapter(adapter);

        Intent data = getIntent();
        pos1 = data.getExtras().getInt("calYear");
        if(pos1 == 2017)
            pos1 = 0;
        else if(pos1 == 2018)
            pos1 = 1;
        else
            pos1 = 2;

        pos2 = data.getExtras().getInt("calMonth");
        pos3 = data.getExtras().getInt("calDay");

        spinner5.setSelection(pos1);
        spinner6.setSelection(pos2);
        spinner7.setSelection(pos3-1);

        spinner8.setSelection(pos1);
        spinner9.setSelection(pos2);
        spinner10.setSelection(pos3-1);

        setCustomActionbar();
        backBtn = (ImageView) findViewById(R.id.menu_back);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num10_Main.this, num13_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num10_Main.this, num09_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String doTitle = todoTitle.getText().toString();

                Intent intent = new Intent(num10_Main.this, num09_Main.class);
                intent.putExtra("todo", doTitle);
                setResult(RESULT_OK, intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });


    }

    private void setCustomActionbar() {
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
