package com.example.myshop.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.myshop.R;
import com.example.myshop.bean.ShoppingCart;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by thank on 2016/8/13.
 */
public class OrderWareAdapter extends MySimpleAdapter<ShoppingCart>{

    public OrderWareAdapter(Context mContext, List<ShoppingCart> mDatas) {
        super(mContext, mDatas, R.layout.template_order_wares);
    }

    @Override
    public void onBindViewData(MyViewHolder holder, ShoppingCart cart) {

        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.draweeview);
        draweeView.setImageURI(Uri.parse(cart.getImgUrl()));
    }

    //通过检索列表中的商品数量，计算出总的价格
    public float getTotalPrice() {
        float sum = 0;
        if (isNull())
            return sum;

        for (ShoppingCart cart : mDatas) {
            if (cart.isChecked())
                sum += cart.getCount() * cart.getPrice();
        }

        return sum;
    }

    //判断数据是否为空
    private boolean isNull(){

        return !(mDatas !=null && mDatas.size()>0);
    }

    //设置列表复选按钮全选状态
    public void setCheckAll(boolean isChecked) {
        int i = 0;
        for (ShoppingCart cart : mDatas) {
            cart.setChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }

    }
}
