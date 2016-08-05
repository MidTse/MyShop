package com.example.myshop.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myshop.Contants;
import com.example.myshop.MyShopApplication;
import com.example.myshop.R;
import com.example.myshop.bean.User;
import com.example.myshop.bean.msg.LoginRespMsg;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.http.SpotsCallBack;
import com.example.myshop.utils.DESUtil;
import com.example.myshop.utils.ToastUtils;
import com.example.myshop.widget.ClearEditText;
import com.example.myshop.widget.MyToolbar;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private MyToolbar mToolbar;
    private ClearEditText edit_phone, edit_pwd;
    private Button btn_login;
    private TextView txt_reg;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initEvent();
    }

    private void initView() {

        mToolbar = (MyToolbar) findViewById(R.id.toolbar);
        edit_phone = (ClearEditText) findViewById(R.id.edit_phone);
        edit_pwd = (ClearEditText) findViewById(R.id.edit_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
        txt_reg = (TextView) findViewById(R.id.txt_toReg);
    }

    private void initEvent() {

        mToolbar.getmLeftButton().setOnClickListener(this);
        btn_login.setOnClickListener(this);
        txt_reg.setOnClickListener(this);
    }

    private void toLogin() {
        String phone = edit_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入电话号码");
            return;
        }

        String pwd = edit_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(this, "请输入密码");
            return;
        }

        Map<String, String> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));
        mHttpHelper.doPost(Contants.API.LOGIN, params, new SpotsCallBack<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                if (userLoginRespMsg != null) {

                    MyShopApplication application = MyShopApplication.getInstance();
                    application.putUserData(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                    if(application.getIntent() == null){
                        setResult(RESULT_OK);
                        finish();
                    }else{

                        application.jumpToTargetActivity(LoginActivity.this);
                        finish();

                    }
                }


            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.btn_login:
                toLogin();
                break;
            case R.id.toolbar_leftButton:
                onBackPressed();
                break;
            case R.id.txt_toReg:
                break;
            default:
                break;
        }
    }
}
