package com.sharewisesc.inbroadheart.retrofit;

import com.orhanobut.logger.Logger;
import com.sharewisesc.inbroadheart.InBroadcastHeartApplication;
import com.sharewisesc.inbroadheart.utils.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggingInterceptor implements Interceptor {

    private static final String F_BREAK = " %n";
    private static final String F_URL = " %s";
    private static final String F_TIME = " in %.1fms";
    private static final String F_HEADERS = "%s";
    private static final String F_RESPONSE = F_BREAK + "Response: %d";
    private static final String F_BODY = "body: %s";

    private static final String F_BREAKER = F_BREAK + "-------------------------------------------" + F_BREAK;
    private static final String F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS;
    private static final String F_RESPONSE_WITHOUT_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BREAKER;
    private static final String F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK;
    private static final String F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BODY + F_BREAK + F_BREAKER;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

//        统一添加请求消息头
//        Request.Builder requestBuilder = request.newBuilder();
//        Request signedRequest = requestBuilder
//                .addHeader("Source", "1")
//                .addHeader("Channel", AppUtil.getChanel(context))
//                .addHeader("IMEI", AppUtil.getImeiCode(context))
//                .addHeader("UserId", QicheChaorenApplication.getInstance().getUserId() + "")
//                .addHeader("Version", AppUtil.getAppVersionName(context))
//                .addHeader("Net", "" + NetWorkUtil.getNetWorkType(context))
//                .addHeader("Token", TOKEN)
//                .addHeader("SessionId", "" + QicheChaorenApplication.getInstance().getSessionId())
//                .build();
//        long t1 = System.nanoTime();
//        Response response = chain.proceed(signedRequest);

        if (!NetUtils.isNetworkReachable(InBroadcastHeartApplication.getContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        MediaType contentType = null;
        String bodyString = null;
        if (response.body() != null) {
            contentType = response.body().contentType();
            bodyString = response.body().string();
        }
        double time = (t2 - t1) / 1e6d;
        switch (request.method()) {
            case "GET":
                Logger.d(String.format("GET " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), response.code(), response.headers(), stringifyResponseBody(bodyString)));
                break;
            case "POST":
                Logger.d(String.format("POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), stringifyRequestBody(request), response.code(), response.headers(), stringifyResponseBody(bodyString)));
                break;
            case "PUT":
                Logger.d(String.format("PUT " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), request.body().toString(), response.code(), response.headers(), stringifyResponseBody(bodyString)));
                break;
            case "DELETE":
                Logger.d(String.format("DELETE " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITHOUT_BODY, request.url(), time, request.headers(), response.code(), response.headers()));
                break;
        }

        if (NetUtils.isNetworkReachable(InBroadcastHeartApplication.getContext())) {
            int maxAge = 0;
            // 有网络时 设置缓存超时时间0个小时
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            // 无网络时，设置超时为1周
            int maxStale = 60 * 60 * 24 * 28;
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }

        if (response.body() != null && bodyString != null) {
            ResponseBody body = ResponseBody.create(contentType, bodyString);
            return response.newBuilder().body(body).build();
        } else {
            return response;
        }
    }


    private static String stringifyRequestBody(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public String stringifyResponseBody(String responseBody) {
        return responseBody;
    }
}