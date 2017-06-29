package com.sharewisesc.inbroadheart.api;

import com.sharewisesc.inbroadheart.api.service.ApiService;
import com.sharewisesc.inbroadheart.retrofit.RetrofitManager;

/**
 * Created by wenlin on 2017/6/15.
 */
public class ApiFactory {
    protected static final Object monitor = new Object();

    private static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            synchronized (monitor) {
                apiService = RetrofitManager.getInstance().create(ApiService.class);
            }
        }
        return apiService;
    }


    public static void reset() {
        apiService = null;
    }
}
