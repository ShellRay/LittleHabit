package com.example.frescogif.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.frescogif.R;

import java.util.List;

/**
 * Created by GG on 2017/5/26.
 */
public class SignAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
    private  List mData;
    private  Context context;

    public SignAdapter(Context p0, List list) {
        this.context = p0;
        this.mData = list;
    }

    private class StyleViewHolder extends RecyclerView.ViewHolder{

        private final TextView tv_item;

        public StyleViewHolder(View itemView) {
            super(itemView);
            tv_item = (TextView) itemView.findViewById(R.id.tv_item);

        }

        public void bindData(int postion) {
            String text = (String) mData.get(postion);
            tv_item.setText(text);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sign, parent, false);
        return new StyleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((StyleViewHolder)holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
