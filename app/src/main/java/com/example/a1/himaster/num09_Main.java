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
import android.widget.ImageView;
import android.widget.Toast;

import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;

import java.util.Calendar;
import java.util.Date;

import static android.view.LayoutInflater.from;


public class num09_Main extends AppCompatActivity {
    private OneCalendarView calendarView;
    ImageView backBtn;
    Button detailBtn;
    Button addBtn;
    int dateCheck = -1;
    int datePos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num09);

        setCustomActionbar();
        backBtn = (ImageView) findViewById(R.id.menu_back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(num09_Main.this, num10_Main.class);
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

                detailBtn.setVisibility(View.VISIBLE);
                addBtn.setVisibility(View.VISIBLE);

                Date date = day.getDate();
                int year = date.getYear();
                int month = date.getMonth();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int numDay = cal.get(Calendar.DAY_OF_MONTH);
                //Toast.makeText(num09_Main.this, numDay + " " + calendarView.getStringMonth(month) + " " + year, Toast.LENGTH_SHORT).show();

                if(dateCheck == -1)
                {
                    datePos = position;
                    dateCheck  = 0;
                }
                else if(dateCheck == 0) {
                    calendarView.removeDaySeleted(datePos);
                    dateCheck = 1;
                    datePos = position;
                }
                else if(dateCheck == 1)
                {
                    calendarView.removeDaySeleted(datePos);
                    dateCheck = 0;
                    datePos = position;
                }
                calendarView.addDaySelected(datePos);

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
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        finish();
                    }

                });




            }

                @Override
                public void dateOnLongClick(Day day, int position) {
                    Intent intent = new Intent(num09_Main.this, MainActivity.class);
                    startActivity(intent);

                }
            });
    }

}

