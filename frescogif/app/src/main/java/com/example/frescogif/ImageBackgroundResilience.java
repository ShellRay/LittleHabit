package com.example.frescogif;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * Created by GG on 2017/5/3.
 */
public class ImageBackgroundResilience extends Activity{
    private LinearLayout ll_viewArea;
    private LinearLayout.LayoutParams parm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN,
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);


//        setContentView(R.layout.activity_image_background);//另一种
        setContentView(R.layout.activity_scale_scrollview);

    }
}
