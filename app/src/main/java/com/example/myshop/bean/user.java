package com.example.myshop.bean;

import java.io.Serializable;

/**
 * Created by hjh on 2016/8/5.
 */
public class User implements Serializable {

    private long id;
    private String email;
    private String logo_url;
    private String username;
    private String mobi;

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMobi(String mobi) {
        this.mobi = mobi;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public String getUsername() {
        return username;
    }

    public String getMobi() {
        return mobi;
    }
}
