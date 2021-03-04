package com.example.frescogif.view.emoji;
/**
 * <p>Copyright: Copyright (c) 2013</p>
 * 
 * <p>Company: 呱呱视频社区<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @description ViewPagerAdapter
 * @author TPX
 * @modify 
 * @version 1.0.0 
*/

import android.view.View;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPageAdapter extends PagerAdapter {
	private ArrayList<? extends View> pageViews;

	public ViewPageAdapter(ArrayList<? extends View> pageViews) {
		this.pageViews = pageViews;
	}

	@Override
	public int getCount() {
		return pageViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		//((ViewPager) arg0).removeView(this.pageViews.get(arg1));
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		try{
			((ViewPager) arg0).addView(pageViews.get(arg1));
		}catch(Exception ignored){
		}
		return pageViews.get(arg1);
	}

}
