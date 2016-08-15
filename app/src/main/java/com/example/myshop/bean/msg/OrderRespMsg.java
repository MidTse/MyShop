package com.example.myshop.bean.msg;


import com.example.myshop.bean.Charge;

import java.io.Serializable;

public class OrderRespMsg implements Serializable{

    private String orderNum;

    private Charge charge;


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

}