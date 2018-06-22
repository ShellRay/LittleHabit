package com.example.frescogif.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.danmu.Barrage;
import com.example.frescogif.view.opendanmaku.DanmakuItem;
import com.example.frescogif.view.opendanmaku.DanmakuView;
import com.example.frescogif.view.runwaylaout.FlyItemLinkLayout;
import com.example.frescogif.view.runwaylaout.RunnerLinkLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.frescogif.view.runwaylaout.FlyItemLinkLayout.FLY_SCREEN;

/**
 * Created by GG on 2017/6/23.
 */
public class ShapeActivity extends BaseActivity{

    private RunnerLinkLayout runnerLayout;
    private com.example.frescogif.view.runwaylaout.BarrageView barrageView;
    private com.example.frescogif.view.danmu.BarrageViewSimple barrageViewSimple;
    private List<Barrage> mBarrages = new ArrayList<>();

    private boolean showDanmaku;

    private DanmakuView openDanmaku;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_custom);

        runnerLayout = (RunnerLinkLayout)findViewById(R.id.runnerLayout);
        openDanmaku = (DanmakuView)findViewById(R.id.openDanmaku);


        barrageView = (com.example.frescogif.view.runwaylaout.BarrageView)findViewById(R.id.BarrageView);
        barrageViewSimple = (com.example.frescogif.view.danmu.BarrageViewSimple)findViewById(R.id.BarrageView1);
        barrageView.setRowNum(4);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClick(View v) {

        for (int i = 0; i < 50; i++) {
            ArrayList<Object> arrayList = new ArrayList<>();
            for (int y = 0; y < 50; y++) {
                arrayList.add("宝宝正在梳"+ y +"，展现更好的");
            }
            FlyItemLinkLayout item = new FlyItemLinkLayout(getApplicationContext(), FLY_SCREEN);
            item.setMsg(arrayList);
            runnerLayout.displayFlyItem(item);
        }

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
            mBarrages.add(new Barrage("宝宝正在梳妆打扮，展现更好的一面" + i));
        }
        barrageViewSimple.setBarrages(mBarrages);
//        barrageView1.addBarrage(new Barrage("111111111111", R.color.colorPrimary, Color.RED));
    }

    public void onClick4(View v) {
        for (int i = 0; i < 200; i++) {
            openDanmaku.addItem(new DanmakuItem(this,
                    "宝宝正在梳妆打扮，展现更好的一面"+i, openDanmaku.getWidth()));
        }
        openDanmaku.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        barrageViewSimple.destroy();
        showDanmaku = false;
    }
}
