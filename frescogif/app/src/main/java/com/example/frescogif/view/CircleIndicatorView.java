package com.example.frescogif.view;

/**
 * Created by GG on 2017/11/24.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.example.frescogif.R.styleable;
import com.example.frescogif.utils.MediaUtils;

import java.util.ArrayList;
import java.util.List;


public class CircleIndicatorView extends View implements ViewPager.OnPageChangeListener {
    private static final String[] LETTER = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "G", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int mSelectColor = Color.parseColor("#FFFFFF");
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private int mCount;
    private int mRadius;
    private int mStrokeWidth;
    private int mTextColor;
    private int mDotNormalColor;
    private int mSpace = 0;
    private List<Indicator> mIndicators;
    private int mSelectPosition = 0;
    private CircleIndicatorView.FillMode mFillMode;
    private ViewPager mViewPager;
    private CircleIndicatorView.OnIndicatorClickListener mOnIndicatorClickListener;
    private boolean mIsEnableClickSwitch;

    public CircleIndicatorView(Context context) {
        super(context);
        this.mFillMode = CircleIndicatorView.FillMode.NONE;
        this.mIsEnableClickSwitch = false;
        this.init();
    }

    public CircleIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mFillMode = CircleIndicatorView.FillMode.NONE;
        this.mIsEnableClickSwitch = false;
        this.getAttr(context, attrs);
        this.init();
    }

    public CircleIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mFillMode = CircleIndicatorView.FillMode.NONE;
        this.mIsEnableClickSwitch = false;
        this.getAttr(context, attrs);
        this.init();
    }

    @RequiresApi(
            api = 21
    )
    public CircleIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mFillMode = CircleIndicatorView.FillMode.NONE;
        this.mIsEnableClickSwitch = false;
        this.getAttr(context, attrs);
        this.init();
    }

    private void init() {
        this.mCirclePaint = new Paint();
        this.mCirclePaint.setDither(true);
        this.mCirclePaint.setAntiAlias(true);
        this.mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mTextPaint = new Paint();
        this.mTextPaint.setDither(true);
        this.mTextPaint.setAntiAlias(true);
        this.mIndicators = new ArrayList();
        this.initValue();
    }

    private void initValue() {
        this.mCirclePaint.setColor(this.mDotNormalColor);
        this.mCirclePaint.setStrokeWidth((float)this.mStrokeWidth);
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setTextSize((float)this.mRadius);
    }

    private void getAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, styleable.CircleIndicatorView);
        this.mRadius = typedArray.getDimensionPixelSize(styleable.CircleIndicatorView_indicatorRadius, MediaUtils.dip2px(context,6));
        this.mStrokeWidth = typedArray.getDimensionPixelSize(styleable.CircleIndicatorView_indicatorBorderWidth, MediaUtils.dip2px(context,2));
        this.mSpace = typedArray.getDimensionPixelSize(styleable.CircleIndicatorView_indicatorSpace, MediaUtils.dip2px(context,5));
        this.mTextColor = typedArray.getColor(styleable.CircleIndicatorView_indicatorTextColor, -16777216);
        this.mSelectColor = typedArray.getColor(styleable.CircleIndicatorView_indicatorSelectColor, -1);
        this.mDotNormalColor = typedArray.getColor(styleable.CircleIndicatorView_indicatorColor, -7829368);
        this.mIsEnableClickSwitch = typedArray.getBoolean(styleable.CircleIndicatorView_enableIndicatorSwitch, false);
        int fillMode = typedArray.getInt(styleable.CircleIndicatorView_fill_mode, 2);
        if(fillMode == 0) {
            this.mFillMode = CircleIndicatorView.FillMode.LETTER;
        } else if(fillMode == 1) {
            this.mFillMode = CircleIndicatorView.FillMode.NUMBER;
        } else {
            this.mFillMode = CircleIndicatorView.FillMode.NONE;
        }

        typedArray.recycle();
    }

    private void measureIndicator() {
        this.mIndicators.clear();
        float cx = 0.0F;

        for(int i = 0; i < this.mCount; ++i) {
            CircleIndicatorView.Indicator indicator = new CircleIndicatorView.Indicator();
            if(i == 0) {
                cx = (float)(this.mRadius + this.mStrokeWidth);
            } else {
                cx += (float)((this.mRadius + this.mStrokeWidth) * 2 + this.mSpace);
            }

            indicator.cx = cx;
            indicator.cy = (float)(this.getMeasuredHeight() / 2);
            this.mIndicators.add(indicator);
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (this.mRadius + this.mStrokeWidth) * 2 * this.mCount + this.mSpace * (this.mCount - 1);
        int height = this.mRadius * 2 + this.mSpace * 2;
        this.setMeasuredDimension(width, height);
        this.measureIndicator();
    }

    protected void onDraw(Canvas canvas) {
        for(int i = 0; i < this.mIndicators.size(); ++i) {
            CircleIndicatorView.Indicator indicator = (CircleIndicatorView.Indicator)this.mIndicators.get(i);
            float x = indicator.cx;
            float y = indicator.cy;
            if(this.mSelectPosition == i) {
                this.mCirclePaint.setStyle(Paint.Style.FILL);
                this.mCirclePaint.setColor(this.mSelectColor);
            } else {
                this.mCirclePaint.setColor(this.mDotNormalColor);
                if(this.mFillMode != CircleIndicatorView.FillMode.NONE) {
                    this.mCirclePaint.setStyle(Paint.Style.STROKE);
                } else {
                    this.mCirclePaint.setStyle(Paint.Style.FILL);
                }
            }

            canvas.drawCircle(x, y, (float)this.mRadius, this.mCirclePaint);
            if(this.mFillMode != CircleIndicatorView.FillMode.NONE) {
                String text = "";
                if(this.mFillMode == CircleIndicatorView.FillMode.LETTER) {
                    if(i >= 0 && i < LETTER.length) {
                        text = LETTER[i];
                    }
                } else {
                    text = String.valueOf(i + 1);
                }

                Rect bound = new Rect();
                this.mTextPaint.getTextBounds(text, 0, text.length(), bound);
                int textWidth = bound.width();
                int textHeight = bound.height();
                float textStartX = x - (float)(textWidth / 2);
                float textStartY = y + (float)(textHeight / 2);
                canvas.drawText(text, textStartX, textStartY, this.mTextPaint);
            }
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        float xPoint = 0.0F;
        float yPoint = 0.0F;
        switch(event.getAction()) {
            case 0:
                xPoint = event.getX();
                yPoint = event.getY();
                this.handleActionDown(xPoint, yPoint);
            default:
                return super.onTouchEvent(event);
        }
    }

    private void handleActionDown(float xDis, float yDis) {
        for(int i = 0; i < this.mIndicators.size(); ++i) {
            CircleIndicatorView.Indicator indicator = (CircleIndicatorView.Indicator)this.mIndicators.get(i);
            if(xDis < indicator.cx + (float)this.mRadius + (float)this.mStrokeWidth && xDis >= indicator.cx - (float)(this.mRadius + this.mStrokeWidth) && yDis >= yDis - (indicator.cy + (float)this.mStrokeWidth) && yDis < indicator.cy + (float)this.mRadius + (float)this.mStrokeWidth) {
                if(this.mIsEnableClickSwitch) {
                    this.mViewPager.setCurrentItem(i, false);
                }

                if(this.mOnIndicatorClickListener != null) {
                    this.mOnIndicatorClickListener.onSelected(i);
                }
                break;
            }
        }

    }

    public void setOnIndicatorClickListener(CircleIndicatorView.OnIndicatorClickListener onIndicatorClickListener) {
        this.mOnIndicatorClickListener = onIndicatorClickListener;
    }

    private void setCount(int count) {
        this.mCount = count;
        this.invalidate();
    }

    public void setBorderWidth(int borderWidth) {
        this.mStrokeWidth = borderWidth;
        this.initValue();
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        this.initValue();
    }

    public void setSelectColor(int selectColor) {
        this.mSelectColor = selectColor;
    }

    public void setDotNormalColor(int dotNormalColor) {
        this.mDotNormalColor = dotNormalColor;
        this.initValue();
    }

    public void setSelectPosition(int selectPosition) {
        this.mSelectPosition = selectPosition;
    }

    public void setFillMode(CircleIndicatorView.FillMode fillMode) {
        this.mFillMode = fillMode;
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
        this.initValue();
    }

    public void setSpace(int space) {
        this.mSpace = space;
    }

    public void setEnableClickSwitch(boolean enableClickSwitch) {
        this.mIsEnableClickSwitch = enableClickSwitch;
    }

    public void setUpWithViewPager(ViewPager viewPager) {
        this.releaseViewPager();
        if(viewPager != null) {
            this.mViewPager = viewPager;
            this.mViewPager.addOnPageChangeListener(this);
            int count = this.mViewPager.getAdapter().getCount();
            this.setCount(count);
        }
    }

    private void releaseViewPager() {
        if(this.mViewPager != null) {
            this.mViewPager.removeOnPageChangeListener(this);
            this.mViewPager = null;
        }

    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        this.mSelectPosition = position;
        this.invalidate();
    }

    public void onPageScrollStateChanged(int state) {
    }

    public static enum FillMode {
        LETTER,
        NUMBER,
        NONE;

        private FillMode() {
        }
    }

    public static class Indicator {
        public float cx;
        public float cy;

        public Indicator() {
        }
    }

    public interface OnIndicatorClickListener {
        void onSelected(int var1);
    }
}
