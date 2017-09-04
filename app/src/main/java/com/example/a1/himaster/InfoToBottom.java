package com.example.a1.himaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoToBottom extends AppCompatActivity {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    EditText addressEt, nameEt, emailEt;
    Button okBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoinit);
        okBtn = (Button)findViewById(R.id.okButton);

        nameEt = (EditText)findViewById(R.id.nameEdit);
        emailEt = (EditText)findViewById(R.id.emailEdit);
        addressEt = (EditText)findViewById(R.id.addressEdit);
        final SharedPreferences pref = getSharedPreferences("loginFlag", MODE_PRIVATE);
        final String loginFlag = pref.getString("FLAG", "4");
        String email = "";

        if(loginFlag.equals("1"))
        {
            SharedPreferences guestInfo = getSharedPreferences("fb_login", MODE_PRIVATE);
            email = guestInfo.getString("EMAIL", "");
            String name = guestInfo.getString("NAME", "");
            String gender = guestInfo.getString("GENDER", "");
            nameEt.setText(name);
            emailEt.setText(email);
            nameEt.setFocusable(false);
            nameEt.setClickable(false);
            emailEt.setFocusable(false);
            emailEt.setClickable(false);


        }
        else if(loginFlag.equals("2"))
        {
            SharedPreferences guestInfo = getSharedPreferences("gg_login", MODE_PRIVATE);
            String name = guestInfo.getString("GNAME", "");
            email = guestInfo.getString("GMAIL", "");
            nameEt.setText(name);
            emailEt.setText(email);
            emailEt.setFocusable(false);
            emailEt.setClickable(false);
        }
        else if(loginFlag.equals("3"))
        {
            SharedPreferences guestInfo = getSharedPreferences("ka_login", MODE_PRIVATE);
            String name = guestInfo.getString("KNAME", "");
            email = guestInfo.getString("KEMAIL", "");
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
            email = guestInfo.getString("CEMAIL", "");
            nameEt.setText(name);
            emailEt.setText(email);
        }


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
                if(emailEt.length() == 0 || checkEmail(emailEt.getText().toString()) != true)
                {
                    Intent intent = new Intent(InfoToBottom.this, Popup_emailchk.class);
                    startActivity(intent);
                    Log.d("emailchk", "wrong email format");
                }
                else {
                    Log.d("emailchk", "right email format");
                    SharedPreferences saveInfo = getSharedPreferences("loginFlag", MODE_PRIVATE);
                    SharedPreferences.Editor editor = saveInfo.edit();
                    editor.putString("FLAG", loginFlag);
                    editor.putString("USERID", emailEt.getText().toString());
                    editor.putString("NAME", nameEt.getText().toString());

                    //Log.d("dfdf", loginFlag);
                    editor.commit();
                    Intent intent = new Intent(InfoToBottom.this, BottombarActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                    finish();
                }
            }

        });
    }

    /**
     * 이메일 포맷 체크
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InfoToBottom.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        finish();
    }
}