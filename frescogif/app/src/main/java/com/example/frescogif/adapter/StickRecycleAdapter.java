package com.example.frescogif.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.frescogif.R;
import com.example.frescogif.bean.StickyHeadEntity;
import com.example.frescogif.bean.StockEntity;
import com.example.frescogif.callback.OnItemClickListener;
import com.example.frescogif.holder.RecyclerViewHolder;
import com.example.frescogif.utils.FullSpanUtil;

import java.util.List;

/**
 * Created by GG on 2018/2/5.
 *
 * @author shell ray
 */

public  class StickRecycleAdapter extends RecyclerView.Adapter<RecyclerViewHolder>implements CompoundButton.OnCheckedChangeListener {

    public final static int TYPE_DATA = 1;
    public final static int TYPE_STICKY_HEAD = 2;
    public final static int TYPE_SMALL_STICKY_HEAD_WITH_DATA = 3;
    public final static int TYPE_HEAD = 4;

    public List<StickyHeadEntity<StockEntity.StockInfo>> mData;

    public OnItemClickListener mItemClickListener;

    public StickRecycleAdapter(List<StickyHeadEntity<StockEntity.StockInfo>> data) {
        mData = data;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, TYPE_STICKY_HEAD);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        FullSpanUtil.onViewAttachedToWindow(holder, this, TYPE_STICKY_HEAD);
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerViewHolder holder = new RecyclerViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(viewType), parent, false));
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = holder.getLayoutPosition();
                    mItemClickListener.onItemClick(view, getData().get(position).getData(), position);
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        bindData(holder, getItemViewType(position), position, mData.get(position).getData());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemType();
    }

    public int getItemLayoutId(int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                return R.layout.item_stock_head;
            case TYPE_STICKY_HEAD:
                return R.layout.item_stock_sticky_head;
            case TYPE_DATA:
                return R.layout.item_stock_data;
            case TYPE_SMALL_STICKY_HEAD_WITH_DATA:
                return R.layout.item_stock_small_sticky_data;
        }
        return 0;
    }

    public  void bindData(RecyclerViewHolder holder, int viewType, int position, StockEntity.StockInfo item){

        int type = holder.getItemViewType();
        switch (type) {

            case TYPE_STICKY_HEAD:

                CheckBox checkBox = holder.getCheckBox(R.id.checkbox);
                checkBox.setTag(position);
                checkBox.setOnCheckedChangeListener(this);
                checkBox.setChecked(item.check);

                holder.setText(R.id.tv_stock_name, item.stickyHeadName);

                break;

            case TYPE_DATA:
                setData(holder, item);
                break;
            case TYPE_SMALL_STICKY_HEAD_WITH_DATA:
                setData(holder, item);
                holder.setText(R.id.tv_stock_name, item.stickyHeadName);
                break;

        }
    }

    private void setData(RecyclerViewHolder holder, StockEntity.StockInfo item) {
        final String stockNameAndCode = item.stock_name + "\n" + item.stock_code;
        SpannableStringBuilder ssb = new SpannableStringBuilder(stockNameAndCode);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#a4a4a7")), item.stock_name.length(), stockNameAndCode.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new AbsoluteSizeSpan(dip2px(holder.itemView.getContext(), 13)), item.stock_name.length(), stockNameAndCode.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.setText(R.id.tv_stock_name_code, ssb).setText(R.id.tv_current_price, item.current_price)
                .setText(R.id.tv_rate, (item.rate < 0 ? String.format("%.2f", item.rate) : "+" + String.format("%.2f", item.rate)) + "%");
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int pos = (Integer) buttonView.getTag();
        mData.get(pos).getData().check = isChecked;
    }

    public List<StickyHeadEntity<StockEntity.StockInfo>> getData() {
        return mData;
    }

    public void setData(List<StickyHeadEntity<StockEntity.StockInfo>> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

}
