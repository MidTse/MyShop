package com.example.myshop.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.myshop.R;
import com.example.myshop.bean.Address;
import com.example.myshop.utils.ToastUtils;

import java.util.List;

/**
 * Created by LittleDay on 2016/8/16.
 */
public class AddrListAdapter extends MySimpleAdapter<Address>{
    private AddressListener addressListener;


    public AddrListAdapter(Context mContext, List<Address> mDatas, AddressListener listener) {
        super(mContext, mDatas, R.layout.template_address);
        this.addressListener = listener;
    }

    @Override
    public void onBindViewData(MyViewHolder holder, final Address item) {
        Address mAddress = item;
        Log.i("address", item.getConsignee());
        holder.getTextView(R.id.txt_consignee).setText(item.getConsignee());
        holder.getTextView(R.id.txt_phone).setText(replacePhoneNum(item.getPhone()));
        holder.getTextView(R.id.txt_address).setText(item.getAddr());
        CheckBox checkBox = (CheckBox) holder.getView(R.id.chk_default);

        holder.getTextView(R.id.txt_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressListener != null) {
                    addressListener.eidtAddr(item);
                }
            }
        });

        holder.getTextView(R.id.txt_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressListener != null) {

                    addressListener.delAddr(item);
                }
            }
        });

        boolean isDefault = item.getIsDefault();
        if (isDefault) {

            checkBox.setText("默认地址");

        } else {
            checkBox.setClickable(true);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked && addressListener != null) {
                        item.setIsDefault(true);
                        addressListener.setDefault(item);
                    }
                }
            });
        }
    }

    //替换手机号码格式
    private String replacePhoneNum(String phone){
        if (phone.length() != 11) {

            ToastUtils.show(mContext, "手机号格式不正确");
        }
        return phone.substring(0,phone.length()-(phone.substring(3)).length())+"****"+phone.substring(7);
    }


    public void deleteItem(Address address) {

        int position = mDatas.indexOf(address);
        mDatas.remove(position);
        notifyItemRemoved(position);

    }


    public interface AddressListener {

        public void setDefault(Address address);

        public void eidtAddr(final Address address);

        public void delAddr(Address address);
    }
}
