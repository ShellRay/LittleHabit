package com.example.frescogif;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frescogif.bean.GiftDialogBean;
import com.example.frescogif.utils.GlideLoadUtils;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.List;


/**
 * Created
 */
public class LiveGlideGiftAdapter extends BaseAdapter {
    private static final String TAG = "LiveGiftAdapter";
    private  Context context;
    private int selected;
    private int clicknum;
    private static final String GIFT_IMG_TYPE_GIF = "_9.gif";
    private static final String GIFT_IMG_TYPE_PNG = "_7.png";
    //private static final String Tag_no_gif_res

    List<GiftDialogBean> datalist;
    LayoutInflater inflater;

    public LiveGlideGiftAdapter(Context context, List<GiftDialogBean> datalist, LayoutInflater inflater) {
        this.selected = -1;
        this.clicknum = -1;
        this.datalist = datalist;
        this.inflater = inflater;
        this.context = context;
    }

    public int getSelected() {
        return selected;
    }

    public int getClicknum() {
        return clicknum;
    }

    private Handler mGiftCtrHandler;

    public void cancleSelect(View view, int selected) {
        if (view != null) {
            TextView giftCount = (TextView) view.findViewById(R.id.giftCount);
            giftCount.setVisibility(View.GONE);
            giftCount.setText("");
            view.setSelected(false);

            ImageView giftImage = (ImageView) view.findViewById(R.id.giftImage);
            if (mGiftCtrHandler == null) {
                mGiftCtrHandler = new Handler();
            }

            String url = "https://res.guagua.cn/pic//6897_9.gif";
            GlideLoadUtils.getInstance().loadImageAsBitmap(context,datalist.get(selected).path,giftImage);

        }
        this.selected = -1;
        clicknum = -1;
        //notifyDataSetChanged();
    }

    public void updateSelect(View view, int position, int num) {
        this.selected = position;
        this.clicknum = num;


        if (view != null) {
            TextView giftCount = (TextView) view.findViewById(R.id.giftCount);
            giftCount.setVisibility(View.VISIBLE);
            String text = clicknum < 10 ? "x" + clicknum : "  x" + clicknum + "  ";
            giftCount.setText(text);
            view.setSelected(true);

            ObjectAnimator a1 = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.06f, 1.0f).setDuration(200);
            ObjectAnimator a2 = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.06f, 1.0f).setDuration(200);

            AnimatorSet set = new AnimatorSet();
            set.play(a1).with(a2);
            set.start();

            // 播放 gif
            ImageView giftImage = (ImageView) view.findViewById(R.id.giftImage);
            String url = "https://res.guagua.cn/pic//6897_9.gif";
            GlideLoadUtils.getInstance().loadImageAsGif(context,datalist.get(position).path,giftImage);

        }

    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    private static class MyControllerListener extends BaseControllerListener<ImageInfo> {
        private String mGiftUrl;
        //private SimpleDraweeView mTargetView;
        private ViewHolder mViewHolder;

        public MyControllerListener(ViewHolder viewHolder, String imageUrl) {
            this.mGiftUrl = imageUrl;
            //this.mTargetView = targetView;
            this.mViewHolder = viewHolder;
        }


        @Override
        public void onFailure(String id, Throwable throwable) {
            // 图片加载失败时 回调
            //Log.e(TAG, "========= 图片加载失败:" + id);
            // 1 加载gif 图片失败，直接加载png 图片 ？
            reloadGiftImg();
        }

        private void reloadGiftImg() {
            if (mViewHolder != null) {
               // Log.e(TAG, "========= 标志 礼物类型");
                mViewHolder.giftImage.setImageURI(Uri.parse(mGiftUrl));
                // 记录该 资源无gif 格式的资源
               // mViewHolder.giftImage.setTag(GIFT_IMG_TYPE_PNG);
                mViewHolder.noGifRes = true;
            }
        }

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            //Log.e(TAG, "========= 图片加载成功:" + id);
        }
    }


    private class ViewHolder {

        public ImageView giftImage;
        public TextView giftPrice;
        public TextView giftLable;
        public TextView giftCount;
        public boolean  noGifRes;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder vieHolder;
        if (convertView == null) {
            //Log.e(TAG,"=========>11111111");
            convertView = inflater.inflate(R.layout.item_room_glide_livegift, null);
            vieHolder = new ViewHolder();
            vieHolder.giftImage = (ImageView) convertView.findViewById(R.id.giftImage);
            vieHolder.giftPrice =   (TextView) convertView.findViewById(R.id.giftPrice);
            vieHolder.giftLable =  (TextView) convertView.findViewById(R.id.giftLable);
            vieHolder.giftCount =   (TextView) convertView.findViewById(R.id.giftCount);
            convertView.setTag(vieHolder);
        }else{
            vieHolder = (ViewHolder) convertView.getTag();
        }

        String giftUrl = datalist.get(position).path;

        String url = "https://res.guagua.cn/pic//6897_9.gif";
        GlideLoadUtils.getInstance().loadImageAsBitmap(context,giftUrl,vieHolder.giftImage);

//        }

        vieHolder.giftPrice.setText( "" +datalist.get(position).giftNum);
        vieHolder.giftLable.setText(datalist.get(position).giftName);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) vieHolder.giftCount.getLayoutParams();
//        个位数=1831%10  获取个位数
        params.gravity = position % 10 == 8 || position % 10 == 9 ? Gravity.LEFT : Gravity.RIGHT;
        vieHolder.giftCount.setLayoutParams(params);

        String text = clicknum < 10 ? "x" + clicknum : "  x" + clicknum + "  ";
        vieHolder.giftCount.setText(text);

        vieHolder.giftCount.setVisibility(position == selected ? View.VISIBLE : View.GONE);

        convertView.setSelected(position == selected);

        return convertView;
    }


}
