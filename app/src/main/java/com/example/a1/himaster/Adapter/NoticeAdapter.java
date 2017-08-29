package com.example.a1.himaster.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1.himaster.DetailSchedule;
import com.example.a1.himaster.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by a1 on 2017. 8. 10..
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    Context context;
    Activity mActivity;
    ArrayList<HashMap<String, String>> scheduleList; //일정 정보 담겨있음
    String dTitle, dDate, dDest, dTime;
    ImageView delBtn, rewriteBtn;
    String urlDel = "http://192.168.0.12:8080/deleteschedule";
    String urlPost = "http://192.168.0.12:8080/postschedule";

    String userId = "";
    String title = "";
    String date = "";

    //public static final int REQUEST_CODE = 1101;
    public NoticeAdapter(Context context, ArrayList<HashMap<String, String>> scheduleList, Activity mActivity) {
        this.context = context;
        this.scheduleList = scheduleList;
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_custom, null);
        return new ViewHolder(v);
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    /**
     * 정보 및 이벤트 처리는 이 메소드에서 구현
     **/
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> noticeItem = scheduleList.get(position);
        holder.tv_title.setText(noticeItem.get("title")); //제목
        holder.tv_date.setText(noticeItem.get("startDate")); //작성일
        holder.tv_time.setText(noticeItem.get("startTime"));
        holder.tv_dest.setText(noticeItem.get("destination"));

        userId = noticeItem.get("userId");
        title = noticeItem.get("title");
        date = noticeItem.get("startDate");

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return this.scheduleList.size();
    }

    /**
     * item layout 불러오기
     **/
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_title;
        TextView tv_date;
        TextView tv_time;
        TextView tv_dest;
        CardView cv;
        ImageView delBtn, rewriteBtn;

        public ViewHolder(View v) {
            super(v);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            tv_dest = (TextView) v.findViewById(R.id.tv_dest);
            cv = (CardView) v.findViewById(R.id.cv);
            delBtn = (ImageView) v.findViewById(R.id.delScheBtn);
            rewriteBtn = (ImageView) v.findViewById(R.id.rewriteScheBtn);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), DetailSchedule.class);
            dTitle = tv_title.getText().toString();
            dDate = tv_date.getText().toString();
            dTime = tv_time.getText().toString();
            dDest = tv_dest.getText().toString();
            intent.putExtra("DTITLE", dTitle);
            intent.putExtra("DDATE", dDate);
            intent.putExtra("DTIME", dTime);
            intent.putExtra("DDEST", dDest);
            v.getContext().startActivity(intent);
            mActivity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        }


    }

    class PostDataJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            //JSON 받아온다.
            String uri = params[0];
            String str = params[1];
            Log.d("url", uri);

            BufferedWriter bw = null;
            try {
                JSONObject jsonO = null;
                jsonO = new JSONObject(str);
                URL url = new URL(uri);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);  // Enable writing
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-type", "application/json");
                con.setConnectTimeout(5000);
                Log.d("sendContents", str);
                byte[] outputInBytes = params[1].getBytes("UTF-8");
                OutputStream os = con.getOutputStream();
                os.write( outputInBytes );
                os.flush();
                os.close();

                //--------------------------
                //   서버에서 전송받기
                //--------------------------
                InputStreamReader tmp = new InputStreamReader(con.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str1;
                while ((str1 = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                    builder.append(str1 + "\n");                     // View에 표시하기 위해 라인 구분자 추가
                }
                String result = builder.toString();                       // 전송결과를 전역 변수에 저장
                Log.d("receiveString", result);

                Log.d("sendResult", "success");
                return null;
            } catch (Exception e) {
                Log.d("sendResult", "fail");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String myJSON) {
            //Log.d("my", myJSON);
        }

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