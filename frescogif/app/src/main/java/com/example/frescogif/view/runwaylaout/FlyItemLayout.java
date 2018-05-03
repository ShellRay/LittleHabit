package com.example.frescogif.view.runwaylaout;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.frescogif.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 *
 * @author TPX
 * @version 1.0.0
 * @description 飞屏的布局
 * @modify
 */
public class FlyItemLayout extends FrameLayout
{
    RelativeLayout fly_screen;
    private TextView mMsgTV;


    public  int  type;
    public static final int FLY_SCREEN            = 0;
    public static final int FLY_LABA_SCREEN       = 1;
    public static final int FLY_SUPER_GIFT_SCREEN = 2;
    public int flySupVar = 1;
    private final LinearLayout msgContainerLL;

    public FlyItemLayout(Context context, int type)
    {
        super(context);
        this.type = type;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.li_item_fly_screen, this);
        fly_screen = (RelativeLayout) view.findViewById(R.id.fly_screen);
        msgContainerLL = (LinearLayout) view.findViewById(R.id.ll_msg_container);
        mMsgTV = (TextView) view.findViewById(R.id.tv_fly_screen_msg);

        if (type == FLY_SCREEN)
        {
            fly_screen.setVisibility(View.VISIBLE);
//            laba.setVisibility(View.GONE);
//            super_gift.setVisibility(View.GONE);
        }


    }

    public void setMsg(String msg)
    {
        if (TextUtils.isEmpty(msg))
        {
            return;
        }
        if (type == FLY_SCREEN)
        {
            mMsgTV.setText(msg);
        }
        else if (type == FLY_LABA_SCREEN)
        {
//            laba_msg.setText(msg);
            /*laba_msg.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            laba_msg.setSingleLine(true);
            laba_msg.setSelected(true);
            laba_msg.setFocusable(true);
            laba_msg.setFocusableInTouchMode(true);*/

        }
        else
        {
//            super_gift_msg.setText(msg);
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
}
