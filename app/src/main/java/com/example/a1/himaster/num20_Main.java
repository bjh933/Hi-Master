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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.LayoutInflater.from;

public class num20_Main extends AppCompatActivity {

    ArrayAdapter<CharSequence> adapter;
    EditText addressEt, nameEt, emailEt;
    Button okBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num20);
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

            nameEt.setText(name);
            emailEt.setText(email);

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
                    Intent intent = new Intent(num20_Main.this, Popup_emailchk.class);
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
                    Intent intent = new Intent(num20_Main.this, MyInfo.class);
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
        Intent intent = new Intent(num20_Main.this, MyInfo.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
    }
}