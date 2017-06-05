package com.example.frescogif.activity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class RecycleViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        List list = new ArrayList();
        for(int x =0;x < 20;x++){
            list.add("真的美");
        }
        //(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));//
        RecyclerView rcl_style = (RecyclerView)findViewById(R.id.rcl_style);
        rcl_style.setLayoutManager(new SignLayoutManager(3, 20));
        rcl_style.setAdapter(new SignAdapter(this,list));
    }
}
