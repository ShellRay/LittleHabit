package com.example.frescogif.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frescogif.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/5/26.
 */
public class DesignAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
    private  List mData;
    private  Context context;
    private final List<Integer> mheight;

    public DesignAdapter(Context p0, List list) {
        this.context = p0;
        this.mData = list;

        //随机高度集合
        mheight = new ArrayList<Integer>();
        for(int i=0;i<mData.size();i++){
            mheight.add((int)(200+Math.random()*600));
        }
    }


    private class DesignViewHolder extends RecyclerView.ViewHolder{

        TextView tv_item;

        public DesignViewHolder(View itemView) {
            super(itemView);
            tv_item = (TextView) itemView.findViewById(R.id.tv_item2);

        }

        public void bindData(int postion) {
            String text = (String) mData.get(postion);
            tv_item.setText(text);
        }
    }

    @Override
    public DesignViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new DesignViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewGroup.LayoutParams lp=holder.itemView.getLayoutParams();
        lp.height=mheight.get(position);
        holder.itemView.setLayoutParams(lp);
        ((DesignViewHolder)holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
