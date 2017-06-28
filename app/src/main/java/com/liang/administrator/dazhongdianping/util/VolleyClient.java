package com.liang.administrator.dazhongdianping.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.liang.administrator.dazhongdianping.R;
import com.liang.administrator.dazhongdianping.app.MyApp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class VolleyClient {

    private ImageLoader imageLoader;

    //声明一个私有的静态的属性
    private static VolleyClient INSTANCE;
    //声明一个公有的静态的获取上一个属性的方法
    public static VolleyClient getINSTANCE(){
        if (INSTANCE == null){
            synchronized (VolleyClient.class){
                if (INSTANCE == null){
                INSTANCE = new VolleyClient();
                }
            }
        }
        return INSTANCE;
    }

    RequestQueue queue;

    private VolleyClient() {
        queue = Volley.newRequestQueue(MyApp.CONTEXT);
        imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {

            LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory()/8)){
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getHeight()*value.getRowBytes();
                }
            };
            @Override
            public Bitmap getBitmap(String s) {
                return cache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                cache.put(s, bitmap);
            }
        });
    }

    private VolleyClient(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public void test() {

        Map<String, String> params = new HashMap<>();
        params.put("city", "北京");
        params.put("category", "美食");
        String url = HttpUtil.getURL("http://api.dianping.com/v1/business/find_businesses", params);

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.i("TAG", "利用Volley获得响应内容：" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    public void loadImage(String url, ImageView imageView){
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.drawable.bucket_no_picture, R.mipmap.ic_launcher);
        imageLoader.get(url, listener);
    }

}
