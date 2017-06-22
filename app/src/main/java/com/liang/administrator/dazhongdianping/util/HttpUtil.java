package com.liang.administrator.dazhongdianping.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.liang.administrator.dazhongdianping.app.MyApp;
import com.liang.administrator.dazhongdianping.entity.City;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class HttpUtil {

    public static final String APPKEY = "49814079";
    public static final String APPSECRET = "90e3438a41d646848033b6b9d461ed54";

    /*获得满足大众点评服务器要求的请求路劲
    *
    * */
    public static String getURL(String url, Map<String, String> params){
        String result= "";

        String sign = getSign(APPKEY, APPSECRET, params);

        String query = getQuery(APPKEY, sign, params);

        result = url + "?" + query;

        return result;
    }

    private static String getQuery(String appkey, String sign, Map<String, String> params) {

        try {
            // 添加签名
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("appkey=").append(appkey).append("&sign=").append(sign);
            for (Map.Entry<String, String> entry : params.entrySet())
            {
                stringBuilder.append('&').append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "utf-8"));
            }
            String queryString = stringBuilder.toString();

            return queryString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("使用了不正确的字符串");
        }
    }

    public static String getSign(String appkey, String appsecret, Map<String, String> params) {

        StringBuilder stringBuilder = new StringBuilder();

        // 对参数名进行字典排序
        String[] keyArray = params.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);
        // 拼接有序的参数名-值串
        stringBuilder.append(appkey);
        for (String key : keyArray)
        {
            stringBuilder.append(key).append(params.get(key));
        }
        String codes = stringBuilder.append(appsecret).toString();
        //在纯JAVA环境中，利用Codec对字符串进行SHA1转码采用以下方式
//        String sign = org.apache.commons.codec.digest.DigestUtils.shaHex(codes).toUpperCase();
        //在Android环境中，利用Codec对字符串进行SHA1转码采用以下方式
        String sign = new String(Hex.encodeHex(DigestUtils.sha(codes))).toUpperCase();

        return sign;
    }

    public static void testHttpURLConnection(){
        Map<String, String> params = new HashMap<>();
        params.put("city", "北京");
        params.put("category", "美食");
        final String url = getURL("http://api.dianping.com/v1/business/find_businesses", params);
//        Log.i("TAG", "生成的网络请求的地址：" + url);

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL u = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();

                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    StringBuilder builder = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine()) != null){
                        builder.append(line);
                    }

                    reader.close();
                    String responser = builder.toString();
                    Log.i("TAG", responser);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void testVolley() {
        VolleyClient.getINSTANCE().test();
    }

    public static void testRetrofit() {
        RetrofitClient.getInstance().test();
    }

    public static void retrofitGetDailyNewList(String city, Callback<String> callback) {
//        RetrofitClient.getInstance().getDailyNewList(city, callback);
        RetrofitClient.getInstance().getDailyNewList2(city, callback);
    }

    public static void getCities(Callback<City> callback){
        RetrofitClient.getInstance().getCities(callback);
    }
}
