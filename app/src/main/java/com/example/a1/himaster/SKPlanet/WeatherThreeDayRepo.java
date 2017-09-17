package com.example.a1.himaster.SKPlanet;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by a1 on 2017. 8. 20..
 */

public class WeatherThreeDayRepo {

    @SerializedName("result")
    Result result;
    @SerializedName("weather")
    weather weather;


    public class Result {
        @SerializedName("message")
        String message;
        @SerializedName("code")
        String code;

        public String getMessage() {return message;}
        public String getCode() {return code;}
    }

    public class weather {

        public List<forecast3days> forecast3days = new ArrayList<>();
        public List<forecast3days> getForecast3days() {return forecast3days;}


        public class forecast3days {
            @SerializedName("fcst3hour")
            fcst3hour fcst3hour;
            @SerializedName("fcstdaily")
            fcstdaily fcstdaily;

            public fcst3hour getFcst3hour() {return fcst3hour;}
            public fcstdaily getFcstdaily() {return fcstdaily;}

            public class fcst3hour {

                @SerializedName("sky") sky sky;
                @SerializedName("precipitation") precipitation precipitation;


                public class sky{
                    @SerializedName("name10hour")
                    String name10hour;
                    @SerializedName("name31hour")
                    String name31hour;
                    @SerializedName("name46hour")
                    String name46hour;

                    public String getName10hour() {return name10hour;}
                    public String getName31hour() {return name31hour;}
                    public String getName46hour() {return name46hour;}
                }

                public class precipitation{ // 강수 정보
                    @SerializedName("prob10hour")
                    String prob10hour;    // 강수확률
                    @SerializedName("prob31hour")
                    String prob31hour;    // 강수확률
                    @SerializedName("prob46hour")
                    String prob46hour;    // 강수확률

                    public String getprob10hour() {return prob10hour;}
                    public String getprob31hour() {return prob31hour;}
                    public String getprob46hour() {return prob46hour;}
                }


                public forecast3days.fcst3hour.precipitation getPrecipitation() {return precipitation;}
                public forecast3days.fcst3hour.sky getSky() {return sky;}
            }

            public class fcstdaily{

                @SerializedName("temperature") temperature temperature;

                public class temperature{
                    @SerializedName("tmax1day")
                    String tmax1day;
                    @SerializedName("tmax2day")
                    String tmax2day;
                    @SerializedName("tmax3day")
                    String tmax3day;
                    @SerializedName("tmin1day")
                    String tmin1day;
                    @SerializedName("tmin2day")
                    String tmin2day;
                    @SerializedName("tmin3day")
                    String tmin3day;

                    public String getTmax1day() {return tmax1day;}
                    public String getTmin1day() {return tmin1day;}
                    public String getTmax2day() {return tmax2day;}
                    public String getTmin2day() {return tmin2day;}
                    public String getTmax3day() {return tmax3day;}
                    public String getTmin3day() {return tmin3day;}
                }

                public fcstdaily.temperature getTemperature() {return temperature;}
            }

        }
    }
    public Result getResult() {return result;}
    public weather getWeather() {return weather;}

    public interface WeatherApiInterface {
        @Headers({"Accept: application/json", "appKey:b42a1814-4abc-36c2-a743-43c5f81cd73d"})
        @GET("weather/forecast/3days")
        Call<WeatherThreeDayRepo> get_Weather_3day(@Query("version") int version,
                                                   @Query("lat") String lat, @Query("lon") String lon);
    }
}