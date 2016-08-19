package com.example.myshop.bean;

/**
 * Created by Thank on 2016/1/11.
 */
public class Category extends BaseBean {
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
