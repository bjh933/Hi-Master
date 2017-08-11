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

    import com.example.a1.himaster.Model.Schedule;
    import com.example.a1.himaster.Service.ContentService;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;
    import org.w3c.dom.Text;

    import java.io.BufferedInputStream;
    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.sql.Timestamp;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.HashMap;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.GsonConverterFactory;
    import retrofit2.Response;
    import retrofit2.Retrofit;


public class RvFirstFrag extends Fragment {
    public static RvFirstFrag newInstance() {
        return new RvFirstFrag();
    }
    //String url = "http://192.168.0.12:8080/home?userId=abc&date=2017-08-09 20:20:20";
    private static final String TAG_RESULTS="schedules";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DATE = "dueDate";
    JSONArray posts = null;
    ArrayList<HashMap<String,String>> scheduleList;
    //UI 관련
    private RecyclerView rv;
    private LinearLayoutManager mLinearLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.secondfragment, container, false);

        scheduleList = new ArrayList<HashMap<String, String>>();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView) view.findViewById(R.id.scheduleRv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);

        HttpURLConnection urlConnection = null;
        BufferedInputStream buf = null;
        try {
            //[URL 지정과 접속]

            //웹서버 URL 지정
            URL url= new URL("http://192.168.0.12:8080/home?userId=abc&date=2017-08-09 20:20:20/");
            //URL 접속
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d("222", "222");
            urlConnection.setRequestMethod("GET"); // URL 요청에 대한 메소드 설정 : GET.
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            urlConnection.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");
            //[웹문서 소스를 버퍼에 저장]
            //데이터를 버퍼에 기록
            int resCode = urlConnection.getResponseCode();
            String code = Integer.toString(resCode);
            Log.d("ddd",code);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
            Log.d("line:",bufreader.toString());

            String line = null;
            String page = "";

            //버퍼의 웹문서 소스를 줄단위로 읽어(line), Page에 저장함
            while((line = bufreader.readLine())!=null){
                Log.d("line:",line);
                page+=line;
            }

            //읽어들인 JSON포맷의 데이터를 JSON객체로 변환
            JSONObject json = new JSONObject(page);

            //ksk_list 에 해당하는 배열을 할당
            JSONArray jArr = json.getJSONArray("schedules");

            //배열의 크기만큼 반복하면서, name과 address의 값을 추출함
            for (int i=0; i<jArr.length(); i++){

                //i번째 배열 할당
                json = jArr.getJSONObject(i);

                //ksNo,korName의 값을 추출함
                String title = json.getString("title");
                String dueDate = json.getString("dueDate");
                Log.d("title: ", title);



            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ex", "excep");

        }finally{
            //URL 연결 해제
            urlConnection.disconnect();
        }


        //getData(url);
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
            posts = jsonObj.getJSONArray(TAG_RESULTS);
            for(int i=0; i<posts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = posts.getJSONObject(i);
                String title = c.getString(TAG_TITLE);
                String date = c.getString(TAG_DATE);
                if(date.length() > 50 ) {
                    date = date.substring(0,50) + "..."; //50자 자르고 ... 붙이기
                }
                if(title.length() > 16 ) {
                    title = title.substring(0,16) + "..."; //18자 자르고 ... 붙이기
                }

                //HashMap에 붙이기
                HashMap<String,String> posts = new HashMap<String,String>();
                posts.put(TAG_TITLE,title);
                posts.put(TAG_DATE,date);

                //ArrayList에 HashMap 붙이기
                scheduleList.add(posts);
            }
            //카드 리스트뷰 어댑터에 연결
            NoticeAdapter adapter = new NoticeAdapter(getActivity(), scheduleList);
            Log.e("onCreate[scheduleList]", "" + scheduleList.size());
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
