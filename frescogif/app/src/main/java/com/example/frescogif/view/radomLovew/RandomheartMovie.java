package com.example.frescogif.view.radomLovew;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.AnimationUtils;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by yuanwei on 17/3/28.
 */
public class RandomheartMovie
{
    private  String TAG = "RandomheartMovie";

    static class Curve
    {
        final float radioX;

        final float radioY;

        public Curve(float radioX, float radioY)
        {
            this.radioX = radioX;
            this.radioY = radioY;
        }
    }


    private Drawable drawable;
    private Curve[]  movepath;
    private float    rotation;
    private float    progress;
    private long     duration;
    private long     fromtime;

    public RandomheartMovie(float rotation, float waveCenterX, float waveCenterY, Drawable drawable, long duration)
    {
        this.drawable = drawable;
        this.duration = duration;
        this.fromtime = 0;

        Random rd = new Random();
        this.rotation = rotation;

        float f2 = rd.nextFloat();
        float positionX = 0.2f + f2 * 0.6f;

        float f3 = rd.nextFloat();
        float positionY = 0.2f + f3 * 0.6f;

        Log.e(TAG,"positionX" + positionX +"positionY"+positionY);
        movepath = new Curve[4];
        movepath[0] = new Curve(positionX, positionY);
        movepath[1] = new Curve(positionX * 0.8f, positionY * 0.8f);
        movepath[2] = new Curve(positionX * 0.5f, positionY * 0.5f);
        movepath[3] = new Curve(waveCenterX, waveCenterY);

    }

    public boolean isFinish()
    {
        return progress >= 1;
    }

    public void move()
    {

        long currtime = System.currentTimeMillis();

        if (fromtime <= 0)
        {
            fromtime = currtime;
        }

        long timepast = currtime - fromtime;

        if (timepast < 0)
        {
            fromtime = currtime;
            timepast = 0;
        }

        progress = timepast / (float) duration;
        progress = Math.max(progress, 0);
        progress = Math.min(progress, 1);

    }

    public void draw(Canvas canvas, int screen_width, int screenHeight)
    {


        float perio = 1.0f / (movepath.length - 1);

        int index1 = (int) (progress / perio);

        int index2 = index1 + 1;

        float radiox = 0, radioy = 0;

        if (index2 >= movepath.length)
        {
            Curve curve = movepath[index1];
            radiox = curve.radioX;
            radioy = curve.radioY;
            Log.e(TAG,"radiox+ "+radiox+"===radiox+ "+radiox);
        }
        else
        {
            Curve c1 = movepath[index1];
            Curve c2 = movepath[index2];

            float t1 = perio * index1;
            float t2 = perio * index2;

            radiox = c1.radioX + (c2.radioX - c1.radioX) * (progress - t1) / (t2 - t1);
            radioy = c1.radioY + (c2.radioY - c1.radioY) * (progress - t1) / (t2 - t1);
        }

        int centerX = (int) (screen_width * radiox);
        int centerY = (int) (screenHeight * radioy);
        canvas.rotate(rotation,centerX,centerY);


        float scale = 1.0f;

       /* if (progress < 0.5f)
        {
            scale = (1 - progress)*0.5f + 0.5f;
        }*/
        scale = (1 - progress) + 0.5f;

//        float scale = 1.0f - (1 - progress) * 1.2f;

        float alpha = 1.0f;

        if (progress > 0.5f)
        {
            alpha = 1 - progress;
        }
        float progress1 = progress;
        if(progress1 + 0.2f < 1f){
            progress1 = progress1 + 0.2f;
        }



        int intAlpha = (int)(255*alpha);

        int image_width = (int) (scale * drawable.getIntrinsicWidth());
        int imageHeight = (int) (scale * drawable.getIntrinsicHeight());

        int left = centerX - image_width / 2;
        int right = left + image_width;

        int top = centerY - imageHeight / 2;
        int bottom = top + imageHeight;

        drawable.setBounds(left, top, right, bottom);
        canvas.rotate(- rotation * progress1,centerX,centerY);
//        drawable.setAlpha(intAlpha);

        drawable.draw(canvas);
    }


}
