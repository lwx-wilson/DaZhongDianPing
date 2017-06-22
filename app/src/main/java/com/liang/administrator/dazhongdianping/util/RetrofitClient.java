package com.liang.administrator.dazhongdianping.util;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liang.administrator.dazhongdianping.app.MyApp;
import com.liang.administrator.dazhongdianping.config.Constant;
import com.liang.administrator.dazhongdianping.entity.BatchDeals;
import com.liang.administrator.dazhongdianping.entity.City;
import com.liang.administrator.dazhongdianping.entity.DailyId;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class RetrofitClient {

    private static RetrofitClient INSTANCE;
    public static RetrofitClient getInstance(){
        if (INSTANCE == null){
            synchronized (RetrofitClient.class){
                if (INSTANCE == null){
                    INSTANCE = new RetrofitClient();
                }
            }
        }
        return INSTANCE;
    }

    private Retrofit retrofit;
    private NetService netService;

    private OkHttpClient okHttpClient;

    public RetrofitClient() {

        okHttpClient = new OkHttpClient.Builder().addInterceptor(new MyOkHttpInterceptor()).build();

        //创建Retrofit对象,baseUrl中，如果是用converter-scalars-2.x版本，末尾要加/
        retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(Constant.BASEURL).
                addConverterFactory(ScalarsConverterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).build();
        netService = retrofit.create(NetService.class);
    }

    public void test() {
        Map<String, String> params = new HashMap<>();
        params.put("city", "北京");
        params.put("category", "美食");
        String sign = HttpUtil.getSign(HttpUtil.APPKEY, HttpUtil.APPSECRET, params);

        //获取请求对象
        Call<String> call = netService.test(HttpUtil.APPKEY, sign, params);
        //将请求对象放到请求队列中去
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String string = response.body();
//                Log.i("TAG", "利用Retrofit请求获得的响应：" + string);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {

            }
        });
    }

    public void getDailyNewList(String city, final Callback<String> callback) {

        long timeMillions = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(timeMillions);

        Map<String, String> params = new HashMap<>();
        params.put("city", city);
        params.put("date", date);
        String sign = HttpUtil.getSign(HttpUtil.APPKEY, HttpUtil.APPSECRET, params);

        Call<String> call = netService.getDailyNewList(HttpUtil.APPKEY, sign, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String temp=response.body();
                Gson gson=new Gson();
                DailyId daily=gson.fromJson(temp, DailyId.class);
//                Log.i("lwx",daily.getCount()+"==================");
//                Log.i("lwx", daily.getId_list().toString());
                getListId(daily.getId_list(), callback);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {

            }
        });
    }

    public void getDailyNewList2(String city, final Callback<String> callback) {

        long timeMillions = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(timeMillions);

        Map<String, String> params = new HashMap<>();
        params.put("city", city);
        params.put("date", date);

        Call<String> call = netService.getDailyNewList2(params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String temp=response.body();
                Gson gson=new Gson();
                DailyId daily=gson.fromJson(temp, DailyId.class);
                Log.i("LWX",daily.getCount()+"==================");
//                Log.i("lwx", daily.getId_list().toString());
                getListId(daily.getId_list(), callback);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {

            }
        });
    }

    private void getListId(List<String> listId, Callback<String> callback) {

            int num = listId.size() / 40;
            for (int j = 0; j <= num; j++) {
                StringBuilder builder = new StringBuilder();
                if (j == num) {
                    for (int i = j * 40; i < listId.size(); i++) {
                        builder.append(listId.get(i)).append(",");
                    }
                } else {
                    for (int i = j * 40; i < (j + 1) * 40; i++) {
                        builder.append(listId.get(i)).append(",");
                    }
                }

                if (builder.length() > 0) {
                    String id = builder.deleteCharAt(builder.length() - 1).toString();
//                Log.i("LWX========", id);
//            getBatchDealsById(id, callback);
                    getBatchDealsById2(id, callback);

                } else {
                    Toast.makeText(MyApp.CONTEXT, "该城市太low，没钱吃外卖", Toast.LENGTH_SHORT).show();
                }
            }
        /*for(int i = 0; i < listId.size(); i++){
            String id = listId.get(i);
            Log.i("LWX==========", id);
            getBatchDealsById(id);
        }*/
    }

    private void getBatchDealsById(String id, Callback<String> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("deal_ids", id);
        String sign = HttpUtil.getSign(HttpUtil.APPKEY, HttpUtil.APPSECRET, params);

        Call<String> call = netService.getBatchDealsById(HttpUtil.APPKEY, sign, params);
        call.enqueue(callback);
    }

    private void getBatchDealsById2(String id, Callback<String> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("deal_ids", id);

        Call<String> call = netService.getBatchDealsById2(params);
        call.enqueue(callback);
    }

    public void getCities(Callback<City> callback) {

        Call<City> call = netService.getCities();
        call.enqueue(callback);
    }

    public class MyOkHttpInterceptor implements Interceptor{

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            //获得请求对象
            Request request = chain.request();
            //请求路径
            HttpUrl url = request.url();
            //取出原有请求路径中的参数
            HashMap<String, String> params = new HashMap<>();
            //原有请求路径中，请求参数的名称
            Set<String> set = url.queryParameterNames();
            for (String key : set){
                params.put(key, url.queryParameter(key));
            }

            String sign = HttpUtil.getSign(HttpUtil.APPKEY, HttpUtil.APPSECRET, params);
            //字符串格式的地址
            String urlStr = url.toString();
//            Log.i("LWX:原始请求路径==========", urlStr);
            StringBuilder sb = new StringBuilder(urlStr);

            if (set.size() == 0){
                sb.append("?");
            } else {
                sb.append("&");
            }

            sb.append("appkey=").append(HttpUtil.APPKEY).append("&").append("sign=").append(sign);
//            Log.i("LWX:新的请求路径==========", sb.toString());
            Request newRequest = new Request.Builder().url(sb.toString()).build();

            return chain.proceed(newRequest);
        }
    }
}
