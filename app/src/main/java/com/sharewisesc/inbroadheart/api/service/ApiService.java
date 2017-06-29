package com.sharewisesc.inbroadheart.api.service;

import com.sharewisesc.inbroadheart.api.request.UserRequest;
import com.sharewisesc.inbroadheart.api.response.UserResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wenlin on 2017/6/15.
 */

public interface ApiService {
    @POST("bjqboa/post/?userLogin")
    Observable<UserResponse> login(@Body UserRequest userRequest);

    @GET("bjqboa/get/?getUserInfo")
    Observable<UserResponse> getUserInfo(@Query("user_id") String userId);
}
