package com.example.myshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myshop.Contants;
import com.example.myshop.R;
import com.example.myshop.utils.ManifestUtil;
import com.example.myshop.utils.SMSUtil;
import com.example.myshop.widget.ClearEditText;
import com.example.myshop.widget.MyToolbar;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

public class RegActivity extends AppCompatActivity implements View.OnClickListener{

    private MyToolbar mToolBar;
    private ClearEditText editPwd, editPhone;
    private TextView txtCountryCode, txtCountry;

    private String phone, pwd, code;
    private SMSHandler smsHandler;
    private SMSUtil smsUtil;

    private static String TAG = "RegActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        SMSSDK.initSDK(this, Contants.SMS_KEY, Contants.SMS_SECRET);
        smsUtil = new SMSUtil(this);

        initView();

        initEvent();
    }

    private void initView() {
        mToolBar = (MyToolbar) findViewById(R.id.toolbar);
        editPhone = (ClearEditText) findViewById(R.id.edit_phone);
        editPwd = (ClearEditText) findViewById(R.id.edit_pwd);
        txtCountryCode = (TextView) findViewById(R.id.txtCountryCode);
        txtCountry = (TextView) findViewById(R.id.txtCountry);
    }

    private void initEvent() {

        mToolBar.getmLeftButton().setOnClickListener(this);
        mToolBar.setRightButtonOnClickListener(this);

        smsHandler = new SMSHandler();
        SMSSDK.registerEventHandler(smsHandler);
        String[] countrys = smsUtil.getCurrentCountrys();
        if (countrys != null) {

            txtCountryCode.setText("+" + countrys[1]);
            txtCountry.setText(countrys[0]);
        }


    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.toolbar_leftButton:
                onBackPressed();
                break;
            case R.id.toolbar_rightButton:
                getsmsCode();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       SMSSDK.unregisterEventHandler(smsHandler);
    }

    /** 开始请求短信验证码*/
    private void getsmsCode() {
        phone = editPhone.getText().toString().trim().replaceAll("\\s*", "");
        Log.i(TAG, phone);
        code = txtCountryCode.getText().toString().trim();
        pwd = editPwd.getText().toString().trim();

        smsUtil.checkPhoneNum(phone, code);

        SMSSDK.getVerificationCode(code, phone);

    }

    /** 请求验证码后，跳转到验证码填写页面 */
    private void afterVerificationCodeRequested(boolean smart) {

        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        Intent intent = new Intent(this,RegSecondActivity.class);
        Log.i(TAG,"cc"+phone);
        intent.putExtra(Contants.REG_PHONE, phone);
        intent.putExtra(Contants.REG_PWD, pwd);
        intent.putExtra(Contants.REG_COUNTRY_CODE, code);
        startActivity(intent);

    }

    /****
     * 声明内部类SMSHanler
     ***/
     class SMSHandler extends EventHandler {

        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            // 请求支持国家列表
                            //onCountryListGot((ArrayList<HashMap<String, Object>>) data);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            // 请求验证码后，跳转到验证码填写页面
                            boolean smart = (Boolean) data;
                            Log.i(TAG, "" + smart);
                            afterVerificationCodeRequested(smart);
                        }
                    } else {
                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
//                                ToastUtils.show(RegActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }

                    }
                }
            });
        }
    }


}
