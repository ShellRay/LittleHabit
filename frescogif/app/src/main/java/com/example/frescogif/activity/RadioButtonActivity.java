package com.example.frescogif.activity;

import android.graphics.Rect;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;

import com.example.frescogif.R;
import com.example.frescogif.adapter.UserTagAdapter;
import com.example.frescogif.bean.AnchorImpressionBean;
import com.example.frescogif.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/12/18.
 *
 * 混排标签页
 */

public class RadioButtonActivity extends AppCompatActivity {

    private RecyclerView rclAnchorTag;
    private String[] mDatas = new String[]{"放开那三",
            "王者荣耀",
            "街头篮球",
            "地下城",
            "与勇士",
            "斗地主",
            "CF穿越火",
            "问道破游",
            "龙之谷",
            "王者之心",
            "NBA街球",
            "播放器",
            "捕鱼达",
            "熊出没之",
            "美图秀秀",
            "浏览器",
            "单机游戏",
            "我的世界",
            "电影电视",
            "QQ空间",
            "刀塔传奇",
            "节奏大师",
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_button);
        rclAnchorTag = (RecyclerView) findViewById(R.id.rcl_anchor_tag);


        List<AnchorImpressionBean> list = new ArrayList<>();
        int [] colors = { R.color.green,R.color.blue,R.color.red,R.color.black,};
        String[] color = {"f16d7a","b7d28d","b8f1ed","b8f4ed"};

        for (int x= 0; x < mDatas.length; x++) {
            AnchorImpressionBean anchorImpressionBean = new AnchorImpressionBean();
            anchorImpressionBean.setTagId(x);
            anchorImpressionBean.setTagName(mDatas[x]);
            anchorImpressionBean.setTagRGB(color[x%4]);
            list.add(anchorImpressionBean);
        }

        if (list.size() > 0) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.HORIZONTAL);
            rclAnchorTag.setLayoutManager(staggeredGridLayoutManager);
            UserTagAdapter userTagAdapter = new UserTagAdapter(this, list);
            rclAnchorTag.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    int left = Utils.convertDpToPixel(RadioButtonActivity.this, 20);
                    int kerning = Utils.convertDpToPixel(RadioButtonActivity.this, 20);
                    int childAdapterPosition = parent.getChildAdapterPosition(view);

                    outRect.set(left, kerning, 0, 0);
                    if (childAdapterPosition == 0) {
                        outRect.set(left, kerning, 0, 0);
                    } else if (childAdapterPosition == 1) {
                        outRect.set(2 * left, kerning, 0, 0);
                    } else if (childAdapterPosition == 2) {
                        outRect.set(0, kerning, 0, 0);
                    }else if(childAdapterPosition  == 3){
                        outRect.set(2 * left, kerning, 0, 0);
                    }else if(childAdapterPosition  == 4){
                        outRect.set((int) (1.5 * left), kerning, 0, 0);
                    }

                }
            });
            rclAnchorTag.setAdapter(userTagAdapter);
        }
    }


}