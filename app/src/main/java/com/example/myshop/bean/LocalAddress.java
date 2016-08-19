package com.example.myshop.bean;

/**
 * Created by LittleDay on 2016/8/17.
 */
public class LocalAddress extends BaseBean{

    private String consignee;
    private String phone;
    private String addr;
    private String region;
    private String zip_code;
    private boolean isDefault;

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getConsignee() {
        return consignee;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddr() {
        return addr;
    }

    public String getRegion() {
        return region;
    }

    public String getZip_code() {
        return zip_code;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }
}
