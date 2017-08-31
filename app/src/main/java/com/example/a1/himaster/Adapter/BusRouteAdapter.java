package com.example.a1.himaster.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1.himaster.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by a1 on 2017. 8. 10..
 */

public class BusRouteAdapter extends RecyclerView.Adapter<BusRouteAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> busRouteList; //일정 정보 담겨있음

    public BusRouteAdapter(Context context, ArrayList<HashMap<String, String>> busRouteList) {
        this.context = context;
        this.busRouteList = busRouteList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.busrouteview, null);
        return new ViewHolder(v);
    }

   //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    /** 정보 및 이벤트 처리는 이 메소드에서 구현 **/
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> busListItem = busRouteList.get(position);
        holder.tv_busNum.setText(busListItem.get("busNum"));
        holder.tv_start.setText(busListItem.get("start"));
        holder.tv_end.setText(busListItem.get("end"));

    }

    @Override
    public int getItemCount() {
        return this.busRouteList.size();
    }

    /**
     * item layout 불러오기
     **/
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_busNum;
        TextView tv_start;
        TextView tv_end;
        ImageView forward;
        CardView cv;

        public ViewHolder(View v) {
            super(v);
            tv_busNum = (TextView) v.findViewById(R.id.tv_busNum);
            tv_start = (TextView) v.findViewById(R.id.tv_start);
            tv_end = (TextView) v.findViewById(R.id.tv_end);
            forward = (ImageView) v.findViewById(R.id.forward);
            //cv = (CardView) v.findViewById(R.id.cv);
        }


    }
}