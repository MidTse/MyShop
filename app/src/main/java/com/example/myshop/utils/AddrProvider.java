package com.example.myshop.utils;

import android.content.Context;
import android.util.SparseArray;

import com.example.myshop.bean.LocalAddress;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LittleDay on 2016/8/17.
 */
public class AddrProvider {

    public static final String ADDR_JSON="addr_json";
    private SparseArray<LocalAddress> datas = null;
    private Context mContext;

    private static AddrProvider mProvider;
    public static AddrProvider getInstance(Context context) {
        if (mProvider != null) {
            return mProvider;
        } else {
            mProvider = new AddrProvider(context);
            return mProvider;
        }

    }

    private AddrProvider(Context mContext) {
        this.mContext = mContext;
        datas = new SparseArray<>(10);
        listTosparse();
    }

    public void put(LocalAddress address) {

        datas.put(address.getId().intValue(), address);
        commit();

    }

    public void delete(LocalAddress address) {
        datas.delete(address.getId().intValue());
        commit();
    }

    public void update(LocalAddress address) {

    }

    public LocalAddress get(Long id) {

        return get(id.intValue());
    }

    public LocalAddress get(int id) {

        return datas.get(id);
    }

    //执行存入本地操作
    public void commit() {
        List<LocalAddress> carts = sparseToList();
        PreferencesUtils.putString(mContext, ADDR_JSON, JSONUtil.toJSON(carts));
    }

    //将sparseArray转换为容器数据
    private List<LocalAddress> sparseToList() {
        int size = datas.size();
        List<LocalAddress> list = new ArrayList<>(size);

        for (int i=0; i<size; i++) {
            list.add(datas.valueAt(i));
        }

        return list;

    }

    //将容器数据转换为sparseArray
    private void listTosparse() {
        List<LocalAddress> addressList = getDataFromLocal();
        if (addressList != null && addressList.size() > 0) {

            for (LocalAddress address : addressList) {
                datas.put(address.getId().intValue(), address);
            }
        }

    }

    //获取本地存储的数据
    public List<LocalAddress> getDataFromLocal() {
        String json = PreferencesUtils.getString(mContext,ADDR_JSON);
        List<LocalAddress> addressList =null;
        if (json != null) {

            addressList = JSONUtil.getGson().fromJson(json, new TypeToken<List<LocalAddress>>(){}.getType());
        }

        return addressList;
    }




}
