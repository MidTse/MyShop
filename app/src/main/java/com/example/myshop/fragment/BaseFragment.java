package com.example.myshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myshop.MyShopApplication;
import com.example.myshop.activity.LoginActivity;
import com.example.myshop.bean.User;

/**
 * Created by thank on 2016/8/6.
 */
public class BaseFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void startActivity(Intent intent, boolean isVerify) {

        if (isVerify) {
            User user = MyShopApplication.getInstance().getUser();
            if (user != null) {

                super.startActivity(intent);

            } else {

                MyShopApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else {

            super.startActivity(intent);
        }
    }
}
