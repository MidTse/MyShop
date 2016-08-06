package com.example.myshop.fragment;

import android.content.Intent;
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
import com.example.myshop.activity.WareDetailActivity;
import com.example.myshop.adapter.HotWareAdapter;
import com.example.myshop.adapter.MyBaseAdapter;
import com.example.myshop.adapter.decortion.CardViewtemDecortion;
import com.example.myshop.bean.Page;
import com.example.myshop.bean.Wares;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.http.SpotsCallBack;
import com.example.myshop.utils.Pager;
import com.example.myshop.utils.ToastUtils;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HotFragment extends BaseFragment implements Pager.OnPageListener<Wares>{

    private MaterialRefreshLayout mRefreshlayout;
    private RecyclerView mRecyclerview;
    private HotWareAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_hot,container,false);
        mRefreshlayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recycler);
        initData();
        return view ;

    }

    private void initData() {

        Pager.newBuilder()
                .setCanLoadMore(true)
                .setPageListener(this)
                .setRefreshLayout(mRefreshlayout)
                .setUrl(Contants.API.WARES_HOT_URL)
                .build(getContext(), new TypeToken<Page<Wares>>() {}.getType())
                .request();
    }

    @Override
    public void load(final List<Wares> wares, int totalPage, int totalCount) {
        mAdapter = new HotWareAdapter(getContext(), wares);
        mAdapter.setOnItemClickListener(new MyBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Wares ware = wares.get(position);
                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                intent.putExtra(Contants.WARE, ware);
                startActivity(intent);
            }
        });
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.addItemDecoration(new CardViewtemDecortion());
    }

    @Override
    public void refresh(List<Wares> wares, int totalPage, int totalCount) {
        mAdapter.clear();
        mAdapter.addData(wares);
        mRecyclerview.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> wares, int totalPage, int totalCount) {
        mAdapter.addData(mAdapter.getDatas().size(), wares);
        mRecyclerview.scrollToPosition(mAdapter.getDatas().size());
    }
}
