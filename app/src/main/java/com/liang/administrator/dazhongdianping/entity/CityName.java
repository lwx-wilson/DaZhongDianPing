package com.liang.administrator.dazhongdianping.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2017/6/21 0021.
 */
@DatabaseTable
public class CityName {
    @DatabaseField(id = true)
    String cityName;
    @DatabaseField
    String PYName;
    @DatabaseField
    char letter;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPYName() {
        return PYName;
    }

    public void setPYName(String PYName) {
        this.PYName = PYName;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return "CityName{" +
                "cityName='" + cityName + '\'' +
                ", PYName='" + PYName + '\'' +
                ", letter=" + letter +
                '}';
    }
}
