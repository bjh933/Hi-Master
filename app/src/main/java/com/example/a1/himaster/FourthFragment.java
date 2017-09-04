package com.example.a1.himaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;
import com.example.a1.himaster.Adapter.CalListItemAdapter;
import com.example.a1.himaster.Adapter.EventAdapter;
import com.example.a1.himaster.Adapter.NoticeAdapter;
import com.example.a1.himaster.Adapter.TodoAdapter;
import com.example.a1.himaster.Adapter.ListItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class FourthFragment extends Fragment {

    private OneCalendarView calendarView;
    JSONArray posts = null;
    JSONArray ePosts = null;
    JSONArray tPosts = null;
    Button addBtn;
    Button rewriteBtn, deleteBtn;
    ArrayList<HashMap<String,String>> scheduleList, eventList, todoList, calList;
    int thisPos, exPos;
    int dateCheck = -1;
    int datePos;
    String cMonth;
    int monthEndDay;
    int nowMonth;
    int prevEndDay;
    int cDay;
    int cYear;
    int boolpos[] = new int[60];
    Hashtable<String, ArrayList> ht = null;  //일정 저장용 테이블
    ListView listView;  //리스트뷰
    ListItemAdapter listAdapter;  //리스트 어댑터
    Calendar mCalendar;
    int firstDay;
    int firstPos = 0;
    public static final int REQUEST_CODE = 1001;    //  달력 날짜 데이터 전달
    Context mContext;
    String url;

    public static FourthFragment newInstance() {
        return new FourthFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fourthfragment, container, false);
        SharedPreferences saveInfo = this.getActivity().getSharedPreferences("loginFlag", MODE_PRIVATE);
        final String userId = saveInfo.getString("USERID", "");   //  userId 가져옴
        scheduleList = new ArrayList<HashMap<String, String>>();
        eventList = new ArrayList<HashMap<String, String>>();
        todoList = new ArrayList<HashMap<String, String>>();
        ht = new Hashtable<String, ArrayList>();
        url = "http://192.168.0.12:8080/home?userid="+userId+"&date=2017-08-16 20:20:20";

        calendarView = (OneCalendarView)view.findViewById(R.id.oneCalendar);
        addBtn = (Button)view.findViewById(R.id.addBtn);

        addBtn.setVisibility(View.GONE);
        listView = (ListView) view. findViewById(R.id.listview);

        calendarView.setOneCalendarClickListener(new OneCalendarView.OneCalendarClickListener() {
            @Override
            public void dateOnClick(Day day, int position) {

            }

            @Override
            public void dateOnLongClick(Day day, int position) {

            }
        });


        calendarView.setOnCalendarChangeListener(new OneCalendarView.OnCalendarChangeListener() {
            @Override
            public void prevMonth() {

            }

            @Override
            public void nextMonth() {

            }
        });

        initCalendar();
        recalculate();
        getData(url);

        return view;

        //return inflater.inflate(R.layout.fourthfragment, container, false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    private void initCalendar() {
        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);

        calendarView.setOnCalendarChangeListener(new OneCalendarView.OnCalendarChangeListener() {


            @Override
            public void prevMonth() {
                mCalendar.add(Calendar.MONTH, -1);
                ht = new Hashtable<String, ArrayList>();
                recalculate();
                getData(url);

            }

            @Override
            public void nextMonth() {
                mCalendar.add(Calendar.MONTH, 1);
                ht = new Hashtable<String, ArrayList>();
                recalculate();
                getData(url);

            }
        });


        calendarView.setOneCalendarClickListener(new OneCalendarView.OneCalendarClickListener() {


            @Override
            public void dateOnClick(Day day, int position) {
                Calendar cal = Calendar.getInstance();

                addBtn.setVisibility(View.VISIBLE);
                Date date = day.getDate();
                final int year = date.getYear();
                final int month = date.getMonth();
                cal.setTime(date);
                final int numDay = cal.get(Calendar.DAY_OF_MONTH);
                String strMonth = Integer.toString(month+1);
                String strDay = Integer.toString(numDay);
                Log.d("datePos", String.valueOf(position));
                if(month+1 < 10) {
                    strMonth = "0"+String.valueOf(month+1);
                }
                if(numDay < 10){
                    strDay = "0"+String.valueOf(numDay);
                }

                String key = Integer.toString(year) + "-" + strMonth + "-"
                        + strDay;
                Log.d("dateee", key);

                if(boolpos[position] != 1)
                    exPos = position;

                if(boolpos[position] == 1)
                {
                    if(exPos != position)
                        calendarView.removeDaySeleted(exPos);

                    listAdapter = new ListItemAdapter();

                    ArrayList<listItem> itemList = ht.get(key);

                    for (int i = 0; i < itemList.size(); i++) {  //아이템 추가
                        listAdapter.add(itemList.get(i));
                    }


                    listView.setAdapter(listAdapter);  //연결

                }
                else{
                    listAdapter = null;
                    listView.setAdapter(listAdapter);
                }

                if(dateCheck == -1)
                {
                    datePos = position;
                    dateCheck  = 0;
                    calendarView.addDaySelected(datePos);
                }
                else if(dateCheck == 0 && boolpos[position] != 1) {
                    calendarView.removeDaySeleted(datePos);
                    dateCheck = 1;
                    datePos = position;
                    calendarView.addDaySelected(datePos);
                }
                else if(dateCheck == 1 && boolpos[position] != 1)
                {
                    calendarView.removeDaySeleted(datePos);
                    dateCheck = 0;
                    datePos = position;
                    calendarView.addDaySelected(datePos);
                }


                cYear = year;
                cMonth = calendarView.getStringMonth(month);
                cDay = numDay;

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MakeSchedule.class);
                        intent.putExtra("calDay", numDay);
                        intent.putExtra("calMonth", month);
                        intent.putExtra("calYear", year);
                        startActivityForResult(intent, REQUEST_CODE);

                        getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        //finish();
                    }

                });

            }

            @Override
            public void dateOnLongClick(Day day, int position) {
            }
        });
    }

    public void recalculate() {
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);  //날짜를 현재달의 1일로 설정
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);  //현재요일을 얻는다.
        nowMonth = mCalendar.get(Calendar.MONTH) + 1;
        Log.d("prevMonth", String.valueOf(prevEndDay));
        Log.d("nowMonth", String.valueOf(nowMonth));
        String dow = Integer.toString(dayOfWeek);
        Log.d("dayofWeek : ", dow);
        firstDay = getFirstDay(dayOfWeek);
        String fd = Integer.toString(firstDay);
        monthEndDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.d("firstDay : ", fd);
        Log.d("endDay : ", String.valueOf(monthEndDay));
        firstPos = firstDay;

        for(int i=0;i<42;i++)
            boolpos[i] = 0;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE || resultCode == 1002) {
            String monthfromText = data.getExtras().getString("monthFrom");
            String dayfromText = data.getExtras().getString("dayFrom");
            String monthtoText = data.getExtras().getString("monthTo");
            String daytoText = data.getExtras().getString("dayTo");
            int schePos = Integer.parseInt(daytoText);
            int from = Integer.parseInt(dayfromText);
            int sub = schePos - from;
            int fromPos = from;

            if(datePos == schePos+firstPos-1)
                datePos = 41;
            /*
            for(int i=-1;i<sub;i++) {
                calendarView.addDaySelected(fromPos + firstPos - 1);
                boolpos[fromPos + firstPos - 1] = 1;
                fromPos++;
            }
            */
            calendarView.addDaySelected(schePos + firstPos - 1);
            boolpos[schePos + firstPos - 1] = 1;

            String key = data.getExtras().getString("hashKey");
            Log.d("whyyreceive", key);
            listItem item = (listItem)data.getSerializableExtra("listItem");

            if (ht.containsKey(key)) {  //키가 있으면 있는 ArrayList에 추가
                ArrayList<listItem> al = ht.get(key);
                ht.remove(key);
                al.add(item);
                ht.put(key, al);

            } else {  //없으면 새로운 ArrayList를 만들어서 추가
                ArrayList<listItem> al = new ArrayList<>();
                al.add(item);
                ht.put(key, al);

            }

        }
    }

    private int getFirstDay(int dayOfWeek) {  //요일을 알아온다.
        int result = 0;

        if (dayOfWeek == Calendar.SUNDAY)
            result = 0;
        else if (dayOfWeek == Calendar.MONDAY)
            result = 1;
        else if (dayOfWeek == Calendar.TUESDAY)
            result = 2;
        else if (dayOfWeek == Calendar.WEDNESDAY)
            result = 3;
        else if (dayOfWeek == Calendar.THURSDAY)
            result = 4;
        else if (dayOfWeek == Calendar.FRIDAY)
            result = 5;
        else if (dayOfWeek == Calendar.SATURDAY)
            result = 6;
        return result;
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
            String userId = jsonObj.getString("userId");
            posts = jsonObj.getJSONArray("schedules");

            for(int i=0; i<posts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = posts.getJSONObject(i);
                String title = c.getString("title");
                String startTime = c.getString("startTime");
                String startDate = c.getString("startDate");    //  key
                String endDate = c.getString("endDate");

                String[] endDateStr = endDate.split("-");
                String endDateMonth = endDateStr[1];

                Date sDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                Date eDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

                int subDate = getDateSub(sDate, eDate);
                String[] strDate = startDate.split("-");
                String strDay = strDate[2];



                if(Integer.valueOf(strDate[1]) != Integer.valueOf(endDateMonth) && Integer.valueOf(endDateMonth) == nowMonth) {

                    //시작 달 != 끝나는 달 && 끝나는 달 == 현재 달



                    subDate = subDate - prevEndDay;

                    for (int j = 0; j < subDate; j++) {

                        int startDay = j + 1;
                        String tempDay = String.valueOf(startDay);
                        if(monthEndDay+firstPos-1 < startDay+firstPos-1)
                            break;

                        if (startDay < 10)
                            tempDay = "0" + String.valueOf(startDay);

                        String key = strDate[0] + "-" + strDate[1] + "-" + tempDay;
                        calendarView.addDaySelected(startDay + firstPos - 1);
                        boolpos[startDay + firstPos - 1] = 1;
                        listItem item = new listItem("[일정]    " + startTime, title);

                        if (ht.containsKey(key)) {  //키가 있으면 있는 ArrayList에 추가
                            ArrayList<listItem> al = ht.get(key);
                            ht.remove(key);
                            al.add(item);
                            ht.put(key, al);

                        } else {  //없으면 새로운 ArrayList를 만들어서 추가
                            ArrayList<listItem> al = new ArrayList<>();
                            al.add(item);
                            ht.put(key, al);

                        }



                    }
                }

                else if(Integer.valueOf(strDate[1]) != Integer.valueOf(endDateMonth) && Integer.valueOf(endDateMonth) > nowMonth
                        && Integer.valueOf(strDate[1]) < nowMonth)
                {
                    // 시작 달 != 끝나는 달 && 끝나는 달 > 현재 달 && 시작달 < 현재 달

                    prevEndDay = monthEndDay;
                    for(int j=0;j<subDate;j++)
                    {
                        int startDay = j+1;
                        if(monthEndDay+firstPos-1 < startDay+firstPos-1)
                            break;

                        String tempDay = String.valueOf(startDay);
                        String midMonth = String.valueOf(nowMonth);

                        if(startDay < 10)
                            tempDay = "0" + String.valueOf(startDay);

                        if(nowMonth < 10)
                            midMonth = "0" + String.valueOf(nowMonth);



                        String key = strDate[0]+"-"+midMonth+"-"+tempDay;
                        calendarView.addDaySelected(startDay+firstPos-1);
                        boolpos[startDay+firstPos-1] = 1;
                        listItem item = new listItem("[일정]    "+startTime, title);

                        if (ht.containsKey(key)) {  //키가 있으면 있는 ArrayList에 추가
                            ArrayList<listItem> al = ht.get(key);
                            ht.remove(key);
                            al.add(item);
                            ht.put(key, al);

                        } else {  //없으면 새로운 ArrayList를 만들어서 추가
                            ArrayList<listItem> al = new ArrayList<>();
                            al.add(item);
                            ht.put(key, al);

                        }

                        if(subDate > 41 && j == 41)
                        {
                            break;
                        }
                    }


                }

                else if(Integer.valueOf(strDate[1]) != Integer.valueOf(endDateMonth) && Integer.valueOf(endDateMonth) > nowMonth )
                {
                    // 시작 달 != 끝나는 달 && 끝나는 달 > 현재 달 && 시작달 == 현재 달
                    prevEndDay = monthEndDay;
                    for(int j=0;j<subDate;j++)
                    {


                        int startDay = Integer.valueOf(strDay) + j;
                        String tempDay = String.valueOf(startDay);

                        if(monthEndDay+firstPos-1 < startDay+firstPos-1)
                            break;

                        if (startDay < 10)
                            tempDay = "0" + String.valueOf(startDay);

                        String key = strDate[0] + "-" + strDate[1] + "-" + tempDay;
                        calendarView.addDaySelected(startDay + firstPos - 1);
                        boolpos[startDay + firstPos - 1] = 1;
                        listItem item = new listItem("[일정]    "+startTime, title);

                        if (ht.containsKey(key)) {  //키가 있으면 있는 ArrayList에 추가
                            ArrayList<listItem> al = ht.get(key);
                            ht.remove(key);
                            al.add(item);
                            ht.put(key, al);

                        } else {  //없으면 새로운 ArrayList를 만들어서 추가
                            ArrayList<listItem> al = new ArrayList<>();
                            al.add(item);
                            ht.put(key, al);

                        }
                        if(subDate > 41 && j == 41)
                        {
                            break;
                        }
                    }


                }


            else if(Integer.valueOf(strDate[1]) == Integer.valueOf(endDateMonth) && Integer.valueOf(endDateMonth) == nowMonth)
            {
                prevEndDay = monthEndDay;

                for (int j = 0; j <= subDate; j++) {

                    int startDay = Integer.valueOf(strDay) + j;
                    String tempDay = String.valueOf(startDay);
                    if(monthEndDay+firstPos-1 < startDay+firstPos-1)
                        break;

                    if (startDay < 10)
                        tempDay = "0" + String.valueOf(startDay);

                    String key = strDate[0] + "-" + strDate[1] + "-" + tempDay;
                    calendarView.addDaySelected(startDay + firstPos - 1);
                    boolpos[startDay + firstPos - 1] = 1;
                    listItem item = new listItem("[일정]    " + startTime, title);

                    if (ht.containsKey(key)) {  //키가 있으면 있는 ArrayList에 추가
                        ArrayList<listItem> al = ht.get(key);
                        ht.remove(key);
                        al.add(item);
                        ht.put(key, al);

                    } else {  //없으면 새로운 ArrayList를 만들어서 추가
                        ArrayList<listItem> al = new ArrayList<>();
                        al.add(item);
                        ht.put(key, al);

                    }
                    if(subDate > 41 && j == 41)
                    {
                        break;
                    }

                }
            }

            else {


                }



                if(title.length() > 16 ) {
                    title = title.substring(0,16) + "..."; //18자 자르고 ... 붙이기
                }

            }

            ePosts = jsonObj.getJSONArray("events");
            for(int i=0; i<ePosts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = ePosts.getJSONObject(i);
                String title = c.getString("title");
                String startDate = c.getString("startDate");
                String endDate = c.getString("endDate");

                String[] endDateStr = endDate.split("-");
                String endDateMonth = endDateStr[1];

                Date sDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                Date eDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

                int subDate = getDateSub(sDate, eDate);
                String[] strDate = startDate.split("-");
                String strDay = strDate[2];


                if(Integer.valueOf(strDate[1]) != Integer.valueOf(endDateMonth) && Integer.valueOf(endDateMonth) == nowMonth) {

                    //시작 달 != 끝나는 달 && 끝나는 달 == 현재 달


                    for (int j = 0; j < Integer.valueOf(endDateStr[2]); j++) {

                        int startDay = j + 1;
                        if(monthEndDay+firstPos-1 < startDay+firstPos-1)
                            break;

                        String tempDay = String.valueOf(startDay);

                        if (startDay < 10)
                            tempDay = "0" + String.valueOf(startDay);

                        String key = endDateStr[0] + "-" + endDateStr[1] + "-" + tempDay;
                        calendarView.addDaySelected(startDay + firstPos - 1);
                        boolpos[startDay + firstPos - 1] = 1;
                        listItem item = new listItem("[행사] ", title);

                        if (ht.containsKey(key)) {  //키가 있으면 있는 ArrayList에 추가
                            ArrayList<listItem> al = ht.get(key);
                            ht.remove(key);
                            al.add(item);
                            ht.put(key, al);

                        } else {  //없으면 새로운 ArrayList를 만들어서 추가
                            ArrayList<listItem> al = new ArrayList<>();
                            al.add(item);
                            ht.put(key, al);

                        }


                    }
                }

                else if(Integer.valueOf(strDate[1]) != Integer.valueOf(endDateMonth) && Integer.valueOf(endDateMonth) > nowMonth
                        && Integer.valueOf(strDate[1]) < nowMonth)
                {
                    // 시작 달 != 끝나는 달 && 끝나는 달 > 현재 달 && 시작달 < 현재 달

                    prevEndDay = monthEndDay;
                    for(int j=0;j<subDate;j++)
                    {
                        int startDay = j+1;
                        if(monthEndDay+firstPos-1 < startDay+firstPos-1)
                            break;

                        String tempDay = String.valueOf(startDay);
                        String midMonth = String.valueOf(nowMonth);

                        if(startDay < 10)
                            tempDay = "0" + String.valueOf(startDay);

                        if(nowMonth < 10)
                            midMonth = "0" + String.valueOf(nowMonth);



                        String key = strDate[0]+"-"+midMonth+"-"+tempDay;
                        calendarView.addDaySelected(startDay+firstPos-1);
                        boolpos[startDay+firstPos-1] = 1;
                        listItem item = new listItem("[행사] ", title);

                        if (ht.containsKey(key)) {  //키가 있으면 있는 ArrayList에 추가
                            ArrayList<listItem> al = ht.get(key);
                            ht.remove(key);
                            al.add(item);
                            ht.put(key, al);

                        } else {  //없으면 새로운 ArrayList를 만들어서 추가
                            ArrayList<listItem> al = new ArrayList<>();
                            al.add(item);
                            ht.put(key, al);

                        }

                        if(subDate > 41 && j == 41)
                        {
                            break;
                        }
                    }


                }

                else if(Integer.valueOf(strDate[1]) != Integer.valueOf(endDateMonth) && Integer.valueOf(endDateMonth) > nowMonth )
                {
                    // 시작 달 != 끝나는 달 && 끝나는 달 > 현재 달 && 시작달 == 현재 달
                    prevEndDay = monthEndDay;
                    for(int j=0;j<subDate;j++)
                    {


                        int startDay = Integer.valueOf(strDay) + j;
                        String tempDay = String.valueOf(startDay);

                        if(monthEndDay+firstPos-1 < startDay+firstPos-1)
                            break;

                        if (startDay < 10)
                            tempDay = "0" + String.valueOf(startDay);

                        String key = strDate[0] + "-" + strDate[1] + "-" + tempDay;
                        calendarView.addDaySelected(startDay + firstPos - 1);
                        boolpos[startDay + firstPos - 1] = 1;
                        listItem item = new listItem("[행사] ", title);

                        if (ht.containsKey(key)) {  //키가 있으면 있는 ArrayList에 추가
                            ArrayList<listItem> al = ht.get(key);
                            ht.remove(key);
                            al.add(item);
                            ht.put(key, al);

                        } else {  //없으면 새로운 ArrayList를 만들어서 추가
                            ArrayList<listItem> al = new ArrayList<>();
                            al.add(item);
                            ht.put(key, al);

                        }
                        if(subDate > 41 && j == 41)
                        {
                            break;
                        }

                    }


                }


                else if(Integer.valueOf(strDate[1]) == Integer.valueOf(endDateMonth) && Integer.valueOf(endDateMonth) == nowMonth)
                {
                    //시작 달 == 끝나는 달 && 끝나는 달 == 현재달
                    prevEndDay = monthEndDay;

                    for (int j = 0; j <= subDate; j++) {



                        int startDay = Integer.valueOf(strDay) + j;
                        if(monthEndDay+firstPos-1 < startDay+firstPos-1)
                            break;

                        String tempDay = String.valueOf(startDay);

                        if (startDay < 10)
                            tempDay = "0" + String.valueOf(startDay);

                        String key = strDate[0] + "-" + strDate[1] + "-" + tempDay;
                        calendarView.addDaySelected(startDay + firstPos - 1);
                        boolpos[startDay + firstPos - 1] = 1;
                        listItem item = new listItem("[행사] ", title);

                        if (ht.containsKey(key)) {  //키가 있으면 있는 ArrayList에 추가
                            ArrayList<listItem> al = ht.get(key);
                            ht.remove(key);
                            al.add(item);
                            ht.put(key, al);

                        } else {  //없으면 새로운 ArrayList를 만들어서 추가
                            ArrayList<listItem> al = new ArrayList<>();
                            al.add(item);
                            ht.put(key, al);

                        }
                        if(subDate > 41 && j == 41)
                        {
                            break;
                        }

                    }
                }

                else {


                }

                if(title.length() > 16 ) {
                    title = title.substring(0,16) + "..."; //18자 자르고 ... 붙이기
                }

            }

            ///

            tPosts = jsonObj.getJSONArray("todos");
            for(int i=0; i<tPosts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = tPosts.getJSONObject(i);
                String title = c.getString("title");
                String dueDate = c.getString("dueDate");

                String[] endDateStr = dueDate.split("-");
                String endDateMonth = endDateStr[1];

                Date eDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDate);


                if(Integer.valueOf(endDateMonth) == nowMonth) {

                    //끝나는 달 == 현재 달

                    String[] strDate = dueDate.split("-");
                    String strDay = strDate[2];
                    int startDay = Integer.valueOf(strDay);
                    calendarView.addDaySelected(startDay+firstPos-1);
                    boolpos[startDay+firstPos-1] = 1;

                    listItem item = new listItem("[할일 마감] ", title);

                    if (ht.containsKey(dueDate)) {  //키가 있으면 있는 ArrayList에 추가
                        ArrayList<listItem> al = ht.get(dueDate);
                        ht.remove(dueDate);
                        al.add(item);
                        ht.put(dueDate, al);

                    } else {  //없으면 새로운 ArrayList를 만들어서 추가
                        ArrayList<listItem> al = new ArrayList<>();
                        al.add(item);
                        ht.put(dueDate, al);

                    }
                }

                else {


                }

                if(title.length() > 16 ) {
                    title = title.substring(0,16) + "..."; //18자 자르고 ... 붙이기
                }

            }

        }catch(JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getDateSub(Date a, Date b) {
        long calDate = b.getTime() - a.getTime();
        long calDateDays = calDate / ( 24*60*60*1000);

        calDateDays = Math.abs(calDateDays);

        int ans = (int) calDateDays;
        return ans;
    }

}