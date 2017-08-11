package com.example.a1.himaster;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

import static android.view.LayoutInflater.from;
import static android.view.View.GONE;

public class num10_Main extends AppCompatActivity {

    Spinner spinner1, spinner2, spinner3, spinner4, spinner5, spinner6, spinner7, spinner8,
            spinner9, spinner10;
    ArrayAdapter<CharSequence> adapter;
    ImageView backBtn;
    Button cancelBtn;
    Button okBtn;
    EditText todoTitle;
    RadioButton iljung, halil, hangsa;
    LinearLayout giganLay, giganLay2, siganLay, siganLay2, destLay, fixLay, hangsaLay;
    public static final int REQUEST_CODE = 1001;
    int pos1, pos2, pos3, pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num10);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        todoTitle = (EditText) findViewById(R.id.todoEt);
        iljung = (RadioButton)findViewById(R.id.radio0);
        halil = (RadioButton)findViewById(R.id.radio1);
        hangsa = (RadioButton)findViewById(R.id.radio2);
        giganLay = (LinearLayout)findViewById(R.id.giganLayout);
        giganLay2 = (LinearLayout)findViewById(R.id.giganLayout2);
        siganLay = (LinearLayout)findViewById(R.id.siganLayout);
        destLay = (LinearLayout)findViewById(R.id.destLayout);
        fixLay = (LinearLayout)findViewById(R.id.fixLayout);
        hangsaLay = (LinearLayout)findViewById(R.id.hangsaIconLayout);
        hangsaLay.setVisibility(View.GONE);
        RadioGroup rg   = (RadioGroup) findViewById(R.id.radioGroup1);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //체크된 라디오버튼id가 checkedId에 자동으로 들어가있음
                switch(checkedId){
                    //선택된 라디오버튼에 해당하는 색상으로 글자색상바꾸기
                    case R.id.radio0:
                        giganLay.setVisibility(View.VISIBLE);
                        giganLay2.setVisibility(View.VISIBLE);
                        siganLay.setVisibility(View.VISIBLE);
                        destLay.setVisibility(View.VISIBLE);
                        fixLay.setVisibility(View.VISIBLE);
                        hangsaLay.setVisibility(View.GONE);
                        break;
                    case R.id.radio1:
                        giganLay.setVisibility(View.GONE);
                        siganLay.setVisibility(View.GONE);
                        destLay.setVisibility(View.GONE);
                        fixLay.setVisibility(View.VISIBLE);
                        hangsaLay.setVisibility(View.GONE);
                        break;
                    case R.id.radio2:
                        giganLay.setVisibility(View.VISIBLE);
                        giganLay2.setVisibility(View.VISIBLE);
                        siganLay.setVisibility(View.VISIBLE);
                        destLay.setVisibility(View.VISIBLE);
                        fixLay.setVisibility(View.GONE);
                        hangsaLay.setVisibility(View.VISIBLE);
                       break;
                }
            }
        });
        spinner1 = (Spinner) findViewById(R.id.timeStartSpinner);
        spinner2 = (Spinner) findViewById(R.id.hourStartSpinner);
        spinner3 = (Spinner) findViewById(R.id.minStartSpinner);
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
            pos = 0;
        else if(pos1 == 2018)
            pos = 1;
        else
            pos = 2;

        pos2 = data.getExtras().getInt("calMonth");
        pos3 = data.getExtras().getInt("calDay");



        spinner5.setSelection(pos);
        spinner6.setSelection(pos2);
        spinner7.setSelection(pos3-1);

        spinner8.setSelection(pos);
        spinner9.setSelection(pos2);
        spinner10.setSelection(pos3-1);

        setCustomActionbar();
        backBtn = (ImageView) findViewById(R.id.menu_back);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num10_Main.this, num14_Main.class);
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
                //finish();
            }

        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String doTitle = todoTitle.getText().toString();
                String time = spinner1.getSelectedItem().toString() + " " + spinner2.getSelectedItem().toString() +
                        " : " + spinner3.getSelectedItem().toString();
                listItem item = new listItem(time, doTitle);


                String yText = spinner5.getSelectedItem().toString();
                String mText = spinner6.getSelectedItem().toString();
                String dText = spinner7.getSelectedItem().toString();
                String yEText = spinner8.getSelectedItem().toString();
                String mEText = spinner9.getSelectedItem().toString();
                String dEText = spinner10.getSelectedItem().toString();

                String key = yEText + "-" + mEText + "-" + dEText;  //해쉬 Key

                Intent intent = new Intent(num10_Main.this, num09_Main.class);
                intent.putExtra("todo", doTitle);
                intent.putExtra("dayFrom", dText);
                intent.putExtra("monthFrom", mText);
                intent.putExtra("yearFrom", yText);
                intent.putExtra("dayTo", dEText);
                intent.putExtra("monthTo", mEText);
                intent.putExtra("yearTo", yEText);
                intent.putExtra("listItem", item);
                intent.putExtra("hashKey", key);

                if(doTitle.length() != 0)
                {
                    setResult(RESULT_OK, intent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                    finish();
                 }

            else
                {
                    setResult(RESULT_CANCELED);
                }

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
