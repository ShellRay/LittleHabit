package com.example.frescogif.activity;

import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.support.v7.widget.RecyclerView;

import com.example.frescogif.R;
import com.example.frescogif.adapter.SignAdapter;
import com.example.frescogif.layoutmanager.SignLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/5/26.
 */
public class RecycleViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_loading);
        mySwipeRefreshLayout.setOnRefreshListener(this);
        mySwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorAccent,R.color.saffron, R.color.main_color);
        List list = new ArrayList();
        for(int x =0;x < 10;x++){
            list.add("五彩缤纷" + x);
        }
        //(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));//
        RecyclerView rcl_style = (RecyclerView)findViewById(R.id.rcl_style);
        rcl_style.setLayoutManager(new SignLayoutManager(3, 20));
        // 添加分割线
        rcl_style.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                outRect.set(0, 0, 0, 25);
            }
        });
        rcl_style.setAdapter(new SignAdapter(this,list));
    }

    @Override
    public void onRefresh() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                    }
                }
        ).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mySwipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }
}
