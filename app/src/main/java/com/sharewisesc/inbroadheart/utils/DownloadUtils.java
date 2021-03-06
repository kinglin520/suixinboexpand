package com.sharewisesc.inbroadheart.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

public class DownloadUtils {
    private OkHttpClient mOkHttpClient;

    public DownloadUtils() {
        mOkHttpClient = new OkHttpClient();
    }

    /**
     * 声明一个被观察者对象作为结果返回
     *
     * @param path
     * @return
     */
    public Observable<byte[]> downloadFile(final String path) {

        return Observable.create(new Observable.OnSubscribe<byte[]>() {
            @Override
            public void call(final Subscriber<? super byte[]> subscriber) {

                if (!subscriber.isUnsubscribed()) {
                    //访问网络操作
                    Request request = new Request.Builder().url(path).build();
                    mOkHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                byte[] data = response.body().bytes();
                                if (data != null) {
                                    subscriber.onNext(data);
                                }
                            }
                            subscriber.onCompleted();
                        }
                    });
                }
            }
        });
    }
}
