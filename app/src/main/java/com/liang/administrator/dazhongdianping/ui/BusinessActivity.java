package com.liang.administrator.dazhongdianping.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liang.administrator.dazhongdianping.R;
import com.liang.administrator.dazhongdianping.adapter.BusinessAdapter;
import com.liang.administrator.dazhongdianping.adapter.MyBaseAdapter;
import com.liang.administrator.dazhongdianping.entity.Business;
import com.liang.administrator.dazhongdianping.entity.DistrictBean;
import com.liang.administrator.dazhongdianping.util.HttpUtil;
import com.liang.administrator.dazhongdianping.util.SPUtil;
import com.liang.administrator.dazhongdianping.view.MyBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessActivity extends Activity {

    String cityName;
    @BindView(R.id.business_listView)
    ListView listView;
    List<Business.BusinessesBean> datas;
    BusinessAdapter adapter;
    SPUtil spUtil;
    @BindView(R.id.business_imageView_loading)
    ImageView imageView_loading;
    @BindView(R.id.business_nearby)
    LinearLayout linearLayout_nearby;
    @BindView(R.id.business_imageView_nearby_down)
    ImageView imageView_nearby;
    @BindView(R.id.business_linearLayout_nearby)
    LinearLayout business_linearLayout_nearby;
    @BindView(R.id.business_listView_left)
    ListView listView_left;
    @BindView(R.id.business_listView_right)
    ListView listView_right;
    @BindView(R.id.business_textView_nearby)
    TextView textView_nearby;

    List<String> leftDatas;
    List<String> rightDatas;
    ArrayAdapter leftAdapter;
    ArrayAdapter rightAdapter;

    boolean isClick = true;
    private List<DistrictBean.CitiesBean.DistrictsBean> districts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        cityName = intent.getStringExtra("city");
        Log.i("LWX================", "onCreate()+cityName:" + cityName);
        spUtil = new SPUtil(this);
        initialListView();
        initialNearby();
    }

    private void initialNearby() {

        linearLayout_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick) {
                    imageView_nearby.setImageResource(R.drawable.ic_arrow_up_black);
                    business_linearLayout_nearby.setVisibility(View.VISIBLE);
                    isClick = false;
                } else {
                    imageView_nearby.setImageResource(R.drawable.ic_arrow_down_black);
                    business_linearLayout_nearby.setVisibility(View.GONE);
                    isClick = true;
                }
            }
        });

        leftDatas = new ArrayList<>();
        leftAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, leftDatas);
        listView_left.setAdapter(leftAdapter);

        rightDatas = new ArrayList<>();
        rightAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, rightDatas);
        listView_right.setAdapter(rightAdapter);

        listView_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DistrictBean.CitiesBean.DistrictsBean district = districts.get(position);
                List<String> neighborhoods = new ArrayList<String>(district.getNeighborhoods());
                neighborhoods.add(0, "全部" + district.getDistrict_name());
                rightDatas.clear();
                rightDatas.addAll(neighborhoods);
                rightAdapter.notifyDataSetChanged();

            }
        });

        listView_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String region = (String) rightAdapter.getItem(position);
                if (position == 0){
                    region = region.substring(2, region.length());
                }

                textView_nearby.setText(region);
                business_linearLayout_nearby.setVisibility(View.GONE);
                adapter.removeAll();

                HttpUtil.getFoods(cityName, region, new Callback<Business>() {
                    @Override
                    public void onResponse(Call<Business> call, Response<Business> response) {
                        Business business = response.body();
                        List<Business.BusinessesBean> list = business.getBusinesses();
                        adapter.addAll(list, true);
                    }

                    @Override
                    public void onFailure(Call<Business> call, Throwable throwable) {

                    }
                });
            }
        });
    }

    private void initialListView() {
        datas = new ArrayList<>();
        adapter = new BusinessAdapter(this, datas);

        if (!spUtil.isCloseBanner()) {
            final MyBanner myBanner = new MyBanner(this, null);
            myBanner.setOnCloseBannerListener(new MyBanner.OnCloseBannerListener() {
                @Override
                public void onClose() {
                    spUtil.setCloseBanner(true);
                    listView.removeHeaderView(myBanner);
                }
            });
            listView.addHeaderView(myBanner);
        }
        listView.setAdapter(adapter);

        AnimationDrawable drawable = (AnimationDrawable) imageView_loading.getDrawable();
        drawable.start();

        listView.setEmptyView(imageView_loading);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Business.BusinessesBean business;

                //判断listView有没有头
                if (spUtil.isCloseBanner()){
                    business = adapter.getItem(position);
                } else {
                    business = adapter.getItem(position-1);
                }

                Intent intent = new Intent(BusinessActivity.this, DetailActivity.class);
                intent.putExtra("business", business);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        HttpUtil.getFoods(cityName, null, new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                Business business = response.body();
                List<Business.BusinessesBean> list = business.getBusinesses();
                adapter.addAll(list, true);
            }

            @Override
            public void onFailure(Call<Business> call, Throwable throwable) {

            }
        });

        HttpUtil.getRegions(cityName, new Callback<DistrictBean>() {
            @Override
            public void onResponse(Call<DistrictBean> call, Response<DistrictBean> response) {
                DistrictBean districtBean = response.body();
                districts = districtBean.getCities().get(0).getDistricts();

                List<String> districtNames = new ArrayList<String>();
                for (int i = 0; i < districts.size(); i++){
                    DistrictBean.CitiesBean.DistrictsBean district = districts.get(i);
                    districtNames.add(district.getDistrict_name());
                }

                leftDatas.clear();
                leftDatas.addAll(districtNames);
                leftAdapter.notifyDataSetChanged();

                List<String> neighborhoods = new ArrayList<String>(districts.get(0).getNeighborhoods());
                String districtName = districts.get(0).getDistrict_name();
                neighborhoods.add(0, "全部" + districtName);

                rightDatas.clear();
                rightDatas.addAll(neighborhoods);
                rightAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<DistrictBean> call, Throwable throwable) {

            }
        });
    }
}
