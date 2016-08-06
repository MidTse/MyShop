package com.example.myshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.myshop.R;
import com.example.myshop.activity.BuildOrderActivity;
import com.example.myshop.adapter.CartAdapter;
import com.example.myshop.adapter.decortion.DividerItemDecoration;
import com.example.myshop.bean.ShoppingCart;
import com.example.myshop.utils.CartProvider;
import com.example.myshop.utils.ToastUtils;
import com.example.myshop.widget.MyToolbar;

import java.util.List;


public class CartFragment extends BaseFragment implements View.OnClickListener{

    public static final int ACTION_EDIT=1;
    public static final int ACTION_CAMPLATE=2;

    private RecyclerView mRecyclerView;
    private CheckBox mCheckBox;
    private TextView mTextTotal;
    private Button mBtnOrder;
    private Button mBtnDel;
    protected MyToolbar mToolbar;

    private CartProvider mCartProvider;
    private CartAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart,container,false);
        initView(view);
        mCartProvider = CartProvider.getInstance(getContext());
        showData();
        return  view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_all);
        mTextTotal = (TextView) view.findViewById(R.id.txt_total);

        mBtnOrder = (Button) view.findViewById(R.id.btn_order);
        mBtnOrder.setOnClickListener(this);
        mBtnDel = (Button) view.findViewById(R.id.btn_del);
        mBtnDel.setOnClickListener(this);

        mToolbar = (MyToolbar) view.findViewById(R.id.toolbar);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
        mToolbar.setRightButtonOnClickListener(this);

    }

    private void showData() {

        List<ShoppingCart> datas = mCartProvider.getAll();
        mAdapter = new CartAdapter(getContext(), datas, mCheckBox, mTextTotal);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    public void refData() {

        mAdapter.clear();
        List<ShoppingCart> carts = mCartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.isCheckAll();
        mAdapter.showPrice();
        hideDelCtrl();

    }

    @Override
    public void onClick(View v) {

//        if (v.getId() == R.id.btn_del) {
//
//            mAdapter.delItem();
//
//        } else {
//            int action = (int) v.getTag();
//            if (action == ACTION_EDIT) {
//
//                showDelCtrl();
//
//            } else if (action == ACTION_CAMPLATE) {
//
//                hideDelCtrl();
//            }
//
//        }

        int viewId = v.getId();
        switch (viewId) {

            case R.id.btn_del:
                mAdapter.delItem();
                break;
            case R.id.btn_order:

                Intent intent = new Intent(getActivity(), BuildOrderActivity.class);
                startActivity(intent, true);
                break;
            default:
                int action = (int) v.getTag();
                if (action == ACTION_EDIT) {

                    showDelCtrl();

                } else if (action == ACTION_CAMPLATE) {

                    hideDelCtrl();
                }
                break;
        }


    }

    //显示编辑功能
    private void showDelCtrl() {

        mToolbar.setRightButtonText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);

        mAdapter.setCheckAll(false);
        mCheckBox.setChecked(false);
    }

    //隐藏编辑功能
    private void hideDelCtrl() {

        mToolbar.setRightButtonText("编辑");
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);
        mBtnDel.setVisibility(View.GONE);
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.setCheckAll(true);
        mAdapter.showPrice();
        mCheckBox.setChecked(true);
    }


}
