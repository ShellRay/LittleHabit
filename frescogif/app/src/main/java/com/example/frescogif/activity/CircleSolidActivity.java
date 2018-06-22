package com.example.frescogif.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.utils.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by GG on 2017/8/8.
 */
public class CircleSolidActivity extends BaseActivity {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                simpleView.setImageResource(res[chache % res.length]);
                chache++;
                initAnim();
//                initBuAnim();
            }
            super.handleMessage(msg);
        }
    };

    private SimpleDraweeView simpleView;
    int chache;
    int[] res = {R.mipmap.dead1, R.mipmap.dead2, R.mipmap.dead3, R.mipmap.dead4, R.mipmap.dead5, R.mipmap.dead6};
    private AnimatorSet setLast;
    private AlphaAnimation mHideAnimation;
    private ScaleAnimation scaleAnimation;
    private ObjectAnimator scaleX;
    private ObjectAnimator scaleY;
    private ObjectAnimator alpha;
    private ObjectAnimator alphaShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_solid);
        simpleView = (SimpleDraweeView) findViewById(R.id.simpleView);
        simpleView.setImageResource(res[chache]);
        chache++;

    }


    public void onClick(View v) {
        initAnim();
//        initBuAnim();
    }

    //补间动画
    public void initBuAnim(){
        AnimationSet animationSet = new AnimationSet(this,null);

        if(scaleAnimation == null) {
            scaleAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(0);
        scaleAnimation.setDuration(3000);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                simpleView.startAnimation(mHideAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if(mHideAnimation == null) {
            mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        }
        mHideAnimation.setDuration( 2000 );
        mHideAnimation.setFillAfter( false );
        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        animationSet.addAnimation(scaleAnimation1);
//        animationSet.addAnimation(mHideAnimation);
        simpleView.startAnimation(scaleAnimation);
    }

    //属性动画是会改变他的属性的，所以最后的透明动画完成重新启动的时候老是有白屏不显示然后突然图变大了
    //还有就是那个监听的问题了，是个循环比较
    private void initAnim() {
        if(setLast != null){
            setLast.cancel();
        }

        AnimatorSet   set = new AnimatorSet();
        setLast = set;
        /*ViewGroup.LayoutParams  lp = simpleView.getLayoutParams();
        lp.width = Utils.convertDpToPixel(this,296);
        lp.height = Utils.convertDpToPixel(this,317);
        simpleView.setLayoutParams(lp);*/
        if(alphaShow == null) {
            alphaShow = ObjectAnimator.ofFloat(simpleView, "alpha", 1f, 1f);
        }
        alphaShow.setDuration(1);
        alphaShow.setRepeatMode(ObjectAnimator.REVERSE);

        //X轴方向缩放到原图大小
        if(scaleX == null) {
            scaleX = ObjectAnimator.ofFloat(simpleView, "scaleX", 1f, 1.05f);
        }
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);

        scaleX.setDuration(4000);

        //Y轴方向缩放到原图大小
        if(scaleY == null) {
            scaleY = ObjectAnimator.ofFloat(simpleView, "scaleY", 1f, 1.05f);
        }
        scaleY.setDuration(4000);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);

        if(alpha == null) {
            alpha = ObjectAnimator.ofFloat(simpleView, "alpha", 1f, 0f);
        }
        alpha.setDuration(2000);
        alpha.setRepeatMode(ObjectAnimator.REVERSE);

        set.play(scaleY).with(scaleX).with(alphaShow);
        set.play(scaleY).before(alpha);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("shell", "onAnimationStart 一次");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                set.start();
                Log.e("shell", "onAnimationEnd 一次");
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("shell", "onAnimationCancel 一次");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("shell", "onAnimationRepeat 一次");
            }
        });
        set.start(); //开始播放动画
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(setLast != null){
            setLast.removeAllListeners();
        }

    }
}
