package com.example.frescogif;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by yuanwei on 17/3/17.
 */
public class PageMarkView extends View
{

    //页面的总数量
    int pageCount = 0;

    //当前页面的位置
    int pageIndex = 0;

    //标记之间的间隔
    int spaceSize = 0;

    //图片的显示尺寸
    int imageSize = 0;

    Paint dotPaint = new Paint();

    Drawable normal = null;
    Drawable select = null;

    public PageMarkView(Context context)
    {
        this(context, null);
    }

    public PageMarkView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public PageMarkView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        this(context, attrs, defStyleAttr);
    }

    public PageMarkView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        dotPaint.setAntiAlias(true);
        dotPaint.setStyle(Paint.Style.FILL);

        float density = context.getResources().getDisplayMetrics().density;

        int imageDefaultSize = (int) (density * 20 + 0.5f);
        int spaceDefaultSize = (int) (density * 10 + 0.5f);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageMarkView);

        imageSize = a.getDimensionPixelSize(R.styleable.PageMarkView_pageImageSize, imageDefaultSize);
        spaceSize = a.getDimensionPixelSize(R.styleable.PageMarkView_pageSpaceSize, spaceDefaultSize);

        normal = a.getDrawable(R.styleable.PageMarkView_pageImageNormal);
        select = a.getDrawable(R.styleable.PageMarkView_pageImageSelect);

        a.recycle();
    }

    public int getPageIndex()
    {
        return pageIndex;
    }

    public PageMarkView setPageCount(int count)
    {
        if (pageCount != count)
        {
            pageCount = count;
            requestLayout();
        }

        return this;
    }

    public PageMarkView setPageIndex(int index)
    {
        if (pageIndex != index)
        {
            pageIndex = index;
            invalidate();
        }

        return this;
    }

    public PageMarkView setPageInfo(int count, int index)
    {
        setPageCount(count);
        setPageIndex(index);
        return this;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int measuredWidth = 0, measuredHeight = 0;

        if (widthMode == MeasureSpec.EXACTLY)
        {
            measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        else
        {
            measuredWidth = getMainWidth();
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        }
        else
        {
            measuredHeight = imageSize;
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    public int getMainWidth()
    {
        int width = pageCount * (spaceSize + imageSize) - spaceSize;
        return Math.max(0, width);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int mainWidth = getMainWidth();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int offet = (width - mainWidth) / 2;

        int colorSelect = Color.parseColor("#fe306a");
        int colorBlack = Color.parseColor("#000000");


        for (int i = 0; i < pageCount; i++)
        {
            Drawable drawable = pageIndex == i ? select : normal;

            int left = offet + (imageSize + spaceSize) * i;
            int right = left + imageSize;
            int top = (height - imageSize) / 2;
            int bottom = (height + imageSize) / 2;

            if (drawable != null)
            {
                drawable.setBounds(left, top, right, bottom);
                drawable.draw(canvas);
            }
            else
            {
                float cx = (left + right) / 2;
                float cy = (top + bottom) / 2;
                float radius = imageSize / 2;
                dotPaint.setColor(pageIndex == i ? colorSelect : colorBlack);
                canvas.drawCircle(cx, cy, radius, dotPaint);
            }
        }


    }

}
