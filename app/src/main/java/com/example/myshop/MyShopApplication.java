package com.example.myshop;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.myshop.bean.User;
import com.example.myshop.utils.UserLocalData;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Thank on 2016/1/3.
 */
public class MyShopApplication extends Application {

    private User user;
    private  Intent intent;
    private static  MyShopApplication mInstance;


    public static  MyShopApplication getInstance(){

        return  mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initUser();

        Fresco.initialize(this);
    }


    private void initUser() {
        this.user = UserLocalData.getUser(this);
    }

    public User getUser() {
        return this.user;
    }

    public String getToken() {
        return UserLocalData.getToken(this);
    }

    public void putUserData(User user, String token) {
        this.user = user;
        UserLocalData.putUser(this, user);
        UserLocalData.putToken(this, token);
    }

    public void clearUserData() {
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }


    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){

        context.startActivity(intent);
        this.intent =null;
    }
}
