package com.example.myshop.bean.order;

import com.example.myshop.bean.Address;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Xhz on 2016/10/18.
 */

public class Order implements Serializable {

    public static final int STATUS_SUCCESS=1; //支付成功的订单
    public static final int STATUS_PAY_FAIL=-2; //支付失败的订单
    public static final int STATUS_PAY_WAIT=0; //：待支付的订单

    private Long id;
    private String orderNum;
    private Long createdTime;
    private Float amount;
    private int  status;
    private List<OrderItem> items;
    private Address address;

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public Float getAmount() {
        return amount;
    }

    public int getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Address getAddress() {
        return address;
    }
}
