package com.example.frescogif.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.AutoTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/12/14.
 */

public class AnimationActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout mainFlCardBack;
    private FrameLayout mainFlCardFront;
    private FrameLayout mainFlContainer;
    private AutoTextView autoTextView;

    private AnimatorSet mRightOutSet;
    private AnimatorSet mLeftInSet;
    private boolean mIsShowBack;
    private List<String> info;
    private Handler handler = new Handler();
    private boolean isFlipping = false; // 是否启用预警信息轮播

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_animation);
        initView();

    }
    int count = 0;

    private void initView() {

        info = new ArrayList<>();
        info.add("心中有阳光，脚底有力量！");
        info.add("兮兮好棒 致 小鬼头：你始终是我的唯一");
        info.add("关关雎鸠 在河之洲");
        info.add("窗前明月光");
        info.add("疑似地上霜");
        info.add("巨头邀明月");

        mainFlContainer = (FrameLayout) findViewById(R.id.main_fl_container);
        mainFlCardBack = (FrameLayout) findViewById(R.id.main_fl_card_back);
        mainFlCardFront = (FrameLayout) findViewById(R.id.main_fl_card_front);
        autoTextView = (AutoTextView) findViewById(R.id.autoTextView);

        mainFlContainer.setOnClickListener(this);
        autoTextView.setOnClickListener(this);
        setAnimators(); // 设置动画
        setCameraDistance(); // 设置镜头距离

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isFlipping) {
                return;
            }
            count++;
            autoTextView.setText(info.get(count % info.size()));
            if (count == info.size()) {
                count = 0;
            }
            startFlipping();
        }
    };

    //开启信息轮播
    public void startFlipping() {
        if (info.size() > 1) {
            handler.removeCallbacks(runnable);
            isFlipping = true;
            handler.postDelayed(runnable, 3000);
        }
    }
    //关闭信息轮播
    public void stopFlipping() {
        if (info.size() > 1) {
            isFlipping = false;
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startFlipping();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopFlipping();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.main_fl_container:
                flipCard(mainFlContainer);
                break;
            case R.id.autoTextView:
//                autoTextView.next();
//
//                autoTextView.setText(info.get(count % info.size()));
//                count ++;
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
