package com.example.frescogif.utils;

import android.content.Context;

/**
 * Created by GG on 2017/6/6.
 */
public class Utils {

    public static int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}
