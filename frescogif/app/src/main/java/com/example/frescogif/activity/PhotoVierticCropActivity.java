package com.example.frescogif.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.utils.MediaUtils;
import com.example.frescogif.view.camera.ClipImageLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by GG on 2017/6/15.
 */
public class PhotoVierticCropActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_cancel;
    private TextView tv_complete;
    private ClipImageLayout cropImageView;
    private String mCropPath;
    private String uri;
    private boolean isGallery;
    private Uri uri1;
    private Bitmap bitmap;
    private InputStream inputStream;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_head_crop);

        Intent intent = getIntent();
        mCropPath = intent.getStringExtra("cropPath");
        uri = intent.getStringExtra("uri");
        isGallery = intent.getBooleanExtra("isGallery", false);
        uri1 = Uri.parse(uri);

        cropImageView = (ClipImageLayout) findViewById(R.id.CropImageView);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_complete = (TextView) findViewById(R.id.tv_complete);
        tv_cancel.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        cropImageView.setIsHeaderCrop(false);
        showBitmap();
    }

    private void showBitmap() {

        if (isGallery) {
//            showImgFromGallery();
        } else {
            showImgFromTakePhoto();
        }
    }

    private void showImgFromTakePhoto() {

        try {
            inputStream = getContentResolver().openInputStream(uri1);
            bitmap = BitmapFactory.decodeStream(inputStream);
            bitmap = MediaUtils.comp(bitmap);
            cropImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_complete){
            Bitmap clip = cropImageView.clip();
            String fileName =  System.currentTimeMillis() + ".png";
            String mPicPath = MediaUtils.KELE_PHOTOS_DIR + File.separator + fileName;
            boolean b = MediaUtils.saveBitmap(clip, mPicPath);//将剪切的图片保存
            if(b){
                Toast.makeText(this,"成功",Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(this,"失败",Toast.LENGTH_LONG).show();
            }
        }else {
            finish();
        }
    }
}
