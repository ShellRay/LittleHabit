package com.example.frescogif.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
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
    private float depthFloat = 0.1f;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_level_view);

        realWaterLevel = (RealWaveLevelView) findViewById(R.id.realWaterLevel);
        Button addDepth = (Button) findViewById(R.id.addDepth);
        addDepth.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(depthFloat >= 1){
            depthFloat = 0.1f;
        }else {
            depthFloat = depthFloat + 0.1f;
        }
        realWaterLevel.setDepthOfWater(depthFloat);

    }
}
