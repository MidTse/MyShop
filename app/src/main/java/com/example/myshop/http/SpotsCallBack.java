package com.example.myshop.http;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by Thank on 2015/12/20.
 */
public abstract class SpotsCallBack<T> extends BaseCallback<T> {

    private Context mContext;

    public SpotsCallBack(Context mContext) {

        this.mContext = mContext;
    }

    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {
        Log.i("SpotsCallBack:failure", "" + e);
    }

    @Override
    public void onResponse(Response response) {
        Log.i("SpotsCallBack:reponse", "" + response.code());
    }

    @Override
    public void onTokenError(Response response, int code) {
        Log.i("SpotsCallBack:token", code + "");
    }
}
