package com.example.a1.himaster.PopUp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.a1.himaster.R;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by a1 on 2017. 7. 29..
 */

public class Popup_deletechk extends Activity {

    Button okBtn, cancelBtn;
    TextView tvPop;
    String delFlag = "";
    String str = "";
    String urlsche = "http://192.168.0.12:8080/deleteschedule";
    String urltodo = "http://192.168.0.12:8080/deletetodo";
    String urlevent = "http://192.168.0.12:8080/deleteevent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_delete);
        this.setFinishOnTouchOutside(false);
        tvPop = (TextView)findViewById(R.id.popText);
        okBtn = (Button)findViewById(R.id.okBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        Intent data = getIntent();
        str = data.getExtras().getString("delStr");
        delFlag = data.getExtras().getString("delFlag");

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDataJSON g = new DeleteDataJSON();
                if(delFlag.equals("1")) {
                    g.execute(urlsche, str);
                }
                else if(delFlag.equals("2"))
                {
                    g.execute(urltodo, str);
                }

                else if(delFlag.equals("3"))
                {
                    g.execute(urlevent, str);
                }
                finish();
            }

        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });



    }

    class DeleteDataJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            //JSON 받아온다.
            String uri = params[0];
            String str = params[1];
            Log.d("url", uri);

            try {
                URL url = new URL(uri);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);  // Enable writing
                con.setRequestMethod("DELETE");
                con.setRequestProperty("Content-type", "application/json");
                con.setConnectTimeout(5000);
                Log.d("DeleteContents", str);

                byte[] outputInBytes = params[1].getBytes("UTF-8");
                OutputStream os = con.getOutputStream();
                os.write( outputInBytes );
                os.flush();
                os.close();

                con.connect();

                String response = String.valueOf(con.getResponseCode());
                Log.d("DeleteCode", response);
                Log.d("DeleteResult", "success");
                return null;
            } catch (Exception e) {
                Log.d("DeleteResult", "fail");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String myJSON) {
            //Log.d("my", myJSON);
        }

    }
}