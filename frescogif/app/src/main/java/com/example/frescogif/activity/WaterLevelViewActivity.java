package com.example.frescogif.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.WaterLevel.WaterLevelView;
import com.example.frescogif.view.loadingview.Titanic;

import org.w3c.dom.Text;

/**
 * 水位图的自定义
 * Created by GG on 2017/11/28.
 */

public class WaterLevelViewActivity extends BaseActivity{

    private WaterLevelView water_level;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_level_view);

        water_level = (WaterLevelView) findViewById(R.id.water_level);

    }


}
