package com.example.myshop.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Thank on 2015/12/19.
 */
public class OkHttpHelper {

    public static final String TAG="OkHttpHelper";

    public static final int TOKEN_MISSING=401;// token 丢失
    public static final int TOKEN_ERROR=402; // token 错误
    public static final int TOKEN_EXPIRE=403; // token 过期

    private static OkHttpHelper mInstance;
    private OkHttpClient mHttpClient;
    private Gson mGson;
    private Handler mHandler;

    static {
        mInstance = new OkHttpHelper();
    }

    public static OkHttpHelper getInstance() {
        return mInstance;
    }

    private OkHttpHelper() {
        mHttpClient = new OkHttpClient();
        mHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mHttpClient.setReadTimeout(10,TimeUnit.SECONDS);
        mHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);

        mHandler = new Handler(Looper.getMainLooper());

        mGson = new Gson();
    }

    public void doGet(String url, BaseCallback callback) {

        Request request = buildRequest(url, HttpMethodType.GET, null);
        doRequest(request, callback);

    }

    public void doTokenGet(String u) {

    }

    public void doPost(String url, Map param, BaseCallback callback) {

        Request request = buildRequest(url, HttpMethodType.POST, param);
        doRequest(request, callback);

    }



    public void doRequest(final Request request, final BaseCallback callback) {

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callbackFailure(callback, request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callbackResponse(callback, response);
                if (response.isSuccessful()) {
                    String result = response.body().string();

                    if (callback.mType == String.class) {
                        callbackSuccess(callback, response, result);
                    }else {
                        try {
                            Object object = mGson.fromJson(result, callback.mType);
                            callbackSuccess(callback, response, object);
                        }catch (com.google.gson.JsonParseException e) {
                            callbackError(callback, response, e);
                        }

                    }
                }
                else if (response.code() == TOKEN_MISSING || response.code() == TOKEN_ERROR
                        || response.code() == TOKEN_EXPIRE) {

                    callbackTokenError(callback, response);

                } else {
                    callbackError(callback, response, null);
                }

            }
        });

    }

    private Request buildRequest(String url, HttpMethodType methodType, Map<String,String> param) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        if (methodType == HttpMethodType.GET) {

            builder.get();
        }
        else if (methodType == HttpMethodType.POST) {

            builder.post(buildRequestBody(param));

        }
        return builder.build();

    }

    private RequestBody buildRequestBody (Map<String, String> params) {
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();

        if (params != null) {

            for (Map.Entry<String,String> entry : params.entrySet()) {

//                formEncodingBuilder.add(entry.getKey(), entry.getValue());
                formEncodingBuilder.add(entry.getKey(),entry.getValue()==null?"":entry.getValue().toString());
            }

        }

        return formEncodingBuilder.build();
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object object) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, object);
            }
        });
    }

    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }

    private void callbackResponse(final BaseCallback callback, final Response response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }

    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }

    private void callbackTokenError(final BaseCallback callback, final Response response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response, response.code());
            }
        });
    }

    enum HttpMethodType{

        GET,
        POST,

    }
}
