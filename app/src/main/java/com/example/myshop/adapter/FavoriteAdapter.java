package com.example.myshop.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.myshop.R;
import com.example.myshop.bean.Favorites;
import com.example.myshop.bean.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Xhz on 2016/10/18.
 */

public class FavoriteAdapter extends MySimpleAdapter<Favorites> {

    public FavoriteAdapter(Context mContext, List<Favorites> mDatas) {
        super(mContext, mDatas, R.layout.template_favorite);
    }

    @Override
    public void onBindViewData(MyViewHolder holder, Favorites item) {

        Wares wares = item.getWares();

        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.draweeview);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        holder.getTextView(R.id.txt_fav_title).setText(wares.getDescription());
        holder.getTextView(R.id.txt_fav_price).setText("¥:"+ wares.getPrice());
    }
}
