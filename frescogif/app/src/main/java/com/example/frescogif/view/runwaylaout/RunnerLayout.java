package com.example.frescogif.view.runwaylaout;

import android.content.Context;
import android.graphics.Color;
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

    private final Context context;
    private  boolean index;
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
        this.context = context;

        showlist = new ArrayList<FlyItemLayout>();

        viewlist = new ArrayList<RunnerViewer>();

        int count = 4;//跑道数量

        setWeightSum(count);
    }

    public void displayFlyItem(FlyItemLayout item)
    {
        if(!index){
            index = true;
            for (int i = 0; i < 4; i++)
            {
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
                params.weight = 1;

                switch (i){
                    case 0:
                        RunnerViewer viewer = new RunnerViewer(context,this,2500* item.listDate.size());
                        addViewInLayout(viewer, getChildCount(), params);
                        viewlist.add(viewer);
                        break;
                    case 1:
                        RunnerViewer viewer1 = new RunnerViewer(context,this,1500*item.listDate.size());
                        addViewInLayout(viewer1, getChildCount(), params);
                        viewlist.add(viewer1);
                        break;
                    case 2:
                        RunnerViewer viewer2 = new RunnerViewer(context,this,2000*item.listDate.size());
                        addViewInLayout(viewer2, getChildCount(), params);
                        viewlist.add(viewer2);
                        break;
                    case 3:
                        RunnerViewer viewer3 = new RunnerViewer(context,this,3000*item.listDate.size());
                        addViewInLayout(viewer3, getChildCount(), params);
                        viewlist.add(viewer3);
                        break;
                    default:
                        break;
                }

            }
        }
        showlist.add(item);

        RunnerViewer viewer = getAvaliableViewer();

        if (viewer != null)
        {
            runnerAvaliable(viewer,true);
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

    void runnerAvaliable(RunnerViewer viewer,boolean isIndex)
    {
        if (showlist.size() > 0)
        {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_VERTICAL;
            FlyItemLayout item = showlist.remove(0);
            if(isIndex){
                viewer.addView(item, 0, lp);
            }else {
                viewer.addView(item, 1, lp);
            }

        }
    }

}
