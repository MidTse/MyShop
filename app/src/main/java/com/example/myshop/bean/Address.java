package com.example.myshop.bean;

/**
 * Created by littleDay on 2016/8/16.
 */
public class Address  extends BaseBean implements Comparable<Address> {

    private String consignee;
    private String phone;
    private String addr;
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

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
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

    public String getZip_code() {
        return zip_code;
    }


    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }


    @Override
    public int compareTo(Address another) {

        return another.getIsDefault().compareTo(this.getIsDefault());
    }
}
