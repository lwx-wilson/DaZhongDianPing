package com.liang.administrator.dazhongdianping.app;

import android.app.Application;

import com.liang.administrator.dazhongdianping.entity.CityName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class MyApp extends Application {

    public static MyApp CONTEXT;
    public static List<CityName> cityNameList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
    }
}
