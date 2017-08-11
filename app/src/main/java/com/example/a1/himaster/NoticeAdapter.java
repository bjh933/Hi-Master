package com.example.a1.himaster;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by a1 on 2017. 8. 10..
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> scheduleList; //일정 정보 담겨있음

    public NoticeAdapter(Context context, ArrayList<HashMap<String, String>> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_custom, null);
        return new ViewHolder(v);
    }

   //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    /** 정보 및 이벤트 처리는 이 메소드에서 구현 **/
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> noticeItem = scheduleList.get(position);
        holder.tv_title.setText(noticeItem.get("title")); //제목
        holder.tv_date.setText(noticeItem.get("dueDate")); //작성일
        holder.tv_dest.setText(noticeItem.get("destination"));

    }

    @Override
    public int getItemCount() {
        return this.scheduleList.size();
    }

    /**
     * item layout 불러오기
     **/
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_date;
        TextView tv_dest;
        CardView cv;

        public ViewHolder(View v) {
            super(v);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_dest = (TextView) v.findViewById(R.id.tv_dest);
            cv = (CardView) v.findViewById(R.id.cv);
        }
    }
}