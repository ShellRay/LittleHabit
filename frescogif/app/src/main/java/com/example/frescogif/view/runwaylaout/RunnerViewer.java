package com.example.frescogif.view.runwaylaout;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by yuanwei on 17/4/12.
 */
public class RunnerViewer extends FrameLayout
{

    private  int duration;
    RunnerLayout parent;

    Scroller mScroller;

    int content_width;

    int contentHeight;

    public RunnerViewer(Context context, RunnerLayout parent,int duration)
    {
        super(context);
        this.duration = duration;
//        setBackgroundColor(Color.GREEN);
        mScroller = new Scroller(parent.getContext(), new LinearInterpolator());
        this.parent = parent;
    }

    public RunnerViewer(RunnerLayout parent)
    {
        this(parent, null);
    }

    public RunnerViewer(RunnerLayout parent, AttributeSet attrs)
    {
        this(parent, attrs, 0);
    }

    public RunnerViewer(RunnerLayout parent, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        this(parent, attrs, defStyleAttr);
    }

    public RunnerViewer(RunnerLayout parent, AttributeSet attrs, int defStyleAttr)
    {
        super(parent.getContext(), attrs, defStyleAttr);
        mScroller = new Scroller(parent.getContext(), new LinearInterpolator());
        this.parent = parent;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childMeasureSpec = MeasureSpec.makeMeasureSpec(10000, MeasureSpec.UNSPECIFIED);
        int max_width = 0, maxHeight = 0;
        int count = getChildCount();

        for (int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            child.measure(childMeasureSpec, childMeasureSpec);
            int child_width = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            max_width = Math.max(max_width, child_width);
            maxHeight = Math.max(maxHeight, childHeight);
        }

        if (max_width > 0)
        {
            content_width = max_width;
        }

        if (maxHeight > 0)
        {
            contentHeight = maxHeight;
        }

        int this_width = widthMode == MeasureSpec.EXACTLY ? widthSize : content_width;
        int thisHeight = heightMode == MeasureSpec.EXACTLY ? heightSize : contentHeight;

        //        int this_width = max_width;
        //        int thisHeight = maxHeight;

        setMeasuredDimension(this_width, thisHeight);

        if (count > 0)
        {
            checkViewScroll();
        }
    }

    public void checkViewScroll()
    {
        View target = getChildAt(0);
        int finalX = target.getMeasuredWidth();
        int stayed = mScroller.isFinished() ? getScrollX() : mScroller.getFinalX();

        if (finalX != stayed)
        {
            int startX = -getMeasuredWidth();
//            int duration = (finalX - startX) * 5;
            mScroller.abortAnimation();
            mScroller.startScroll(startX, 0, finalX - startX, 0, duration);
            invalidate();
        }
    }

    @Override
    public void computeScroll()
    {

        if (mScroller.computeScrollOffset())//滚动未完成
        {
            int sx = mScroller.getCurrX();
            int sy = mScroller.getCurrY();
            scrollTo(sx, sy);
            invalidate();
            return;
        }

        if (mScroller.isFinished())
        {
            if (getScrollX() != 0)
            {
                scrollTo(0, getScrollY());
            }

            if (getChildCount() > 0)
            {
                removeAllViewsInLayout();
                parent.runnerAvaliable(this,true);
            }
        }
    }

}
