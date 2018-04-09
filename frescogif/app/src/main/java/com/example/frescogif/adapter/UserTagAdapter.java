package com.example.frescogif.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;


import com.example.frescogif.R;
import com.example.frescogif.bean.AnchorImpressionBean;
import com.example.frescogif.bean.TagBean;
import com.example.frescogif.utils.Utils;
import com.example.frescogif.view.loadingview.Typefaces;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/5/26.
 */
public class UserTagAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
    private List<AnchorImpressionBean> mData;
    private Context context;

    private ArrayList<TagBean> checkedList = new ArrayList(3);
    private View.OnClickListener listener;
    private Handler handler = new Handler();
    public UserTagAdapter(Context p0, List<AnchorImpressionBean> list) {
        this.context = p0;
        this.mData = list;
    }


    private class StyleViewHolder extends RecyclerView.ViewHolder {


        private final TextView view;

        public StyleViewHolder(View itemView) {
            super(itemView);
            view = (TextView) itemView;
        }

        @SuppressLint("ResourceAsColor")
        public void bindData(final int position) {
            view.setBackgroundColor(android.R.color.transparent);
            view.setText(mData.get(view.getId()).getTagName());
            int paddingTop = Utils.convertDpToPixel(context, 3);
            final int paddingLeft = Utils.convertDpToPixel(context, 5);
            int viewWidth = Utils.convertDpToPixel(context, 60);
            int viewHight = Utils.convertDpToPixel(context, 22);
            view.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
            view.setWidth(viewWidth);
            view.setHeight(viewHight);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(11);
            view.setMaxLines(1);
            view.setTypeface(Typeface.DEFAULT_BOLD);


            final GradientDrawable pressedDrawable = new GradientDrawable();
            pressedDrawable.setShape(GradientDrawable.RECTANGLE);
            pressedDrawable.setCornerRadius(100);
            pressedDrawable.setColor(Color.parseColor("#" + mData.get(position).getTagRGB()));

            final GradientDrawable normalDrawable = new GradientDrawable();
            normalDrawable.setCornerRadius(100);
            normalDrawable.setStroke(5, Color.parseColor("#" + mData.get(position).getTagRGB()));
            normalDrawable.setColor(Color.parseColor("#00ffffff"));

            view.setBackgroundDrawable(normalDrawable);
            view.setTextColor(Color.parseColor(mData.get(position).isCheck() ? "#ffffff" : "#" + mData.get(position).getTagRGB()));


            if (mData.get(position).isCheck()) {
                view.setTextColor(context.getResources().getColor(R.color.white));
                view.setBackgroundDrawable(pressedDrawable);
            }

            view.setOnClickListener(null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    listener.onClick(view);
                    if (mData.get(view.getId()).isCheck()) {
                        mData.get(view.getId()).setCheck(false);
                        view.setBackgroundDrawable(normalDrawable);
                        view.setTextColor(Color.parseColor(mData.get(position).isCheck() ? "#ffffff" : "#" + mData.get(position).getTagRGB()));
                        for (TagBean bean : checkedList) {
                            if (bean.getTagid() == mData.get(view.getId()).getTagId()) {
                                checkedList.remove(bean);
                                specialUpdate(view.getId());
                                return;
                            }
                        }
                    } else {
                        mData.get(view.getId()).setCheck(true);
                        view.setTextColor(context.getResources().getColor(R.color.white));
                        view.setBackgroundDrawable(pressedDrawable);
                    }


                    if (checkedList.size() > 2) {
                        int tagid = checkedList.get(0).getTagid();
                        for (int x = 0; x < mData.size(); x++) {
                            if (tagid == (mData.get(x).getTagId())) {
                                mData.get(x).setCheck(false);
                                checkedList.remove(0);
                                specialUpdate(x);
                            }
                        }
                    }
                    TagBean tagBean = new TagBean();
                    tagBean.setTag_name(mData.get(position).getTagName());
                    tagBean.setTag_rgb(mData.get(position).getTagRGB());
                    tagBean.setTagid(mData.get(position).getTagId());
                    checkedList.add(tagBean);

                }
            });

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = new TextView(context);
        return new StyleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((StyleViewHolder) holder).view.setId(position);
        ((StyleViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<TagBean> getCheckedList() {
        return checkedList;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    //xCannot call this method while RecyclerView is computing a layout or scrolling 异常
    private void specialUpdate(final int positon) {

        final Runnable r = new Runnable() {
            public void run() {
                notifyItemChanged(positon);
            }
        };
        handler.post(r);
    }

}
