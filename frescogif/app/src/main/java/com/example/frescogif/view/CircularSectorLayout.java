package com.example.frescogif.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.frescogif.utils.MediaUtils;


/**
 *
 * 扇形自定义view
 * Created by GG on 2017/6/23.
 */
public class CircularSectorLayout extends View{

    private int hight;
    private int width;

    public CircularSectorLayout(Context context) {
        super(context);
    }

    public CircularSectorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularSectorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         hight = getMeasuredHeight();
         width = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 创建画笔
        Paint p = new Paint();
        p.setColor(Color.WHITE);// 设置红色
        Shader mShader = new LinearGradient(0, 0, width, hight,
                new int[] { Color.WHITE,Color.WHITE }, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        p.setShader(mShader);
        // p.setColor(Color.BLUE);
        RectF oval2 = new RectF(-900 , 0, 2000, 2400);// 设置个新的长方形，扫描测量
//        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线
        canvas.drawArc(oval2, 225, 90, true, p);
//        canvas.drawCircle(width/2, hight/2 + MediaUtils.dip2px(getContext(),110), 1100, p);
    }
}
