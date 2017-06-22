package com.liang.administrator.dazhongdianping.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.liang.administrator.dazhongdianping.entity.CityName;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class DBUtil {

    private DBHelper dbHelper;
    private Dao<CityName, String> dao;

    public DBUtil(Context context) {
        dbHelper = DBHelper.getINSTANCE(context);
        try {
            dao = dbHelper.getDao(CityName.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(CityName cityName){
        try {
            dao.createIfNotExists(cityName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertAll(List<CityName> list){
        for (CityName cityName : list){
            insert(cityName);
        }
    }

    public void insertBatch(final List<CityName> list){
        //建立连接后，一次性将数据全部写入后，再断开连接
        try {
            dao.callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {

                    for (CityName name : list){
                        insert(name);
                    }

                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CityName> query() {
        try {
            List<CityName> cityNameList = dao.queryForAll();
            return cityNameList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询数据库时出现异常");
        }
    }

}
