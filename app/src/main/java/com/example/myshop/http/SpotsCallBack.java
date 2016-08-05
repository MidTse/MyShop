package com.example.myshop.http;

import android.content.Context;

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

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onTokenError(Response response, int code) {

    }
}
