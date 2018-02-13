package com.example.frescogif.layoutmanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.frescogif.adapter.StickRecycleAdapter;

/**
 * Created by GG on 2018/2/5.
 */

public  class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;

    public SpaceItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            DividerHelper.drawBottomAlignItem(c, mDivider, child, params);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int type = parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(view));
        if (type != StickRecycleAdapter.TYPE_DATA && type != StickRecycleAdapter.TYPE_SMALL_STICKY_HEAD_WITH_DATA) {
            outRect.set(0, 0, 0, 0);
        } else {
            int intrinsicHeight = mDivider.getIntrinsicHeight();
            Log.e("SpaceItemDecoration","Drawable"+intrinsicHeight);
            outRect.set(0, 0, 0, intrinsicHeight);
        }
    }

}
