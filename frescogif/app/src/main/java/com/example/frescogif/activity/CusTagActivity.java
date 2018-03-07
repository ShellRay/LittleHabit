package com.example.frescogif.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.utils.Utils;
import com.example.frescogif.view.FlowLayout;

import java.util.Random;

/**
 * Created by GG on 2017/6/5.
 */
public class CusTagActivity extends AppCompatActivity {

    /**
     * 显示的文字
     */
    private String[] mDatas = new String[]{"放开那三",
            "电子书",
            "放开那三",
            "电子书",
            "斗地主1",
            "斗地主2",
            "斗地主3",
            "斗地主4",
            "斗地主5",
            "斗地主6",
            "WIFI万",
            "播放器",
            "捕鱼达",
            "机票",
            "游戏",
            "熊出没之",
            "美图秀秀",
            "浏览器",
            "单机游戏",
            "我的世界",
            "电影电视",
            "QQ空间",
            "旅游",
            "免费游戏",
            "2048",
            "刀塔传奇",
            "壁纸",
            "节奏大师",
            "锁屏",
            };//"装机必备",
            /*"天天动听",
                    "备份",
                    "网盘",
                    "海淘网",
                    "大众点评",
                    "爱奇艺频",
                    "腾讯手",
                    "百度地图",
                    "猎豹清师",
                    "谷歌图",
                    "网导航",
                    "京东",
                    "有你",
                    "万年",
                    "支付宝"*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_tag);

        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);

        Random random = new Random();

        // 循环添加TextView到容器
        for (int i = 0; i < mDatas.length; i++) {
            final TextView view = new TextView(this);
            view.setText(mDatas[i]);
            view.setTextColor(Color.WHITE);
            int paddingTop = Utils.convertDpToPixel(CusTagActivity.this, 3);
            int paddingLeft = Utils.convertDpToPixel(CusTagActivity.this, 5);
            int viewWidth = Utils.convertDpToPixel(CusTagActivity.this, 50);
            view.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
            view.setWidth(viewWidth);
            view.setBackgroundResource(R.drawable.blue_bg_tag_selected);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(11);

            // 设置点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CusTagActivity.this, view.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });

            // 设置彩色背景
            GradientDrawable normalDrawable = new GradientDrawable();
            normalDrawable.setShape(GradientDrawable.RECTANGLE);
            int a = 255;
            int r = 50 + random.nextInt(150);
            int g = 50 + random.nextInt(150);
            int b = 50 + random.nextInt(150);
            normalDrawable.setColor(Color.argb(a, r, g, b));

            // 设置按下的灰色背景
            GradientDrawable pressedDrawable = new GradientDrawable();
            pressedDrawable.setShape(GradientDrawable.RECTANGLE);
            pressedDrawable.setColor(Color.GRAY);

            // 背景选择器
            StateListDrawable stateDrawable = new StateListDrawable();
            stateDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
            stateDrawable.addState(new int[]{}, normalDrawable);

            // 设置背景选择器到TextView上
            view.setBackground(stateDrawable);

            flowLayout.addView(view);
        }
    }
}
