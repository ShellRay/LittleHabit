package com.example.frescogif.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.frescogif.R;

import java.util.List;

/**
 * Created by GG on 2017/5/26.
 */
public class NormalAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
    private  List mData;
    private  Context context;

    int [] drable = { R.drawable.green_item_selector,R.drawable.blue_item_selector,R.drawable.red_item_selector,R.drawable.black_item_selector};
    int [] colors = { R.color.green,R.color.blue,R.color.red,R.color.black,};


    public NormalAdapter(Context p0, List list) {
        this.context = p0;
        this.mData = list;
    }


    private class StyleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_item;
        private final TextView tv_item2;
        private final TextView tv_item3;
        private final TextView tv_item4;

        public StyleViewHolder(View itemView) {
            super(itemView);
            tv_item = (TextView) itemView.findViewById(R.id.tv_item1);
            tv_item2 = (TextView) itemView.findViewById(R.id.tv_item2);
            tv_item3 = (TextView) itemView.findViewById(R.id.tv_item3);
            tv_item4 = (TextView) itemView.findViewById(R.id.tv_item4);
            tv_item.setOnClickListener(this);
            tv_item2.setOnClickListener(this);
            tv_item3.setOnClickListener(this);
            tv_item4.setOnClickListener(this);
//            int v = (int)(Math.random() * (drable.length - 1));

//            tv_item.setTextColor(context.getResources().getColor(colors[getPosition()]));
//            tv_item.setBackgroundResource(drable[getPosition()]);
//            String resourceName = context.getResources().getResourceName(R.drawable.bg_tag_black_color);

//            Log.e("resourceName","resourceName = "+ resourceName + "position===" + v);

        }

        public void bindData(int postion) {
            String text = (String) mData.get(postion);
            tv_item.setTextColor(context.getResources().getColor(colors[postion%drable.length]));
            tv_item.setBackgroundResource(drable[postion%drable.length]);
            tv_item.setText(text);
        }

        @Override
        public void onClick(View v) {
            tv_item.setTextColor(Color.WHITE);
            tv_item.setSelected(true);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_normal, parent, false);
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
