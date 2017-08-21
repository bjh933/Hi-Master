package com.example.a1.himaster.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a1.himaster.DetailSchedule;
import com.example.a1.himaster.R;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by a1 on 2017. 8. 10..
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    Context context;
    Activity mActivity;
    ArrayList<HashMap<String, String>> scheduleList; //일정 정보 담겨있음
    String dTitle, dDate, dDest, dTime;
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

        public ViewHolder(View v) {
            super(v);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            tv_dest = (TextView) v.findViewById(R.id.tv_dest);
            cv = (CardView) v.findViewById(R.id.cv);

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
}