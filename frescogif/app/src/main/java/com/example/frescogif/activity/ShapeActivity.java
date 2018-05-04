package com.example.frescogif.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.danmu.Barrage;
import com.example.frescogif.view.runwaylaout.FlyItemLayout;
import com.example.frescogif.view.runwaylaout.RunnerLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

import static com.example.frescogif.view.runwaylaout.FlyItemLayout.FLY_SCREEN;

/**
 * Created by GG on 2017/6/23.
 */
public class ShapeActivity extends BaseActivity{

    private RunnerLayout runnerLayout;
    private com.example.frescogif.view.runwaylaout.BarrageView barrageView;
    private com.example.frescogif.view.danmu.BarrageViewSimple barrageViewSimple;
    private List<Barrage> mBarrages = new ArrayList<>();

    private boolean showDanmaku;

    private DanmakuView danmakuView;

    private DanmakuContext danmakuContext;

    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_custom);

        runnerLayout = (RunnerLayout)findViewById(R.id.runnerLayout);

        barrageView = (com.example.frescogif.view.runwaylaout.BarrageView)findViewById(R.id.BarrageView);
        barrageViewSimple = (com.example.frescogif.view.danmu.BarrageViewSimple)findViewById(R.id.BarrageView1);
        barrageView.setRowNum(4);

        danmakuView = (DanmakuView) findViewById(R.id.danmaku_view);

        danmakuView.enableDanmakuDrawingCache(true);
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                showDanmaku = true;
                danmakuView.start();
                generateSomeDanmaku();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        danmakuContext = DanmakuContext.create();
        danmakuView.prepare(parser, danmakuContext);
    }

    /**
     * 向弹幕View中添加一条弹幕
     * @param content
     *          弹幕的具体内容
     * @param  withBorder
     *          弹幕是否有边框
     */
    private void addDanmaku(String content, boolean withBorder) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.textSize = sp2px(20);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.BLACK;
        danmaku.setTime(danmakuView.getCurrentTime());
        if (withBorder) {
            danmaku.borderColor = Color.GREEN;
        }
        danmakuView.addDanmaku(danmaku);
    }

    /**
     * 随机生成一些弹幕内容以供测试
     */
    private void generateSomeDanmaku() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(showDanmaku) {
                    int time = new Random().nextInt(300);
                    String content = "" + time + time;
                    addDanmaku(content, false);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * sp转px的方法。
     */
    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
    }

    public void onClick(View v) {
        FlyItemLayout item = new FlyItemLayout(getApplicationContext(), FLY_SCREEN);
        item.setMsg("this is danmu I wish it can work!");
        runnerLayout.displayFlyItem(item);
    }
    public void onClick1(View v) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 50; i++){
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalI%2 == 0){
                                barrageView.addBarrageItemView(ShapeActivity.this,"第" + finalI +"条弹幕",16, R.color.white);
                            }else{
                                barrageView.addBarrageItemView(ShapeActivity.this,"第" + finalI +"条弹幕"+"第" + finalI +"条弹幕",16, R.color.white);
                            }
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void onClick2(View v) {
        for (int i = 0; i < 200; i++) {
            mBarrages.add(new Barrage("弹幕数据----" + i));
        }
        barrageViewSimple.setBarrages(mBarrages);
//        barrageView1.addBarrage(new Barrage("111111111111", R.color.colorPrimary, Color.RED));
    }

    public void onClick3(View v) {
        addDanmaku("dfdf",false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        barrageViewSimple.destroy();
        showDanmaku = false;
        if (danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
        }
    }
}
