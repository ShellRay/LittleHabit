package com.example.frescogif.view.runwaylaout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanwei on 17/4/12.
 */
public class RunnerLayout extends LinearLayout
{

    List<FlyItemLayout> showlist;
    List<RunnerViewer> viewlist;


    public RunnerLayout(Context context)
    {
        this(context, null);
    }

    public RunnerLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RunnerLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        showlist = new ArrayList<FlyItemLayout>();

        viewlist = new ArrayList<RunnerViewer>();

        int count = 4;//跑道数量

        for (int i = 0; i < count; i++)
        {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
            params.weight = 1;
            RunnerViewer viewer = new RunnerViewer(this);
            addViewInLayout(viewer, getChildCount(), params);

            viewlist.add(viewer);
        }

        setWeightSum(count);
    }

    public void displayFlyItem(FlyItemLayout item)
    {
        showlist.add(item);

        RunnerViewer viewer = getAvaliableViewer();

        if (viewer != null)
        {
            runnerAvaliable(viewer);
        }
    }

    RunnerViewer getAvaliableViewer()
    {
        RunnerViewer result = null;

        for (RunnerViewer viewer : viewlist)
        {
            if (viewer.getChildCount() == 0)
            {
                result = viewer;
                break;
            }
        }

        return result;
    }

    void runnerAvaliable(RunnerViewer viewer)
    {
        if (showlist.size() > 0)
        {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_VERTICAL;
            FlyItemLayout item = showlist.remove(0);
            viewer.addView(item, 0, lp);
        }
    }

}
