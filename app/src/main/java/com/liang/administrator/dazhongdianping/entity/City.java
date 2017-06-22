package com.liang.administrator.dazhongdianping.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/6/21 0021.
 */

public class City {

    /**
     * status : OK
     * cities : ["阿坝","鞍山","安康","安庆","安阳","......"]
     */

    String status;
    List<String> cities;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "City{" +
                "status='" + status + '\'' +
                ", cities=" + cities +
                '}';
    }
}
