package com.example.frescogif.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.frescogif.R;
import com.example.frescogif.view.NumberScrollTextView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

/**
 * Created by GG on 2017/6/15.
 */
    /**

        在使用Android.support.v7.widget.Toolbar时，如果需要隐藏系统默认的标题，自己定义标题时，必须在onCreate()方法执行完成之后修改。

        因为在onCreate()方法中设置任何标题值都会被系统重置为AndroidManifest中android:lable的值。

        为了抵消这种行为，我们可以在onCreate()执行之后执行的onPostCreate()方法中执行修改标题的。

        @Override
        public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (mActionBarToolbar != null) {//mActionBarToolbar就是android.support.v7.widget.Toolbar
        mActionBarToolbar.setTitle("");//设置为空，可以自己定义一个居中的控件，当做标题控件使用
        }
        }
    */


public class AppBarActivity extends AppCompatActivity{

    private CollapsingToolbarLayout collapse;
    private Toolbar toolbar;
    private AppBarLayout appbar;
        private NumberScrollTextView numberView;

        private enum State {
        EXPANDED,//展开
        COLLAPSED,//折叠
        IDLE //空闲
    }

    int sigleInt = 1000;
    int startInt = 0;
    int duration = 10000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapse = (CollapsingToolbarLayout) findViewById(R.id.collapse);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
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

        numberView = (NumberScrollTextView) findViewById(R.id.numberView);
        numberView.setOnEndListener(new NumberScrollTextView.EndListener() {
            @Override
            public void onEndFinish() {
                Log.e("shell","anim is end");
            }
        });
        Button btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberView.setFromAndEndNumber(startInt, sigleInt);
                numberView.setDuration(duration);
                numberView.start();
                startInt = sigleInt;
                sigleInt = sigleInt+1000;
                if(duration > 1000){
                    duration = duration - 1000;
                }
            }
        });


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitle("十刃之四号");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        toolbar.setLogo(R.drawable.sun);
//        toolbar.setLogoDescription("sun");
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        numberView.clearCacheList();
    }
}
