package com.example.myshop.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myshop.Contants;
import com.example.myshop.R;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.utils.CountTimerView;
import com.example.myshop.utils.SMSUtil;
import com.example.myshop.utils.ToastUtils;
import com.example.myshop.widget.ClearEditText;
import com.example.myshop.widget.MyToolbar;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

public class RegSecondActivity extends AppCompatActivity implements View.OnClickListener{

    private MyToolbar mToolBar;
    private Button mBtnReSend;
    private TextView mTxtTip;
    private ClearEditText mEditCode;

    private CountTimerView countTimerView;
    private SMSHandler smsHandler;
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    private String phone;
    private String pwd;
    private String countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_second);

        SMSSDK.initSDK(this, Contants.SMS_KEY, Contants.SMS_SECRET);


        getIntentData();
        initView();
        initEvent();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        phone = intent.getStringExtra(Contants.REG_PHONE);
        Log.i("RegSecondActivity", phone);
        pwd = intent.getStringExtra(Contants.REG_PWD);
        countryCode = intent.getStringExtra(Contants.REG_COUNTRY_CODE);
    }

    private void initView() {
        mToolBar = (MyToolbar) findViewById(R.id.toolbar);
        mBtnReSend = (Button) findViewById(R.id.btn_reSend);
        mTxtTip = (TextView) findViewById(R.id.txt_tip);
        mEditCode = (ClearEditText) findViewById(R.id.edit_code);
    }

    private void initEvent() {
        mToolBar.getmLeftButton().setOnClickListener(this);
        mToolBar.setRightButtonOnClickListener(this);

        smsHandler = new SMSHandler();
        SMSSDK.registerEventHandler(smsHandler);

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);
        String text = getString(R.string.smssdk_send_mobile_detail)+formatedPhone;
        mTxtTip.setText(Html.fromHtml(text));

        countTimerView = new CountTimerView(mBtnReSend);
        countTimerView.start();

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.toolbar_leftButton:
                onBackPressed();
                break;
            case R.id.toolbar_rightButton:
                submitCode();
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

    /** 分割电话号码 */
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }

    /** 提交验证码***/
    private void submitCode() {
        String code = mEditCode.getText().toString().trim();

        if (TextUtils.isEmpty(code)) {
            ToastUtils.show(this, "验证码不能为空");
        }
        SMSSDK.submitVerificationCode(countryCode, phone, code);
    }

    /** 提交注册信息***/
    private void submitReg() {

        ToastUtils.show(this, "已经注册");
    }



    /****
     * ******************声明内部类SMSHanler****************************
     ***/
    class SMSHandler extends EventHandler {

        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            submitReg();
                        }

                    }else {
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
