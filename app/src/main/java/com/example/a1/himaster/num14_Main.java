package com.example.a1.himaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import static android.view.LayoutInflater.from;

public class num14_Main extends AppCompatActivity {

    ImageView backBtn;
    Button myInfo;
    Button appConfig;
    Button help;
    Button logout;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num14);

        setCustomActionbar();
        backBtn = (ImageView)findViewById(R.id.menu_back);
        myInfo = (Button)findViewById(R.id.myInfoBtn);
        appConfig = (Button)findViewById(R.id.appConfigBtn);
        help = (Button)findViewById(R.id.helpBtn);
        logout = (Button)findViewById(R.id.logoutBtn);

        SharedPreferences pref = getSharedPreferences("loginFlag", MODE_PRIVATE);
        String loginFlag = pref.getString("FLAG", "4");
        if(loginFlag.equals("4") || loginFlag.equals(""))
        {
            logout.setText("로그인");
        }
        else
            logout.setText("로그아웃");

        if(logout.getText().toString().equals("로그인"))
        {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(num14_Main.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                    finish();
                }
            });

        }
        else
        {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences pref = getSharedPreferences("loginFlag", MODE_PRIVATE);
                    String loginFlag = pref.getString("FLAG", "4");

                    if(loginFlag.equals("1"))
                        LoginManager.getInstance().logOut();    //  페이스북 로그아웃
                    else if(loginFlag.equals("2"))  //  구글 로그아웃
                    {

                    }
                    else if(loginFlag.equals("3"))  //  카톡 로그아웃
                    {
                        onClickLogout();
                    }

                    SharedPreferences logoutFlag = getSharedPreferences("loginFlag", MODE_PRIVATE);
                    SharedPreferences.Editor edit = logoutFlag.edit();
                    edit.putString("FLAG", "");
                    edit.commit();

                    Intent intent = new Intent(num14_Main.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                    finish();
                }

            });

        }


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num14_Main.this, num15_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num14_Main.this, num20_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        appConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num14_Main.this, num15_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num14_Main.this, num16_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

    }

    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {

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
