package com.liang.administrator.dazhongdianping.app;

import android.app.Application;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class MyApp extends Application {

    public static MyApp CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
    }
}
