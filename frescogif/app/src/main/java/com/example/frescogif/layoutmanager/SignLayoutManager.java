package com.example.frescogif.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by GG on 2017/5/26.
 */
public class SignLayoutManager extends RecyclerView.LayoutManager {

    private int mVerticalOffset = 0;

    public SignLayoutManager(int i, int verticalDevideSpace) {

    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler); // 分离所有的itemView

        int offsetX = 0;
        int offsetY = 0;

        for (int i = 0; i < getItemCount(); i++) {
            View scrap = recycler.getViewForPosition(i); // 根据position获取一个碎片view，可以从回收的view中获取，也可能新构造一个

            addView(scrap);
            measureChildWithMargins(scrap, 0, 0);  // 计算此碎片view包含边距的尺寸

            int width = getDecoratedMeasuredWidth(scrap);  // 获取此碎片view包含边距和装饰的宽度width
            int height = getDecoratedMeasuredHeight(scrap); // 获取此碎片view包含边距和装饰的高度height

            layoutDecorated(scrap, offsetX , offsetY, offsetX + width, offsetY + height); // Important！布局到RecyclerView容器中，所有的计算都是为了得出任意position的item的边界来布局

//            offsetX += width;
            offsetY += height;
        }
        //初始化时调用 填充childView
//        fill(recycler, state, realOffset);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {

        //位移0、没有子View 当然不移动
        if (dy == 0 || getChildCount() == 0) {
            return 0;
        }
        int realOffset = dy;//实际滑动的距离， 可能会在边界处被修复
        //边界修复代码
        if (mVerticalOffset + realOffset < 0) {//上边界
            realOffset = -mVerticalOffset;
        } else if (realOffset > 0) {//下边界
            //利用最后一个子View比较修正
            View lastChild = getChildAt(getChildCount() - 1);
            if (getPosition(lastChild) == getItemCount() - 1) {
                int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    realOffset = -gap;
                } else if (gap == 0) {
                    realOffset = 0;
                } else {
                    realOffset = Math.min(realOffset, -gap);
                }
            }
        }

//        realOffset = fill(recycler, state, realOffset);//先填充，再位移。

        mVerticalOffset += realOffset;//累加实际滑动距离

        offsetChildrenVertical(-realOffset);//滑动

        return realOffset;
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }
}
