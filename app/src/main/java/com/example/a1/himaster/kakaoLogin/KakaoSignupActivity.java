package com.example.a1.himaster.kakaoLogin;

/**
 * Created by a1 on 2017. 7. 28..
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.a1.himaster.R;
import com.example.a1.himaster.num02_Schedule;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import com.kakao.util.helper.log.Logger;

public class KakaoSignupActivity extends Activity{
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환

                String kname = userProfile.getNickname();
               // String kemail = userProfile.getEmail();
                long kid = userProfile.getId();

                SharedPreferences pref = getSharedPreferences("loginFlag", MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("FLAG", "3");
                edit.commit();

                SharedPreferences ka_login = getSharedPreferences("ka_login", MODE_PRIVATE);
                SharedPreferences.Editor editor = ka_login.edit();
                //editor.putString("EMAIL", kemail);
                editor.putString("KNAME", kname);
                editor.commit();
               // Log.d("UserProfile", userProfile.getNickname());
               // Log.d("UserProfile", userProfile.getEmail());
               // Log.d("myLog", "userProfile " + userProfile.getId());
               // Log.d("myLog",  userProfile.getThumbnailImagePath());
               redirectMainActivity(); // 로그인 성공
            }
        });
    }

    private void redirectMainActivity() {

        startActivity(new Intent(this, num02_Schedule.class));
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, Kakao_LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}