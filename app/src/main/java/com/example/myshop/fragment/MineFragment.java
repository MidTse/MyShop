package com.example.myshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myshop.Contants;
import com.example.myshop.MyShopApplication;
import com.example.myshop.R;
import com.example.myshop.activity.AddrListActivity;
import com.example.myshop.activity.LoginActivity;
import com.example.myshop.bean.User;
import com.example.myshop.utils.ToastUtils;
import com.squareup.picasso.Picasso;


/**
 * Created by Ivan on 15/9/22.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private ImageView img_head;
    private TextView txt_username;
    private Button btn_loginout;
    private TextView txtLocation;
    private TextView txtFavorite;
    private TextView txtOrders;
    private TextView txtMsg;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        initVeiw(view);
        initEvent();

        User user = MyShopApplication.getInstance().getUser();
        showUser(user);
        return view;
    }

    private void initVeiw(View view) {

        img_head = (ImageView) view.findViewById(R.id.img_head);
        txt_username = (TextView) view.findViewById(R.id.txt_username);
        btn_loginout = (Button) view.findViewById(R.id.btn_loginout);
        txtOrders = (TextView) view.findViewById(R.id.txt_orders);
        txtFavorite = (TextView) view.findViewById(R.id.txt_favorite);
        txtLocation = (TextView) view.findViewById(R.id.txt_location);
        txtMsg = (TextView) view.findViewById(R.id.txt_msg);
    }

    private void initEvent() {

        img_head.setOnClickListener(this);
        txt_username.setOnClickListener(this);
        btn_loginout.setOnClickListener(this);
        txtOrders.setOnClickListener(this);
        txtFavorite.setOnClickListener(this);
        txtLocation.setOnClickListener(this);
        txtMsg.setOnClickListener(this);
    }

    private void skipToLogin() {

        if (MyShopApplication.getInstance().getUser() == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, Contants.REQUEST_CODE);
        }
    }

    private void showUser(User user) {

        if (user != null) {
            String url = user.getLogo_url();
            if (!TextUtils.isEmpty(url)) {
                Picasso.with(getContext()).load(url).into(img_head);
                txt_username.setText(user.getUsername());
                btn_loginout.setVisibility(View.VISIBLE);
            }
        } else {
            img_head.setImageResource(R.drawable.default_head);
            txt_username.setText(R.string.tologin);
            btn_loginout.setVisibility(View.GONE);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        User user =  MyShopApplication.getInstance().getUser();
        showUser(user);
    }

    @Override
    public void onClick(View view) {

        int viewId = view.getId();
        switch (viewId) {

            case R.id.img_head:
            case R.id.txt_username:
                skipToLogin();
                break;
            case R.id.btn_loginout:

                MyShopApplication.getInstance().clearUserData();
                showUser(null);
                break;
            case R.id.txt_location:
                Intent intent = new Intent(getActivity(), AddrListActivity.class);
                startActivity(intent);
                break;
        }
    }


}
