package com.liang.administrator.dazhongdianping.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.liang.administrator.dazhongdianping.entity.CityName;
import com.liang.administrator.dazhongdianping.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class MyApp extends Application {

    public static MyApp CONTEXT;
    public static List<CityName> cityNameList = new ArrayList<>();
    public static LatLng myLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        CONTEXT = this;
        SPUtil spUtil = new SPUtil(this);
        spUtil.setCloseBanner(false);
        myLocation = new LatLng(23.171968, 113.344923);
    }
}
