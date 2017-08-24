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

public class WeatherWeekThread extends Thread {
    final static String TAG = "WeatherWeekThread";
    Context mContext;
    WeatherWeekRepo weatherWeekRepo;
    Handler handler;
    int version = 1;
    String lat;
    String lon;

    public WeatherWeekThread(Handler handler, Context mContext, double lat, double lon) {
        this.mContext = mContext;
        this.lat = String. valueOf(lat);
        this.lon = String.valueOf(lon);
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherWeekRepo.WeatherApiInterface service = client.create(WeatherWeekRepo.WeatherApiInterface.class);

        Call<WeatherWeekRepo> call = service.get_Weather_6day(version, lat, lon);
        call.enqueue(new Callback<WeatherWeekRepo>() {
            @Override
            public void onResponse(Response<WeatherWeekRepo> response) {
                if(response.isSuccess()){
                    weatherWeekRepo = response.body();
                    Log.d(TAG,"response.raw :"+response.raw());
                    if(weatherWeekRepo.getResult().getCode().equals("9200")){ // 9200 = 성공

                        Weather.getInstance().setDayAfterStatus(weatherWeekRepo.getWeather().getForecast6days().get(0).getSky().getPmName3day());
                        Weather.getInstance().setDay4Status(weatherWeekRepo.getWeather().getForecast6days().get(0).getSky().getPmName4day());
                        Weather.getInstance().setDay5Status(weatherWeekRepo.getWeather().getForecast6days().get(0).getSky().getPmName5day());
                        Weather.getInstance().setDay6Status(weatherWeekRepo.getWeather().getForecast6days().get(0).getSky().getPmName6day());
                        Weather.getInstance().setDay7Status(weatherWeekRepo.getWeather().getForecast6days().get(0).getSky().getPmName7day());
                        // 7일간 하늘 상태

                        Weather.getInstance().setDayAfterTmax(weatherWeekRepo.getWeather().getForecast6days().get(0).getTemperature().getTmax3day());
                        Weather.getInstance().setDayAfterTmin(weatherWeekRepo.getWeather().getForecast6days().get(0).getTemperature().getTmin3day());
                        Weather.getInstance().setDay4Tmax(weatherWeekRepo.getWeather().getForecast6days().get(0).getTemperature().getTmax4day());
                        Weather.getInstance().setDay4Tmin(weatherWeekRepo.getWeather().getForecast6days().get(0).getTemperature().getTmin4day());
                        Weather.getInstance().setDay5Tmax(weatherWeekRepo.getWeather().getForecast6days().get(0).getTemperature().getTmax5day());
                        Weather.getInstance().setDay5Tmin(weatherWeekRepo.getWeather().getForecast6days().get(0).getTemperature().getTmin5day());
                        Weather.getInstance().setDay6Tmax(weatherWeekRepo.getWeather().getForecast6days().get(0).getTemperature().getTmax6day());
                        Weather.getInstance().setDay6Tmin(weatherWeekRepo.getWeather().getForecast6days().get(0).getTemperature().getTmin6day());
                        Weather.getInstance().setDay7Tmax(weatherWeekRepo.getWeather().getForecast6days().get(0).getTemperature().getTmax7day());
                        Weather.getInstance().setDay7Tmin(weatherWeekRepo.getWeather().getForecast6days().get(0).getTemperature().getTmin7day());
                        // 7일간 기온 상태

                        String dayAfterStatus = Weather.getInstance().getDayAfterStatus();
                        String dayAfterTmax = Weather.getInstance().getDayAfterTmax();
                        String dayAfterTmin = Weather.getInstance().getDayAfterTmin();
                        String day4Status = Weather.getInstance().getDay4Status();
                        String day4Tmax = Weather.getInstance().getDay4Tmax();
                        String day4Tmin = Weather.getInstance().getDay4Tmin();
                        String day5Status = Weather.getInstance().getDay5Status();
                        String day5Tmax = Weather.getInstance().getDay5Tmax();
                        String day5Tmin = Weather.getInstance().getDay5Tmin();
                        String day6Status = Weather.getInstance().getDay6Status();
                        String day6Tmax = Weather.getInstance().getDay6Tmax();
                        String day6Tmin = Weather.getInstance().getDay6Tmin();
                        String day7Status = Weather.getInstance().getDay7Status();
                        String day7Tmax = Weather.getInstance().getDay7Tmax();
                        String day7Tmin = Weather.getInstance().getDay7Tmin();


                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("WeekWeather", dayAfterStatus + "-" + dayAfterTmax + "-" + dayAfterTmin
                                + "-" + day4Status + "-" + day4Tmax + "-" + day4Tmin + "-" + day5Status + "-" + day5Tmax + "-" +
                                day5Tmin + "-" + day6Status + "-" + day6Tmax + "-" + day6Tmin + "-" + day7Status
                                + "-" + day7Tmax + "-" + day7Tmin);

                        Log.d("WeekWeather", dayAfterStatus + " " + dayAfterTmax + " " + dayAfterTmin
                                + " " + day4Status + " " + day4Tmax + " " + day4Tmin + " " + day5Status + " " + day5Tmax + " " +
                                day5Tmin + " " + day6Status + " " + day6Tmax + " " + day6Tmin + " " + day7Status
                                + " " + day7Tmax + " " + day7Tmin);

                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    }else{
                        Log.e(TAG,"요청 실패 :"+weatherWeekRepo.getResult().getCode());
                        Log.e(TAG,"메시지 :"+weatherWeekRepo.getResult().getMessage());
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
