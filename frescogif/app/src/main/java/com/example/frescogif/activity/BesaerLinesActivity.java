package com.example.frescogif.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.radomLovew.RandomHeartView;
import com.example.frescogif.view.radomLovew.RandomHeartView1;

/**
 * Created by GG on 2017/12/7.
 */

public class BesaerLinesActivity extends BaseActivity{

    private RandomHeartView randomHeart;
    private RandomHeartView1 randomHeart1;
    int count = 0;
    private ImageView imgView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_besaer_view);
        randomHeart = (RandomHeartView) findViewById(R.id.randomHeart);
        randomHeart1 = (RandomHeartView1) findViewById(R.id.randomHeart1);
        imgView = (ImageView) findViewById(R.id.imgView);
        randomHeart.setDstChild(imgView);
    }


    public void click(View view){
        ++count;
        if(randomHeart1.getVisibility() == View.VISIBLE){
            randomHeart1.playHeartAnim();
        }else {
            randomHeart.playHeartAnim(count);
        }
    }

    public void click1(View view){

        if(randomHeart1.getVisibility() == View.VISIBLE){
            randomHeart1.setVisibility(View.GONE);
            randomHeart.setVisibility(View.VISIBLE);
        }else {
            randomHeart1.setVisibility(View.VISIBLE);
            randomHeart.setVisibility(View.GONE);
        }
    }

}
