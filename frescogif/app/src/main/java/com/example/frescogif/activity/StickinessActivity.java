package com.example.frescogif.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.adapter.StickRecycleAdapter;
import com.example.frescogif.bean.StickyHeadEntity;
import com.example.frescogif.bean.StockEntity;
import com.example.frescogif.callback.OnItemClickListener;
import com.example.frescogif.layoutmanager.SpaceItemDecoration;
import com.example.frescogif.layoutmanager.StickyItemDecoration;
import com.example.frescogif.view.StickyView.StickyHeadContainer;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by GG on 2018/2/5.
 */

public class StickinessActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private StickRecycleAdapter mAdapter;
    private int mStickyPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickiness);
        initToolBar();
        initView();
        initData();
    }

    private void initData() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                initView();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return getStrFromAssets(StickinessActivity.this, "rasking.json");
            }

            @Override
            protected void onPostExecute(String result) {
                parseAndSetData(result);
            }

        }.execute();
    }

    private String getStrFromAssets(Context context, String jsonName) {

        AssetManager assets = context.getAssets();
        try {
            InputStream inputStream = assets.open(jsonName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null){
                sb.append(str);
            }
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void parseAndSetData(String result) {

        Gson gson = new Gson();
        StockEntity stockEntity = gson.fromJson(result, StockEntity.class);

        List<StockEntity.StockInfo> data = new ArrayList<>();

        data.add(new StockEntity.StockInfo(StickRecycleAdapter.TYPE_STICKY_HEAD, "涨幅榜"));
        for(StockEntity.StockInfo info : stockEntity.increase_list){
            info.setItemType(StickRecycleAdapter.TYPE_DATA);
            data.add(info);
        }
        data.add(new StockEntity.StockInfo(StickRecycleAdapter.TYPE_STICKY_HEAD, "跌幅榜"));
        for(StockEntity.StockInfo info : stockEntity.down_list){
            info.setItemType(StickRecycleAdapter.TYPE_DATA);
            data.add(info);
        }
        data.add(new StockEntity.StockInfo(StickRecycleAdapter.TYPE_STICKY_HEAD, "换手率"));
        for(StockEntity.StockInfo info : stockEntity.change_list){
            info.setItemType(StickRecycleAdapter.TYPE_DATA);
            data.add(info);
        }
        data.add(new StockEntity.StockInfo(StickRecycleAdapter.TYPE_STICKY_HEAD, "振幅榜"));
        for(StockEntity.StockInfo info : stockEntity.amplitude_list){
            info.setItemType(StickRecycleAdapter.TYPE_DATA);
            data.add(info);
        }

        List<StickyHeadEntity<StockEntity.StockInfo>> list = new ArrayList<>(data.size());
        list.add(new StickyHeadEntity<StockEntity.StockInfo>(null, StickRecycleAdapter.TYPE_HEAD, null));
        for (StockEntity.StockInfo info : data) {
            list.add(new StickyHeadEntity<>(info, info.getItemType(), info.stickyHeadName));
        }

        mAdapter.setData(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("粘性列表");
        setSupportActionBar(toolbar);
    }

    private void initView() {
        final StickyHeadContainer container = (StickyHeadContainer) findViewById(R.id.shc);

        final TextView tvStockName = (TextView) container.findViewById(R.id.tv_stock_name);
        final CheckBox checkBox = (CheckBox) container.findViewById(R.id.checkbox);
        final ImageView more = (ImageView) container.findViewById(R.id.iv_more);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAdapter.getData().get(mStickyPosition).getData().check = isChecked;
                mAdapter.notifyItemChanged(mStickyPosition);
            }
        });
        container.setDataCallback(new StickyHeadContainer.DataCallback() {
            @Override
            public void onDataChange(int pos) {
                mStickyPosition = pos;
                StockEntity.StockInfo item = mAdapter.getData().get(pos).getData();
                tvStockName.setText(item.stickyHeadName);
                checkBox.setChecked(item.check);
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StickinessActivity.this, "点击了粘性头部的更多", Toast.LENGTH_SHORT).show();
            }
        });
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StickinessActivity.this, "点击了粘性头部：" + tvStockName.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(StickinessActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new StickyItemDecoration(container, StickRecycleAdapter.TYPE_STICKY_HEAD));

        mRecyclerView.addItemDecoration(new SpaceItemDecoration(mRecyclerView.getContext()));

        mAdapter = new StickRecycleAdapter(null);
        mAdapter.setItemClickListener(new OnItemClickListener<StockEntity.StockInfo>() {
            @Override
            public void onItemClick(View view, StockEntity.StockInfo data, int position) {
                Toast.makeText(StickinessActivity.this, "点击了Item" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(),"setting",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
