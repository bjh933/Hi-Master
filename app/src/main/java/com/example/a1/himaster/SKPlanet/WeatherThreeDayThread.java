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

public class WeatherThreeDayThread extends Thread {
    final static String TAG = "WeatherThreeDayThread";
    Context mContext;
    WeatherThreeDayRepo weatherThreeDayRepo;
    Handler handler;
    int version = 1;
    String lat;
    String lon;

    public WeatherThreeDayThread(Handler handler, Context mContext, double lat, double lon) {
        this.mContext = mContext;
        this.lat = String. valueOf(lat);
        this.lon = String.valueOf(lon);
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherThreeDayRepo.WeatherApiInterface service = client.create(WeatherThreeDayRepo.WeatherApiInterface.class);

        Call<WeatherThreeDayRepo> call = service.get_Weather_3day(version, lat, lon);
        call.enqueue(new Callback<WeatherThreeDayRepo>() {
            @Override
            public void onResponse(Response<WeatherThreeDayRepo> response) {
                if(response.isSuccess()){
                    weatherThreeDayRepo = response.body();
                    Log.d(TAG,"response.raw :"+response.raw());
                    if(weatherThreeDayRepo.getResult().getCode().equals("9200")){ // 9200 = 성공
                        String contents = "";
                        //contents=;

                        Weather.getInstance().setDayTomorrowStatus(weatherThreeDayRepo.getWeather().getForecast3days().get(0).getFcst3hour().getSky().getName31hour());
                        Weather.getInstance().setDayAfterStatus(weatherThreeDayRepo.getWeather().getForecast3days().get(0).getFcst3hour().getSky().getName46hour());
                        Weather.getInstance().setTomorrowTmax(weatherThreeDayRepo.getWeather().getForecast3days().get(0).getFcstdaily().getTemperature().getTmax2day());
                        Weather.getInstance().setTomorrowTmin(weatherThreeDayRepo.getWeather().getForecast3days().get(0).getFcstdaily().getTemperature().getTmin2day());

                        Weather.getInstance().setDayAfterTmax(weatherThreeDayRepo.getWeather().getForecast3days().get(0).getFcstdaily().getTemperature().getTmax3day());
                        Weather.getInstance().setDayAfterTmin(weatherThreeDayRepo.getWeather().getForecast3days().get(0).getFcstdaily().getTemperature().getTmin3day());

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
                        Log.e(TAG,"요청 실패 :"+weatherThreeDayRepo.getResult().getCode());
                        Log.e(TAG,"메시지 :"+weatherThreeDayRepo.getResult().getMessage());
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
