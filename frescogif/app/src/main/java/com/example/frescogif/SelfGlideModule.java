package com.example.frescogif;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by GG on 2018/12/4.
 */

@GlideModule

public final class SelfGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));

        Log.e("shell","glide 已经初始化完成");
        //自定义缓存大小
//        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
//        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));

//        Disk Cache.自定义内置磁盘缓存大小
//        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));

//        Disk Cache.自定义内置磁盘缓存大小并指定路径.
//        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
//        builder.setDiskCache(
//                new InternalCacheDiskCacheFactory(context, "cacheFolderName", diskCacheSizeBytes));

//        Disk Cache.自定义外置磁盘缓存大小并指定路径.
//        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
//        builder.setDiskCache(
//                new ExternalCacheDiskCacheFactory(context, "cacheFolderName", diskCacheSizeBytes));


    }

//    isManifestParsingEnabled 设置清单解析，设置为false，避免添加相同的modules两次
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }


}
