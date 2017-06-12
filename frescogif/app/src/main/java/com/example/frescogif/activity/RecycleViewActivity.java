package com.example.frescogif.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.frescogif.R;
import com.example.frescogif.adapter.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/5/26.
 */
public class RecycleViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView rcl_style;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_loading);
        rcl_style = (RecyclerView)findViewById(R.id.rcl_style);
        mySwipeRefreshLayout.setOnRefreshListener(this);
        mySwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorAccent,R.color.saffron, R.color.main_color);
        List list = new ArrayList();
        for(int x =0;x < 10;x++){
            list.add("五彩缤纷" + x);
        }
        rcl_style.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(16);
//        rcl_style.addItemDecoration(spacesItemDecoration);
        CustomAdapter adapter = new CustomAdapter(this, list);
        rcl_style.setAdapter(adapter);
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space=space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left=space;
            outRect.right=space;
            outRect.bottom=space;
            if(parent.getChildAdapterPosition(view)==0){
                outRect.top=space;
            }
        }
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
