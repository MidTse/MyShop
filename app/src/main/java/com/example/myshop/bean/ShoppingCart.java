package com.example.myshop.bean;

/**
 * Created by Thank on 2016/2/24.
 */
public class ShoppingCart extends Wares {

    private int count;
    private boolean isChecked=true;

    public boolean isChecked() {
        return isChecked;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
