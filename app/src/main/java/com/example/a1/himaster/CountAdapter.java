package com.example.a1.himaster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class CountAdapter extends RecyclerView.Adapter<CountAdapter.MyViewHolder> {
    Context mContext;
    List<String> items;
    String text;

    public CountAdapter(Context c, List<String> items, String text){
        this.mContext = c;
        this.items = items;
        this.text = text;
    }

    @Override
    public CountAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_custom, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CountAdapter.MyViewHolder myViewHolder, int position){
        final String item = items.get(position);
        myViewHolder.tv.setText(""+item);

        myViewHolder.tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Toast.makeText(mContext, item, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return this.items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }

}



