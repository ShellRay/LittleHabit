package com.example.frescogif.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.frescogif.R;
import com.example.frescogif.fragment.ListViewFragment;
import com.example.frescogif.fragment.RecyclerViewFragment;
import com.google.android.material.tabs.TabLayout;

/**
 * Created by Oleksii Shliama.
 */
public class PullToRefreshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        @SuppressLint("WrongViewCast") TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
            setSupportActionBar(toolbar);
        }

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ListViewFragment();
                case 1:
                default:
                    return new RecyclerViewFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ListView";
                case 1:
                default:
                    return "RecyclerView";
            }
        }
    }

}

