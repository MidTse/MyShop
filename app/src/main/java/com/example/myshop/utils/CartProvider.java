package com.example.myshop.utils;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.example.myshop.bean.ShoppingCart;
import com.example.myshop.bean.Wares;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thank on 2016/2/24.
 */
public class CartProvider {

    public static final String CART_JSON="cart_json";
    private SparseArray<ShoppingCart> datas = null;
    private Context mContext;

    private static CartProvider mProvider;
    public static CartProvider getInstance(Context context) {
        if (mProvider != null) {
            return mProvider;
        } else {
            mProvider = new CartProvider(context);
            return mProvider;
        }

    }

    private CartProvider(Context mContext) {
        this.mContext = mContext;
        datas = new SparseArray<>(10);
        listTosparse();
    }

    //向购物车选项添加数据
    public void put(ShoppingCart cart) {

        ShoppingCart temp = datas.get(cart.getId().intValue());
        if (temp != null) {
           temp.setCount(temp.getCount() + 1);
        }else {
            temp = cart;
            temp.setCount(1);
        }

        datas.put(cart.getId().intValue(), temp);
        commit();
    }
    public void put(Wares wares) {
        put(convertData(wares));
    }

    //向购物车选项更新数据
    public void update(ShoppingCart cart) {
        datas.put(cart.getId().intValue(), cart);
        commit();
    }

    //向购物车选项删除数据
    public void delete(ShoppingCart cart) {
        datas.delete(cart.getId().intValue());
        commit();
    }

    //向购物车选项获取数据
    public List<ShoppingCart> getAll() {
        return getDataFromLocal();
    }

    //执行存入本地操作
    public void commit() {
        List<ShoppingCart> carts = sparseToList();
        PreferencesUtils.putString(mContext,CART_JSON,JSONUtil.toJSON(carts));
    }


    //将sparseArray转换为容器数据
    private List<ShoppingCart> sparseToList() {
        int size = datas.size();
        List<ShoppingCart> list = new ArrayList<>(size);

        for (int i=0; i<size; i++) {
            list.add(datas.valueAt(i));
        }

        return list;

    }

    //将容器数据转换为sparseArray
    private void listTosparse() {
        List<ShoppingCart> carts = getDataFromLocal();
        if (carts != null && carts.size() > 0) {

            for (ShoppingCart cart : carts) {
                datas.put(cart.getId().intValue(), cart);
            }
        }

    }

    //获取本地存储的数据
    private List<ShoppingCart> getDataFromLocal() {
        String json = PreferencesUtils.getString(mContext,CART_JSON);
        List<ShoppingCart> carts =null;
        if (json != null) {

            carts = JSONUtil.getGson().fromJson(json, new TypeToken<List<ShoppingCart>>(){}.getType());
        }

        return carts;
    }

    private ShoppingCart convertData(Wares item) {
        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }
}
