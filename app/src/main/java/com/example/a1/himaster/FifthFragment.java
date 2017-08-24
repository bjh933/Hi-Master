package com.example.a1.himaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Adib on 13-Apr-17.
 */

public class FifthFragment extends Fragment {

    Button weekWeatherBtn;
    public static FifthFragment newInstance() {
        return new FifthFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.num04, container, false);
        weekWeatherBtn = (Button)view.findViewById(R.id.weekWeatherBtn);

        weekWeatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeekWeather.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
