package com.example.frescogif.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.frescogif.R;
import com.example.frescogif.view.loadingview.Titanic;
import com.example.frescogif.view.loadingview.TitanicTextView;
import com.example.frescogif.view.loadingview.Typefaces;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by GG on 2017/6/7.
 */
public class LoadingActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ImmersionBar.with(this)
                .transparentNavigationBar()
                .statusBarColor(R.color.colorAccent)
                .init(); //初始化，默认透明状态栏和黑色导航栏
            TitanicTextView tv = (TitanicTextView) findViewById(R.id.my_text_view);

            // set fancy typeface
            tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));

        // start animation
        new Titanic().start(tv);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
