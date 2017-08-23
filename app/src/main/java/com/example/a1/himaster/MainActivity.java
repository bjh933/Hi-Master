package com.example.a1.himaster;

import android.*;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.a1.himaster.kakaoLogin.Kakao_LoginActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    Button skip;
    Button CustomloginButton;
    Button KakaoBtn;
    Button googleBtn;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 2;

    private GoogleApiClient mGoogleApiClient;

    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final int REQUEST_CODE_SIGN_IN = 1111;
    private boolean mResolvingError = false;

    private static final String STATE_RESOLVING_ERROR = "resolving_error";

   // private SignInButton signInButton;
    //private TextView printData;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }

    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext()); // SDK 초기화 (setContentView 보다 먼저 실행되어야합니다. 안그럼 에러납니다.)
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();  //로그인 응답을 처리할 콜백 관리자
        CustomloginButton = (Button)findViewById(R.id.facebookBtn);
        skip = (Button)findViewById(R.id.skipBtn);
        googleBtn = (Button)findViewById(R.id.googleBtn) ;
        checkPermission1();

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InfoToBottom.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });


        CustomloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,
                        Arrays.asList("public_profile", "email"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Log.e("onSuccess", "onSuccess");
                                GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        try{
                                            String email = object.getString("email");
                                            String name = object.getString("name");
                                            String gender = object.getString("gender");

                                            SharedPreferences pref = getSharedPreferences("loginFlag", MODE_PRIVATE);
                                            SharedPreferences.Editor edit = pref.edit();
                                            edit.putString("FLAG", "1");
                                            edit.commit();

                                            SharedPreferences fb_login = getSharedPreferences("fb_login", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = fb_login.edit();
                                            editor.putString("EMAIL", email);
                                            editor.putString("NAME", name);
                                            editor.putString("GENDER", gender);
                                            editor.commit();

                                            Intent intent = new Intent(MainActivity.this, BottombarActivity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                                            finish();
                                        }
                                        catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,name,email,gender, birthday");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel() {
                                Log.e("onCancel", "onCancel");
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                Log.e("onError", "onError " + exception.getLocalizedMessage());
                            }
                        });
            }
        });


        KakaoBtn = (Button)findViewById(R.id.kakaoBtn);

        KakaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Kakao_LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.server_client_id))
                //.requestServerAuthCode(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, connectionFailedListener /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiClient.isConnected())
                {
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                }
                if (!mResolvingError) {
                    signIn();
                }
            }
        });


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        // Very important to start intent from Activity, otherwise requestCode top bit gets set
        // and messes it up
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else    //  페이스북 콜백
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String gemail = acct.getEmail() == null ? "" : acct.getEmail();
            String gname = acct.getDisplayName() == null ? "" : acct.getDisplayName();
            String gid = acct.getId() == null ? "" : acct.getId();
            String gidToken = acct.getIdToken() == null ? "" : acct.getIdToken();                       // Android support this via OAuth2.0 but have failed to retrieve
            String gaccessToken = acct.getServerAuthCode() == null ? "" : acct.getServerAuthCode();     // Android support this via OAuth2.0 but have failed to retrieve

            SharedPreferences pref = getSharedPreferences("loginFlag", MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("FLAG", "2");
            edit.commit();

            SharedPreferences gg_login = getSharedPreferences("gg_login", MODE_PRIVATE);
            SharedPreferences.Editor editor = gg_login.edit();
            editor.putString("GMAIL", gemail);
            //editor.putString("NAME", gname);
            editor.commit();

            Intent intent = new Intent(MainActivity.this, BottombarActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            finish();
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);

        }
    }

    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            if (mResolvingError) {
                return;
            } else if (result.hasResolution()) {
                try {
                    mResolvingError = true;
                    result.startResolutionForResult(MainActivity.this, REQUEST_RESOLVE_ERROR);
                } catch (IntentSender.SendIntentException e) {
                    mGoogleApiClient.connect();
                }
            } else {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                mResolvingError = true;
            }
        }
    };

    /////구글 로그인 관련 함수/////

    public void checkPermission1() {

        int pCheck1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (pCheck1 == PackageManager.PERMISSION_DENIED) {
            //권한 추가
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);

        }

        int pCheck2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (pCheck2 == PackageManager.PERMISSION_DENIED) {
            //권한 추가
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            Log.d("ssss","success");

                        } else {
                            Log.d("ssss","fail");
                        }
                    }
                }
                break;

            case REQUEST_ACCESS_FINE_LOCATION:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            Log.d("ssss","success2");
                        } else {
                            Log.d("ssss","fail2");
                        }
                    }
                }
                break;
        }
    }
}