package com.example.frescogif.view.danmu;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimationHelper {
    public static Animation createTranslateAnim(Context context, int fromX, int toX) {
        TranslateAnimation tlAnim = new TranslateAnimation(fromX, toX, 0, 0);
        long duration = (long) (Math.abs(toX - fromX) * 1.0f / BarrageTools.getScreenWidth(context) * 3000);

        tlAnim.setDuration(5000);
//        tlAnim.setInterpolator(new DecelerateAccelerateInterpolator());
        tlAnim.setInterpolator(new LinearInterpolator());
        tlAnim.setFillAfter(true);
        return tlAnim;
    }

}