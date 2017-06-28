package com.liang.administrator.dazhongdianping.adapter;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.liang.administrator.dazhongdianping.R;
import com.liang.administrator.dazhongdianping.app.MyApp;
import com.liang.administrator.dazhongdianping.entity.BatchDeals;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class DealAdapter extends MyBaseAdapter<BatchDeals.DealsBean> {

    public DealAdapter(Context context, List<BatchDeals.DealsBean> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null){
            view = inflater.inflate(R.layout.list_myfavourite, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView_pic = (ImageView) view.findViewById(R.id.list_myfavourite_imageView);
            viewHolder.textView_name = (TextView) view.findViewById(R.id.list_myfavourite_shopName);
            viewHolder.textView_activity = (TextView) view.findViewById(R.id.list_myfavourite_activity);
            viewHolder.textView_distance = (TextView) view.findViewById(R.id.list_myfavourite_distance);
            viewHolder.textView_cost = (TextView) view.findViewById(R.id.list_myfavourite_cost);
            viewHolder.textView_count = (TextView) view.findViewById(R.id.list_myfavourite_count);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        BatchDeals.DealsBean deal = getItem(position);

        Picasso.with(context).load(deal.getS_image_url()).placeholder(R.drawable.bucket_no_picture).error(R.mipmap.ic_launcher).into(viewHolder.imageView_pic);

        viewHolder.textView_name.setText(deal.getTitle());
        viewHolder.textView_activity.setText(deal.getDescription());
//        viewHolder.textView_distance.setText();
        viewHolder.textView_cost.setText(deal.getCurrent_price()+"");
        Random random = new Random();
        int count = random.nextInt(2000)+500;
        viewHolder.textView_count.setText("已售" + count);

        if (MyApp.myLocation != null){
            double distance = DistanceUtil.getDistance(new LatLng(deal.getBusinesses().get(0).getLatitude(), deal.getBusinesses().get(0).getLongitude()), MyApp.myLocation);
            int result = (int) distance;
//            viewHolder.textView_distance.setText(result);
//            Log.i("LWX========", "MainActivity.distance:" + result);
        } else {
//            viewHolder.textView_distance.setText("");
        }

        return view;
    }

    public class ViewHolder{
        ImageView imageView_pic;
        TextView textView_name;
        TextView textView_activity;
        TextView textView_cost;
        TextView textView_distance;
        TextView textView_count;
    }
}
