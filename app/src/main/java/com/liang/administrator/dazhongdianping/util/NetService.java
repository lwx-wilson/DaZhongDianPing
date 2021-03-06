package com.liang.administrator.dazhongdianping.util;

import com.liang.administrator.dazhongdianping.entity.Business;
import com.liang.administrator.dazhongdianping.entity.City;
import com.liang.administrator.dazhongdianping.entity.DistrictBean;

import java.util.Calendar;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public interface NetService {
    @GET("business/find_businesses")
    public Call<String> test(@Query("appkey") String appkey, @Query("sign") String sign, @QueryMap Map<String, String> params);

    @GET("deal/get_daily_new_id_list")
    public Call<String> getDailyNewList(@Query("appkey") String appkey, @Query("sign") String sign, @QueryMap Map<String, String> params);

    @GET("deal/get_batch_deals_by_id")
    public Call<String> getBatchDealsById(@Query("appkey") String appkey, @Query("sign") String sign,@QueryMap Map<String,String> params);

    @GET("deal/get_daily_new_id_list")
    public Call<String> getDailyNewList2(@QueryMap Map<String, String> params);

    @GET("deal/get_batch_deals_by_id")
    public Call<String> getBatchDealsById2(@QueryMap Map<String,String> params);

    @GET("metadata/get_cities_with_businesses")
    public Call<City> getCities();

    @GET("business/find_businesses")
    public Call<Business> getFoods(@QueryMap Map<String, String> params);

    @GET("metadata/get_regions_with_businesses")
    public Call<DistrictBean> getRegions(@QueryMap Map<String, String> params);
}
