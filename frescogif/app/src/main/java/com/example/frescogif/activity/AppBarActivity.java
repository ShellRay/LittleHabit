package com.example.frescogif.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by GG on 2017/6/15.
 */
public class AppBarActivity extends AppCompatActivity{

    private CollapsingToolbarLayout collapse;
    private Toolbar toolbar;
    private AppBarLayout appbar;
    private enum State {
        EXPANDED,//展开
        COLLAPSED,//折叠
        IDLE //空闲
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapse = (CollapsingToolbarLayout) findViewById(R.id.collapse);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                private State state;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (verticalOffset == 0) {
                        if (state != State.EXPANDED) {

                        }
                        state = State.EXPANDED;

                    } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                        if (state != State.COLLAPSED) {

                        }
                        state = State.COLLAPSED;

                    } else {
                        if (state != State.IDLE) {
                        }
                        state = State.IDLE;
                    }
                }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
