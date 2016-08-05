package com.example.myshop.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.myshop.Contants;
import com.example.myshop.bean.User;

/**
 * Created by thank on 2016/8/5.
 */
public class UserLocalData {

    public static void putUser(Context context, User user) {
        String user_json = JSONUtil.toJSON(user);
        PreferencesUtils.putString(context, Contants.USER_JSON, user_json);
    }

    public static void putToken(Context context, String token) {
        PreferencesUtils.putString(context, Contants.TOKEN, token);
    }

    public static User getUser(Context context) {

        String user_json = PreferencesUtils.getString(context, Contants.USER_JSON);
        if (!TextUtils.isEmpty(user_json)) {
            return JSONUtil.fromJson(user_json, User.class);
        }
        return null;
    }

    public static String getToken(Context context) {

        String token = PreferencesUtils.getString(context, Contants.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            return token;
        }
        return null;

    }

    public static void clearUser(Context context) {

        PreferencesUtils.putString(context, Contants.USER_JSON, "");
    }

    public static void clearToken(Context context) {

        PreferencesUtils.putString(context, Contants.TOKEN, "");
    }
}
