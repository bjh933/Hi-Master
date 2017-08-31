package com.example.a1.himaster.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.a1.himaster.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by a1 on 2017. 8. 10..
 */

public class TodoDelayAdapter extends RecyclerView.Adapter<TodoDelayAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> todoList; //일정 정보 담겨있음
    String dUser, dTitle, dDate, dMemo, dRepeat;
    Handler handler;
    public TodoDelayAdapter(Context context, ArrayList<HashMap<String, String>> todoList, Handler handler) {
        this.context = context;
        this.todoList = todoList;
        this.handler = handler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delaytodo, null);
        return new ViewHolder(v);
    }

   //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    /** 정보 및 이벤트 처리는 이 메소드에서 구현 **/
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final HashMap<String, String> noticeItem = todoList.get(position);
        holder.tv_dTitle.setText(noticeItem.get("title")); //제목;

    }

    @Override
    public int getItemCount() {
        return this.todoList.size();
    }

    /**
     * item layout 불러오기
     **/
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_dTitle;
        //CardView cv;
        TextView dday;
        int chkFlag = 0;

        public ViewHolder(View v) {
            super(v);
            tv_dTitle = (TextView) v.findViewById(R.id.tv_dtitle);
            //cv = (CardView) v.findViewById(R.id.cv);
            dday = (TextView) v.findViewById(R.id.dday);
        }

        @Override
        public void onClick(View v) {

        }

    }
}