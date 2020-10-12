package com.example.frescogif.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;


import com.example.frescogif.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanwei on 2017/8/29.
 */

public class BeatingWaveView extends View
{

    public static class Beating
    {
        private long       position;
        private long       playLoop;
        private float      progress;
        private long       fromtime;
        private long       duration;
        private boolean    loopmode;
        private Drawable[] resource;

        public Beating(long fromtime, long duration, boolean loopmode, Drawable[] resource)
        {
            this.position = 0;
            this.playLoop = 1;
            this.progress = 0;
            this.fromtime = fromtime;
            this.duration = duration;
            this.loopmode = loopmode;
            this.resource = resource;
        }

        public Beating setPlayLoop(long playLoop)
        {
            this.playLoop = playLoop;
            return this;
        }

        public void cancleLoop()
        {
            if (loopmode)
            {
                long time = AnimationUtils.currentAnimationTimeMillis();
                fromtime = (long) (time - progress * duration);
                loopmode = false;
            }
        }

        public void updateProgess()
        {
            long time = AnimationUtils.currentAnimationTimeMillis();
            long pass = time - fromtime;

            if (loopmode)
            {
                if (pass > duration)
                {
                    pass = pass % duration;
                    fromtime = time - pass;
                }
            }
            else
            {
                position = pass / duration;

                if (pass > duration)
                {
                    pass = pass % duration;
                }
            }

            progress = pass / (float) (duration);
            progress = Math.min(progress, 1);
            progress = Math.max(progress, 0);
        }

        public long finishTime()
        {
            return fromtime + duration * playLoop;
        }

        public boolean finished()
        {
            return !loopmode && position >= playLoop;
        }

        public Drawable getDrawable()
        {
            int position = (int) (progress * (resource.length - 1));
            return resource[position];
        }

    }

    private Drawable[]    resource;
    private List<Beating> beatings;

    private static void drawDrawable(Canvas canvas, Drawable drawable, int sx, int sy)
    {
        float f1 = drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        float f2 = sx / (float) sy;

        if (f1 >= f2)
        {
            int dh = (int) (sx / f1);
            drawable.setBounds(0, (sy - dh) / 2, sx, (sy + dh) / 2);
        }
        else
        {
            int dw = (int) (sy * f1);
            drawable.setBounds((sx - dw) / 2, 0, (sx + dw) / 2, sy);
        }

        drawable.draw(canvas);

    }

    public BeatingWaveView(Context context)
    {
        this(context, null);
    }

    public BeatingWaveView(Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BeatingWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        beatings = new ArrayList<>();

      /*  TypedArray t = context.getResources().obtainTypedArray(R.array.beatingHeart);
        int[] array = context.getResources().getIntArray(R.array.beatingHeart);
        resource = new Drawable[array.length];

        for (int i = 0; i < array.length; i++)
        {
            int id = t.getResourceId(i, 0);
            resource[i] = LiveCompat.getDrawable(context, id);
        }
        t.recycle();*/

    }

    public BeatingWaveView clearBeating()
    {
        beatings.clear();
        return this;
    }

    public long playBeaintg(long loop_count, long heartCount)
    {
        if (heartCount >= 8000)
        {
            beatings.clear();
        }

        int size = beatings.size();

        // 如果有新的心跳任务，先取消循环模式
        if(size > 0){
            beatings.get(size-1).cancleLoop();
        }

        long fromtime = size > 0 ? beatings.get(size - 1).finishTime() : AnimationUtils.currentAnimationTimeMillis();
        long duration = 0;

        int length = resource.length;

        if (heartCount < 2000)
        {
            duration = 1000;
        }
        else if (heartCount < 4000)
        {
            duration = 1000 * length / (length + 15);
        }
        else if (heartCount < 6000)
        {
            duration = 1000 * length / (length + 30);
        }
        else if (heartCount < 8000)
        {
            duration = 1000 * length / (length + 45);
        }
        else if (heartCount < 10000)
        {
            duration = 1000 * length / (length + 60);
        }
        else
        {
            duration = 1000 * length / (length + 75);
        }

        Beating beating = new Beating(fromtime, duration, heartCount >= 8000, resource).setPlayLoop(heartCount < 8000 ? loop_count : 1);
        beatings.add(beating);
        invalidate();
        return fromtime;
    }

    public long playBeating(long heartCount)
    {
        return playBeaintg(1, heartCount);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int sx = getMeasuredWidth();
        int sy = getMeasuredHeight();

        if (beatings.isEmpty())
        {
            Drawable drawable = getResources().getDrawable(R.mipmap.water_level_icon);//resource[0];
            drawDrawable(canvas, drawable, sx, sy);
        }
        else
        {
            Drawable drawable = beatings.get(0).getDrawable();
            drawDrawable(canvas, drawable, sx, sy);
        }

    }

    @Override
    public void computeScroll()
    {
        if (beatings.size() > 0)
        {

            if (beatings.get(0).finished())
            {
                beatings.remove(0);
            }
            else
            {
                beatings.get(0).updateProgess();
            }

            invalidate();
        }
    }

}
