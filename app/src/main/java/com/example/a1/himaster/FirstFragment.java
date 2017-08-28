package com.example.a1.himaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1.himaster.Adapter.EventAdapter;
import com.example.a1.himaster.Adapter.NoticeAdapter;
import com.example.a1.himaster.Adapter.TodoAdapter;
import com.example.a1.himaster.Model.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class FirstFragment extends Fragment {
    public static FirstFragment newInstance() {
        return new FirstFragment();
    }
    String url = "";
    private static final String TAG_RESULTS="schedules";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DATE = "startDate";
    private static final String TAG_TIME = "startTime";
    private static final String TAG_DEST = "destination";
    JSONArray posts = null;
    JSONArray ePosts = null;
    JSONArray tPosts = null;
    ArrayList<HashMap<String,String>> scheduleList, eventList, todoList;
    //UI 관련
    private RecyclerView rv1, rv2, rv3;
    private LinearLayoutManager mLinearLayoutManager1, mLinearLayoutManager2, mLinearLayoutManager3;
    TextView tv1, tv2, tv3, dateTv;
    ImageView optMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.firstfragment, container, false);
        SharedPreferences saveInfo = this.getActivity().getSharedPreferences("loginFlag", MODE_PRIVATE);
        final String userId = saveInfo.getString("USERID", "");   //  userId 가져옴

        url = "http://192.168.0.12:8080/home?userid="+userId+"&date=2017-08-16 20:20:20";
        scheduleList = new ArrayList<HashMap<String, String>>();
        eventList = new ArrayList<HashMap<String, String>>();
        todoList = new ArrayList<HashMap<String, String>>();

        dateTv = (TextView)view.findViewById(R.id.dateTv);

        mLinearLayoutManager1 = new LinearLayoutManager(getActivity());
        mLinearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rv1 = (RecyclerView) view.findViewById(R.id.scheduleRv);
        rv1.setHasFixedSize(true);
        rv1.setLayoutManager(mLinearLayoutManager1);

        mLinearLayoutManager2 = new LinearLayoutManager(getActivity());
        mLinearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        rv2 = (RecyclerView) view.findViewById(R.id.eventRv);
        rv2.setHasFixedSize(true);
        rv2.setLayoutManager(mLinearLayoutManager2);

        mLinearLayoutManager3 = new LinearLayoutManager(getActivity());
        mLinearLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        rv3 = (RecyclerView) view.findViewById(R.id.todoRv);
        rv3.setHasFixedSize(true);
        rv3.setLayoutManager(mLinearLayoutManager3);

        tv1 = (TextView)view.findViewById(R.id.tv_title);
        tv2 = (TextView)view.findViewById(R.id.tv_date);
        tv3 = (TextView)view.findViewById(R.id.tv_dest);

        optMenu = (ImageView)view.findViewById(R.id.optMenuBtn);
        optMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyInfo.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                //finish();
            }

        });

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat toDate = new SimpleDateFormat("MMM . dd / EEE", Locale.ENGLISH);
        String todayDate = toDate.format(date);
        dateTv.setText(todayDate);

        getData(url);


        return view;


    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params) {
                //JSON 받아온다.
                String uri = params[0];
                BufferedReader br = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    StringBuilder sb = new StringBuilder();

                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String json;
                    while((json = br.readLine()) != null) {
                        sb.append(json+"\n");
                    }
                    Log.d("ssssss", String.valueOf(sb));
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                Log.d("my", myJSON);
                makeList(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }


    public void makeList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            posts = jsonObj.getJSONArray("schedules");
            for(int i=0; i<posts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = posts.getJSONObject(i);
                String title = c.getString(TAG_TITLE);
                String date = c.getString(TAG_DATE);
                String startTime = c.getString(TAG_TIME);
                String pattern = "yyyy년 M월 d일";

                SimpleDateFormat formatter = new SimpleDateFormat(pattern);
                Date cDate = Date.valueOf(date);
                String conDate = (String)formatter.format(cDate);

                String dest = c.getString(TAG_DEST);

                if(date.length() > 50 ) {
                    date = date.substring(0,50) + "..."; //50자 자르고 ... 붙이기
                }
                if(title.length() > 16 ) {
                    title = title.substring(0,16) + "..."; //18자 자르고 ... 붙이기
                }
                Log.d("title", title);
                Log.d("destination", dest);
                //HashMap에 붙이기
                HashMap<String,String> posts = new HashMap<String, String>();
                posts.put(TAG_TITLE,title);
                posts.put(TAG_DATE,conDate);
                posts.put(TAG_TIME,startTime);
                posts.put(TAG_DEST,dest);

                //ArrayList에 HashMap 붙이기
                scheduleList.add(posts);
            }
            //카드 리스트뷰 어댑터에 연결
            NoticeAdapter adapter = new NoticeAdapter(getActivity(), scheduleList, getActivity());

            rv1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            ///

            ePosts = jsonObj.getJSONArray("events");
            for(int i=0; i<ePosts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = ePosts.getJSONObject(i);
                String title = c.getString(TAG_TITLE);
                String date = c.getString(TAG_DATE);
                /*
                String pattern = "M월 d일";
                long mills = Long.parseLong(date);
                SimpleDateFormat formatter = new SimpleDateFormat(pattern);
                String conDate = (String)formatter.format(new Long(mills));
                */
                String pattern = "M월 d일";
                SimpleDateFormat formatter = new SimpleDateFormat(pattern);
                Date cDate = Date.valueOf(date);
                String conDate = (String)formatter.format(cDate);
                String dest = c.getString(TAG_DEST);

                if(date.length() > 50 ) {
                    date = date.substring(0,50) + "..."; //50자 자르고 ... 붙이기
                }
                if(title.length() > 16 ) {
                    title = title.substring(0,16) + "..."; //18자 자르고 ... 붙이기
                }
                Log.d("title", title);
                Log.d("destination", dest);
                //HashMap에 붙이기
                HashMap<String,String> ePosts = new HashMap<String, String>();
                ePosts.put(TAG_TITLE,title);
                ePosts.put(TAG_DATE,conDate);
                ePosts.put(TAG_DEST,dest);

                //ArrayList에 HashMap 붙이기
                eventList.add(ePosts);
            }
            //카드 리스트뷰 어댑터에 연결
            EventAdapter adapter2 = new EventAdapter(getActivity(), eventList);

            rv2.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();

            ///

            tPosts = jsonObj.getJSONArray("todos");
            for(int i=0; i<tPosts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = tPosts.getJSONObject(i);
                String title = c.getString(TAG_TITLE);

                if(title.length() > 16 ) {
                    title = title.substring(0,16) + "..."; //18자 자르고 ... 붙이기
                }
                Log.d("title", title);
                //HashMap에 붙이기
                HashMap<String,String> tPosts = new HashMap<String, String>();
                tPosts.put(TAG_TITLE,title);

                //ArrayList에 HashMap 붙이기
                todoList.add(tPosts);
            }
            //카드 리스트뷰 어댑터에 연결
            TodoAdapter adapter3 = new TodoAdapter(getActivity(), todoList);

            rv3.setAdapter(adapter3);
            adapter3.notifyDataSetChanged();

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }
}