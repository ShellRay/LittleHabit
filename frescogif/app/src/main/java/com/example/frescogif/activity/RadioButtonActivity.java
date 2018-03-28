package com.example.frescogif.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.frescogif.R;
import com.example.frescogif.adapter.NormalAdapter;
import com.example.frescogif.adapter.UserTagAdapter;
import com.example.frescogif.bean.AnchorImpressionBean;
import com.example.frescogif.constant.Constant;
import com.example.frescogif.layoutmanager.SignLayoutManager;
import com.example.frescogif.utils.Utils;
import com.example.frescogif.view.citychocie.CustomerAddressDialog;
import com.example.frescogif.view.citychocie.CustomerTimeDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by GG on 2017/12/18.
 */

public class RadioButtonActivity extends AppCompatActivity {

    private RecyclerView rclAnchorTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_button);
        rclAnchorTag = (RecyclerView) findViewById(R.id.rcl_anchor_tag);


        List<AnchorImpressionBean> list = new ArrayList<>();
        int [] colors = { R.color.green,R.color.blue,R.color.red,R.color.black,};
        String[] color = {"f16d7a","b7d28d","b8f1ed","b8f4ed"};

        for (int x= 0; x<25; x++) {
            AnchorImpressionBean anchorImpressionBean = new AnchorImpressionBean();
            anchorImpressionBean.setTagId(x);
            anchorImpressionBean.setTagName("test"+x);
            anchorImpressionBean.setTagRGB(color[x%4]);
            list.add(anchorImpressionBean);
        }

        if (list.size() > 0) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
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
                    }

                }
            });
            rclAnchorTag.setAdapter(userTagAdapter);
        }
    }


}