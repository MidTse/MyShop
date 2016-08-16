package com.example.myshop.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myshop.Contants;
import com.example.myshop.R;
import com.example.myshop.bean.Address;
import com.example.myshop.bean.Wares;
import com.example.myshop.widget.ClearEditText;
import com.example.myshop.widget.MyToolbar;

import java.io.Serializable;

public class CreateAddrActivity extends AppCompatActivity {

    private MyToolbar mToolbar;
    private ClearEditText editName;
    private ClearEditText editPhone;
    private TextView txtRegion;
    private ClearEditText editAddress;

    private Address mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_addr);

        getIntentData();

    }

    /**
     * 获取Intent传递过来的值
     * */
    private void getIntentData() {
        Serializable data = getIntent().getSerializableExtra(Contants.ADDRESS);

        initView();

        if (data == null) {

        } else {
            mAddress = (Address) data;


        }
    }


//    private void setAddrValue() {
//        editName.setText(mAddress.getConsignee());
//        editPhone.setText(mAddress.getPhone());
//        editAddress
//    }

    private void initView() {
        mToolbar = (MyToolbar) findViewById(R.id.toolbar);
        editName = (ClearEditText) findViewById(R.id.edit_name);
        editPhone = (ClearEditText) findViewById(R.id.edit_phone);
        editAddress = (ClearEditText) findViewById(R.id.edit_address);
        txtRegion = (TextView) findViewById(R.id.txt_region);

    }


}
