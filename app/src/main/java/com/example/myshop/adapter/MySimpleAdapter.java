package com.example.myshop.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by Thank on 2016/1/3.
 */
public abstract class MySimpleAdapter<T> extends MyBaseAdapter<T, MyViewHolder> {

    public MySimpleAdapter(Context mContext, List<T> mDatas, int mLayoutResId) {
        super(mContext, mDatas, mLayoutResId);
    }


}
