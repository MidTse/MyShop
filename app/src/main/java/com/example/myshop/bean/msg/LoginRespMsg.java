package com.example.myshop.bean.msg;

/**
 * Created by hjh on 2016/8/5.
 */
public class LoginRespMsg<T> extends BaseRespMsg {

    private String token;
    private T data;

    public void setData(T data) {
        this.data = data;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public String getToken() {
        return token;
    }
}
