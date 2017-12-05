package com.example.frescogif.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.BeatingWaveView;
import com.example.frescogif.view.WaterLevel.RealWaveLevelView;
import com.example.frescogif.view.WaterLevel.WaterLevelView;
import com.example.frescogif.view.loadingview.Titanic;

import org.w3c.dom.Text;

/**
 * 水位图的自定义
 * Created by GG on 2017/11/28.
 */

public class WaterLevelViewActivity extends BaseActivity implements View.OnClickListener {

    private RealWaveLevelView realWaterLevel;
    private WaterLevelView water_level;

    private long depthFloat ;
    private float depthFloat1 = 0.1f;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_level_view);

        realWaterLevel = (RealWaveLevelView) findViewById(R.id.realWaterLevel);
        water_level = (WaterLevelView) findViewById(R.id.water_level);

        Button addDepth = (Button) findViewById(R.id.addDepth);
        addDepth.setOnClickListener(this);
        water_level.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addDepth:
                depthFloat = 500 + depthFloat;
                if(depthFloat >= 10000){
                    depthFloat = 0;
                    realWaterLevel.clearBeating().playBeating(0);
                }else {
                    realWaterLevel.playBeating(depthFloat);
                }
                break;

            case R.id.water_level:
                if(depthFloat1 >= 1){
                    depthFloat1 = 0.1f;
                }else {
                    depthFloat1 = depthFloat1 + 0.1f;
                }
                water_level.setAddWaveHeight(depthFloat1);
                break;
        }


    }
}
