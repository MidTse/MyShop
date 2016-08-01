package com.example.myshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thank on 2016/1/3.
 */
public abstract class MyBaseAdapter<T, H extends MyViewHolder> extends RecyclerView.Adapter<MyViewHolder> {
    protected List<T> mDatas;

    protected Context mContext;

    protected int mLayoutResId;

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public MyBaseAdapter(Context mContext, List<T> datas, int mLayoutResId) {
        this.mContext = mContext;
        this.mDatas = datas == null ? new ArrayList<T>() : datas;
        this.mLayoutResId = mLayoutResId;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).
                inflate(mLayoutResId, parent, false);
        return new MyViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        T item = getItem(position);
        onBindViewData((H)holder, item);
    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() <= 0) {
            return 0;
        }
        return mDatas.size();
    }

    public T getItem(int position) {
        if (position >= mDatas.size()) return null;
        return mDatas.get(position);
    }

    public void clear() {
        int itemCount = mDatas.size();
        mDatas.clear();
        this.notifyItemRangeRemoved(0, itemCount);
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void addData(List<T> datas) {
        addData(0, datas);
    }

    public void addData(int position, List<T> datas) {
        if (datas != null && datas.size() > 0) {
            this.mDatas.addAll(datas);
            this.notifyItemRangeChanged(position, datas.size());
        }
    }

    public abstract void onBindViewData(H holder, T item);

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
