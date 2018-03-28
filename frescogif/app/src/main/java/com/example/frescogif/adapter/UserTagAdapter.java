package com.example.frescogif.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;


import com.example.frescogif.R;
import com.example.frescogif.bean.AnchorImpressionBean;
import com.example.frescogif.bean.TagBean;
import com.example.frescogif.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/5/26.
 */
public class UserTagAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
    private  List<AnchorImpressionBean> mData;
    private  Context context;
    private boolean[] flag;

    private ArrayList<TagBean> checkedList = new ArrayList(3);
    private  List<RadioButton>  viewList = new ArrayList();
    private View.OnClickListener listener;

    public UserTagAdapter(Context p0, List<AnchorImpressionBean> list) {
        this.context = p0;
        this.mData = list;
        flag = new boolean[mData.size()];
    }


    private class StyleViewHolder extends RecyclerView.ViewHolder {


        private final RadioButton view;

        public StyleViewHolder(View itemView) {
            super(itemView);
            view = (RadioButton) itemView;
        }

        public void bindData(final int position) {

            Log.e("userTag","bindData === "+view.getId());
            view.setButtonDrawable(android.R.color.transparent);
            view.setText(position+"--"+ view.getId());//mData.get(postion).getTagName());
            int paddingTop = Utils.convertDpToPixel(context, 3);
            int paddingLeft = Utils.convertDpToPixel(context, 5);
            int viewWidth = Utils.convertDpToPixel(context, 60);
            int viewHight = Utils.convertDpToPixel(context, 22);
            view.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
            view.setWidth(viewWidth);
            view.setHeight(viewHight);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(11);
            view.setMaxLines(1);
            view.setChecked(flag[position]);
            view.setOnCheckedChangeListener(null);
            view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    int position = (int) view.getTag();
                    if(position == view.getId()){
                        flag[position] = isChecked;
                        listener.onClick(view);
                        view.setTextColor(context.getResources().getColor(R.color.white));
                        view.setChecked(true);

                        if(checkedList.size()> 2 ){
                            int tagid = checkedList.get(0).getTagid();

                            for (int x = 0; x < viewList.size(); x++ )//RadioButton radioButton : viewList) {
                            {
                                RadioButton radioButton = viewList.get(x);
                                if(tagid == (mData.get(radioButton.getId()).getTagId())){
                                    radioButton.setChecked(false);
                                    flag[radioButton.getId()] = false;
                                    radioButton.setTextColor(Color.parseColor("#"+mData.get(x).getTagRGB()));
                                    checkedList.remove(0);
                                }
                            }
                        }
                        TagBean tagBean = new TagBean();
                        tagBean.setTag_name(mData.get(position).getTagName());
                        tagBean.setTag_rgb(mData.get(position).getTagRGB());
                        tagBean.setTagid(mData.get(position).getTagId());
                        checkedList.add(tagBean);
                    }}
            });

            GradientDrawable normalDrawable = new GradientDrawable();
            normalDrawable.setCornerRadius(100);
            normalDrawable.setStroke(1, Color.parseColor("#"+mData.get(position).getTagRGB()));
            normalDrawable.setColor(Color.parseColor("#00ffffff"));
            view.setBackgroundDrawable(normalDrawable);
            view.setTextColor(Color.parseColor("#"+ mData.get(position).getTagRGB()));

            // 设置按下的灰色背景
            GradientDrawable pressedDrawable = new GradientDrawable();
            pressedDrawable.setShape(GradientDrawable.RECTANGLE);
            pressedDrawable.setCornerRadius(100);
            pressedDrawable.setColor(Color.parseColor("#"+mData.get(position).getTagRGB()));

            // 背景选择器
            StateListDrawable stateDrawable = new StateListDrawable();
            stateDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
            stateDrawable.addState(new int[]{android.R.attr.state_checked}, pressedDrawable);
            stateDrawable.addState(new int[]{}, normalDrawable);

            // 设置背景选择器到TextView上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(stateDrawable);
            }

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RadioButton view  = new RadioButton(context);
        view.setChecked(false);
        viewList.add(view);
        return new StyleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((StyleViewHolder) holder).view.setId(position);
        ((StyleViewHolder)holder).bindData(position);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<TagBean> getCheckedList(){
        return checkedList;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
}
