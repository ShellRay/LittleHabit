package com.example.frescogif;

import android.app.Application;
import android.net.http.HttpResponseCache;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.example.frescogif.utils.GlideLoadUtils;
import com.facebook.stetho.Stetho;

import java.io.File;
import java.io.IOException;

import androidx.multidex.MultiDexApplication;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by GG on 2017/9/22.
 */
public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //https://github.com/facebook/stetho
        //chrome://inspect/ 在浏览器中打开
        if(BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
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
