package com.example.frescogif.activity;

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

import static com.example.frescogif.view.runwaylaout.FlyItemLayout.FLY_SCREEN;

/**
 * Created by GG on 2017/6/23.
 */
public class ShapeActivity extends BaseActivity{

    private RunnerLayout runnerLayout;
    private com.example.frescogif.view.runwaylaout.BarrageView barrageView;
    private com.example.frescogif.view.danmu.BarrageViewSimple barrageViewSimple;
    private List<Barrage> mBarrages = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_custom);

        runnerLayout = (RunnerLayout)findViewById(R.id.runnerLayout);
        barrageView = (com.example.frescogif.view.runwaylaout.BarrageView)findViewById(R.id.BarrageView);
        barrageViewSimple = (com.example.frescogif.view.danmu.BarrageViewSimple)findViewById(R.id.BarrageView1);
        barrageView.setRowNum(4);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        barrageViewSimple.destroy();
    }
}
