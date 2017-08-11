package com.example.a1.himaster.Service;

import com.example.a1.himaster.Model.Schedule;

import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by a1 on 2017. 7. 10..
 */

public interface ContentService {

    @GET("/home")
    Call<Schedule> repo(@Query("userId") String userId, @Query("date") Timestamp date);

}
