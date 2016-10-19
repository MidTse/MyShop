package com.example.myshop.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.myshop.Contants;
import com.example.myshop.MyShopApplication;
import com.example.myshop.R;
import com.example.myshop.adapter.OrderAdapter;
import com.example.myshop.adapter.decortion.CardViewtemDecortion;
import com.example.myshop.bean.order.Order;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.http.TokenCallBack;
import com.example.myshop.widget.MyToolbar;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderListActivity extends AppCompatActivity implements View.OnClickListener,TabLayout.OnTabSelectedListener{

    public static final int STATUS_ALL=1000;
    public static final int STATUS_SUCCESS=1; //支付成功的订单
    public static final int STATUS_PAY_FAIL=-2; //支付失败的订单
    public static final int STATUS_PAY_WAIT=0; //：待支付的订单
    private int status = STATUS_ALL;

    private MyToolbar mToolbar;
    private TabLayout mTabLayout;
    private RecyclerView mRecyclerView;

    private OrderAdapter mAdapter;
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    private String token;
    private Long userId;
    private Map<String, Object> params;
    private String TAG = "OrderListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);

        initData();
        requestData();

    }

    private void initData() {
        mToolbar = (MyToolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);

        mToolbar.getmLeftButton().setOnClickListener(this);

        token = MyShopApplication.getInstance().getToken();
        userId = MyShopApplication.getInstance().getUser().getId();

        initTab();
    }

    private void initTab() {

        TabLayout.Tab tab= mTabLayout.newTab();
        tab.setText("全部");
        tab.setTag(STATUS_ALL);
        mTabLayout.addTab(tab);


        tab= mTabLayout.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        mTabLayout.addTab(tab);

        tab= mTabLayout.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        mTabLayout.addTab(tab);

        tab= mTabLayout.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FAIL);
        mTabLayout.addTab(tab);

        mTabLayout.setOnTabSelectedListener(this);

    }

    private void requestData() {

        if (params == null) {
            params = new HashMap<>();
        } else {
            params.clear();
        }

        params.put("user_id", "" + userId);
        params.put("status","" + status);
        params.put("token", token);

        mHttpHelper.doPost(Contants.API.ORDER_LIST, params, new TokenCallBack<List<Order>>(this) {

            @Override
            public void onSuccess(Response response, List<Order> orders) {
                showDatas(orders);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, "code:" + code);
            }
        });
    }

    private void showDatas(List<Order> orders) {

        if (mAdapter == null) {

            mAdapter = new OrderAdapter(this, orders);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new CardViewtemDecortion());

        } else {

            mAdapter.refreshData(orders);
            mAdapter.notifyDataSetChanged();

        }
    }


    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        status = (int) tab.getTag();
        requestData();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
