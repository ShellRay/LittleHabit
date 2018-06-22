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
 *
 */
public class RunnerLinkLayout extends LinearLayout
{

    private final Context context;
    private  boolean index;
    List<FlyItemLinkLayout> showlist;
    List<RunnerLinkViewer> viewlist;


    public RunnerLinkLayout(Context context)
    {
        this(context, null);
    }

    public RunnerLinkLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RunnerLinkLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
        this.context = context;

        showlist = new ArrayList<FlyItemLinkLayout>();

        viewlist = new ArrayList<RunnerLinkViewer>();

        int count = 4;//跑道数量

        setWeightSum(count);
    }

    public void displayFlyItem(FlyItemLinkLayout item)
    {
        if(!index){
            index = true;
            for (int i = 0; i < 4; i++)
            {
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
                params.weight = 1;

                switch (i){
                    case 0:
                        RunnerLinkViewer viewer = new RunnerLinkViewer(context,this,4000* item.listDate.size());
                        addViewInLayout(viewer, getChildCount(), params);
                        viewlist.add(viewer);
                        break;
                    case 1:
                        RunnerLinkViewer viewer1 = new RunnerLinkViewer(context,this,3500*item.listDate.size());
                        addViewInLayout(viewer1, getChildCount(), params);
                        viewlist.add(viewer1);
                        break;
                    case 2:
                        RunnerLinkViewer viewer2 = new RunnerLinkViewer(context,this,4500*item.listDate.size());
                        addViewInLayout(viewer2, getChildCount(), params);
                        viewlist.add(viewer2);
                        break;
                    case 3:
                        RunnerLinkViewer viewer3 = new RunnerLinkViewer(context,this,5000*item.listDate.size());
                        addViewInLayout(viewer3, getChildCount(), params);
                        viewlist.add(viewer3);
                        break;
                    default:
                        break;
                }

            }
        }
        showlist.add(item);

        RunnerLinkViewer viewer = getAvaliableViewer();

        if (viewer != null)
        {
            runnerAvaliable(viewer,true);
        }
    }

    RunnerLinkViewer getAvaliableViewer()
    {
        RunnerLinkViewer result = null;

        for (RunnerLinkViewer viewer : viewlist)
        {
            if (viewer.getChildCount() == 0)
            {
                result = viewer;
                break;
            }
        }

        return result;
    }

    void runnerAvaliable(RunnerLinkViewer viewer, boolean isIndex)
    {
        if (showlist.size() > 0)
        {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_VERTICAL;
            FlyItemLinkLayout item = showlist.remove(0);
            if(isIndex){
                viewer.addView(item, 0, lp);
            }else {
                viewer.addView(item, 1, lp);
            }

        }
    }

}
