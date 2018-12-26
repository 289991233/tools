package com.jack.utils.wheelview.dialog.entity;

/**
 * @desc：
 * @author: yongzhi
 * @time: 2017/4/12 0012
 * @reviser_and_time:
 */

public class Address {
    /**
     * @param provinceName 省、自治区名称
     * @param provinceID   省id
     * @param cityName     城市名称
     * @param cityID       城市id
     * @param countryName  城区、县名称
     * @param countryID    城区、县id
     */
    private String provinceName,  provinceID,  cityName,  cityID,  countryName,  countryID;

    public Address(String provinceName, String provinceID, String cityName, String cityID, String countryName, String countryID) {
        this.provinceName = provinceName;
        this.provinceID = provinceID;
        this.cityName = cityName;
        this.cityID = cityID;
        this.countryName = countryName;
        this.countryID = countryID;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }
}
