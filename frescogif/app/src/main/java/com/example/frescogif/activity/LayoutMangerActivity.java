package com.example.frescogif.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;

import com.example.frescogif.R;
import com.example.frescogif.adapter.SignAdapter;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.RecycleViewForAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/6/9.
 */
public class LayoutMangerActivity extends BaseActivity{

    private RadioGroup rg_btn;
    private RecycleViewForAnimation rv_recycle;
    private int type = 2;
    private SignAdapter adapter;
    private GridLayoutManager layoutManager;
    private int allHeight;
    private int hotHeight;
    private int scrwidth;
    private int unitSize;
    private List list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_manager);
        rg_btn = (RadioGroup) findViewById(R.id.rg_btn);
        rv_recycle = (RecycleViewForAnimation) findViewById(R.id.rv_recycle);

        list = new ArrayList();
        for(int x =0;x < 10;x++){
            list.add("五彩缤纷" + x);
        }

        float density = getResources().getDisplayMetrics().density;
        scrwidth = getResources().getDisplayMetrics().widthPixels;
        unitSize = (int) (density * 5 + 0.5f);

        hotHeight = (scrwidth - 3 * unitSize) / 2 + unitSize;
        allHeight = (scrwidth - 4 * unitSize) / 3 + unitSize;

        layoutManager = new GridLayoutManager(this, spanCount);

        initData();
        initView();

    }

    private void initData() {
        rg_btn.check(R.id.rn_three);
        rg_btn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rv_recycle.scheduleLayoutAnimation();
                switch (checkedId){
                    case R.id.rn_normal:
                        type = 0;
                        break;
                    case R.id.rn_two:
                        type = 1;
                        break;
                    case R.id.rn_three:
                        type = 2;
                        break;
                    case R.id.rn_waterfall:
                        type = 3;
                        break;
                }
            }
        });
    }
    int spanCount = 6;
    private void initView() {
        //此方法会使recycleview的layoutannimation动画不运行
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                int showSpanCount = spanCount;
                switch (type){
                    case 0:
                        showSpanCount = spanCount;
                        if(adapter != null) {
                            adapter.setAllHeight(scrwidth);
                            specialUpdate();
                        }
                        break;
                    case 1:
                        showSpanCount = spanCount/2;
                        if(adapter != null) {
                            adapter.setAllHeight(hotHeight);
                            specialUpdate();
                        }
                        break;
                    case 2:
                        showSpanCount = spanCount/3;
                        if(adapter != null) {
                            adapter.setAllHeight(allHeight);
                            specialUpdate();
                        }
                        break;
                    case 3:
                        showSpanCount = spanCount/4;
                        if(adapter != null) {
                            /*rv_recycle.addItemDecoration(new RecyclerView.ItemDecoration() {
                                @Override
                                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                    outRect.set(10, 0, 10, 20);
                                }
                            });*/
                            adapter.setAllHeight(120);
                            specialUpdate();
                        }
                        break;

                }
                return showSpanCount;
            }
        });

        rv_recycle.setLayoutManager(layoutManager);

        rv_recycle.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(20, 0, 20, 20);
            }
        });

        adapter = new SignAdapter(this,list);
        adapter.setAllHeight(scrwidth);
        rv_recycle.setAdapter(adapter);

    }

    private void specialUpdate() {
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
//                adapter.notifyItemChanged(adapter.getItemCount() - 1);
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }



}
