package com.liang.administrator.dazhongdianping.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.liang.administrator.dazhongdianping.R;
import com.liang.administrator.dazhongdianping.entity.City;
import com.liang.administrator.dazhongdianping.entity.CityName;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/21 0021.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> implements SectionIndexer{

    Context context;
    List<CityName> datas;
    LayoutInflater inflater;
    OnMyItemListener listener;
    View headerView;

    private static final int HEADER = 0;
    private static final int ITEM = 1;

    public CityAdapter(Context context, List<CityName> datas) {

        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == HEADER){
            MyViewHolder viewHolder = new MyViewHolder(headerView);
            return viewHolder;
        }

        View view = inflater.inflate(R.layout.city_list_cityname, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (position == 0 && headerView != null){
            return;
        }

        final int dataIndex = position == 0 ? position : position - 1;

        CityName cityName = datas.get(dataIndex);
        holder.textView_cityName.setText(cityName.getCityName());
        holder.textView_letter.setText(cityName.getLetter() + "");

        holder.textView_cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getItemCityName(holder.textView_cityName.getText().toString());
            }
        });

//        setHideLetter(holder, position);

        if (dataIndex == getPositionForSection(getSectionForPosition(dataIndex))){
            holder.textView_letter.setVisibility(View.VISIBLE);
        } else {
            holder.textView_letter.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addAll(List<CityName> list, boolean isClear) {
        if (isClear){
            datas.clear();
        }
        datas.addAll(list);
        notifyDataSetChanged();
    }



    private void setHideLetter(MyViewHolder viewHolder, int position){
        if (position > 0){
            CityName cityName01 = datas.get(position);
            CityName cityName02 = datas.get(position-1);
            if (String.valueOf(cityName01.getLetter()).equals(String.valueOf(cityName02.getLetter()))){
                viewHolder.textView_letter.setVisibility(View.GONE);
            } else {
                viewHolder.textView_letter.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 某一个分组的起始位置是什么
     */
    @Override
    public int getPositionForSection(int sectionIndex) {

        for (int i = 0; i < datas.size(); i++){
            if (datas.get(i).getLetter() == sectionIndex){
                return i;
            }
        }
        //当前的数据源(datas)中没有任何一个数据属于传入的section分组
        //只要返回一个数据源中不存在的下标值即可。datas.size()或更大，-1或更小
        //TODO ???这个值如何返回
        return datas.size()+1;
    }

    /**
     * 第position位置上的数据的分组是什么
     * @return
     */
    @Override
    public int getSectionForPosition(int position) {
        return datas.get(position).getLetter();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.city_list_letter)
        TextView textView_letter;
        @Nullable
        @BindView(R.id.city_list_cityname)
        TextView textView_cityName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnMyItemListener{
        void getItemCityName(String cityName);
    }

    public void setOnMyItemListener(OnMyItemListener listener){
        this.listener = listener;
    }

    public void addHeaderView(View v){
        if (headerView == null) {
            this.headerView = v;
            notifyItemChanged(0);
        } else {
            throw new RuntimeException("不允许添加多个头视图");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (this.headerView != null){
            if (position == 0){
                return HEADER;
            } else {
                return ITEM;
            }
        } else {
            return ITEM;
        }
    }
}
