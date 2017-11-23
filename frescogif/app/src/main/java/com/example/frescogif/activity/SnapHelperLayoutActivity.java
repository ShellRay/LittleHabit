package com.example.frescogif.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.frescogif.HelperLable.StartSnapHelper;
import com.example.frescogif.R;
import com.example.frescogif.adapter.CustomAdapter;
import com.example.frescogif.adapter.RecyclerViewAdapter;
import com.example.frescogif.adapter.SnapHelperAdapter;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.utils.MediaUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/11/23.
 */

public class SnapHelperLayoutActivity extends BaseActivity{

    private RecyclerView rclSnapHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_snaphelper);

        initView();

    }

    private void initView() {
        rclSnapHelper = (RecyclerView) findViewById(R.id.rcl_snap_helper);
        List list = new ArrayList();
        for(int x =0;x < 10;x++){
            list.add("五彩缤纷" + x);
        }
        rclSnapHelper.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        SnapHelperAdapter customAdapter = new SnapHelperAdapter(this, list);
        rclSnapHelper.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if(parent.getChildAdapterPosition(view)!= 0){
                    outRect.right = MediaUtils.dip2px(SnapHelperLayoutActivity.this,10);
                }

            }
        });
        rclSnapHelper.setAdapter(customAdapter);
//        LinearSnapHelper snapHelper = new StartSnapHelper();
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rclSnapHelper);

    }

}
