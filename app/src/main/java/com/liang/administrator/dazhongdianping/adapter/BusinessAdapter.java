package com.liang.administrator.dazhongdianping.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.liang.administrator.dazhongdianping.R;
import com.liang.administrator.dazhongdianping.app.MyApp;
import com.liang.administrator.dazhongdianping.entity.Business;
import com.liang.administrator.dazhongdianping.util.HttpUtil;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class BusinessAdapter extends MyBaseAdapter<Business.BusinessesBean> {


    public BusinessAdapter(Context context, List<Business.BusinessesBean> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (view == null){
            view = inflater.inflate(R.layout.business_list_food_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Business.BusinessesBean businessesBean = getItem(position);
        HttpUtil.loadImage(businessesBean.getPhoto_url(), viewHolder.imageView_picture);
        String name = businessesBean.getName().substring(0, businessesBean.getName().indexOf("("));
        if (!TextUtils.isEmpty(businessesBean.getBranch_name())){
            name = name + "(" + businessesBean.getBranch_name() + ")";
        }
        viewHolder.textView_name.setText(name);

        int[] stars = new int[]{R.drawable.movie_star10,
                R.drawable.movie_star20,
                R.drawable.movie_star30,
                R.drawable.movie_star35,
                R.drawable.movie_star40,
                R.drawable.movie_star45,
                R.drawable.movie_star50};

        Random rand = new Random();
        int index = rand.nextInt(7);
        viewHolder.imageView_rating.setImageResource(stars[index]);

        int price = rand.nextInt(100) + 50;
        viewHolder.textView_price.setText("￥" + price + "/人");

        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < businessesBean.getRegions().size(); j++){
            if (j == 0){
                builder.append(businessesBean.getRegions().get(j));
            } else {
                builder.append("/").append(businessesBean.getRegions().get(j));
            }
        }
        viewHolder.textView_info01.setText(builder.toString());

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < businessesBean.getCategories().size(); j++) {
            if (j == 0) {
                sb.append(businessesBean.getCategories().get(j));
            } else {
                sb.append("/").append(businessesBean.getCategories().get(j));
            }
        }
        viewHolder.textView_info02.setText(sb.toString());

        if (MyApp.myLocation != null){
            double distance = DistanceUtil.getDistance(new LatLng(businessesBean.getLatitude(), businessesBean.getLongitude()), MyApp.myLocation);
            int result = (int) distance;
            viewHolder.textView_distance.setText(result + "米");
        } else {
            viewHolder.textView_distance.setText("");
        }

        return view;
    }

    public class ViewHolder{
        @BindView(R.id.business_iamgeView_shop)
        ImageView imageView_picture;
        @BindView(R.id.business_textView_shopName)
        TextView textView_name;
        @BindView(R.id.business_imageView_rating)
        ImageView imageView_rating;
        @BindView(R.id.business_textView_price)
        TextView textView_price;
        @BindView(R.id.business_textView_info01)
        TextView textView_info01;
        @BindView(R.id.business_textView_info02)
        TextView textView_info02;
        @BindView(R.id.business_textView_distance)
        TextView textView_distance;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
