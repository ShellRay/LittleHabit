package com.example.frescogif.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.utils.MediaUtils;
import com.example.frescogif.view.NbPhotoCropView.CropImageView;
import com.example.frescogif.view.NbPhotoCropView.GestureCropImageView;
import com.example.frescogif.view.NbPhotoCropView.OverlayView;
import com.example.frescogif.view.NbPhotoCropView.TransformImageView;
import com.example.frescogif.view.NbPhotoCropView.UCrop;
import com.example.frescogif.view.NbPhotoCropView.UCropView;
import com.example.frescogif.view.NbPhotoCropView.utils.BitmapLoadUtils;
import com.example.frescogif.view.camera.ClipImageLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by GG on 2017/6/15.
 */
public class PhotoCropPerfectActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PhotoCropPerfectActivity" ;
    private TextView tv_cancel;
    private TextView tv_complete;
    private String mCropPath;
    private String uri;
    private boolean isGallery;
    private Uri uri1;
    private Bitmap bitmap;
    private InputStream inputStream;
    private Cursor cursor;

    UCropView mUCropView;
    GestureCropImageView mGestureCropImageView;
    OverlayView mOverlayView;
    private Uri mOutputUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_crop_perfect);

        initView();
        final Intent intent = getIntent();
        setImageData(intent);

       /* Intent intent = getIntent();
        mCropPath = intent.getStringExtra("cropPath");
        uri = intent.getStringExtra("uri");
        isGallery = intent.getBooleanExtra("isGallery", false);
        uri1 = Uri.parse(uri);*/

    }
    private void initView() {
        mUCropView = (UCropView) findViewById(R.id.CropImageView);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_complete = (TextView) findViewById(R.id.tv_complete);
        tv_cancel.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        mGestureCropImageView = mUCropView.getCropImageView();
        mOverlayView = mUCropView.getOverlayView();

        // 设置允许缩放
        mGestureCropImageView.setScaleEnabled(true);
        // 设置禁止旋转
        mGestureCropImageView.setRotateEnabled(false);

        // 设置外部阴影颜色
        mOverlayView.setDimmedColor(Color.parseColor("#AA000000"));
        // 设置周围阴影是否为椭圆(如果false则为矩形)
        mOverlayView.setOvalDimmedLayer(false);
        // 设置显示裁剪边框
        mOverlayView.setShowCropFrame(true);
        // 设置不显示裁剪网格
        mOverlayView.setShowCropGrid(false);

        mGestureCropImageView.setTransformImageListener(mImageListener);
    }

    @SuppressLint("LongLogTag")
    private void setImageData(Intent intent) {
        Uri inputUri = intent.getParcelableExtra(UCrop.EXTRA_INPUT_URI);
        mOutputUri = intent.getParcelableExtra(UCrop.EXTRA_OUTPUT_URI);

        if (inputUri != null && mOutputUri != null) {
            try {
                mGestureCropImageView.setImageUri(inputUri);
            } catch (Exception e) {
                setResultException(e);
                finish();
            }
        } else {
            setResultException(new NullPointerException("Both input and output Uri must be specified"));
            finish();
        }

        // 设置裁剪宽高比
        if (intent.getBooleanExtra(UCrop.EXTRA_ASPECT_RATIO_SET, false)) {
            float aspectRatioX = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_X, 0);
            float aspectRatioY = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_Y, 0);

            if (aspectRatioX > 0 && aspectRatioY > 0) {
                mGestureCropImageView.setTargetAspectRatio(aspectRatioX / aspectRatioY);
            } else {
                mGestureCropImageView.setTargetAspectRatio(CropImageView.SOURCE_IMAGE_ASPECT_RATIO);
            }
        }

        // 设置裁剪的最大宽高
        if (intent.getBooleanExtra(UCrop.EXTRA_MAX_SIZE_SET, false)) {
            int maxSizeX = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_X, 0);
            int maxSizeY = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_Y, 0);

            if (maxSizeX > 0 && maxSizeY > 0) {
                mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
                mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
            } else {
                Log.w(TAG, "EXTRA_MAX_SIZE_X and EXTRA_MAX_SIZE_Y must be greater than 0");
            }
        }

    }
    private void setResultUri(Uri uri, float resultAspectRatio) {
        setResult(MediaUtils.RESULTCODE_CROP, new Intent()
                .putExtra(UCrop.EXTRA_OUTPUT_URI, uri)
                .putExtra(UCrop.EXTRA_OUTPUT_CROP_ASPECT_RATIO, resultAspectRatio));
    }

    private void setResultException(Throwable throwable) {
        setResult(UCrop.RESULT_ERROR, new Intent().putExtra(UCrop.EXTRA_ERROR, throwable));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_complete){
            cropAndSaveImage();
        }else {
            finish();
        }
    }

    private void cropAndSaveImage() {
        OutputStream outputStream = null;
        try {
            final Bitmap croppedBitmap = mGestureCropImageView.cropImage();
            if (croppedBitmap != null) {
                outputStream = getContentResolver().openOutputStream(mOutputUri);
                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);
                croppedBitmap.recycle();

                setResultUri(mOutputUri, mGestureCropImageView.getTargetAspectRatio());
                finish();
            } else {
                setResultException(new NullPointerException("CropImageView.cropImage() returned null."));
            }
        } catch (Exception e) {
            setResultException(e);
            finish();
        } finally {
            BitmapLoadUtils.close(outputStream);
        }
    }

    private TransformImageView.TransformImageListener mImageListener = new TransformImageView.TransformImageListener() {
        @Override
        public void onRotate(float currentAngle) {
//            setAngleText(currentAngle);
        }

        @Override
        public void onScale(float currentScale) {
//            setScaleText(currentScale);
        }

        @Override
        public void onLoadComplete() {
            Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ucrop_fade_in);
            fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mUCropView.setVisibility(View.VISIBLE);
                    mGestureCropImageView.setImageToWrapCropBounds();
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mUCropView.startAnimation(fadeInAnimation);
        }

        @Override
        public void onLoadFailure(Exception e) {
            setResultException(e);
            finish();
        }

    };
}
