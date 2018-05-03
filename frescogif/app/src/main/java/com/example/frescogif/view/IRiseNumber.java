package com.example.frescogif.view;

/**
 * Created by GG on 2018/4/28.
 */

/**
 * 增长的数字接口
 */
public interface IRiseNumber {
    /**
     * 开始播放动画的方法
     */
    public void start();

    /**
     * 设置小数
     *
     * @param number
     * @return
     */
    public void withNumber(float number);

    /**
     * 设置整数
     *
     * @param number
     * @return
     */
    public void withNumber(int number);

    void setFromAndEndNumber(int fromNumber, int endNumber);

    void setFromAndEndNumber(float fromNumber, float endNumber);

    /**
     * 设置动画播放时长
     *
     * @param duration
     * @return
     */
    public void setDuration(long duration);

    void setOnEndListener(NumberScrollTextView.EndListener callback);

}