package com.sharewisesc.inbroadheart.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharewisesc.inbroadheart.InBroadcastHeartApplication;
import com.sharewisesc.inbroadheart.utils.FileUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wenlin on 2017/2/16.
 */

public class RetrofitManager {

    public static final String Host = "http://www.baidu.com/";
    private static RetrofitManager instance;
    private static Retrofit retrofit;
    private static Gson mGson;
    private static String cookie = "";

    private RetrofitManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Host)
                .client(httpClient())
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    public static void reset() {
        instance = null;
    }

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                instance = new RetrofitManager();
            }
        }
        return instance;
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }

    private static OkHttpClient httpClient() {
        File cacheFile = new File(FileUtil.getAppCacheDir(InBroadcastHeartApplication.getContext()), "/HttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);
//        Interceptor cacheInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                if (!NetUtils.isNetworkReachable(App.getContext())) {
//                    request = request.newBuilder()
//                            .cacheControl(CacheControl.FORCE_CACHE)
//                            .build();
//                }
//                Response response = chain.proceed(request);
//                if (NetUtils.isNetworkReachable(App.getContext())) {
//                    int maxAge = 0;
//                    // 有网络时 设置缓存超时时间0个小时
//                    response.newBuilder()
//                            .removeHeader("Pragma")
//                            .header("Cache-Control", "public, max-age=" + maxAge)
//                            .build();
//                } else {
//                    // 无网络时，设置超时为1周
//                    int maxStale = 60 * 60 * 24 * 28;
//                    response.newBuilder()
//                            .removeHeader("Pragma")
//                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                            .build();
//                }
//                return response;
//            }
//        };
        return new OkHttpClient.Builder()
                .cache(cache).addInterceptor(new LoggingInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    public static Gson gson() {
        if (mGson == null) {
            synchronized (RetrofitManager.class) {
                mGson = new GsonBuilder().setLenient().create();
            }
        }
        return mGson;
    }
}
