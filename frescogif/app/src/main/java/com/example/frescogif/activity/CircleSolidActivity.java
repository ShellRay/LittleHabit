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
            }
            super.handleMessage(msg);
        }
    };

    private SimpleDraweeView simpleView;
    int chache;
    int[] res = {R.mipmap.dead1, R.mipmap.dead2, R.mipmap.dead3, R.mipmap.dead4, R.mipmap.dead5, R.mipmap.dead6};
    private AnimatorSet set;
    private ObjectAnimator scaleY;
    private ObjectAnimator alpha;
    private ObjectAnimator scaleX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_solid);
        simpleView = (SimpleDraweeView) findViewById(R.id.simpleView);

        simpleView.setImageResource(res[chache]);
        chache++;

    }

    @SuppressLint("WrongConstant")
    public void onClick(View v) {
        initAnim();
    }

    @SuppressLint("WrongConstant")
    private void initAnim() {
        set = new AnimatorSet();
        /*ViewGroup.LayoutParams  lp = simpleView.getLayoutParams();
        lp.width =400;
        lp.height =400;
        simpleView.setLayoutParams(lp);*/
        //X轴方向缩放到原图大小
        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(simpleView, "scaleX", 1f, 1.2f);
        scaleX.setDuration(5000);
        scaleX.setRepeatMode(ValueAnimator.INFINITE);

        //Y轴方向缩放到原图大小
        final ObjectAnimator scaleY = ObjectAnimator.ofFloat(simpleView, "scaleY", 1f, 1.2f);
        scaleY.setDuration(5000);
        scaleY.setRepeatMode(ValueAnimator.INFINITE);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(simpleView, "alpha", 1f, 0f);
        alpha.setDuration(3000);
        alpha.setRepeatMode(ValueAnimator.INFINITE);

//        set.play(scaleY).with(scaleX);
//        set.play(scaleY).before(alpha);
        set.playSequentially(scaleX,scaleY,alpha);

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
        if(set != null){
            set.removeAllListeners();
        }

    }
}
