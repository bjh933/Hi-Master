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

public class WeatherWeekRepo {

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

        public List<forecast6days> forecast6days = new ArrayList<>();
        public List<forecast6days> getForecast6days() {return forecast6days;}


        public class forecast6days {
            @SerializedName("sky")
            sky sky;
            @SerializedName("temperature")
            temperature temperature;

            public sky getSky() {return sky;}
            public temperature getTemperature() {return temperature;}

            public class sky {

                @SerializedName("pmName3day")
                String pmName3day;
                @SerializedName("pmName4day")
                String pmName4day;
                @SerializedName("pmName5day")
                String pmName5day;
                @SerializedName("pmName6day")
                String pmName6day;
                @SerializedName("pmName7day")
                String pmName7day;

                public String getPmName3day() {
                    return pmName3day;
                }

                public String getPmName4day() {
                    return pmName4day;
                }

                public String getPmName5day() {
                    return pmName5day;
                }

                public String getPmName6day() {
                    return pmName6day;
                }

                public String getPmName7day() {
                    return pmName7day;
                }
            }

            public class temperature
            {

                @SerializedName("tmax3day")
                String tmax3day;
                @SerializedName("tmin3day")
                String tmin3day;
                @SerializedName("tmax4day")
                String tmax4day;
                @SerializedName("tmin4day")
                String tmin4day;
                @SerializedName("tmax5day")
                String tmax5day;
                @SerializedName("tmin5day")
                String tmin5day;
                @SerializedName("tmax6day")
                String tmax6day;
                @SerializedName("tmin6day")
                String tmin6day;
                @SerializedName("tmax7day")
                String tmax7day;
                @SerializedName("tmin7day")
                String tmin7day;

                public String getTmax3day() {
                    return tmax3day;
                }

                public String getTmin3day() {
                    return tmin3day;
                }

                public String getTmax4day() {
                    return tmax4day;
                }

                public String getTmin4day() {
                    return tmin4day;
                }

                public String getTmax5day() {
                    return tmax5day;
                }

                public String getTmin5day() {return tmin5day;}

                public String getTmax6day() {return tmax6day;}

                public String getTmin6day() {
                    return tmin6day;
                }

                public String getTmax7day() {
                    return tmax7day;
                }

                public String getTmin7day() {
                    return tmin7day;
                }

            }

        }

    }
    public Result getResult() {return result;}
    public weather getWeather() {return weather;}

    public interface WeatherApiInterface {
        @Headers({"Accept: application/json", "appKey:b42a1814-4abc-36c2-a743-43c5f81cd73d"})
        @GET("weather/forecast/6days")
        Call<WeatherWeekRepo> get_Weather_6day(@Query("version") int version, @Query("lat") String lat, @Query("lon") String lon);
    }
}