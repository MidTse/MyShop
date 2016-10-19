package com.example.myshop.bean.order;

import com.example.myshop.bean.Wares;

import java.io.Serializable;

/**
 * Created by Xhz on 2016/10/18.
 */

public class OrderItem implements Serializable {

    private Long id;
    private Float amount;
    private Wares wares;

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setWares(Wares wares) {
        this.wares = wares;
    }

    public Long getId() {
        return id;
    }

    public Float getAmount() {
        return amount;
    }

    public Wares getWares() {
        return wares;
    }
}
