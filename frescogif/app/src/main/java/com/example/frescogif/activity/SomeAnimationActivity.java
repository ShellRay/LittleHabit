package com.example.frescogif.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;

/**
 * Created by Administrator on 2017/8/6.
 */

public class SomeAnimationActivity extends BaseActivity implements View.OnClickListener, Animation.AnimationListener {

    private ImageView iv_super_man;
    private Button btn_click;
    private RotateAnimation animation_rotate;

    int count;
    private boolean started;
    private boolean imgStarted;
    private ImageView imageView;
    private RelativeLayout rl_root;
    private int measuredHeight;
    private int measuredWidth;
    private ImageView imageView1;
    private ImageView imageView2;
    private ScaleAnimation animation_scale;
    private AlphaAnimation animation_alpha;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_some_animation);

        iv_super_man = (ImageView) findViewById(R.id.iv_super_man);
        btn_click = (Button) findViewById(R.id.btn_click);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);

        measuredWidth = getWindowManager().getDefaultDisplay().getWidth();
        measuredHeight = getWindowManager().getDefaultDisplay().getHeight();
        Toast.makeText(this,"height"+measuredHeight + "----width"+ measuredWidth ,Toast.LENGTH_SHORT).show();

        btn_click.setOnClickListener(this);

        initAnimation();
    }


    private void initAnimation() {
        animation_rotate = new RotateAnimation(0, -720,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation_rotate.setRepeatCount(0);
        animation_rotate.setDuration(1000);//设置时间持续时间为 5000毫秒
        animation_rotate.setAnimationListener(this);

        animation_scale = new ScaleAnimation(0.1f,3.0f,0.1f,3.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        //第一个参数fromX为动画起始时 X坐标上的伸缩尺寸
        //第二个参数toX为动画结束时 X坐标上的伸缩尺寸
        //第三个参数fromY为动画起始时Y坐标上的伸缩尺寸
        //第四个参数toY为动画结束时Y坐标上的伸缩尺寸
    /*说明:
                        以上四种属性值
          0.0表示收缩到没有
          1.0表示正常无伸缩
                       值小于1.0表示收缩
                       值大于1.0表示放大
    */
        //第五个参数pivotXType为动画在X轴相对于物件位置类型
        //第六个参数pivotXValue为动画相对于物件的X坐标的开始位置
        //第七个参数pivotXType为动画在Y轴相对于物件位置类型
        //第八个参数pivotYValue为动画相对于物件的Y坐标的开始位置
        animation_scale.setRepeatCount(0);
        animation_scale.setDuration(1000);//设置时间持续时间为 5000毫秒

        /**
         * 透明度控制动画效果 alpha
         */
        animation_alpha = new AlphaAnimation(1.0f,0f);
        //第一个参数fromAlpha为 动画开始时候透明度
        //第二个参数toAlpha为 动画结束时候透明度
        animation_alpha.setRepeatCount(0);//设置循环
        animation_alpha.setDuration(1000);//设置时间持续时间为 5000毫秒
        animation_alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imgStarted = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rl_root.removeView(imageView1);

                if (count >0){
                    setImageAniamtion();
                }
                if(count == 0) {
                    imgStarted = false;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
//        ++count;
        count = 50;
        if(!started && count >0 && !imgStarted){
            setAniamtion();
            setImageAniamtion();
        }
    }

    private void setImageAniamtion() {
        imageView1 = new ImageView(this);
        imageView1.setImageResource(R.drawable.heart);
        float v = (float)(Math.random() * 360);
        imageView1.setRotation(v);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        int intrinsicHeight = getResources().getDrawable(R.drawable.heart).getIntrinsicHeight();
        int intrinsicWidth = getResources().getDrawable(R.drawable.heart).getIntrinsicWidth();
        imageView1.measure(intrinsicWidth,intrinsicHeight);
        int width = imageView1.getMeasuredWidth();
        int height = imageView1.getMeasuredHeight();
        int topMargin = (int)(Math.random() * (measuredHeight - height));
        int leftMargin = (int)(Math.random() * (measuredWidth - width));
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        Log.e("SomeAnimationActivity","leftMargin =" +leftMargin +"----topMargin=" +
                "" +topMargin  + "+++++++width" +width + "----height" + height);
        rl_root.addView(imageView1,params);
        imageView1.startAnimation(animation_alpha);
        --count;
    }

    private void setAniamtion() {
        iv_super_man.startAnimation(animation_rotate);

    }

    @Override
    public void onAnimationStart(Animation animation) {
        started = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (count >0){
            setAniamtion();
        }
        if(count == 0) {
            started = false;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
