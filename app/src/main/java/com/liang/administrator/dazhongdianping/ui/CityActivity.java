package com.liang.administrator.dazhongdianping.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.liang.administrator.dazhongdianping.R;
import com.liang.administrator.dazhongdianping.adapter.CityAdapter;
import com.liang.administrator.dazhongdianping.app.MyApp;
import com.liang.administrator.dazhongdianping.entity.City;
import com.liang.administrator.dazhongdianping.entity.CityName;
import com.liang.administrator.dazhongdianping.util.DBUtil;
import com.liang.administrator.dazhongdianping.util.HttpUtil;
import com.liang.administrator.dazhongdianping.util.PinYinUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityActivity extends FragmentActivity {

    @BindView(R.id.city_imageView_previous)
    ImageView imageView_previous;
    @BindView(R.id.city_radiobutton_all)
    RadioButton radioButton_all;
    @BindView(R.id.city_radiobutton_overseas)
    RadioButton radioButton_overSeas;
    @BindView(R.id.city_searchWithPinYin)
    LinearLayout linearLayout_searchWithPinYin;
    @BindView(R.id.city_recyclerView)
    RecyclerView recyclerView;

    CityAdapter adapter;
    List<CityName> datas;
    DBUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        dbUtil = new DBUtil(this);
        initialRecyclerView();

        imageView_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initialRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        datas = new ArrayList<>();
        adapter = new CityAdapter(this, datas);
        recyclerView.setAdapter(adapter);

        View headerView = LayoutInflater.from(this).inflate(R.layout.city_header, recyclerView, false);
        adapter.addHeaderView(headerView);

        adapter.setOnMyItemListener(new CityAdapter.OnMyItemListener() {
            @Override
            public void getItemCityName(String cityName) {
                Intent intent = new Intent(CityActivity.this, MainActivity.class);
                intent.putExtra("cityName", cityName);

                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {

        if (MyApp.cityNameList != null && MyApp.cityNameList.size() > 0){
            Log.i("LWX==========", "城市数据从缓存中加载");
            adapter.addAll(MyApp.cityNameList, true);
            return;
        }

        List<CityName> list = dbUtil.query();
        if (list != null && list.size() > 0){
            adapter.addAll(list, true);
            Log.i("LWX==========", "城市数据从数据库中加载");
            MyApp.cityNameList = list;
            return;
        }


        HttpUtil.getCities(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                City city = response.body();
                List<String> list = city.getCities();

                final List<CityName> cityNames = new ArrayList<CityName>();

                for (String name : list){
                    if (!name.equals("全国") && !name.equals("其它城市") && !name.equals("点评实验室")){
                        CityName cityName = new CityName();
                        cityName.setCityName(name);
                        cityName.setPYName(PinYinUtil.getPinYin(name));
                        cityName.setLetter(PinYinUtil.getLetter(name));
//                        Log.i("LWX========", "创建的CityName内容：" + cityName);
                        cityNames.add(cityName);
                    }
                }

                Collections.sort(cityNames, new Comparator<CityName>() {
                    @Override
                    public int compare(CityName o1, CityName o2) {
                        return o1.getPYName().compareTo(o2.getPYName());
                    }
                });

                adapter.addAll(cityNames, true);
                Log.i("LWX==========", "城市数据从网络中加载");

                //将数据缓存起来
                MyApp.cityNameList = cityNames;
                //向数据库中写入城市数据
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Long time_start = System.currentTimeMillis();
                        dbUtil.insertAll(cityNames);
                        Long time_end = System.currentTimeMillis();
                        Log.i("LWX++++++", (time_end-time_start) + "");
                    }
                }.start();
            }

            @Override
            public void onFailure(Call<City> call, Throwable throwable) {

            }
        });
    }

    @OnClick(R.id.city_searchCity)
    public void searchCity(){
        Intent intent = new Intent(CityActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}
