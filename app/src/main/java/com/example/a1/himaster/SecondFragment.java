package com.example.a1.himaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1.himaster.Adapter.TodoDelayAdapter;
import com.example.a1.himaster.SKPlanet.WeatherThreeDayThread;

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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class SecondFragment extends Fragment {

    public static SecondFragment newInstance() {
        return new SecondFragment();
    }

    ImageView rightBtn, weatherIv, optionBtn;
    TextView tomorrowDateTv, cityTv, tempTv, skyTv, todayTv, tMaxTv, tMinTv;
    private RecyclerView rv1;
    ArrayList<HashMap<String,String>> todoList;
    private LinearLayoutManager mLinearLayoutManager;
    JSONArray tPosts = null;
    String url = "";
    private static final String TAG_TITLE = "title";
    Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.secondfragment, container, false);

        Context mContext = getActivity();

        weatherIv = (ImageView)view.findViewById(R.id.weatherIv);
        optionBtn = (ImageView)view.findViewById(R.id.optMenuBtn);
        rv1 = (RecyclerView)view.findViewById(R.id.todoRv);
        tMaxTv = (TextView)view.findViewById(R.id.tMaxTv);
        tMinTv = (TextView)view.findViewById(R.id.tMinTv);
        todayTv = (TextView)view.findViewById(R.id.todayTv);
        tomorrowDateTv = (TextView)view.findViewById(R.id.tomorrowTv);
        cityTv = (TextView)view.findViewById(R.id.cityTv);
        tempTv = (TextView)view.findViewById(R.id.tempTv);
        skyTv = (TextView)view.findViewById(R.id.skyTv);

        SharedPreferences saveInfo = this.getActivity().getSharedPreferences("loginFlag", MODE_PRIVATE);
        final String userId = saveInfo.getString("USERID", "");   //  userId 가져옴

        //url = "http://192.168.0.12:8080/home?userid="+userId+"&date=2017-08-16 20:20:20";
        url = "http://192.168.21.129:8080/home?userid="+userId+"&date=2017-08-16 20:20:20";

        todoList = new ArrayList<HashMap<String, String>>();

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv1 = (RecyclerView) view.findViewById(R.id.todoRv);
        rv1.setHasFixedSize(true);
        rv1.setLayoutManager(mLinearLayoutManager);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat toDate = new SimpleDateFormat("MMM . dd / EEE", Locale.ENGLISH);
        String todayDate = toDate.format(date);
        todayTv.setText(todayDate);

        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        String tmoDate = toDate.format(date);
        tomorrowDateTv.setText(tmoDate);

        Handler handler = new Handler(){
            public void handleMessage(Message msg)
            {
                String str = msg.getData().getString("weather");    /// 번들에 들어있는 값 꺼냄
                Log.d("weather3days", str);
                String[] weatherStr = str.split("-");
                skyTv.setText(weatherStr[0]);
                setSky(weatherStr[0], weatherIv);
                String tMax = weatherStr[1].replace(".00", "");
                String tMin = weatherStr[2].replace(".00", "");
                tMaxTv.setText(tMax);
                tMaxTv.append(" ℃");
                tMinTv.setText(tMin);
                tMinTv.append(" ℃");
            }};

        WeatherThreeDayThread wt = new WeatherThreeDayThread(handler, mContext, 37.5714000000, 126.9658000000);
        wt.run();   //  내일 날씨

        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyInfo.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        getData(url);

        return view;
    }

    public void setSky(String skyStatus, ImageView iv) {

        if(skyStatus.equals("맑음"))
            iv.setImageResource(R.drawable.sunny);
        else if(skyStatus.equals("구름조금") || skyStatus.equals("구름많음") || skyStatus.equals("흐림"))
            iv.setImageResource(R.drawable.cloudy);
        else if(skyStatus.equals("구름많고 비") || skyStatus.equals("흐리고 비"))
            iv.setImageResource(R.drawable.rainy);
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
            String userId = jsonObj.getString("userId");    //  userId 추가

            tPosts = jsonObj.getJSONArray("todos");
            for(int i=0; i<tPosts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = tPosts.getJSONObject(i);
                String title = c.getString(TAG_TITLE);
                String dueDate = c.getString("dueDate");
                String memo = c.getString("memo");

                if(title.length() > 16 ) {
                    title = title.substring(0,16) + "..."; //18자 자르고 ... 붙이기
                }
                Log.d("title", title);
                //HashMap에 붙이기
                HashMap<String,String> tPosts = new HashMap<String, String>();
                tPosts.put(TAG_TITLE, title);
                tPosts.put("dueDate", dueDate);
                tPosts.put("userId", userId);
                tPosts.put("memo", memo);

                //ArrayList에 HashMap 붙이기
                todoList.add(tPosts);
            }

            //카드 리스트뷰 어댑터에 연결
            TodoDelayAdapter adapter = new TodoDelayAdapter(getActivity(), todoList, handler);

            rv1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }


}