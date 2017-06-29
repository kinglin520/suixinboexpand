package com.sharewisesc.inbroadheart.api.controller;

import com.sharewisesc.inbroadheart.api.ApiFactory;
import com.sharewisesc.inbroadheart.api.request.UserRequest;
import com.sharewisesc.inbroadheart.api.response.UserResponse;
import com.sharewisesc.inbroadheart.api.service.ApiService;
import com.sharewisesc.inbroadheart.entity.UserModel;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wenlin on 2017/6/15.
 */

public class ApiController {

    private static final ApiService apiService = ApiFactory.getApiService();

    /**
     * 登录
     *
     * @param name
     * @param password
     */
    public static Observable<UserModel> login(String name, String password) {
        UserRequest userRequest = new UserRequest();
        userRequest.passwd = password;
        userRequest.user_id = name;
        userRequest.MEID = "dfsdfdsfdsfds";
//        userRequest.MEID = Constants.getPhoneIMEID();
        return handThread(apiService.login(userRequest)).map(new Func1<UserResponse, UserModel>() {
            @Override
            public UserModel call(UserResponse userResponse) {
                return userResponse.result;
            }
        });
    }

    private static <T> Observable<T> handThread(Observable<T> obs) {
        return obs.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}

