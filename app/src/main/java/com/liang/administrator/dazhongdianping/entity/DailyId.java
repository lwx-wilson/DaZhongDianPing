package com.liang.administrator.dazhongdianping.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class DailyId {


    /**
     * status : OK
     * count : 309
     * id_list : ["1-33946","1-4531","1-4571","1-5336","1-5353","......"]
     */

    private String status;
    private int count;
    private List<String> id_list;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getId_list() {
        return id_list;
    }

    public void setId_list(List<String> id_list) {
        this.id_list = id_list;
    }

    @Override
    public String toString() {
        return "DailyId{" +
                "status='" + status + '\'' +
                ", count=" + count +
                ", id_list=" + id_list +
                '}';
    }
}
