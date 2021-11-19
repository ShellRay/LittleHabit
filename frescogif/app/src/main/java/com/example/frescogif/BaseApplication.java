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

        try {
            File cacheDir = new File(Environment.getExternalStorageDirectory() + "/frescogif/cache", "http");
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
