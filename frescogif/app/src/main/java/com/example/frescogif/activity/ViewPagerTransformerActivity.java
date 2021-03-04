package com.example.frescogif.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.fragment.CustomerViewPagerFragment;
import com.example.frescogif.transformer.DepthPageTransformer;
import com.example.frescogif.transformer.ZoomOutPageTransformer;
import com.example.frescogif.utils.MediaUtils;
import com.example.frescogif.view.CircleIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/11/24.
 */

public class ViewPagerTransformerActivity extends BaseActivity{

    public static final int []RES = new int[]{R.mipmap.dead1,R.mipmap.dead2,R.mipmap.dead3
            ,R.mipmap.dead4,R.mipmap.dead5,R.mipmap.dead6,R.mipmap.dead7,R.mipmap.dead8,R.mipmap.dead9,R.mipmap.dead10};

    public static final int []RES1 = new int[]{R.mipmap.icn_1,R.mipmap.icn_2,R.mipmap.icn_3
            ,R.mipmap.icn_4,R.mipmap.icn_5,R.mipmap.icn_6,R.mipmap.icn_7};

    public static final String TITLE[] = new String[]{"哥特萝莉","小红帽","安妮梦游仙境","舞会公主","冰爽烈焰","安博斯与提妮",
            "科学怪熊的新娘","你见过我的熊猫吗？安妮","甜心宝贝","海克斯科技"};

    ArrayList<ImageView> mImageViews = new ArrayList<ImageView>();
    ArrayList<ImageView> mImageViews1 = new ArrayList<ImageView>();

    private ViewPager viewPagerZoom;
    private ViewPager viewPager;
    private CircleIndicatorView mCircleIndicatorView;
    private MyPagerAdapter pagerAdapter;
    private FragmentAdapter fragmentPagerAdapter;
    private MyPagerAdapter pagerAdapter1;
    private ViewPager viewPagerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_transformer);
        initData();
        initView();

    }

    private void initView() {
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPagerZoom = (ViewPager)findViewById(R.id.viewPagerZoom);
        viewPagerFragment = (ViewPager)findViewById(R.id.viewPagerFragment);

        mCircleIndicatorView = (CircleIndicatorView) findViewById(R.id.indicatorView);

        viewPager.setPageTransformer(true,new DepthPageTransformer());
        viewPagerZoom.setPageTransformer(true,new ZoomOutPageTransformer());
        viewPagerFragment.setPageTransformer(true,new ZoomOutPageTransformer());

        pagerAdapter = new MyPagerAdapter(mImageViews);
        pagerAdapter1 = new MyPagerAdapter(mImageViews1);
        fragmentPagerAdapter = new FragmentAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        viewPagerZoom.setAdapter(pagerAdapter1);
        viewPagerFragment.setAdapter(fragmentPagerAdapter);

        viewPager.setOffscreenPageLimit(3);
        viewPagerZoom.setOffscreenPageLimit(3);
        mCircleIndicatorView.setUpWithViewPager(viewPager);
        mCircleIndicatorView.setOnIndicatorClickListener(new CircleIndicatorView.OnIndicatorClickListener() {
            @Override
            public void onSelected(int var1) {
                Toast.makeText(getBaseContext(),"selected ==" + var1,Toast.LENGTH_SHORT).show();
                viewPager.setCurrentItem(var1,false);
            }
        });
    }

    private void initData()
    {
        for (int imgId : RES)
        {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageResource(imgId);
            imageView.setPadding(MediaUtils.dip2px(this,80),MediaUtils.dip2px(this,30),MediaUtils.dip2px(this,80),MediaUtils.dip2px(this,30));
            imageView.setRotation(10f);
            mImageViews.add(imageView);
        }

        for (int imgId : RES1)
        {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageResource(imgId);
            imageView.setPadding(MediaUtils.dip2px(this,80),MediaUtils.dip2px(this,30),MediaUtils.dip2px(this,80),MediaUtils.dip2px(this,30));
            imageView.setRotation(10f);
            mImageViews1.add(imageView);
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        private  ArrayList<ImageView> mImageViews;

        public MyPagerAdapter(ArrayList<ImageView> mImageViews) {
            this.mImageViews= mImageViews;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView child = mImageViews.get(position);
            container.addView(child);
            return mImageViews.get(position);
        }

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViews.get(position));
        }
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            for(int i=0;i<RES.length;i++){
                mFragments.add(new CustomerViewPagerFragment(RES[i]));
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }


    }
}
