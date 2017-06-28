package com.liang.administrator.dazhongdianping.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.liang.administrator.dazhongdianping.R;
import com.liang.administrator.dazhongdianping.adapter.DealAdapter;
import com.liang.administrator.dazhongdianping.entity.BatchDeals;
import com.liang.administrator.dazhongdianping.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    @BindView(R.id.pullToRefreshListView_main)
    PullToRefreshListView pullToRefreshListView;

    @BindView(R.id.radioButton_main)
    RadioButton radioButton_main;
    @BindView(R.id.radioButton_tuan)
    RadioButton radioButton_tuan;
    @BindView(R.id.radioButton_search)
    RadioButton radioButton_search;
    @BindView(R.id.radioButton_my)
    RadioButton radioButton_my;
    @BindView(R.id.cityContainer)
    LinearLayout cityContainer;
    @BindView(R.id.search_shop)
    LinearLayout searchShop;
    @BindView(R.id.title_add)
    LinearLayout title_add;
    @BindView(R.id.menu_layout)
    View meun_layout;
    @BindView(R.id.main_title_city)
    TextView textView_cityName;

    ListView listView;
//    List<String> datas;
//    ArrayAdapter<String> adapter;
    List<BatchDeals.DealsBean> datas;
    DealAdapter adapter;
    private ViewPager viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initialListView();
        initialCityContainer();
//        initialRadioButton();
    }

    private void initialCityContainer() {

        cityContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CityActivity.class);
                startActivityForResult(intent,101);
            }
        });

    }

    @OnClick(R.id.cityContainer)
    public void jumpToCity(){
        Intent intent = new Intent(this, CityActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.title_imageView)
    public void toggleMenu(){
        if (meun_layout.getVisibility() == View.INVISIBLE){
            meun_layout.setVisibility(View.VISIBLE);
        } else {
            meun_layout.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.radioButton_search)
    public void jump(){
        Intent intent = new Intent(this, FindActivity.class);
        intent.putExtra("from", "Main");
        startActivity(intent);
    }

    private void initialRadioButton() {

        Drawable drawable_main = getResources().getDrawable(R.drawable.radiogroup_main_selector, null);
        drawable_main.setBounds(0, 0, 70, 70);
        radioButton_main.setCompoundDrawables(null, drawable_main, null, null);

        Drawable drawable_tuan = getResources().getDrawable(R.drawable.radiogroup_tuan_selector, null);
        drawable_tuan.setBounds(0, 0, 70, 70);
        radioButton_tuan.setCompoundDrawables(null, drawable_tuan, null, null);

        Drawable drawable_search = getResources().getDrawable(R.drawable.radiogroup_search_selector, null);
        drawable_search.setBounds(0, 0, 70, 70);
        radioButton_search.setCompoundDrawables(null, drawable_search, null, null);

        Drawable drawable_my = getResources().getDrawable(R.drawable.radiogroup_my_selector, null);
        drawable_my.setBounds(0, 0, 70, 70);
        radioButton_my.setCompoundDrawables(null, drawable_my, null, null);

    }

    private void initialListView() {

        listView = pullToRefreshListView.getRefreshableView();



        //为listView添加若干头部
        LayoutInflater inflater = LayoutInflater.from(this);
        View listHeaderIcons = inflater.inflate(R.layout.header_list_icons, listView, false);
        View listHeaderSquare = inflater.inflate(R.layout.header_list_square, listView, false);
        View listHeaderAds = inflater.inflate(R.layout.header_list_ads, listView, false);
        View listHeaderCategories = inflater.inflate(R.layout.header_list_categories, listView, false);
        View listHeaderRecommend = inflater.inflate(R.layout.header_list_recommend, listView, false);

        listView.addHeaderView(listHeaderIcons);
        listView.addHeaderView(listHeaderSquare);
        listView.addHeaderView(listHeaderAds);
        listView.addHeaderView(listHeaderCategories);
        listView.addHeaderView(listHeaderRecommend);

        /* datas = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);*/

        datas = new ArrayList<>();
        adapter = new DealAdapter(this, datas);
        listView.setAdapter(adapter);

        initialListHeaderIcons(listHeaderIcons);

        //添加下拉松手后的刷新
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        datas.add(0, "新增内容");
                        adapter.notifyDataSetChanged();
                        pullToRefreshListView.onRefreshComplete();
                    }
                }, 1500);*/

                refresh();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem != 0){
                    cityContainer.setVisibility(View.GONE);
                    title_add.setVisibility(View.GONE);
                } else {
                    cityContainer.setVisibility(View.VISIBLE);
                    title_add.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void initialListHeaderIcons(final View listHeaderIcons) {

        viewpager = (ViewPager) listHeaderIcons.findViewById(R.id.vp_header_list_icons);

        PagerAdapter adapter = new PagerAdapter() {

            int[] icons = new int[]{R.layout.icons_list_1, R.layout.icons_list_2, R.layout.icons_list_3};

            @Override
            public int getCount() {
                return 10000;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                int layoutId = icons[position%3];
                View view = LayoutInflater.from(MainActivity.this).inflate(layoutId, viewpager, false);
                container.addView(view);

                if (position%3 == 0){
                    LinearLayout lineaLayout_food = (LinearLayout)view.findViewById(R.id.ll_icons_list_food);
                    lineaLayout_food.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, BusinessActivity.class);
                            intent.putExtra("city", textView_cityName.getText().toString());
                            startActivity(intent);
                        }
                    });
                }

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };

        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(5001);

        final ImageView imageView01 = (ImageView) listHeaderIcons.findViewById(R.id.iv_header_list_icons_indicator1);
        final ImageView imageView02 = (ImageView) listHeaderIcons.findViewById(R.id.iv_header_list_icons_indicator2);
        final ImageView imageView03 = (ImageView) listHeaderIcons.findViewById(R.id.iv_header_list_icons_indicator3);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imageView01.setImageResource(R.drawable.banner_dot);
                imageView02.setImageResource(R.drawable.banner_dot);
                imageView03.setImageResource(R.drawable.banner_dot);

                switch (position%3){
                    case 0:
                        imageView01.setImageResource(R.drawable.banner_dot_pressed);
                        break;
                    case 1:
                        imageView02.setImageResource(R.drawable.banner_dot_pressed);
                        break;
                    case 2:
                        imageView03.setImageResource(R.drawable.banner_dot_pressed);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    private void refresh() {

//        HttpUtil.testHttpURLConnection();
//        HttpUtil.testVolley();
//        HttpUtil.testRetrofit();

        String cityName = textView_cityName.getText().toString();
        HttpUtil.retrofitGetDailyNewList(cityName, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response != null){
                    String temp = response.body();
//                    Log.i("LWX=========", temp);
                    Gson gson = new Gson();
                    BatchDeals batchDeals = gson.fromJson(temp, BatchDeals.class);
                    List<BatchDeals.DealsBean> dealsList = batchDeals.getDeals();
//                Log.i("LWX++++++++++", batchDeals.toString());

                    adapter.addAll(dealsList, true);
//                    Log.i("LWX==========", "addAll:" + datas);
                } else {
                    adapter.removeAll();
                    Toast.makeText(MainActivity.this, "今日无新增团购！", Toast.LENGTH_SHORT).show();
                }
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                pullToRefreshListView.onRefreshComplete();
            }
        });

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 101:
                if (resultCode==RESULT_OK){
                    String cityName=data.getStringExtra("cityName");
//                    Log.i("LWX======", cityName);
                    textView_cityName.setText(cityName);
                }
                break;
        }
    }
}
