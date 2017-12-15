package com.example.frescogif.view.radomLovew;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;


import com.example.frescogif.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by GG on 2017/8/24.
 */
public class RandomHeartView1 extends View
{

    static class HeartInfo
    {

        private final float startX;
        private final float startY;
        private final float finalX;
        private final float finalY;
        private final float angle;
        private final long  stamp;
        private       float value;

        public HeartInfo(float startX, float startY, float finalX, float finalY, float angle, long stamp)
        {
            this.startX = startX;
            this.startY = startY;
            this.finalX = finalX;
            this.finalY = finalY;
            this.angle = angle;
            this.stamp = stamp;
        }

        public float getStartX()
        {
            return startX;
        }

        public float getStartY()
        {
            return startY;
        }

        public float getValue()
        {
            return value;
        }

        public float getAngle()
        {
            return angle * (1 - value);
        }

        public boolean isFinished()
        {
            return value >= 1;
        }

        public void computeValue()
        {
            long duration = 1000;
            value = Math.min(AnimationUtils.currentAnimationTimeMillis() - stamp, duration) / (float) duration;
        }

    }

    private View            dstChild;
    private Drawable        drawable;
    private List<HeartInfo> playlist;

    public RandomHeartView1(Context context)
    {
        this(context, null);
    }

    public RandomHeartView1(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RandomHeartView1(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        drawable = context.getResources().getDrawable( R.mipmap.free_gift_full_dynamic);
        playlist = new ArrayList<>();
    }

    public void setDstChild(View dstChild)
    {
        this.dstChild = dstChild;
    }

    public void playHeartAnim()
    {
        float finalX = 0;
        float finalY = 0;

        if (dstChild != null)
        {
            int[] arr1 = new int[2];
            int[] arr2 = new int[2];

            getLocationInWindow(arr1);
            dstChild.getLocationInWindow(arr2);

            int x = arr2[0] - arr1[0] + dstChild.getMeasuredWidth() / 2;
            int y = arr2[1] - arr1[1] + dstChild.getMeasuredHeight() / 2;

            finalX = x / (float) getMeasuredWidth();
            finalY = y / (float) getMeasuredHeight();
        }

        Random rd = new Random();
        float f1 = rd.nextFloat();
        float angle = -45 + 90 * f1;

        float f2 = rd.nextFloat();
        float startX = 0.4f + f2 * 0.5f;

        float f3 = rd.nextFloat();
        float startY = 0.4f + f3 * 0.5f;

        long stamp = AnimationUtils.currentAnimationTimeMillis();

        playlist.add(new HeartInfo(startX, startY, finalX, finalY, angle, stamp));
        invalidate();

    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int datasize = playlist.size();

        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        float max_scale = 1.5f;
        float beg_scale = 0.5f;
        float dst_scale = 0.5f;

        for (int i = 0; i < datasize; i++)
        {
            HeartInfo info = playlist.get(i);

            float v = info.getValue();
            float s = 1.0f;
            float a = 0;

            int x = 0;
            int y = 0;

            if (v < 0.3f)
            {
                s = v * (max_scale - beg_scale) / 0.3f + beg_scale;
                x = (int) (info.getStartX() * w);
                y = (int) (info.getStartY() * h);
                a = v * info.angle / 0.3f;
            }
            else
            {
                s = (v - 0.3f) / 0.7f * (dst_scale - max_scale) + max_scale;

                float f = (v - 0.3f) / 0.7f;
                float cx = info.startX + f * (info.finalX - info.startX);
                float cy = info.startY + f * (info.finalY - info.startY);

                x = (int) (w * cx);
                y = (int) (h * cy);

                a = info.angle - info.angle * (v - 0.3f) / 0.7f;
            }

            int imageX = (int) (drawable.getIntrinsicWidth() * s);
            int imageY = (int) (drawable.getIntrinsicHeight() * s);


            drawable.setBounds(x - imageX / 2, y - imageY / 2, x + imageX / 2, y + imageY / 2);
            canvas.rotate(a, x, y);

            drawable.draw(canvas);

        }

    }


    @Override
    public void computeScroll()
    {
        int datasize = playlist.size();

        for (int i = datasize - 1; i >= 0; i--)
        {
            HeartInfo info = playlist.get(i);

            if (info.isFinished())
            {
                playlist.remove(i);
            }
            else
            {
                info.computeValue();
            }
        }

        if (playlist.size() > 0)
        {
            postInvalidate();
        }

    }

}
