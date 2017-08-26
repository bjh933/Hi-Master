package com.example.a1.himaster.SKPlanet;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by a1 on 2017. 8. 20..
 */

public class WeatherTodayThread extends Thread {
    final static String TAG = "WeatherThreeDayThread";
    Context mContext;
    WeatherTodayRepo weatherTodayRepo;
    Handler handler;
    int version = 1;
    String lat;
    String lon;

    public WeatherTodayThread(Handler handler, Context mContext, double lat, double lon) {
        this.mContext = mContext;
        this.lat = String. valueOf(lat);
        this.lon = String.valueOf(lon);
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherTodayRepo.WeatherApiInterface service2 = client.create(WeatherTodayRepo.WeatherApiInterface.class);

        Call<WeatherTodayRepo> call2 = service2.get_Weather_1day(version, lat, lon);
        call2.enqueue(new Callback<WeatherTodayRepo>() {
            @Override
            public void onResponse(Response<WeatherTodayRepo> response) {
                if(response.isSuccess()){
                    weatherTodayRepo = response.body();
                    Log.d(TAG,"response.raw :"+response.raw());
                    if(weatherTodayRepo.getResult().getCode().equals("9200")){ // 9200 = 성공
                        Weather.getInstance().setTodayTmax(weatherTodayRepo.getWeather().getHourly().get(0).getTemperature().getTmax());
                        Weather.getInstance().setTodayTmin(weatherTodayRepo.getWeather().getHourly().get(0).getTemperature().getTmin());
                        Weather.getInstance().setDayStatus(weatherTodayRepo.getWeather().getHourly().get(0).getSky().getName());
                        String todayStatus = Weather.getInstance().getDayStatus();
                        String todayTmax = Weather.getInstance().getTodayTmax();
                        String todayTmin = Weather.getInstance().getTodayTmin();
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("weatherToday", todayStatus + "-" + todayTmax+ "-" + todayTmin);
                        Log.d("weatherrr", todayStatus + "-" + todayTmax+ "-" + todayTmin);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }else{
                        Log.e(TAG,"요청 실패 :"+weatherTodayRepo.getResult().getCode());
                        Log.e(TAG,"메시지 :"+weatherTodayRepo.getResult().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG,"날씨정보 불러오기 실패 :" + t.getMessage() );
            }
        });
    }
}
