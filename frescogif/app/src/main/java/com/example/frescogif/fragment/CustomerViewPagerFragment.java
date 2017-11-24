package com.example.frescogif.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.frescogif.R;
import com.example.frescogif.utils.MediaUtils;

/**
 * Created by GG on 2017/11/24.
 */

@SuppressLint("ValidFragment")
public class CustomerViewPagerFragment extends Fragment {

    private  int resId;
    private ImageView imgView;

    public CustomerViewPagerFragment(int resId) {
        this.resId = resId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_item_view_pager,null);
        imgView = (ImageView)view.findViewById(R.id.imgView);
        // 做一个属性动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(imgView,"rotation",0f,10f);
        animator.setDuration(10);
        animator.start();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgView.setImageResource(resId);
    }
}
