package com.example.a1.himaster;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.example.a1.himaster.bottomtab.BottomBarHolderActivity;
import com.example.a1.himaster.bottomtab.NavigationPage;

import java.util.ArrayList;
import java.util.List;



public class BottombarActivity extends BottomBarHolderActivity {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationPage page1 = new NavigationPage("홈", ContextCompat.getDrawable(this, R.drawable.navhomee), FirstFragment.newInstance());
        NavigationPage page2 = new NavigationPage("오늘 할일", ContextCompat.getDrawable(this, R.drawable.navsche), SecondFragment.newInstance());
        NavigationPage page3 = new NavigationPage("날씨", ContextCompat.getDrawable(this, R.drawable.navwea), ThirdFragment.newInstance());
        NavigationPage page4 = new NavigationPage("달력", ContextCompat.getDrawable(this, R.drawable.navcal), FourthFragment.newInstance());
        NavigationPage page5 = new NavigationPage("장소 추천", ContextCompat.getDrawable(this, R.drawable.navreco), FifthFragment.newInstance());
        List<NavigationPage> navigationPages = new ArrayList<>();
        navigationPages.add(page1);
        navigationPages.add(page2);
        navigationPages.add(page3);
        navigationPages.add(page4);
        navigationPages.add(page5);

        super.setupBottomBarHolderActivity(navigationPages);
    }

    @Override
    public void onBackPressed()
    {
        long tempTime        = System.currentTimeMillis();
        long intervalTime    = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
        }

    }

}