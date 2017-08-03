package com.example.a1.himaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import static android.view.LayoutInflater.from;

public class num20_Main extends AppCompatActivity {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    ImageView backBtn;
    EditText addressEt, nameEt, emailEt;
    RadioButton rb_male, rb_female;
    Button okBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num20);
        okBtn = (Button)findViewById(R.id.okButton);
        rb_male = (RadioButton)findViewById(R.id.male);
        rb_female = (RadioButton)findViewById(R.id.female);

        nameEt = (EditText)findViewById(R.id.nameEdit);
        emailEt = (EditText)findViewById(R.id.emailEdit);
        addressEt = (EditText)findViewById(R.id.addressEdit);

        spinner = (Spinner) findViewById(R.id.jobSpinner);

        adapter = ArrayAdapter.createFromResource(this, R.array.jobSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setCustomActionbar();

        final SharedPreferences pref = getSharedPreferences("loginFlag", MODE_PRIVATE);
        final String loginFlag = pref.getString("FLAG", "4");

        if(loginFlag.equals("1"))
        {
            SharedPreferences guestInfo = getSharedPreferences("fb_login", MODE_PRIVATE);
            String email = guestInfo.getString("EMAIL", "");
            String name = guestInfo.getString("NAME", "");
            String gender = guestInfo.getString("GENDER", "");
            nameEt.setText(name);
            emailEt.setText(email);
            nameEt.setFocusable(false);
            nameEt.setClickable(false);
            emailEt.setFocusable(false);
            emailEt.setClickable(false);

            if(gender.equals("male"))
            {
                rb_male.setChecked(true);
                rb_female.setChecked(false);

            }
            else if(gender.equals("female"))
            {
                rb_female.setChecked(true);
                rb_male.setChecked(false);
            }
        }
        else if(loginFlag.equals("2"))
        {
            SharedPreferences guestInfo = getSharedPreferences("gg_login", MODE_PRIVATE);
            String name = guestInfo.getString("GNAME", "");
            String email = guestInfo.getString("GMAIL", "");
            nameEt.setText(name);
            emailEt.setText(email);
            emailEt.setFocusable(false);
            emailEt.setClickable(false);
        }
        else if(loginFlag.equals("3"))
        {
            SharedPreferences guestInfo = getSharedPreferences("ka_login", MODE_PRIVATE);
            String name = guestInfo.getString("KNAME", "");
            String email = guestInfo.getString("KEMAIL", "");
            nameEt.setText(name);
            nameEt.setFocusable(false);
            nameEt.setClickable(false);
            emailEt.setText(email);
        }
        else
        {
            nameEt.setFocusable(true);
            nameEt.setClickable(true);
            emailEt.setFocusable(true);
            emailEt.setClickable(true);
            SharedPreferences guestInfo = getSharedPreferences("common_login", MODE_PRIVATE);
            String name = guestInfo.getString("CNAME", "");
            String email = guestInfo.getString("CEMAIL", "");
            nameEt.setText(name);
            emailEt.setText(email);
        }

        backBtn = (ImageView)findViewById(R.id.menu_back);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num20_Main.this, num21_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(loginFlag.equals("2")){
                    SharedPreferences pref = getSharedPreferences("gg_login", MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("GNAME", nameEt.getText().toString());
                    edit.commit();
            }

            else if(loginFlag.equals("3")){
                SharedPreferences pref = getSharedPreferences("ka_login", MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("KEMAIL", emailEt.getText().toString());
                edit.commit();
            }

            else if(loginFlag.equals("4")) {
                SharedPreferences pref = getSharedPreferences("common_login", MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("CNAME", nameEt.getText().toString());
                edit.putString("CEMAIL", emailEt.getText().toString());
                edit.commit();
            }
                SharedPreferences saveInfo = getSharedPreferences("loginFlag", MODE_PRIVATE);
                SharedPreferences.Editor editor = saveInfo.edit();
                editor.putString("FLAG", loginFlag);
                Log.d("dfdf", loginFlag);
                editor.commit();
                Intent intent = new Intent(num20_Main.this, num02_Schedule.class);
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