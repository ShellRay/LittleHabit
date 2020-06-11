package com.example.frescogif;

import android.app.Application;
import android.net.http.HttpResponseCache;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.example.frescogif.utils.GlideLoadUtils;

import java.io.File;
import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by GG on 2017/9/22.
 */
public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "system/fonts/Roboto-Bold.ttf")//这样并不能找到这个字体，具体不知道是为什么，因为系统的是几百k大小而这个是8M
                .setDefaultFontPath(Environment.getRootDirectory().getAbsoluteFile() + File.separator + "fonts/Roboto-Regular.ttf")//小米是得用文件工具根目录 system/fonts
                .setFontAttrId(R.attr.fontPath)
//                .addCustomViewWithSetTypeface(CustomViewWithTypefaceSupport.class)
//                .addCustomStyle(TextField.class, R.attr.textFieldStyle)
                .build()
        );

        try {
            File cacheDir = new File(Environment.getExternalStorageDirectory() + "/frescogif/cache", "http");
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
