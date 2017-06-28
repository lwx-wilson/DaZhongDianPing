package com.liang.administrator.dazhongdianping.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
import com.liang.administrator.dazhongdianping.R;
import com.liang.administrator.dazhongdianping.entity.Comment;
import com.liang.administrator.dazhongdianping.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class DetailAdapter extends MyBaseAdapter<Comment>{

    public DetailAdapter(Context context, List<Comment> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (view == null){
            view = inflater.inflate(R.layout.detail_list_comment, parent, false);
            viewHolder = new ViewHolder(view);
            viewHolder.linearLayout_list.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int width = viewHolder.linearLayout_list.getWidth();
                    int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                    int size = (width - 2 * margin)/3;
                    for (int i = 0; i < viewHolder.linearLayout_list.getChildCount(); i++){
                        ImageView imageView = (ImageView) viewHolder.linearLayout_list.getChildAt(i);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
                        if (i != 0){
                            params.setMargins(margin, 0,0,0);
                        }
                        imageView.setLayoutParams(params);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    viewHolder.linearLayout_list.requestLayout();
                }
            });
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Comment comment = getItem(position);
        Picasso.with(context).load(comment.getAvatar()).placeholder(R.drawable.bucket_no_picture).error(R.mipmap.ic_launcher).into(viewHolder.circleImageView);
        viewHolder.textView_userName.setText(comment.getName());
        viewHolder.textView_date.setText(comment.getDate().substring(0,5));

        String rating = comment.getRating();
        if (rating.equals("star10")){
            viewHolder.imageView_rating.setImageResource(R.drawable.movie_star10);
        } else if (rating.equals("star20")){
            viewHolder.imageView_rating.setImageResource(R.drawable.movie_star20);
        } else if (rating.equals("star30")){
            viewHolder.imageView_rating.setImageResource(R.drawable.movie_star30);
        } else if (rating.equals("star35")){
            viewHolder.imageView_rating.setImageResource(R.drawable.movie_star35);
        } else if (rating.equals("star40")){
            viewHolder.imageView_rating.setImageResource(R.drawable.movie_star40);
        } else if (rating.equals("star45")){
            viewHolder.imageView_rating.setImageResource(R.drawable.movie_star45);
        } else if (rating.equals("star50")){
            viewHolder.imageView_rating.setImageResource(R.drawable.movie_star50);
        }

        /*if (!TextUtils.isEmpty(comment.getPrice())){
            viewHolder.textView_price.setText(comment.getPrice());
        } else {
            viewHolder.textView_price.setVisibility(View.GONE);
        }*/

        viewHolder.textView_price.setText(comment.getPrice());

        viewHolder.textView_detail.setText(comment.getContent());

        if (comment.getImgs().length == 0){
            viewHolder.imageView_detail01.setVisibility(View.GONE);
            viewHolder.imageView_detail02.setVisibility(View.GONE);
            viewHolder.imageView_detail03.setVisibility(View.GONE);
        } else if (comment.getImgs().length == 1){
            Picasso.with(context).load(comment.getImgs()[0])
                    .placeholder(R.drawable.bucket_no_picture)
                    .error(R.mipmap.ic_launcher).
                    into(viewHolder.imageView_detail01);
            viewHolder.imageView_detail02.setVisibility(View.GONE);
            viewHolder.imageView_detail03.setVisibility(View.GONE);
        } else if (comment.getImgs().length == 2){
            Picasso.with(context).load(comment.getImgs()[0])
                    .placeholder(R.drawable.bucket_no_picture)
                    .error(R.mipmap.ic_launcher).
                    into(viewHolder.imageView_detail01);
            Picasso.with(context).load(comment.getImgs()[1])
                    .placeholder(R.drawable.bucket_no_picture)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.imageView_detail02);
            viewHolder.imageView_detail03.setVisibility(View.GONE);
        } else if (comment.getImgs().length == 3) {
            Picasso.with(context).load(comment.getImgs()[0])
                    .placeholder(R.drawable.bucket_no_picture)
                    .error(R.mipmap.ic_launcher).
                    into(viewHolder.imageView_detail01);
            Picasso.with(context).load(comment.getImgs()[1])
                    .placeholder(R.drawable.bucket_no_picture)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.imageView_detail02);
            Picasso.with(context).load(comment.getImgs()[2])
                    .placeholder(R.drawable.bucket_no_picture)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.imageView_detail03);
        }

        return view;
    }

    public class ViewHolder{
        @BindView(R.id.detail_list_imageView_picture)
        CircleImageView circleImageView;
        @BindView(R.id.detail_list_username)
        TextView textView_userName;
        @BindView(R.id.detail_list_textView_date)
        TextView textView_date;
        @BindView(R.id.detail_list_rating)
        ImageView imageView_rating;
        @BindView(R.id.detail_list_price)
        TextView textView_price;
        @BindView(R.id.detail_list_textView_detail)
        TextView textView_detail;
        @BindView(R.id.detail_list_imageView01)
        ImageView imageView_detail01;
        @BindView(R.id.detail_list_imageView02)
        ImageView imageView_detail02;
        @BindView(R.id.detail_list_imageView03)
        ImageView imageView_detail03;
        @BindView(R.id.detail_list_linearLayout)
        LinearLayout linearLayout_list;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
