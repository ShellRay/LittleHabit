package com.example.frescogif.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.guide.NewbieGuide;
import com.example.frescogif.view.guide.core.Controller;
import com.example.frescogif.view.guide.listener.OnGuideChangedListener;
import com.example.frescogif.view.guide.listener.OnLayoutInflatedListener;
import com.example.frescogif.view.guide.listener.OnPageChangedListener;
import com.example.frescogif.view.guide.model.GuidePage;
import com.example.frescogif.view.guide.model.HighLight;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GG on 2017/12/14.
 */

public class GuideImageActivity extends BaseActivity {

    @BindView(R.id.etInput)
    EditText etInput;
    @BindView(R.id.tvCenter)
    TextView tvCenter;
    @BindView(R.id.tvBottom)
    TextView tvBottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_image);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);

        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(600);
        exitAnimation.setFillAfter(true);

        //新增多页模式，即一个引导层显示多页引导内容
        NewbieGuide.with(this)
                .setLabel("page")//设置引导层标示区分不同引导层，必传！否则报错
//                .anchor(anchor)
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        Log.e("shell", "NewbieGuide onShowed: ");
                        //引导层显示
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        Log.e("shell", "NewbieGuide  onRemoved: ");
                        //引导层消失（多页切换不会触发）
                    }
                })
                .setOnPageChangedListener(new OnPageChangedListener() {

                    @Override
                    public void onPageChanged(int page) {
                        //引导页切换，page为当前页位置，从0开始
                        Toast.makeText(GuideImageActivity.this, "引导页切换：" + page, Toast.LENGTH_SHORT).show();
                    }
                })
                .alwaysShow(true)//是否每次都显示引导层，默认false，只显示一次
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(tvCenter)//添加高亮的view
//                                .addHighLight(tvBottom,
//                                        new RelativeGuide(R.layout.view_relative_guide, Gravity.TOP, 100) {
//                                            @Override
//                                            protected void offsetMargin(MarginInfo marginInfo, ViewGroup viewGroup, View view) {
//                                                marginInfo.leftMargin += 100;
//                                            }
//                                        })
                                .setLayoutRes(R.layout.view_guide)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {

                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        //引导页布局填充后回调，用于初始化
                                        TextView tv = view.findViewById(R.id.textView2);
                                        tv.setText("我是动态设置的文本");
                                    }
                                })
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(tvBottom, HighLight.Shape.RECTANGLE, 20)
                        .setLayoutRes(R.layout.view_guide_custom, R.id.iv)//引导页布局，点击跳转下一页或者消失引导层的控件id
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, final Controller controller) {
                                view.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controller.showPreviewPage();
                                    }
                                });
                            }
                        })
                        .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
                        .setBackgroundColor(getResources().getColor(R.color.ucrop_color_default_dimmed))//设置背景色，建议使用有透明度的颜色
                        .setEnterAnimation(enterAnimation)//进入动画
                        .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(etInput)
                        .setLayoutRes(R.layout.view_guide_dialog)
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, final Controller controller) {
                                view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controller.showPage(0);
                                    }
                                });
                            }
                        })
                )
                .show();//显示引导层(至少需要一页引导页才能显示)
    }

}
