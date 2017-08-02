package com.example.a1.himaster;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import static android.view.LayoutInflater.from;


public class num09_Main extends AppCompatActivity {
    private OneCalendarView calendarView;
    ImageView backBtn;
    Button detailBtn, addBtn;
    Button rewriteBtn, deleteBtn;
    int thisPos;
    int dateCheck = -1;
    int datePos;
    String cMonth;
    int cDay;
    int cYear;
    int boolpos[] = new int[60];
    Hashtable<String, ArrayList> ht = new Hashtable<String, ArrayList>();  //일정 저장용 테이블
    ListView listView;  //리스트뷰
    listItemAdapter listAdapter;  //리스트 어댑터


    public static final int REQUEST_CODE = 1001;    //  달력 날짜 데이터 전달

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num09);

        setCustomActionbar();

        backBtn = (ImageView) findViewById(R.id.menu_back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num09_Main.this, num13_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        calendarView = (OneCalendarView) findViewById(R.id.oneCalendar);
        detailBtn = (Button)findViewById(R.id.detailBtn);
        addBtn = (Button)findViewById(R.id.addBtn);

        detailBtn.setVisibility(View.GONE);
        addBtn.setVisibility(View.GONE);

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

    }


    private void setCustomActionbar()
    {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = from(this).inflate(R.layout.abs_layout, null);
        actionBar.setCustomView(mCustomView);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#999999")));


        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(mCustomView, params);

    }

    private void initCalendar() {
        //el siguiente fragmento puede ser usado para capturar los swipes en el calendar
        calendarView.setOnCalendarChangeListener(new OneCalendarView.OnCalendarChangeListener() {


            @Override
            public void prevMonth() {
                Toast.makeText(num09_Main.this, calendarView.getStringMonth(calendarView.getMonth()) + " " + calendarView.getYear(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void nextMonth() {
                Toast.makeText(num09_Main.this, calendarView.getStringMonth(calendarView.getMonth()) + " " + calendarView.getYear(), Toast.LENGTH_SHORT).show();
            }
        });


        calendarView.setOneCalendarClickListener(new OneCalendarView.OneCalendarClickListener() {


            @Override
            public void dateOnClick(Day day, int position) {

                thisPos = position;
                detailBtn.setVisibility(View.VISIBLE);
                addBtn.setVisibility(View.VISIBLE);
                String toast;
                Date date = day.getDate();
                final int year = date.getYear();
                final int month = date.getMonth();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                final int numDay = cal.get(Calendar.DAY_OF_MONTH);
                toast = String.valueOf(date.getDate());
                String key = Integer.toString(year) + "-" + Integer.toString(month+1) + "-"
                        + Integer.toString(numDay);
                listView = (ListView) findViewById(R.id.listview);


                //Toast.makeText(num09_Main.this, numDay + " " + calendarView.getStringMonth(month) + " " + year, Toast.LENGTH_SHORT).show();
                Toast.makeText(num09_Main.this, toast, Toast.LENGTH_SHORT).show();
                if(boolpos[position] == 1)
                {

                    listAdapter = new listItemAdapter();

                    ArrayList<listItem> al = ht.get(key);

                    for (int i = 0; i < al.size(); i++) {  //아이템 추가
                        listAdapter.add(al.get(i));
                    }

                    listView.setAdapter(listAdapter);  //연결
                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);




                }
                else{
                    listAdapter = null;
                    listView.setAdapter(listAdapter);
                }

                if(dateCheck == -1)
                {
                    datePos = position;
                    dateCheck  = 0;
                }
                else if(dateCheck == 0) {
                    calendarView.removeDaySeleted(datePos);
                    //calendarView.addDaySelected(exPos);
                    dateCheck = 1;
                    datePos = position;
                }
                else if(dateCheck == 1)
                {
                    calendarView.removeDaySeleted(datePos);
                    //calendarView.addDaySelected(exPos);
                    dateCheck = 0;
                    datePos = position;
                }

                calendarView.addDaySelected(datePos);
                cYear = year;
                cMonth = calendarView.getStringMonth(month);
                cDay = numDay;

                detailBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(num09_Main.this, num21_Main.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        finish();
                    }

                });

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(num09_Main.this, num10_Main.class);
                        intent.putExtra("calDay", numDay);
                        intent.putExtra("calMonth", month);
                        intent.putExtra("calYear", year);
                        //Toast.makeText(num09_Main.this, cDay, Toast.LENGTH_SHORT).show();
                        startActivityForResult(intent, REQUEST_CODE);

                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        //finish();
                    }

                });

            }

            @Override
            public void dateOnLongClick(Day day, int position) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(num09_Main.this, "데이터 수신 실패", Toast.LENGTH_SHORT).show();
            //return;
        }

        if (requestCode == REQUEST_CODE || resultCode == 1002) {
            //num10의 날짜와 같은 달력날짜라면 출력하도록
            //if(data.getExtras().getInt("Year") == cYear && data.getExtras().getInt("Month") == cMonth && data.getExtras().getInt("Day") == cDay) {

            String dayText = data.getExtras().getString("dayTo");
            int schePos = Integer.parseInt(dayText);


            calendarView.addDaySelected(schePos+1);
            boolpos[schePos+1] = 1;

            String key = data.getExtras().getString("hashKey");
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

        } else {
            Toast.makeText(num09_Main.this, "REQUEST_CODE가 아님", Toast.LENGTH_SHORT).show();
        }
    }


}