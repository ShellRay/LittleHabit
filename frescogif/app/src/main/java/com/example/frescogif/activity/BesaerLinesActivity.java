package com.example.frescogif.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.radomLovew.RandomHeartView;

/**
 * Created by GG on 2017/12/7.
 */

public class BesaerLinesActivity extends BaseActivity{

    private RandomHeartView randomHeart;
    int count = 0;
    private ImageView imgView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_besaer_view);
        randomHeart = (RandomHeartView) findViewById(R.id.randomHeart);
        imgView = (ImageView) findViewById(R.id.imgView);
        randomHeart.setDstChild(imgView);
    }


    public void click(View view){
        ++count;
        randomHeart.playHeartAnim(count);
    }
}
