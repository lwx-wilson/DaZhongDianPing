package com.liang.administrator.dazhongdianping.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<T> datas;

    public MyBaseAdapter(Context context, List<T> datas){
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(T t){
        datas.add(t);
        notifyDataSetChanged();
    }

    public void addAll(List<T> list, boolean isClear){
        if (isClear){
            datas.clear();
        }
        datas.addAll(list);
        notifyDataSetChanged();
    }

    public void removeAll(){
        datas.clear();
        notifyDataSetChanged();
    }
}
