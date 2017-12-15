package com.example.frescogif.view.radomLovew;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;


import com.example.frescogif.R;

import java.util.ArrayList;
import java.util.Random;

import static com.example.frescogif.R.mipmap.free_gift_full_dynamic;

/**
 * Created by GG on 2017/8/24.
 */
public class RandomHeartView extends View
{

    private ArrayList<RandomheartMovie> playlist;

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

    Drawable drawable;
    float    positionX;
    float    positionY;
    float    rotation;
    long     starttime;
    long     duration = 5000;

    private View dstChild;
    private float finalX;
    private float finalY;

    public RandomHeartView(Context context)
    {
        this(context, null);
    }

    public RandomHeartView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RandomHeartView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        drawable = getResources().getDrawable( free_gift_full_dynamic);
        playlist = new ArrayList<RandomheartMovie>();
    }

    public void setDstChild(View dstChild)
    {
        this.dstChild = dstChild;
    }

    public void playHeartAnim(long fromtime)
    {

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
        rotation = -90 + 180 * f1;

        RandomheartMovie movie = new RandomheartMovie(rotation ,finalX,finalY,drawable, duration);
        playlist.add(movie);

        invalidate();

    }

    @Override
    public void onDraw(Canvas canvas)
    {
            super.onDraw(canvas);
            for (RandomheartMovie movie : playlist)
            {
                movie.draw(canvas,getMeasuredWidth(),getMeasuredHeight());
            }

    }

    @Override
    public void computeScroll()
    {

            for(int x = 0; x < playlist.size(); x++){

                RandomheartMovie movie = playlist.get(x);
                if(movie.isFinish()){
                    playlist.remove(movie);
                }else {
                    movie.move();
                }
                invalidate();
            }

    }

}
