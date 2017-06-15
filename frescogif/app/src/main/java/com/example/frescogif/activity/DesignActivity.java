package com.example.frescogif.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.frescogif.R;
import com.example.frescogif.adapter.DesignAdapter;
import com.example.frescogif.baseActvity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/6/8.
 */
public class DesignActivity extends BaseActivity {

    private RecyclerView rcv_staff;
    private List<String> list;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);

        list = new ArrayList<>();
        for (int x=0;x<12;x++){
            list.add("兰若"+ x);
        }

        rcv_staff = (RecyclerView) findViewById(R.id.rcv_staff);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rcv_staff.setLayoutManager(staggeredGridLayoutManager);
        rcv_staff.setHasFixedSize(true);

        rcv_staff.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 10, 10);
            }
        });
        DesignAdapter designAdapter = new DesignAdapter(this, list);
        rcv_staff.setAdapter(designAdapter);
    }
}
