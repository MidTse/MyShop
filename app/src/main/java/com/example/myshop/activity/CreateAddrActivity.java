package com.example.myshop.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.myshop.Contants;
import com.example.myshop.MyShopApplication;
import com.example.myshop.R;
import com.example.myshop.bean.Address;
import com.example.myshop.bean.LocalAddress;
import com.example.myshop.bean.city.CityModel;
import com.example.myshop.bean.city.DistrictModel;
import com.example.myshop.bean.city.ProvinceModel;
import com.example.myshop.bean.msg.BaseRespMsg;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.http.TokenCallBack;
import com.example.myshop.utils.AddrProvider;
import com.example.myshop.utils.ToastUtils;
import com.example.myshop.utils.XmlParserHandler;
import com.example.myshop.widget.ClearEditText;
import com.example.myshop.widget.MyToolbar;
import com.squareup.okhttp.Response;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class CreateAddrActivity extends AppCompatActivity implements View.OnClickListener{

    private MyToolbar mToolbar;
    private ClearEditText editName;
    private ClearEditText editPhone;
    private TextView txtRegion;
    private ClearEditText editAddress;
    private OptionsPickerView mCityPikerView;

    private Address mAddress;
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    private List<ProvinceModel> mProvinces;
    private ArrayList<ArrayList<String>> mCities = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<ArrayList<ArrayList<String>>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_addr);

        initProvinceDatas();
        getIntentData();
        initEvent();

    }

    /**
     * 获取Intent传递过来的值
     * */
    private void getIntentData() {
        Serializable data = getIntent().getSerializableExtra(Contants.ADDRESS);

        initView();

        if (data != null) {

            mAddress = (Address) data;
            setAddrValue();
        }
    }

    //进入编辑界面后的未修改选项
    private void setAddrValue() {

        editName.setText(mAddress.getConsignee());
        editPhone.setText(mAddress.getPhone());
        editAddress.setText(mAddress.getAddr());
        txtRegion.setText(mAddress.getAddr());
    }

    private void initView() {
        mToolbar = (MyToolbar) findViewById(R.id.toolbar);
        editName = (ClearEditText) findViewById(R.id.edit_name);
        editPhone = (ClearEditText) findViewById(R.id.edit_phone);
        editAddress = (ClearEditText) findViewById(R.id.edit_address);
        txtRegion = (TextView) findViewById(R.id.txt_region);
        mCityPikerView = new OptionsPickerView(this);

    }

    private void initEvent() {

        mToolbar.setRightButtonOnClickListener(this);
        mToolbar.getmLeftButton().setOnClickListener(this);
        txtRegion.setOnClickListener(this);

        mCityPikerView.setPicker((ArrayList) mProvinces,mCities,mDistricts,true);
        mCityPikerView.setTitle("选择城市");
        mCityPikerView.setCyclic(true,true,true);
        mCityPikerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                String addresss = mProvinces.get(options1).getName() +"  "
                        + mCities.get(options1).get(option2)+"  "
                        + mDistricts.get(options1).get(option2).get(options3);
                txtRegion.setText(addresss);

            }
        });
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {

            case R.id.toolbar_rightButton:
                requestData();
                break;
            case R.id.txt_region:
                mCityPikerView.show();
                break;

            case R.id.toolbar_leftButton:
                onBackPressed();
                break;
        }
    }

    private void requestData() {

        String consignee = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String region = txtRegion.getText().toString();
        String address = editAddress.getText().toString();
        String addr = region + address;

        if (consignee.equals("")) {

            ToastUtils.show(this, "收件人姓名不能为空");

        } else if (phone.equals("")) {

            ToastUtils.show(this, "电话号码不能为空");

        } else if (region.equals("") || address.equals("")) {

            ToastUtils.show(this, "收件地址不能为空");

        } else {

            Map<String,String> params = new HashMap<>();
            String url;
            params.put("token", MyShopApplication.getInstance().getToken());
            if (mAddress == null) {

                params.put("user_id", MyShopApplication.getInstance().getUser().getId() + "");
                params.put("consignee", consignee);
                params.put("phone", phone);
                params.put("addr", addr);
                params.put("zip_code", "000000");

                url = Contants.API.ADDRESS_CREATE;


            } else {

                params.put("id",mAddress.getId()+"");
                params.put("consignee",consignee);
                params.put("phone",phone);
                params.put("addr",addr);
                params.put("zip_code","000000");
                params.put("is_default",mAddress.getIsDefault()+"");

                url = Contants.API.ADDRESS_UPDATE;
            }



            mHttpHelper.doPost(url, params, new TokenCallBack<BaseRespMsg>(this) {
                @Override
                public void onSuccess(Response response, BaseRespMsg msg) {
                    if (msg.getStatus() == 200) {
                        setResult(RESULT_OK);
                        ToastUtils.show(CreateAddrActivity.this, "操作成功");
                    }



                }

                @Override
                public void onError(Response response, int code, Exception e) {

                }
            });
        }




    }


    //初始化中国各个城市的数据
    protected void initProvinceDatas(){

        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            mProvinces = handler.getDataList();

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }

        if(mProvinces !=null){

            for (ProvinceModel p :mProvinces){

                List<CityModel> cities =  p.getCityList();
                ArrayList<String> cityStrs = new ArrayList<>(cities.size()); //城市List


                for (CityModel c :cities){

                    cityStrs.add(c.getName()); // 把城市名称放入 cityStrs


                    ArrayList<ArrayList<String>> dts = new ArrayList<>(); // 地区 List

                    List<DistrictModel> districts = c.getDistrictList();
                    ArrayList<String> districtStrs = new ArrayList<>(districts.size());

                    for (DistrictModel d : districts){
                        districtStrs.add(d.getName()); // 把城市名称放入 districtStrs
                    }
                    dts.add(districtStrs);


                    mDistricts.add(dts);
                }

                mCities.add(cityStrs); // 组装城市数据

            }
        }



    }




}
