package com.example.frescogif.view.runwaylaout;

import android.content.Context;
import android.graphics.Rect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.frescogif.R;
import com.example.frescogif.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 * @author TPX
 * @version 1.0.0
 * @description 飞屏的布局
 * @modify
 */
public class FlyItemLinkLayout extends FrameLayout
{
    RelativeLayout fly_screen;
    private TextView mMsgTV;


    public  int  type;
    public static final int FLY_SCREEN            = 0;
    public static final int FLY_LABA_SCREEN       = 1;
    public static final int FLY_SUPER_GIFT_SCREEN = 2;
    public int flySupVar = 1;
    private final LinearLayout msgContainerLL;
    private final RecyclerView listView;
    public List listDate= new ArrayList();

    public FlyItemLinkLayout(final Context context, int type)
    {
        super(context);
        this.type = type;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.li_item_fly_screen, this);
        fly_screen = (RelativeLayout) view.findViewById(R.id.fly_screen);
        msgContainerLL = (LinearLayout) view.findViewById(R.id.ll_msg_container);
        mMsgTV = (TextView) view.findViewById(R.id.tv_fly_screen_msg);
        listView = (RecyclerView) view.findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        listView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.left = Utils.convertDpToPixel(context,250);
            }
        });
        if (type == FLY_SCREEN)
        {
            fly_screen.setVisibility(View.VISIBLE);
//            laba.setVisibility(View.GONE);
//            super_gift.setVisibility(View.GONE);
        }


    }

    public void setMsg(ArrayList<Object> msg)
    {
        /*if (TextUtils.isEmpty(msg))
        {
            return;
        }*/
        if (type == FLY_SCREEN)
        {
            listDate = msg;
//            mMsgTV.setText(msg);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(listDate);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
//        EventBusManager.getInstance().register(this);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
//        EventBusManager.getInstance().unregister(this);
    }
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private final List<String> mValues;

        public RecyclerViewAdapter(List<String> items) {
            mValues = items;
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.danma_item, parent, false);
            return new RecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.mContentView.setText(mValues.get(new Random().nextInt(mValues.size()-1)));//new Random().nextInt(mValues.size()-1)
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
