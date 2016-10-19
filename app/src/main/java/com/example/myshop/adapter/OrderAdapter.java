package com.example.myshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myshop.R;
import com.example.myshop.bean.order.Order;
import com.example.myshop.bean.order.OrderItem;
import com.squareup.picasso.Picasso;
import com.w4lle.library.NineGridAdapter;
import com.w4lle.library.NineGridlayout;

import java.util.List;

/**
 * Created by Xhz on 2016/10/18.
 */

public class OrderAdapter extends MySimpleAdapter<Order> {

    public OrderAdapter(Context mContext, List<Order> mDatas) {
        super(mContext, mDatas, R.layout.template_order);
    }

    @Override
    public void onBindViewData(MyViewHolder holder, Order item) {
        holder.getTextView(R.id.txt_order_price).setText("实付金额：¥"+item.getAmount());

        TextView txtStatus = holder.getTextView(R.id.txt_order_status);
        switch (item.getStatus()) {
            case Order.STATUS_PAY_FAIL:
                txtStatus.setText("支付失败");
                txtStatus.setTextColor(Color.parseColor("#ffF44336"));
                break;

            case Order.STATUS_PAY_WAIT:
                txtStatus.setText("等待支付");
                txtStatus.setTextColor(Color.parseColor("#ffFFEB3B"));
                break;

            case Order.STATUS_SUCCESS:
                txtStatus.setText("支付成功");
                txtStatus.setTextColor(Color.parseColor("#ff4CAF50"));
                break;

            default:
                break;
        }

        NineGridlayout nineGridlayout = (NineGridlayout) holder.getView(R.id.NineGridlayout);
        nineGridlayout.setGap(5);
        nineGridlayout.setDefaultWidth(36);
        nineGridlayout.setDefaultHeight(36);

        nineGridlayout.setAdapter(new OrderItemAdapter(mContext, item.getItems()));
    }

    class OrderItemAdapter extends NineGridAdapter {

        private  List<OrderItem> items;

        public OrderItemAdapter(Context context, List<OrderItem> items) {
            super(context, items);
            this.items = items;
        }

        @Override
        public int getCount() {
            return (items == null) ? 0 : items.size();
        }

        @Override
        public String getUrl(int positopn) {
            OrderItem item = items.get(positopn);

            return item.getWares().getImgUrl();
        }

        @Override
        public OrderItem getItem(int position) {
            return (items == null) ? null : items.get(position);
        }

        @Override
        public long getItemId(int position) {
            OrderItem item = getItem(position);
            return item==null?0:item.getId();
        }

        @Override
        public View getView(int i, View view) {

            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
            Picasso.with(context).load(getUrl(i)).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(iv);
            return iv;
        }
    }


}
