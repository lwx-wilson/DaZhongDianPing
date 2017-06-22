package com.liang.administrator.dazhongdianping.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.liang.administrator.dazhongdianping.R;
import com.liang.administrator.dazhongdianping.app.MyApp;
import com.liang.administrator.dazhongdianping.entity.CityName;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

public class SearchActivity extends Activity {

    @BindView(R.id.search_previous)
    ImageView imageView_previous;
    @BindView(R.id.search_editText)
    EditText editText_search;
    @BindView(R.id.search_listView)
    ListView listView;

    List<String> cities;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        initialListView();
    }

    @OnTextChanged(R.id.search_editText)
    public void search(Editable editable){
        if (editable.length() == 0){
            cities.clear();
            adapter.notifyDataSetChanged();
        } else {
            searchCitier(editable.toString().toUpperCase());
        }
    }

    /**
     * 根据输入的内容
     * 筛选符合的城市名称
     * @param s
     */
    private void searchCitier(String s) {
        List<String> temps = new ArrayList<>();
        if (s.matches("[\u4e00-\u9fff]+")){
            for (CityName citeName : MyApp.cityNameList){
                if (citeName.getCityName().contains(s)) {
                    temps.add(citeName.getCityName());
                }
            }
        } else {
            for (CityName cityName : MyApp.cityNameList){
                if (cityName.getPYName().contains(s)) {
                    temps.add(cityName.getCityName());
                }
            }
        }

        if (temps.size() > 0){
            cities.clear();
            cities.addAll(temps);
            adapter.notifyDataSetChanged();
        }
    }

    private void initialListView() {
        cities = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        listView.setAdapter(adapter);
    }

    @OnItemClick(R.id.search_listView)
    public void onItemSelect(AdapterView<?> adapterView, View view, int i, long l) {
        Intent data = new Intent();
        String city = adapter.getItem(i);
        data.putExtra("city", city);
        setResult(RESULT_OK, data);
        finish();
    }
}
