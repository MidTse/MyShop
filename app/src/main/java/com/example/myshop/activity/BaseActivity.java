package com.example.myshop.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.myshop.MyShopApplication;
import com.example.myshop.R;
import com.example.myshop.bean.User;

/**
 * Created by thank on 2016/8/6.
 */
public class BaseActivity extends AppCompatActivity{

    public void startActivity(Intent intent, boolean isVerify) {

        if (isVerify) {
            User user = MyShopApplication.getInstance().getUser();
            if (user != null) {

                super.startActivity(intent);

            } else {

                MyShopApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else {

            super.startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.tab_mine));
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
