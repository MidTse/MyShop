package com.example.myshop.adapter;

import android.content.Context;

import com.example.myshop.R;
import com.example.myshop.bean.Category;

import java.util.List;

/**
 * Created by Thank on 2016/1/21.
 */
public class CategoryAdapter extends MySimpleAdapter<Category> {

    public CategoryAdapter(Context mContext, List<Category> datas) {
        super(mContext, datas, R.layout.template_single_text);
    }

    @Override
    public void onBindViewData(MyViewHolder holder, Category item) {

        holder.getTextView(R.id.textView).setText(item.getName());

    }
}
