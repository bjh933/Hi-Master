package com.example.a1.himaster;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a1.himaster.Adapter.NoticeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class FirstFragment extends Fragment {
    public static FirstFragment newInstance() {
        return new FirstFragment();
    }
    String url = "http://192.168.0.12:8080/home?userId=abc&date=2017-08-09 20:20:20";
    private static final String TAG_RESULTS="schedules";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DATE = "dueDate";
    private static final String TAG_DEST = "destination";
    JSONArray posts = null;
    ArrayList<HashMap<String,String>> scheduleList;
    //UI 관련
    private RecyclerView rv;
    private LinearLayoutManager mLinearLayoutManager;
    TextView tv1, tv2, tv3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.secondfragment, container, false);

        scheduleList = new ArrayList<HashMap<String, String>>();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView) view.findViewById(R.id.scheduleRv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);
        tv1 = (TextView)view.findViewById(R.id.tv_title);
        tv2 = (TextView)view.findViewById(R.id.tv_date);
        tv3 = (TextView)view.findViewById(R.id.tv_dest);



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
                    Log.d("ssss", String.valueOf(sb));
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                //Log.d("my", myJSON);
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
                String pattern = "yyyy-MM-dd HH:mm";
                long mills = Long.parseLong(date);
                SimpleDateFormat formatter = new SimpleDateFormat(pattern);
                String conDate = (String)formatter.format(new Long(mills));

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
                posts.put(TAG_DEST,dest);

                //ArrayList에 HashMap 붙이기
                scheduleList.add(posts);
            }
            //카드 리스트뷰 어댑터에 연결
            NoticeAdapter adapter = new NoticeAdapter(getActivity(), scheduleList);

            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }
}