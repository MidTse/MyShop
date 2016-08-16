package com.example.myshop;

/**
 * Created by Thank on 2015/12/21.
 */
public class Contants {

    public static final String  COMPAINGAIN_ID="compaigin_id";
    public static final String  WARE="ware";
    public static final String ADDRESS = "address";
    public  static final String DES_KEY="Cniao5_123456";

    public static final String USER_JSON="user_json";
    public static final String TOKEN="token";
    public  static final int REQUEST_CODE=0;

    public static final String SMS_KEY = "15d6d1cb54dab";
    public static final String SMS_SECRET = "a49e1a16c5eb9460b6db56537897a821";

    public static final String REG_PHONE = "phone";
    public static final String REG_PWD = "pwd";
    public static final String REG_COUNTRY_CODE = "countrycode";

    public  static final int REQUEST_CODE_PAYMENT = 1;

    public static final String ORDER_SUCCESS = "success";
    public static final String ORDER_FAIL = "fail";
    public static final String ORDER_CANCEL = "cancel";
    public static final String ORDER_STATUS = "order_status";
    public static final int NUM_SUCCESS = 1;
    public static final int NUM_FAIL = -1;
    public static final int NUM_CANCEL = -2;

    public static class API {

        public static final String BASE_URL = "http://112.124.22.238:8081/course_api/";
        public static final String BANNER_URL = BASE_URL + "banner/query";
        public static final String CAMPAIGN_HOME_URL = BASE_URL + "campaign/recommend";
        public static final String WARES_HOT_URL = BASE_URL + "wares/hot";
        public static final String CATEGORY_LIST_URL = BASE_URL +"category/list";
        public static final String WARES_LIST = BASE_URL +"wares/list";
        public static final String WARES_CAMPAIN_LIST = BASE_URL +"wares/campaign/list";

        public static final String WARES_DETAIL=BASE_URL +"wares/detail.html";
        public static final String LOGIN=BASE_URL +"auth/login";

        public static final String ORDER_CREATE = BASE_URL +"/order/create";
        public static final String ORDER_COMPLEPE = BASE_URL +"/order/complete";

        public static final String ADDRESS_LIST=BASE_URL +"addr/list";
        public static final String ADDRESS_CREATE=BASE_URL +"addr/create";
        public static final String ADDRESS_UPDATE=BASE_URL +"addr/update";

    }
}
