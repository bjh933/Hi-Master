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

public class WeatherThread extends Thread {
    final static String TAG = "WeatherThread";
    Context mContext;
    WeatherRepo weatherRepo;
    Handler handler;
    int version = 1;
    String lat;
    String lon;

    public WeatherThread(Handler handler, Context mContext, double lat, double lon) {
        this.mContext = mContext;
        this.lat = String. valueOf(lat);
        this.lon = String.valueOf(lon);
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherRepo.WeatherApiInterface service = client.create(WeatherRepo.WeatherApiInterface.class);

        Call<WeatherRepo> call = service.get_Weather_3day(version, lat, lon);
        call.enqueue(new Callback<WeatherRepo>() {
            @Override
            public void onResponse(Response<WeatherRepo> response) {
                if(response.isSuccess()){
                    weatherRepo = response.body();
                    Log.d(TAG,"response.raw :"+response.raw());
                    if(weatherRepo.getResult().getCode().equals("9200")){ // 9200 = 성공
                        String contents = "";
                        //contents=;

                        Weather.getInstance().setDayTomorrowStatus(weatherRepo.getWeather().getForecast3days().get(0).getFcst3hour().getSky().getName31hour());
                        Weather.getInstance().setDayAfterStatus(weatherRepo.getWeather().getForecast3days().get(0).getFcst3hour().getSky().getName46hour());
                        Weather.getInstance().setTomorrowTmax(weatherRepo.getWeather().getForecast3days().get(0).getFcstdaily().getTemperature().getTmax2day());
                        Weather.getInstance().setTomorrowTmin(weatherRepo.getWeather().getForecast3days().get(0).getFcstdaily().getTemperature().getTmin2day());

                        Weather.getInstance().setDayAfterTmax(weatherRepo.getWeather().getForecast3days().get(0).getFcstdaily().getTemperature().getTmax3day());
                        Weather.getInstance().setDayAfterTmin(weatherRepo.getWeather().getForecast3days().get(0).getFcstdaily().getTemperature().getTmin3day());

                        String dayTomorrowStatus = Weather.getInstance().getDayTomorrowStatus();
                        String dayAfterStatus = Weather.getInstance().getDayAfterStatus();

                        String dayTomorrowTmax = Weather.getInstance().getTomorrowTmax();
                        String dayAfterTmax = Weather.getInstance().getDayAfterTmax();
                        String dayTomorrowTmin = Weather.getInstance().getTomorrowTmin();
                        String dayAfterTmin = Weather.getInstance().getDayAfterTmin();

                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("weather", dayTomorrowStatus + "-" + dayTomorrowTmax + "-" + dayTomorrowTmin
                                + "-" + dayAfterStatus + "-" + dayAfterTmax + "-" + dayAfterTmin);
                        Log.d("weather23", dayTomorrowStatus + " " + dayTomorrowTmax + " " + dayTomorrowTmin
                                + " " + dayAfterStatus + " " + dayAfterTmax + " " + dayAfterTmin);
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    }else{
                        Log.e(TAG,"요청 실패 :"+weatherRepo.getResult().getCode());
                        Log.e(TAG,"메시지 :"+weatherRepo.getResult().getMessage());
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
