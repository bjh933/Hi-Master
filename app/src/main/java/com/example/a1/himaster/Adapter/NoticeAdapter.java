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
import com.example.a1.himaster.MakeSchedule;
import com.example.a1.himaster.PopUp.Popup_deletechk;
import com.example.a1.himaster.R;
import com.example.a1.himaster.RewriteSchedule;

import org.json.JSONException;
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
    //ImageView delBtn, rewriteBtn;
    String urlDel = "http://192.168.0.12:8080/deleteschedule";
    int delFlag = 0;
    public static final int REQUEST_CODE = 1001;

    String userId = "";
    String title = "";
    String date = "";
    String endDate = "";
    String startTime = "";
    String departure = "";
    String depart_lat = "";
    String depart_lon = "";
    String depart_subway_name = "";
    String depart_subway_lat = "";
    String depart_subway_lon = "";
    String destination = "";
    String destination_lat = "";
    String destination_lon = "";
    String dest_subway_name = "";
    String dest_subway_lat = "";
    String dest_subway_lon = "";
    String memo = "";

    String str = "";

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
        final int pos = holder.getAdapterPosition();
        HashMap<String, String> noticeItem = scheduleList.get(pos);
        holder.tv_title.setText(noticeItem.get("title")); //제목
        holder.tv_date.setText(noticeItem.get("startDate")); //작성일
        holder.tv_time.setText(noticeItem.get("startTime"));
        holder.tv_dest.setText(noticeItem.get("destination"));

        holder.rewriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> noticeItem = scheduleList.get(pos);
                Intent intent = new Intent(v.getContext(), RewriteSchedule.class);
                intent.putExtra("USERID", noticeItem.get("userId"));
                intent.putExtra("TITLE", noticeItem.get("title"));
                intent.putExtra("DATE", noticeItem.get("startDate"));
                intent.putExtra("ENDDATE", noticeItem.get("endDate"));
                intent.putExtra("STARTTIME", noticeItem.get("startTime"));
                intent.putExtra("DEPARTURE", noticeItem.get("departure"));
                intent.putExtra("DEPART_LAT", noticeItem.get("depart_lat"));
                intent.putExtra("DEPART_LON", noticeItem.get("depart_lon"));
                intent.putExtra("DEPART_SUBWAY_NAME", noticeItem.get("depart_subway_name"));
                intent.putExtra("DEPART_SUBWAY_LAT", noticeItem.get("depart_subway_lat"));
                intent.putExtra("DEPART_SUBWAY_LON", noticeItem.get("depart_subway_lon"));
                intent.putExtra("DESTINATION", noticeItem.get("destination"));
                intent.putExtra("DESTINATION_LAT", noticeItem.get("destination_lat"));
                intent.putExtra("DESTINATION_LON", noticeItem.get("destination_lon"));
                intent.putExtra("DEST_SUBWAY_NAME", noticeItem.get("dest_subway_name"));
                intent.putExtra("DEST_SUBWAY_LAT", noticeItem.get("dest_subway_lat"));
                intent.putExtra("DEST_SUBWAY_LON", noticeItem.get("dest_subway_lon"));
                intent.putExtra("MEMO", noticeItem.get("memo"));

                Log.d("relog2", noticeItem.get("title")+" "+noticeItem.get("startDate")+" "+noticeItem.get("endDate")+" "+noticeItem.get("destination"));
                v.getContext().startActivity(intent);
                mActivity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = holder.getAdapterPosition();
                HashMap<String, String> noticeItem = scheduleList.get(pos);

                JSONObject jsonOb = null;
                jsonOb = new JSONObject();
                try {
                    jsonOb.put("userId", noticeItem.get("userId"));
                    jsonOb.put("title", noticeItem.get("title"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                str = jsonOb.toString();

                Log.d("deleteStr", str);
                //DeleteDataJSON g = new DeleteDataJSON();
               // g.execute(urlDel, str);
                Intent intent = new Intent(context, Popup_deletechk.class);
                intent.putExtra("delFlag", "1");
                intent.putExtra("delStr", str);
                context.startActivity(intent);
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
            final int pos = getAdapterPosition();
            HashMap<String, String> noticeItem = scheduleList.get(pos);
            userId = noticeItem.get("userId");
            title = noticeItem.get("title");
            date = noticeItem.get("startDate");
            endDate = noticeItem.get("endDate");
            startTime = noticeItem.get("startTime");
            departure = noticeItem.get("departure");
            depart_lat = noticeItem.get("depart_lat");
            depart_lon = noticeItem.get("depart_lon");
            depart_subway_name = noticeItem.get("depart_subway_name");
            depart_subway_lat = noticeItem.get("depart_subway_lat");
            depart_subway_lon = noticeItem.get("depart_subway_lon");
            destination = noticeItem.get("destination");
            destination_lat = noticeItem.get("destination_lat");
            destination_lon = noticeItem.get("destination_lon");
            dest_subway_name = noticeItem.get("dest_subway_name");
            dest_subway_lat = noticeItem.get("dest_subway_lat");
            dest_subway_lon = noticeItem.get("dest_subway_lon");
            memo = noticeItem.get("memo");

            Intent intent = new Intent(v.getContext(), DetailSchedule.class);
            dTitle = tv_title.getText().toString();
            dDate = tv_date.getText().toString();
            dTime = tv_time.getText().toString();
            dDest = tv_dest.getText().toString();
            Log.d("dee", departure);
            intent.putExtra("DTITLE", dTitle);
            intent.putExtra("DDATE", dDate);
            intent.putExtra("DTIME", dTime);
            intent.putExtra("DDEST", dDest);

            intent.putExtra("DEPARTURE", departure);
            intent.putExtra("DEPART_LAT", depart_lat);
            intent.putExtra("DEPART_LON", depart_lon);
            intent.putExtra("DEPART_SUBWAY_NAME", depart_subway_name);
            intent.putExtra("DEPART_SUBWAY_LAT", depart_subway_lat);
            intent.putExtra("DEPART_SUBWAY_LON", depart_subway_lon);
            intent.putExtra("DESTINATION", destination);
            intent.putExtra("DESTINATION_LAT", destination_lat);
            intent.putExtra("DESTINATION_LON", destination_lon);
            intent.putExtra("DEST_SUBWAY_NAME", dest_subway_name);
            intent.putExtra("DEST_SUBWAY_LAT", dest_subway_lat);
            intent.putExtra("DEST_SUBWAY_LON", dest_subway_lon);
            v.getContext().startActivity(intent);
            mActivity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        }
    }


}