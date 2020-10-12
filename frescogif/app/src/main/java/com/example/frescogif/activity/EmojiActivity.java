package com.example.frescogif.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.emoji.FaceViewPager;

/**
 * Created by GG on 2017/8/15.
 */
public class EmojiActivity extends BaseActivity{

    private FaceViewPager fvp_emoji;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji);
        fvp_emoji = (FaceViewPager) findViewById(R.id.fvp_emoji);
        RelativeLayout fl_root = (RelativeLayout) findViewById(R.id.fl_root);
        EditText editText = new EditText(this);
        fl_root.addView(editText,0);
        fvp_emoji.setEditText(editText);
        int pageCount = fvp_emoji.init();
        /*View o = (View) fvp_emoji.getAdapter().instantiateItem(fvp_emoji, 0);
        o.setBackgroundResource(R.color.eggplant);*/
        fvp_emoji.setOnPageChangeListener(onPageChangeListener);
    }


    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < fvp_emoji.getChildCount(); i++) {
                if (position == i) {
                    fvp_emoji.getChildAt(position).setBackgroundResource(R.color.eggplant);
                }
                else {
                    fvp_emoji.getChildAt(i).setBackgroundResource(R.color.translate);
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
}
