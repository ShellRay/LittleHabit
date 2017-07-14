package com.example.frescogif.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;

import java.io.File;

/**
 * Created by GG on 2017/6/27.
 */
public class PullScaleActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_scale);

    }

    public void makedir(View view){
        String storePath  = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "aaa/photos/pho";
        //先确定是否有这个文件夹防止保存的时候出错
        File appDir  = new File(storePath);
        if (!appDir .exists()) {
            appDir .mkdirs();
        }
        if(appDir.exists()) {
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        }
    }
}
