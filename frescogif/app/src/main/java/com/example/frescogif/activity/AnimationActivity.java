package com.example.frescogif.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.FrameLayout;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;

/**
 * Created by GG on 2017/12/14.
 */

public class AnimationActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout mainFlCardBack;
    private FrameLayout mainFlCardFront;
    private FrameLayout mainFlContainer;
    private AnimatorSet mRightOutSet;
    private AnimatorSet mLeftInSet;
    private boolean mIsShowBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_animation);
        initView();
    }

    private void initView() {
        mainFlContainer = (FrameLayout) findViewById(R.id.main_fl_container);
        mainFlCardBack = (FrameLayout) findViewById(R.id.main_fl_card_back);
        mainFlCardFront = (FrameLayout) findViewById(R.id.main_fl_card_front);

        mainFlContainer.setOnClickListener(this);
        setAnimators(); // 设置动画
        setCameraDistance(); // 设置镜头距离
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.main_fl_container:
                flipCard(mainFlContainer);
                break;
        }
    }

    // 翻转卡片
    public void flipCard(View view) {
        // 正面朝上
        if (!mIsShowBack) {
            mRightOutSet.setTarget(mainFlCardFront);
            mLeftInSet.setTarget(mainFlCardBack);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = true;
        } else { // 背面朝上
            mRightOutSet.setTarget(mainFlCardBack);
            mLeftInSet.setTarget(mainFlCardFront);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = false;
        }
    }
    // 设置动画
    private void setAnimators() {
        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_out);
        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_in);

        // 设置点击事件
        mRightOutSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mainFlContainer.setClickable(false);
            }
        });
        mLeftInSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mainFlContainer.setClickable(true);
            }
        });
    }

    // 改变视角距离, 贴近屏幕
    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mainFlCardFront.setCameraDistance(scale);
        mainFlCardBack.setCameraDistance(scale);
    }


}
