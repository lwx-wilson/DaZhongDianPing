package com.liang.administrator.dazhongdianping.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.liang.administrator.dazhongdianping.entity.CityName;

import java.sql.SQLException;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper{

    private static DBHelper INSTANCE;

    public static DBHelper getINSTANCE(Context context){
        if (INSTANCE == null){
            synchronized (DBHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new DBHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public DBHelper(Context context) {
        super(context, "city.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, CityName.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, CityName.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
