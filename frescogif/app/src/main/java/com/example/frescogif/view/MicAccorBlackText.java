package com.example.frescogif.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

//import com.guagua.commerce.sdk.LiveSDKManager;

/**
 * Created by GG on 2017/9/14.
 */
public class MicAccorBlackText extends TextView{

    private Typeface typeface;

    public MicAccorBlackText(Context context) {
        this(context,null);
    }

    public MicAccorBlackText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MicAccorBlackText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        try {
            /*
             * 必须事先在assets底下创建一fonts文件夹，并放入要使用的字体文件(.ttf/.otf)
             * 并提供相对路径给creatFromAsset()来创建Typeface对象
             */
//            typeface = LiveSDKManager.getInstance().getTypeface(); //最好在初始化的时候做好
            // 当使用外部字体却又发现字体没有变化的时候(以 Droid Sans代替)，通常是因为这个字体android没有支持,而非你的程序发生了错误
            if (typeface != null) {
                setTypeface(typeface);
                setIncludeFontPadding(false);//默认的资源与Android不太匹配，字体会占用很大的背景空间
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
