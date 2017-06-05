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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;


import java.util.List;



/**
 * Created by yuanwei on 17/3/20.
 */
public class LiveGiftAdapter extends BaseAdapter {
    private static final String TAG = "LiveGiftAdapter";
    private  Context context;
    private int selected;
    private int clicknum;
    private static final String GIFT_IMG_TYPE_GIF = "_9.gif";
    private static final String GIFT_IMG_TYPE_PNG = "_7.png";
    //private static final String Tag_no_gif_res

    List<String> datalist;
    LayoutInflater inflater;

    public LiveGiftAdapter(Context context, List<String> datalist, LayoutInflater inflater) {
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

    public void cancleSelect(View view) {
        if (view != null) {
            TextView giftCount = (TextView) view.findViewById(R.id.giftCount);
            giftCount.setVisibility(View.GONE);
            giftCount.setText("");
            view.setSelected(false);

            SimpleDraweeView giftImage = (SimpleDraweeView) view.findViewById(R.id.giftImage);
            if (mGiftCtrHandler == null) {
                mGiftCtrHandler = new Handler();
            }

            final Animatable anim = giftImage.getController().getAnimatable();
            if (anim != null) {
                // 再次star 让动画回到第一帧，100毫秒后停止
                anim.start();
                mGiftCtrHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        anim.stop();
                    }
                }, 100);
            }
        }
        selected = -1;
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
            SimpleDraweeView giftImage = (SimpleDraweeView) view.findViewById(R.id.giftImage);
            Animatable animatable = giftImage.getController().getAnimatable();
            if (animatable != null && !animatable.isRunning()) {
                giftImage.getController().getAnimatable().start();
            }
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

        public SimpleDraweeView giftImage;
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
            convertView = inflater.inflate(R.layout.item_roomlivegift, null);
            vieHolder = new ViewHolder();
            vieHolder.giftImage = (SimpleDraweeView) convertView.findViewById(R.id.giftImage);
            vieHolder.giftPrice =   (TextView) convertView.findViewById(R.id.giftPrice);
            vieHolder.giftLable =  (TextView) convertView.findViewById(R.id.giftLable);
            vieHolder.giftCount =   (TextView) convertView.findViewById(R.id.giftCount);
            convertView.setTag(vieHolder);
        }else{
            vieHolder = (ViewHolder) convertView.getTag();
        }

        String giftUrl = datalist.get(position);

        // 已知道 该礼物无 gif 资源  直接加载 png
//        if (vieHolder.noGifRes) {
//            vieHolder.giftImage.setImageURI(Uri.parse(gift.giftViewSrc));
//        } else {
//            // 默认所有的礼物都是 gif资源 加载失败 再去加载 png
//            String giftUrl = "";
//            if (gift.giftViewSrc != null) {
//                if (gift.giftViewSrc.endsWith(GIFT_IMG_TYPE_PNG)) {
//                    giftUrl = gift.giftViewSrc.replace(GIFT_IMG_TYPE_PNG, GIFT_IMG_TYPE_GIF);
//                }
//            }

        boolean begin = false;
        if(!begin){
//                Toast.makeText(context,""+giftUrl,Toast.LENGTH_SHORT).show();
            begin = true;
            }

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(false)
                    .setOldController(vieHolder.giftImage.getController())
                    .setUri(Uri.parse(giftUrl))
                    .build();
            vieHolder.giftImage.setController(controller);
//        }


        vieHolder.giftPrice.setText( "0");
        vieHolder.giftLable.setText("gif");

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) vieHolder.giftCount.getLayoutParams();
        params.gravity = (position % 8 == 6 || position % 8 == 7) ? Gravity.LEFT : Gravity.RIGHT;
        vieHolder.giftCount.setLayoutParams(params);

        String text = clicknum < 10 ? "x" + clicknum : "  x" + clicknum + "  ";
        vieHolder.giftCount.setText(text);

        vieHolder.giftCount.setVisibility(position == selected ? View.VISIBLE : View.GONE);

        convertView.setSelected(position == selected);

        return convertView;
    }


}
