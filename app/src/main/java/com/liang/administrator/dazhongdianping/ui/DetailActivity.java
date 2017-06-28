package com.liang.administrator.dazhongdianping.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.liang.administrator.dazhongdianping.R;
import com.liang.administrator.dazhongdianping.adapter.DetailAdapter;
import com.liang.administrator.dazhongdianping.app.MyApp;
import com.liang.administrator.dazhongdianping.entity.Business;
import com.liang.administrator.dazhongdianping.entity.Comment;
import com.liang.administrator.dazhongdianping.util.HttpUtil;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends Activity {

    Business.BusinessesBean business;
    @BindView(R.id.detail_listView)
    ListView listView;

    private TextView textView_shopName;
    private TextView textView_price;
    private TextView textView_info01;
    private TextView textView_info02;
    private ImageView imageView_shop;
    private ImageView imageView_rating;
    private TextView textView_address;

    DetailAdapter adapter;
    private View shopName;
    private View shopMore;
    private List<Comment> datas;
    private TextView textView_phone;
    private TextView textView_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        business = (Business.BusinessesBean) getIntent().getSerializableExtra("business");
        Log.i("LWX==============", business.getName().toString());

        initialListView();
    }

    private void initialListView() {
        datas = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(this);
        shopName = inflater.inflate(R.layout.detail_list_header_shopname, listView, false);
        shopMore = inflater.inflate(R.layout.detail_list_header_shopmore, listView, false);

        initialHeader();
        initialInfo();

        listView.addHeaderView(shopName);
        listView.addHeaderView(shopMore);

        adapter = new DetailAdapter(this, datas);
        listView.setAdapter(adapter);
    }

    private void initialHeader() {
        textView_shopName = (TextView) shopName.findViewById(R.id.detail_textView_shopName);
        textView_price = (TextView) shopName.findViewById(R.id.detail_textView_price);
        textView_info01 = (TextView) shopName.findViewById(R.id.detail_textView_info01);
        textView_info02 = (TextView) shopName.findViewById(R.id.detail_textView_info02);
        imageView_shop = (ImageView) shopName.findViewById(R.id.detail_iamgeView_shop);
        imageView_rating = (ImageView) shopName.findViewById(R.id.detail_imageView_rating);
        textView_distance =  (TextView) shopName.findViewById(R.id.detail_textView_distance);

        HttpUtil.loadImage(business.getPhoto_url(), imageView_shop);

        String name = business.getName().substring(0, business.getName().indexOf("("));
        if (!TextUtils.isEmpty(business.getBranch_name())){
            name = name + "(" + business.getBranch_name() + ")";
        }
        textView_shopName.setText(name);

        int[] stars = new int[]{R.drawable.movie_star10,
                R.drawable.movie_star20,
                R.drawable.movie_star30,
                R.drawable.movie_star35,
                R.drawable.movie_star40,
                R.drawable.movie_star45,
                R.drawable.movie_star50};
        Random rand = new Random();
        int idx = rand.nextInt(7);
        imageView_rating.setImageResource(stars[idx]);

        int price = rand.nextInt(100) + 50;

        textView_price.setText("￥" + price + "/人");

        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < business.getRegions().size(); j++){
            if (j == 0){
                builder.append(business.getRegions().get(j));
            } else {
                builder.append("/").append(business.getRegions().get(j));
            }
        }
        textView_info01.setText(builder.toString());

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < business.getCategories().size(); j++) {
            if (j == 0) {
                sb.append(business.getCategories().get(j));
            } else {
                sb.append("/").append(business.getCategories().get(j));
            }
        }
        textView_info02.setText(sb.toString());

        if (MyApp.myLocation != null){
            double distance = DistanceUtil.getDistance(new LatLng(business.getLatitude(), business.getLongitude()), MyApp.myLocation);
            int result = (int) distance;
            textView_distance.setText(result + "米");
        } else {
            textView_distance.setText("");
        }

    }

    private void initialInfo() {
        textView_address = (TextView) shopMore.findViewById(R.id.detail_address);
        textView_phone = (TextView) shopMore.findViewById(R.id.detail_phone);

        textView_address.setText(business.getAddress());
        textView_phone.setText(business.getTelephone());

        textView_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, FindActivity.class);
                intent.putExtra("business", business);
                intent.putExtra("from", "Detail");
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

        HttpUtil.getComment(business.getReview_list_url(), new HttpUtil.OnresponseListener<Document>() {
            @Override
            public void onResponse(Document document) {
                List<Comment> comments = new ArrayList<Comment>();
                Elements elements = document.select("div[class=comment-list] li[data-id]");
                for (Element element : elements){
                    Comment comment = new Comment();

                    Element imgElement = element.select("div[class=pic] img").get(0);
                    comment.setName(imgElement.attr("title"));
                    comment.setAvatar(imgElement.attr("src"));

                    Elements spanElements = element.select("div[class=user-info] span[class=comm-per]");
                    if (spanElements.size() > 0){
                        comment.setPrice(spanElements.get(0).text().split(" ")[1] + "/人");
                    } else {
                        comment.setPrice("");
                    }

                    Element starElement = element.select("div[class=user-info] span[title]").get(0);
                    String star = starElement.attr("class");
                    comment.setRating(star.split("-")[3]);

                    Element divElement = element.select("div[class=J_brief-cont]").get(0);
                    comment.setContent(divElement.text());

                    Elements imgElements = elements.select("div[class=shop-photo] img");
                    int size = imgElements.size();
                    if (size > 3){
                        size = 3;
                    }
                    String[] imgs = new String[size];
                    for (int i = 0; i < imgs.length; i++){
                        imgs[i] = imgElements.get(i).attr("src");
                    }
                    comment.setImgs(imgs);

                    Element spanElement2 = element.select("div[class=misc-info] span[class=time]").get(0);
                    comment.setDate(spanElement2.text());

                    comments.add(comment);
                }

                Log.i("LWX======", "评论内容" + comments);
                adapter.addAll(comments, true);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
