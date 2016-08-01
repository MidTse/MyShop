package com.example.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.myshop.Contants;
import com.example.myshop.R;
import com.example.myshop.adapter.HotWareAdapter;
import com.example.myshop.adapter.decortion.CardViewtemDecortion;
import com.example.myshop.bean.Page;
import com.example.myshop.bean.Wares;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.http.SpotsCallBack;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HotFragment extends Fragment{

    private MaterialRefreshLayout mRefreshlayout;
    private RecyclerView mRecyclerview;

    private List<Wares> mWares;
    private HotWareAdapter mAdapter;

    private int currPage=1;
    private int totalPage=1;
    private int pageSize=10;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int mState = STATE_NORMAL;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_hot,container,false);

        mRefreshlayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recycler);

        initRefreshLayout();

        requestWares();

        return view ;

    }

    private void initRefreshLayout() {
        mRefreshlayout.setLoadMore(true);
        mRefreshlayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (currPage <= totalPage) {
                    loadmoreData();
                } else {
                    Toast.makeText(getContext(), "已经加载完毕，没有更多了哦！！",Toast.LENGTH_SHORT).show();
                    mRefreshlayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void requestWares() {

        Map<String, String> map = new HashMap<>();
        map.put("curPage", "" + currPage);
        map.put("pageSize", "" + pageSize);

        mHttpHelper.doPost(Contants.API.WARES_HOT_URL, map, new SpotsCallBack<Page<Wares>>(getContext()) {


            @Override
            public void onSuccess(Response response, Page<Wares> datas) {
                mWares = datas.getList();
                currPage = datas.getCurrentPage();
                totalPage = datas.getTotalPage();

                showData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void refreshData() {
        currPage = 1;
        mState = STATE_REFRESH;
        requestWares();
    }

    private void loadmoreData() {
        currPage = ++currPage;
        mState = STATE_MORE;
        requestWares();
    }

    private void showData() {


        switch (mState) {
            case STATE_NORMAL:

                mAdapter = new HotWareAdapter(getContext(), mWares);
                mRecyclerview.setAdapter(mAdapter);
                mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerview.setItemAnimator(new DefaultItemAnimator());
                mRecyclerview.addItemDecoration(new CardViewtemDecortion());
                break;

            case STATE_REFRESH:

                mAdapter.clear();
                mAdapter.addData(mWares);

                mRecyclerview.scrollToPosition(0);
                mRefreshlayout.finishRefresh();
                break;

            case STATE_MORE:

                mAdapter.addData(mAdapter.getDatas().size(), mWares);
                mRecyclerview.scrollToPosition(mAdapter.getDatas().size());

                mRefreshlayout.finishRefreshLoadMore();
                break;
        }

    }





}
