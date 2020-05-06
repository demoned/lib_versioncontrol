package com.bojun.version.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.bojun.version.core.http.AllenHttp;
import com.bojun.version.core.http.HttpRequestMethod;
import com.bojun.version.AllenVersionChecker;
import com.bojun.version.builder.DownloadBuilder;
import com.bojun.version.builder.RequestVersionBuilder;
import com.bojun.version.builder.UIData;
import com.bojun.version.callback.RequestVersionListener;

import java.io.IOException;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestVersionManager {
    private Handler handler = new Handler(Looper.getMainLooper());

    public static RequestVersionManager getInstance() {
        return Holder.instance;
    }

    public static class Holder {
        static RequestVersionManager instance = new RequestVersionManager();

    }

    /**
     * 请求版本接口
     */
    public void requestVersion(final DownloadBuilder builder, final Context context) {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                RequestVersionBuilder requestVersionBuilder = builder.getRequestVersionBuilder();
                OkHttpClient client = AllenHttp.getHttpClient();
                HttpRequestMethod requestMethod = requestVersionBuilder.getRequestMethod();
                Request request = null;
                switch (requestMethod) {
                    case GET:
                        request = AllenHttp.get(requestVersionBuilder).build();
                        break;
                    case POST:
                        request = AllenHttp.post(requestVersionBuilder).build();
                        break;
                    case POSTJSON:
                        request = AllenHttp.postJson(requestVersionBuilder).build();
                        break;
                }
                final RequestVersionListener requestVersionListener = requestVersionBuilder.getRequestVersionListener();
                if (requestVersionListener != null) {
                    try {
                        final Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            final String result = response.body() != null ? response.body().string() : null;
                            post(new Runnable() {
                                @Override
                                public void run() {

                                    UIData versionBundle = requestVersionListener.onRequestVersionSuccess(builder, result);
                                    if (versionBundle != null) {
                                        builder.setVersionBundle(versionBundle);
                                        builder.download(context);
                                    }
                                }


                            });
                        } else {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    requestVersionListener.onRequestVersionFailure(response.message());
                                    AllenVersionChecker.getInstance().cancelAllMission();
                                }
                            });
                        }
                    } catch (final IOException e) {
                        e.printStackTrace();
                        post(new Runnable() {
                            @Override
                            public void run() {
                                requestVersionListener.onRequestVersionFailure(e.getMessage());
                                AllenVersionChecker.getInstance().cancelAllMission();
                            }
                        });
                    }
                } else {
                    throw new RuntimeException("using request version function,you must set a requestVersionListener");
                }
            }
        });

    }

    private void post(Runnable r) {
        handler.post(r);
    }
}
