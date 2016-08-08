package com.example.myshop.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.SMSSDK;

/**
 * Created by thank on 2016/8/8.
 */
public class SMSUtil {

    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";

    private Context mContext;

//    private static SMSUtil sms;
//    public static SMSUtil getInstance(Context context) {
//        if (sms != null) {
//            return sms;
//        } else {
//            sms = new SMSUtil(context);
//            return sms;
//        }
//    }

    public SMSUtil(Context context) {
        this.mContext = context;
    }



    /****
     * 检查号码的有效性，目前仅限于中国地区手机号码
     * @phone 手机号码
     * @code 地区区号
     ***/
    public void checkPhoneNum(String phone, String code) {
        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(mContext, "请输入手机号码");
            return;
        }

        if (code == "86") {
            if(phone.length() != 11) {
                ToastUtils.show(mContext,"手机号码长度不对");
                return;
            }

        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            ToastUtils.show(mContext,"您输入的手机号码格式不正确");
            return;
        }
    }

    /****
     * 获取MCC
     ***/
    private String getMCC() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        // 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
        String networkOperator = tm.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            return networkOperator;
        }

        // 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
        return tm.getSimOperator();
    }

    /****
     * 通过MCC获取当前的城市区号及城市名称
     ***/
    public String[] getCurrentCountrys() {
        String mcc = getMCC();
        String[] countrys = null;
        if (!TextUtils.isEmpty(mcc)) {
            countrys = SMSSDK.getCountryByMCC(mcc);
        }

        if (countrys == null) {
            Log.w("SMSSDK", "no country found by MCC: " + mcc);
            countrys = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        }
        return countrys;
    }

    /****
     * 打印当前城市的信息以及号码规则
     ***/
    public void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
        // 解析国家列表
        for (HashMap<String, Object> country : countries) {
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue;
            }

            Log.d("SMSUtil","code="+code + "rule="+rule);


        }

    }




}
