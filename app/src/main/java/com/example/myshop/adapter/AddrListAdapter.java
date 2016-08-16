package com.example.myshop.adapter;

import android.content.Context;
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
public class AddrListAdapter extends MySimpleAdapter<Address> implements View.OnClickListener{
    private AddressListener addressListener;
    private Address mAddress;

    public AddrListAdapter(Context mContext, List<Address> mDatas, AddressListener listener) {
        super(mContext, mDatas, R.layout.template_address);
        this.addressListener = listener;
    }

    @Override
    public void onBindViewData(MyViewHolder holder, final Address item) {
        mAddress = item;
        holder.getTextView(R.id.txt_consignee).setText(item.getConsignee());
        holder.getTextView(R.id.txt_phone).setText(replacePhoneNum(item.getPhone()));
        holder.getTextView(R.id.txt_address).setText(item.getAddr());
        CheckBox checkBox = (CheckBox) holder.getView(R.id.chk_default);

        holder.getTextView(R.id.txt_edit).setOnClickListener(this);
        holder.getTextView(R.id.txt_del).setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (addressListener != null) {
            switch (viewId) {

                case R.id.txt_edit:
                    addressListener.eidtAddr(mAddress);
                    break;

                case R.id.txt_del:
                    addressListener.delAddr(mAddress);
                    break;

                default:
                    break;

            }
        }

    }

    public interface AddressListener {

        public void setDefault(Address address);

        public void eidtAddr(Address address);

        public void delAddr(Address address);
    }
}
