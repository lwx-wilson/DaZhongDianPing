package com.liang.administrator.dazhongdianping.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.util.Measure;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.liang.administrator.dazhongdianping.R;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class MyLetterView extends View{

    private String[] letters = new String[]{"热门", "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};

    Paint paint;
    OnTouchLetterListener listener;
    int letterColor;


    public MyLetterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.MyLetterView);
        letterColor = t.getColor(R.styleable.MyLetterView_letter_color, Color.LTGRAY);

        t.recycle();

        initialPaint();
    }

    public void setOnTouchLetterListener(OnTouchLetterListener listener){
        this.listener = listener;
    }

    private void initialPaint() {

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        paint.setTextSize(size);
        paint.setColor(letterColor);
    }

    //用来设定MyLetterView的宽和高
    //View的onMeasure并不一定要重写
    //本身已经有很多的设定尺寸的代码
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.AT_MOST){
            int lPadding = getPaddingLeft();
            int rPadding = getPaddingRight();
            int contentWidth = 0;
            for (int i = 0; i < letters.length; i++){
                String letter = letters[i];

                Rect bounds = new Rect();
                paint.getTextBounds(letter, 0, letter.length(), bounds);

                if (bounds.width() > contentWidth){
                    contentWidth = bounds.width();
                }
            }

            int size = lPadding + contentWidth + rPadding;
            setMeasuredDimension(size, MeasureSpec.getSize(heightMeasureSpec));
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();//MYLetterView整体的宽度
        int height = getHeight();//MyLetterView整体的高度

        for (int i = 0; i < letters.length; i++){
            String letter = letters[i];

            //获取文字的边界大小
            Rect bounds = new Rect();
            paint.getTextBounds(letters[i], 0, letter.length(), bounds);

            float x = width/2 - bounds.width()/2;
            float y = height/letters.length/2 + bounds.height()/2 + i * height/letters.length;
            canvas.drawText(letter, x, y, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                //改变背景
                setBackgroundColor(Color.GRAY);
                if (listener != null){
                    float y = event.getY();
                    int index = (int) (y*letters.length/getHeight());
                    if (index >= 0 && index < letters.length){
                        String letter = letters[index];
                        listener.onTouchLetter(letter);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:

                setBackgroundColor(Color.TRANSPARENT);

                break;
        }

        return true;
    }

    public interface OnTouchLetterListener{
        void onTouchLetter(String letter);
    }
}
