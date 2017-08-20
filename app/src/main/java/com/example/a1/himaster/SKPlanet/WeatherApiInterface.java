package com.example.a1.himaster.SKPlanet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface WeatherApiInterface {
    @Headers({"Accept: application/json","appKey:b42a1814-4abc-36c2-a743-43c5f81cd73d"})

    @GET("weather/forecast/3days")
    Call<WeatherRepo> get_Weather_3day(@Query("version") int version, @Query("lat") String lat, @Query("lon") String lon);

    @GET("weather/current/hourly")
    Call<WeatherRepo> get_Weather_1day(@Query("version") int version, @Query("lat") String lat, @Query("lon") String lon);
}
