package com.example.frescogif;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by GG on 2017/9/22.
 */
public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "system/fonts/Roboto-Bold.ttf")
                .setDefaultFontPath(Environment.getRootDirectory().getAbsoluteFile() + File.separator + "fonts/Roboto-Regular.ttf")//小米是得用文件工具根目录 system/fonts
                .setFontAttrId(R.attr.fontPath)
//                .addCustomViewWithSetTypeface(CustomViewWithTypefaceSupport.class)
//                .addCustomStyle(TextField.class, R.attr.textFieldStyle)
                .build()
        );
    }
}
