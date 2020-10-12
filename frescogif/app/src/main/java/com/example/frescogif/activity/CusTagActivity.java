package com.example.frescogif.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.utils.Utils;
import com.example.frescogif.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by GG on 2017/6/5.
 *
 * 不建议使用
 */
public class CusTagActivity extends AppCompatActivity {

    int [] drable = { R.drawable.green_item_selector,R.drawable.blue_item_selector,R.drawable.red_item_selector,R.drawable.black_item_selector};
    int [] colors = { R.color.green,R.color.blue,R.color.red,R.color.black,};

    /**
     * 显示的文字
     */
    private String[] mDatas = new String[]{"放开那三",
            "电子书",
            "放开那三1",
            "电子书2",
            "斗地主1",
            "斗地主2",
            "斗地主3",
            "斗地主4",
            "斗地主5",
            "斗地主6",
            "WIFI万",
            "播放器",
            "捕鱼达",
            "熊出没之",
            "美图秀秀",
            "浏览器",
            "单机游戏",
            "我的世界",
            "电影电视",
            "QQ空间",
            "免费游戏",
            "刀塔传奇",
            "节奏大师",
            };

     private  List  checkedList = new ArrayList(2);
     private  List<RadioButton>  viewList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_tag);
        checkedList.clear();
        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);

        Random random = new Random();

        // 循环添加TextView到容器
        for (int i = 0; i < mDatas.length; i++) {
//            final TextView view = new TextView(this);

            final RadioButton view  = new RadioButton(this);
            viewList.add(view);

            view.setButtonDrawable(android.R.color.transparent);
            view.setText(mDatas[i]);
            view.setTextColor(Color.WHITE);
            int paddingTop = Utils.convertDpToPixel(CusTagActivity.this, 3);
            int paddingLeft = Utils.convertDpToPixel(CusTagActivity.this, 5);
            int viewWidth = Utils.convertDpToPixel(CusTagActivity.this, 60);
            int viewHight = Utils.convertDpToPixel(CusTagActivity.this, 22);
            view.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
            view.setWidth(viewWidth);
            view.setHeight(viewHight);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(11);
            view.setTag(i);

            view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        view.setTextColor(getResources().getColor(R.color.white));
                        view.setChecked(true);
                        int i = (int) view.getTag();

                        if(checkedList.size()> 1 ){
                            String text = (String) checkedList.get(0);

                            for ( RadioButton radioButton : viewList) {

                                if(text.equals(radioButton.getText())){
                                    radioButton.setChecked(false);
                                    radioButton.setTextColor(Color.RED);
                                    checkedList.remove(0);
                                }
                            }
                        }

                        checkedList.add(mDatas[i]);

                        if(checkedList.size()> 1) {
                            String o = (String) checkedList.get(0);
                            String o1 = (String) checkedList.get(1);
                            Toast.makeText(CusTagActivity.this, o + "---" + o1, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            GradientDrawable normalDrawable = new GradientDrawable();
            normalDrawable.setCornerRadius(100);
            normalDrawable.setStroke(1, Color.parseColor("#ff00ff"));
            normalDrawable.setColor(Color.parseColor("#ffffff"));
            view.setBackgroundDrawable(normalDrawable);
            view.setTextColor(Color.parseColor("#ff00ff"));

            // 设置彩色背景
          /*  GradientDrawable normalDrawable = new GradientDrawable();
            normalDrawable.setShape(GradientDrawable.RECTANGLE);
            int a = 255;
            int r = 50 + random.nextInt(150);
            int g = 50 + random.nextInt(150);
            int b = 50 + random.nextInt(150);
            normalDrawable.setColor(Color.argb(a, r, g, b));*/

            // 设置按下的灰色背景
            GradientDrawable pressedDrawable = new GradientDrawable();
            pressedDrawable.setShape(GradientDrawable.RECTANGLE);
            pressedDrawable.setCornerRadius(100);
            pressedDrawable.setColor(Color.parseColor("#ff00ff"));

            // 背景选择器
            StateListDrawable stateDrawable = new StateListDrawable();
            stateDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
            stateDrawable.addState(new int[]{android.R.attr.state_checked}, pressedDrawable);
            stateDrawable.addState(new int[]{}, normalDrawable);

            // 设置背景选择器到TextView上
            view.setBackground(stateDrawable);

            flowLayout.addView(view);
        }
    }
}
