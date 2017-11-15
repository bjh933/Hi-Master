package com.example.a1.himaster.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1.himaster.PopUp.Popup_deletechk;
import com.example.a1.himaster.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by a1 on 2017. 8. 10..
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> todoList; //일정 정보 담겨있음
    String str = "";
    String urlDel = "http://192.168.21.213:8080/deletetodo";

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
        final int pos = holder.getAdapterPosition();
        HashMap<String, String> noticeItem = todoList.get(pos);
        holder.tv_title.setText(noticeItem.get("title")); //제목
        holder.tv_dueDate.setText(noticeItem.get("conDate")); //제목

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = holder.getAdapterPosition();
                HashMap<String, String> noticeItem = todoList.get(pos);

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
                intent.putExtra("delFlag", "2");
                intent.putExtra("delStr", str);
                context.startActivity(intent);
            }

        });
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
        ImageView delBtn;

        public ViewHolder(View v) {
            super(v);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_dueDate = (TextView) v.findViewById(R.id.tv_dueDate);
            delBtn = (ImageView) v.findViewById(R.id.todoDelBtn);
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