package com.example.frescogif.view;

/**
 * Created by GG on 2018/4/28.
 */

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.frescogif.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * NumberScrollTextView,用属性动画完成数字平滑滚动
 */
public class NumberScrollTextView extends AppCompatTextView implements IRiseNumber {

    public static class NumberTexts {
        private float endNumber;
        private float fromNumber;
        private long duration;
        private int numberType;

        public NumberTexts(float fromNumber, float endNumber, long duration, int numberType) {
            this.fromNumber = fromNumber;
            this.endNumber = endNumber;
            this.duration = duration;
            this.numberType = numberType;
        }
    }

    private static final int STOPPED = 0;

    private static final int RUNNING = 1;

    private int mPlayingState = STOPPED;

    private float number;

    private float fromNumber;

    /**
     * 默认时长
     */
    private long duration = 1000;
    /**
     * 1.int 2.float
     */
    private int numberType = 2;

    private DecimalFormat fnum;

    private EndListener mEndListener = null;

    final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE};
    private List<NumberTexts> cacheList;

    /**
     * 构造方法
     *
     * @param context
     */
    public NumberScrollTextView(Context context) {
        super(context);
    }

    /**
     * 构造方法
     *
     * @param context
     * @param attr
     */
    public NumberScrollTextView(Context context, AttributeSet attr) {
        super(context, attr);
//        setTextColor(context.getResources().getColor(R.color.colorgold));
        cacheList = new ArrayList<>();
    }

    public NumberScrollTextView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }


    /**
     * 动画是否正在执行
     *
     * @return
     */
    public boolean isRunning() {
        return (mPlayingState == RUNNING);
    }

    /**
     * 浮点型数字变动
     *
     * @param fromNumber
     * @param endNumber
     * @param duration
     */
    private void runFloat(float fromNumber, float endNumber, long duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromNumber, endNumber);
        valueAnimator.setDuration(duration);

        valueAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {

                        setText(fnum.format(Float.parseFloat(valueAnimator
                                .getAnimatedValue().toString())));
                        if (valueAnimator.getAnimatedFraction() >= 1) {
                            mPlayingState = STOPPED;
                            cacheList.remove(0);
                            if (cacheList.size() > 0) {
                                mPlayingState = RUNNING;
                                if (cacheList.get(0).numberType == 1)
                                    runInt(cacheList.get(0).fromNumber, cacheList.get(0).endNumber, cacheList.get(0).duration);
                                else
                                    runFloat(cacheList.get(0).fromNumber, cacheList.get(0).endNumber, cacheList.get(0).duration);
                            }

                            if (mEndListener != null)
                                mEndListener.onEndFinish();
                        }
                    }

                });

        valueAnimator.start();
    }

    /**
     * 整型数字变动
     *
     * @param fromNumber
     * @param endNumber
     * @param duration
     */
    private void runInt(float fromNumber, float endNumber, long duration) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) fromNumber,
                (int) endNumber);
        valueAnimator.setDuration(duration);

        valueAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {

                        setText(valueAnimator.getAnimatedValue().toString());

                        if (valueAnimator.getAnimatedFraction() >= 1) {
                            mPlayingState = STOPPED;
                            cacheList.remove(0);
                            if (cacheList.size() > 0) {
                                mPlayingState = RUNNING;
                                if (cacheList.get(0).numberType == 1)
                                    runInt(cacheList.get(0).fromNumber, cacheList.get(0).endNumber, cacheList.get(0).duration);
                                else
                                    runFloat(cacheList.get(0).fromNumber, cacheList.get(0).endNumber, cacheList.get(0).duration);
                            }
                            if (mEndListener != null)
                                mEndListener.onEndFinish();
                        }
                    }
                });

        valueAnimator.start();

    }

    static int sizeOfInt(int x) {
        for (int i = 0; ; i++) {
            if (x <= sizeTable[i])
                return i + 1;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        fnum = new DecimalFormat("##0.00");
    }

    /**
     * 开始动画
     */
    @Override
    public void start() {

        cacheList.add(new NumberTexts(fromNumber, number, duration, numberType));

        if (!isRunning() && cacheList.size() == 1) {
            mPlayingState = RUNNING;
            if (cacheList.get(0).numberType == 1)
                runInt(cacheList.get(0).fromNumber, cacheList.get(0).endNumber, cacheList.get(0).duration);
            else
                runFloat(cacheList.get(0).fromNumber, cacheList.get(0).endNumber, cacheList.get(0).duration);
        }
    }

    public void clearCacheList() {
        cacheList.clear();
    }


    /**
     * 设置数字
     *
     * @param number
     */
    @Override
    public void withNumber(int number) {
        this.number = number;
        numberType = 1;
        if (number > 1000) {
            fromNumber = number
                    - (float) Math.pow(10, sizeOfInt((int) number) - 2);
        } else {
            fromNumber = number / 2;
        }
    }

    /**
     * 设置数字
     *
     * @param number
     */
    @Override
    public void withNumber(float number) {

        this.number = number;
        numberType = 2;
        if (number > 1000) {
            fromNumber = number
                    - (float) Math.pow(10, sizeOfInt((int) number) - 1);
        } else {
            fromNumber = number / 2;
        }

    }

    @Override
    public void setFromAndEndNumber(int fromNumber, int endNumber) {
        this.fromNumber = fromNumber;
        this.number = endNumber;
        numberType = 1;


    }

    @Override
    public void setFromAndEndNumber(float fromNumber, float endNumber) {
        this.fromNumber = fromNumber;
        this.number = endNumber;
        numberType = 2;
    }

    /**
     * 设置动画时长
     */
    @Override
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * 设置动画结束监听
     */
    @Override
    public void setOnEndListener(EndListener callback) {
        mEndListener = callback;
    }

    /**
     * 动画结束接口
     */
    public interface EndListener {
        /**
         * 动画结束
         */
        public void onEndFinish();
    }

}