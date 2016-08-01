package com.example.myshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.myshop.R;
import com.example.myshop.bean.ShoppingCart;
import com.example.myshop.utils.CartProvider;
import com.example.myshop.widget.NumberAddSubView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Thank on 2016/2/26.
 */
public class CartAdapter extends MySimpleAdapter<ShoppingCart> implements MyBaseAdapter.OnItemClickListener,View.OnClickListener {

    private CheckBox mCheckBox;
    private TextView mTextTotal;

    private CartProvider mProvider;

    public CartAdapter(Context mContext, List<ShoppingCart> mDatas, CheckBox checkBox, TextView textView) {
        super(mContext, mDatas, R.layout.template_cart);

        this.mCheckBox = checkBox;
        this.mTextTotal = textView;

        mProvider = new CartProvider(mContext);
        setOnItemClickListener(this);
        mCheckBox.setOnClickListener(this);

    }

    @Override
    public void onBindViewData(MyViewHolder holder, final ShoppingCart item) {

        holder.getTextView(R.id.text_title).setText(item.getName());
        holder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        CheckBox checkBox = (CheckBox) holder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());

        NumberAddSubView numberAddSubView = (NumberAddSubView) holder.getView(R.id.num_control);
        numberAddSubView.setValue(item.getCount());

        //监听按钮加减对价格改变的事件
        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                item.setCount(value);
                mProvider.update(item);
                showPrice();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                item.setCount(value);
                mProvider.update(item);
                showPrice();
            }
        });


    }

    //通过检索列表中的商品数量，计算出总的价格
    private float getTotalPrice() {
        float sum = 0;
        if (isNull())
            return sum;

        for (ShoppingCart cart : mDatas) {
            if (cart.isChecked())
                sum += cart.getCount() * cart.getPrice();
        }

        return sum;
    }

    //显示总的价格
    public void showPrice() {
        float total = getTotalPrice();
        mTextTotal.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }


    @Override
    public void onItemClick(View view, int position) {

        ShoppingCart cart = getItem(position);
        cart.setChecked(!cart.isChecked());
        notifyItemChanged(position);

        isCheckAll();
        showPrice();
    }

    //判断是否为全选
    public void isCheckAll() {
        int count = 0;
        int checknum = 0;
        if (mDatas != null) {
            count = mDatas.size();

            for (ShoppingCart cart : mDatas) {
                if (!cart.isChecked()) {
                    mCheckBox.setChecked(false);
                    break;
                } else {
                    checknum += 1;
                }

            }

            if (count == checknum) {
                mCheckBox.setChecked(true);
            }
        }
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

    public void delItem() {
        if (isNull())
            return;

        for (Iterator iterator = mDatas.iterator(); iterator.hasNext();) {
            ShoppingCart cart = (ShoppingCart) iterator.next();
            if (cart.isChecked()) {

                int position = mDatas.indexOf(cart);

                mProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }
    }

    //设置全选按钮的监听
    @Override
    public void onClick(View v) {
        if (isNull())
            return;

        setCheckAll(this.mCheckBox.isChecked());
        showPrice();
    }
}
