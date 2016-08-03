package com.example.myshop.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.myshop.R;
import com.example.myshop.bean.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Thank on 2016/2/17.
 */
public class WaresAdapter extends MySimpleAdapter<Wares> {

    public WaresAdapter(Context mContext, List<Wares> mDatas) {

        super(mContext, mDatas, R.layout.template_grid_wares );
    }

    @Override
    public void onBindViewData(MyViewHolder holder, Wares item) {

        holder.getTextView(R.id.text_title).setText(item.getName());
        holder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.draweeview);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

    }
}
