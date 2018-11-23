package com.example.frescogif.baseActvity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.frescogif.R;
import com.gyf.barlibrary.ImmersionBar;

import me.ele.uetool.UETool;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by GG on 2017/6/12.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .transparentNavigationBar()
                .statusBarColor(R.color.colorPrimaryDark)
                .fullScreen(true)
                .init(); //初始化，默认透明状态栏和黑色导航栏

        //查看器展示
//        UETool.showUETMenu();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        UETool.dismissUETMenu();
        ImmersionBar.with(this).destroy(); //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }
}