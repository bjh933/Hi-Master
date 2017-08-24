package com.example.a1.himaster;

import android.content.Intent;
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
import com.example.a1.himaster.Adapter.listItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import static android.app.Activity.RESULT_OK;

public class FourthFragment extends Fragment {

    private OneCalendarView calendarView;
    Button detailBtn, addBtn;
    Button rewriteBtn, deleteBtn;
    int thisPos, exPos;
    int dateCheck = -1;
    int datePos;
    String cMonth;
    int cDay;
    int cYear;
    int boolpos[] = new int[60];
    Hashtable<String, ArrayList> ht = new Hashtable<String, ArrayList>();  //일정 저장용 테이블
    ListView listView;  //리스트뷰
    listItemAdapter listAdapter;  //리스트 어댑터
    Calendar mCalendar;
    int firstDay;
    int firstPos = 0;
    public static final int REQUEST_CODE = 1001;    //  달력 날짜 데이터 전달

    public static FourthFragment newInstance() {
        return new FourthFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.num09, container, false);

        calendarView = (OneCalendarView)view.findViewById(R.id.oneCalendar);
        detailBtn = (Button)view.findViewById(R.id.detailBtn);
        addBtn = (Button)view.findViewById(R.id.addBtn);

        detailBtn.setVisibility(View.GONE);
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

        return view;

        //return inflater.inflate(R.layout.num09, container, false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);

    }

    private void initCalendar() {
        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);

        calendarView.setOnCalendarChangeListener(new OneCalendarView.OnCalendarChangeListener() {


            @Override
            public void prevMonth() {
                mCalendar.add(Calendar.MONTH, -1);
                recalculate();
                //Toast.makeText(com.example.a1.himaster.FourthFragment.this, calendarView.getStringMonth(calendarView.getMonth()) + " " + calendarView.getYear(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void nextMonth() {
                mCalendar.add(Calendar.MONTH, 1);
                recalculate();
                //Toast.makeText(com.example.a1.himaster.num09_Main.this, calendarView.getStringMonth(calendarView.getMonth()) + " " + calendarView.getYear(), Toast.LENGTH_SHORT).show();
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

                if(month+1 < 10) {
                    strMonth = "0"+String.valueOf(month+1);
                }
                if(numDay < 10){
                    strDay = "0"+String.valueOf(numDay);
                }

                String key = Integer.toString(year) + "-" + strMonth + "-"
                        + strDay;


                if(boolpos[position] != 1)
                    exPos = position;

                if(boolpos[position] == 1)
                {
                    if(exPos != position)
                        calendarView.removeDaySeleted(exPos);

                    listAdapter = new listItemAdapter();

                    ArrayList<listItem> itemList = ht.get(key);
                    Log.d("whykey", key);
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
                        Intent intent = new Intent(getActivity(), num10_Main.class);
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
        String dow = Integer.toString(dayOfWeek);
        Log.d("dayofWeek : ", dow);
        firstDay = getFirstDay(dayOfWeek);
        String fd = Integer.toString(firstDay);
        Log.d("firstDay : ", fd);
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


}