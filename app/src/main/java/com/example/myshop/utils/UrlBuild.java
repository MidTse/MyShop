package com.example.myshop.utils;

import java.util.Map;

/**
 * Created by thank on 2016/8/6.
 */
public class UrlBuild {

    public static String buildUrlParams(String url ,Map<String,Object> params) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }

        if(url.indexOf("?")>0){
            url = url +"&"+s;
        }else{
            url = url +"?"+s;
        }

        return url;
    }


}
