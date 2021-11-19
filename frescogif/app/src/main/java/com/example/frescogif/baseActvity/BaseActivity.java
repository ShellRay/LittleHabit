package com.example.frescogif.baseActvity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frescogif.R;
import com.gyf.barlibrary.ImmersionBar;


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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 如果你的app可以横竖屏切换，并且适配4.4或者emui3手机请务必在onConfigurationChanged方法里添加这句话
        ImmersionBar.with(this).init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        UETool.dismissUETMenu();
        ImmersionBar.with(this).destroy(); //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }
}