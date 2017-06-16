package com.liang.administrator.dazhongdianping.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.liang.administrator.dazhongdianping.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.title_imageView)
    ImageView title_imageView;
    @BindView(R.id.menu_layout)
    View meun_layout;


    ListView listView;
    List<String> datas;
    ArrayAdapter<String> adapter;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initialListView();
        initialRadioButton();
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
        datas = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);

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

        listView.setAdapter(adapter);

        initialListHeaderIcons(listHeaderIcons);

        //添加下拉松手后的刷新
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        datas.add(0, "新增内容");
                        adapter.notifyDataSetChanged();
                        pullToRefreshListView.onRefreshComplete();
                    }
                }, 1500);
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
                    title_imageView.setVisibility(View.GONE);
                } else {
                    cityContainer.setVisibility(View.VISIBLE);
                    title_imageView.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void initialListHeaderIcons(View listHeaderIcons) {

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
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            private void ViewPagerListener(int position) {


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

        for (int i = 1; i <= 20; i++){
            datas.add("第" + i + "个数据");
        }

        adapter.notifyDataSetChanged();
    }
}
