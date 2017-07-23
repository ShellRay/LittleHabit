package com.example.frescogif.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.utils.MediaUtils;
import com.example.frescogif.utils.Utils;
import com.example.frescogif.view.NbPhotoCropView.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by GG on 2017/6/15.
 */
public class CustomPhotoActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_take_photo;
    private String mPicPath;
    private String fileName;
    private String mCropPath;
    private Button btn_pick_gallery;
    // 剪切后图像文件
    private Uri mDestinationUri;
    private Button btn_one;
    private ImageView iv_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_photo);

        btn_take_photo = (Button) findViewById(R.id.btn_take_photo);
        btn_pick_gallery = (Button) findViewById(R.id.btn_pick_gallery);
        btn_one = (Button) findViewById(R.id.btn_one);
        iv_img = (ImageView) findViewById(R.id.iv_img);



        btn_take_photo.setOnClickListener(this);
        btn_pick_gallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_take_photo:
                startTakePhoto();
            break;
            case R.id.btn_pick_gallery:
                pickFromGallery();
                break;
        }
    }
    public void pickFromGallery()
    {
        Intent intent = null;
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // 判断是否存在能处理该intent 的activity
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, MediaUtils.REQUESTCODE_GALLERY);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "打开相册失败", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 拍照
     * @param
     * @param
     */
    public  void startTakePhoto() {

            fileName =  System.currentTimeMillis() + ".png";
            mPicPath = MediaUtils.KELE_PHOTOS_DIR + File.separator + fileName;
            MediaUtils.startTakePhoto(this, MediaUtils.KELE_PHOTOS_DIR, fileName);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_CANCELED){//当为0的时候说明没有回调
            return;
        }
        switch (requestCode)
        {
            case MediaUtils.REQUESTCODE_GALLERY:
               // LogUtils.d(TAG, "CLASS " + TAG + ",FUNC onActivityResult(),requestCode:" + requestCode + ",从相册选择图片返回！");
                if (data != null && data.getData() != null)
                {
                    fileName = System.currentTimeMillis() + ".png";
                    mCropPath = MediaUtils.KELE_PHOTOS_DIR + File.separator + fileName;
                    try
                    {
                        startCustomPhotoCrop(data.getData(), mCropPath, true);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            case MediaUtils.REQUESTCODE_CAPTURE:
                File file = null;
                    int degree = MediaUtils.readPictureDegree(mPicPath);
                    if (degree != 0)
                    {
                        Bitmap bitmap = MediaUtils.rotateBitmap(mPicPath, degree);
                        fileName = System.currentTimeMillis() + ".png";
                        String photoPath = MediaUtils.KELE_PHOTOS_DIR + File.separator + fileName;
                        MediaUtils.writeBitmapToPath(bitmap, MediaUtils.KELE_PHOTOS_DIR, fileName);
                        file = new File(photoPath);
                    }
                    else
                    {
                        file = new File(mPicPath);
                    }

                try
                {
                    Uri uri = Uri.fromFile(file);
                    mCropPath = MediaUtils.KELE_PHOTOS_DIR + File.separator + fileName;
                    startCustomPhotoCrop(uri, mCropPath, false);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                break;
            case MediaUtils.REQUESTCODE_CROP:
//                final Uri resultUri = UCrop.getOutput(data);
               /* Uri bigPath = data.getParcelableExtra("bigPath");
                Uri path = data.getParcelableExtra("Path");*/
                String longPath = data.getStringExtra("longPath");
                if (null != longPath ) {//&& null != mOnPictureSelectedListener
                    Bitmap smallBitmap = MediaUtils.getSmallBitmap(mCropPath, 800, 800);
                    iv_img.setImageBitmap(smallBitmap);
                   /*  Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), bigPath);
                        Bitmap pathbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                       if(bitmap != null && pathbitmap != null){
                            boolean b = MediaUtils.saveBitmap(bitmap, mCropPath);
                            boolean b1 = MediaUtils.saveBitmap(pathbitmap, longPath);
                            if(b && b1){
                                iv_img.setImageBitmap(bitmap);
                                Toast.makeText(this, "开始上传", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
//                mOnPictureSelectedListener.onPictureSelected(resultUri, bitmap);
                } else {
                    Toast.makeText(this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
                }
                break;
        }
       /* if (resultCode == MediaUtils.RESULTCODE_CROP)
        {

            Uri bigBitmap = data.getParcelableExtra("bigPath");

//            deleteTempPhotoFile();
            final Uri resultUri = UCrop.getOutput(data);
            if (null != bigBitmap ) {//&& null != mOnPictureSelectedListener
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iv_img.setImageBitmap(bitmap);
//                MediaUtils.saveBitmap(bitmap,mCropPath);
//                mOnPictureSelectedListener.onPictureSelected(resultUri, bitmap);
            } else {
                Toast.makeText(this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
            }
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startCustomPhotoCrop(Uri uri, String filePath, boolean isGallery) throws IOException
    {
//        Intent intent = new Intent(this, PhotoHeadCropActivity.class);
       /* Intent intent = new Intent(this, PhotoCropPerfectActivity.class);
        intent.putExtra("uri", uri.toString());
        intent.putExtra("cropPath", filePath);
        intent.putExtra("isGallery", isGallery);
        startActivityForResult(intent, MediaUtils.REQUESTCODE_CROP);*/

        mDestinationUri = Uri.fromFile(new File(mCropPath));
        UCrop.Options options = new UCrop.Options();
        options.setCropPath(mCropPath);
        options.setIsBackgroundImg(false);
        UCrop.of(uri, mDestinationUri)
                .withTargetActivity(PhotoCropPerfectActivity.class)
               /* .withAspectRatio(18, 13)
                .withMaxResultSize(1080, 780)*/
                .withAspectRatio(1, 1)
                .withMaxResultSize(1080, 1080)
                .withOptions(options)
               /* .withAspectRatio(3, 4)
            .withMaxResultSize(864, 1080)*/
        .start(this,MediaUtils.REQUESTCODE_CROP);
    }
}
