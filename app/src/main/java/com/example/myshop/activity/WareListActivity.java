package com.example.myshop.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.example.myshop.Contants;
import com.example.myshop.R;
import com.example.myshop.adapter.HotWareAdapter;
import com.example.myshop.adapter.decortion.DividerItemDecoration;
import com.example.myshop.bean.Page;
import com.example.myshop.bean.Wares;
import com.example.myshop.utils.Pager;
import com.example.myshop.widget.MyToolbar;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class WareListActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
        View.OnClickListener,Pager.OnPageListener<Wares>{

    private MaterialRefreshLayout mRefreshLayout;
    private RecyclerView mRecylerView;
    private TextView mSummary;
    private TabLayout mTabLaout;
    private MyToolbar mToolbar;

    private Pager pager;
    private HotWareAdapter mAdapter;

    private static final int TAG_DEFAULT = 0;
    private static final int TAG_SALE = 1;
    private static final int TAG_PRICE = 2;
    private static final int ACTION_GRID = 0;
    private static final int ACTION_LIST = 1;

    private int orderBy = 0;
    private long campaignId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warelist);

        campaignId=getIntent().getLongExtra(Contants.COMPAINGAIN_ID,0);
        initView();
        initEvent();
        requestData();

    }


    private void initView() {

        mRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        mRecylerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSummary = (TextView) findViewById(R.id.txt_summary);
        mTabLaout = (TabLayout) findViewById(R.id.tablayout);
        mToolbar = (MyToolbar) findViewById(R.id.toolbar);

    }

    private void initEvent() {

        //初始化Toolbar的各项状态，并监听控件事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WareListActivity.this.finish();
            }
        });
        mToolbar.setRightButtonIcon(R.mipmap.icon_grid_32);
        mToolbar.getRightButton().setTag(ACTION_LIST);
        mToolbar.getRightButton().setOnClickListener(this);

        //初始化Tablayout中的tab,并监听tab事件
        buildTablayout(R.string.tablayout_default, TAG_DEFAULT);
        buildTablayout(R.string.tablayout_sale, TAG_SALE);
        buildTablayout(R.string.tablayout_price, TAG_PRICE);
        mTabLaout.setOnTabSelectedListener(this);

        //


    }

    /**
     * 请求服务端数据
     */
    private void requestData() {

        pager = Pager.newBuilder()
                .setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .setParam("campaignId",campaignId)
                .setParam("orderBy",orderBy)
                .setCanLoadMore(true)
                .setPageListener(this)
                .setRefreshLayout(mRefreshLayout)
                .build(this, new TypeToken<Page<Wares>>(){}.getType());


        pager.request();

    }

    /**
     * 建立Tablayout中的Tab
     */
    private void buildTablayout(int txtId, int tag) {
        TabLayout.Tab tab = mTabLaout.newTab();
        tab.setText(getResources().getString(txtId));
        tab.setTag(tag);
        mTabLaout.addTab(tab);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pager.putParam("orderBy", orderBy);
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View view) {
        int action = (int) view.getTag();
        switch (action) {
            case ACTION_LIST:
                mToolbar.setRightButtonIcon(R.mipmap.icon_list_32);
                mToolbar.getRightButton().setTag(ACTION_GRID);

                mAdapter.resetLayout(R.layout.template_grid_wares);
                mRecylerView.setLayoutManager(new GridLayoutManager(this, 2));
                mRecylerView.setAdapter(mAdapter);
                break;
            case ACTION_GRID:
                mToolbar.setRightButtonIcon(R.mipmap.icon_grid_32);
                mToolbar.getRightButton().setTag(ACTION_LIST);

                mAdapter.resetLayout(R.layout.template_hot_wares);
                mRecylerView.setLayoutManager(new LinearLayoutManager(this));
                mRecylerView.setAdapter(mAdapter);
                break;
            default:
                break;
        }

    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        mSummary.setText("总共有："+ totalCount + "件商品");
        if (mAdapter == null) {
            mAdapter = new HotWareAdapter(this, datas);
            mRecylerView.setAdapter(mAdapter);
            mRecylerView.setLayoutManager(new LinearLayoutManager(this));
            mRecylerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mRecylerView.setItemAnimator(new DefaultItemAnimator());
        } else {

            mAdapter.clear();
            mAdapter.addData(datas);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mAdapter.clear();
        mAdapter.addData(datas);
        mRecylerView.scrollToPosition(0);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        mAdapter.addData(mAdapter.getDatas().size(),datas);
        mRecylerView.scrollToPosition(mAdapter.getDatas().size());
        mAdapter.notifyDataSetChanged();
    }
}
