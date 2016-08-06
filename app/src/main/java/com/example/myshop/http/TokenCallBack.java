package com.example.myshop.http;

import android.content.Context;
import android.content.Intent;

import com.example.myshop.MyShopApplication;
import com.example.myshop.R;
import com.example.myshop.activity.LoginActivity;
import com.example.myshop.utils.ToastUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by thank on 2016/8/6.
 */
public abstract class TokenCallBack<T> extends BaseCallback<T>{

    protected Context mContext;

    public TokenCallBack(Context mContext) {
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
        ToastUtils.show(mContext, mContext.getString(R.string.token_error));

        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        MyShopApplication.getInstance().clearUserData();
    }
}
