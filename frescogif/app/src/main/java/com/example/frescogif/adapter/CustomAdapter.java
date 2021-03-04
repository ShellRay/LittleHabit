package com.example.frescogif.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frescogif.GlideApp;
import com.example.frescogif.R;
import com.example.frescogif.utils.GlideLoadUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by GG on 2017/5/26.
 */
public class CustomAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
    private  List mData;
    private  Context context;

    public CustomAdapter(Context p0, List list) {
        this.context = p0;
        this.mData = list;
    }


    private class StyleViewHolder extends RecyclerView.ViewHolder{

        private final TextView tv_item;
        private final ImageView iv_gif;

        public StyleViewHolder(View itemView) {
            super(itemView);
            tv_item = (TextView) itemView.findViewById(R.id.tv_item1);
            iv_gif = (ImageView) itemView.findViewById(R.id.iv_gif1);

        }

        public void bindData(int postion) {
            String text = (String) mData.get(postion);
            tv_item.setText(text);
            String url = "https://res.guagua.cn/pic//6897_9.gif";
             GlideLoadUtils.getInstance().loadImageAsGif(context,url.toString(),iv_gif);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom, parent, false);
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

    @Override
    public long getItemId(int position) {
        return position;
    }
}
