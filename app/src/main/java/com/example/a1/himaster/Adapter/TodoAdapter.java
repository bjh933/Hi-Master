package com.example.a1.himaster.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a1.himaster.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by a1 on 2017. 8. 10..
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> todoList; //일정 정보 담겨있음

    public TodoAdapter(Context context, ArrayList<HashMap<String, String>> todoList) {
        this.context = context;
        this.todoList = todoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo, null);
        return new ViewHolder(v);
    }

   //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    /** 정보 및 이벤트 처리는 이 메소드에서 구현 **/
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> noticeItem = todoList.get(position);
        holder.tv_title.setText(noticeItem.get("title")); //제목
        holder.tv_dueDate.setText(noticeItem.get("conDate")); //제목

    }

    @Override
    public int getItemCount() {
        return this.todoList.size();
    }

    /**
     * item layout 불러오기
     **/
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_dueDate;

        public ViewHolder(View v) {
            super(v);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_dueDate = (TextView) v.findViewById(R.id.tv_dueDate);
        }
    }
}