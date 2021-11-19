package com.example.frescogif.activity;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.utils.Utils;
import com.example.frescogif.videocompressor.VideoCompress;
import com.example.frescogif.view.SportlightRoomView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author ShellRay
 * Created  on 2020/8/12.
 * @description
 */
public class SportlightActivity extends BaseActivity {


    private SportlightRoomView sportlightRoomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportlight);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        sportlightRoomView = findViewById(R.id.sportlight);
    }
    public void startShow(View view) {
        float masterX = getWindowManager().getDefaultDisplay().getWidth()/2;
        float masterY = getWindowManager().getDefaultDisplay().getHeight()/2 - 300;
        sportlightRoomView.showMasterView(masterX,masterY);
    }

    //左边
    public void showGuest(View view) {

        float guestX = getWindowManager().getDefaultDisplay().getWidth()/2 - 400;
        float guestY = getWindowManager().getDefaultDisplay().getHeight()/2 - 400;
        sportlightRoomView.showGuestView(true,guestX,guestY);
    }

    //右边
    public void showGuestA(View view) {
        float guestX = getWindowManager().getDefaultDisplay().getWidth()/2 + 150;
        float guestY = getWindowManager().getDefaultDisplay().getHeight()/2 - 500;
        sportlightRoomView.showGuestView(false,guestX,guestY);


    }

    public void goneMaseter(View view) {
        sportlightRoomView.goneMaseter();
    }


    public void goneGuest(View view) {
        sportlightRoomView.goneGuest();
    }

}
