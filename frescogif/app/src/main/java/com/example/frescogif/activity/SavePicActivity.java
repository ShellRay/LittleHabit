package com.example.frescogif.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.utils.GlideLoadUtils;
import com.example.frescogif.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GG on 2017/12/18.
 * <p>
 * 保存图片到相册
 */

public class SavePicActivity extends BaseActivity {


    @BindView(R.id.svga)
    ImageView svga;
    @BindView(R.id.btnChange)
    Button btnChange;

    private String imgURl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591875669829&di=71e42757e8d52b944ffedfa818739a76&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F36%2F48%2F19300001357258133412489354717.jpg";//图片的URL地址
    private static final int SAVE_SUCCESS = 0;//保存图片成功
    private static final int SAVE_FAILURE = 1;//保存图片失败
    private static final int SAVE_BEGIN = 2;//开始保存图片
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_BEGIN:
                    ToastUtils.showShort(SavePicActivity.this,"开始保存图片...");
                    btnChange.setClickable(false);
                    break;
                case SAVE_SUCCESS:
                    ToastUtils.showShort(SavePicActivity.this,"图片保存成功,请到相册查找");
                    btnChange.setClickable(true);
                    break;
                case SAVE_FAILURE:
                    ToastUtils.showShort(SavePicActivity.this,"图片保存失败,请稍后再试...");
                    btnChange.setClickable(true);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_pic);
        ButterKnife.bind(this);

        GlideLoadUtils.getInstance().loadImageAsBitmap(this,imgURl,svga);

    }

    @OnClick(R.id.btnChange)
    public void onViewClicked() {
        btnChange.setClickable(false);//不可重复点击
        //保存图片必须在子线程中操作，是耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.obtainMessage(SAVE_BEGIN).sendToTarget();
                Bitmap bitmap = returnBitMap(SavePicActivity.this, imgURl);
                saveImageToPhotos(SavePicActivity.this, bitmap);
            }
        }).start();

    }

    /**
     * 保存二维码到本地相册
     */
    private void saveImageToPhotos(Context context, Bitmap bmp) {
        if(bmp == null){
            mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
            return;
        }
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "guagua");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        Log.e("shell", "文件名称" + fileName );

        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
            return;
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        deleteSingleFile(file.getAbsolutePath());
        mHandler.obtainMessage(SAVE_SUCCESS).sendToTarget();
    }

    /**
     * 将URL转化成bitmap形式
     *
     * @param url
     */
    public Bitmap returnBitMap(Context context,String url) {

//        GlideApp
//                .with(context)
//                .asBitmap()
//                .load(url)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull final Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mHandler.obtainMessage(SAVE_BEGIN).sendToTarget();
//                                saveImageToPhotos(getApplication(), resource);
//                            }
//
//                        }).start();
//                    }});
//        return null;


        URL myFileUrl;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("shell", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
                Log.e("shell", "删除单个文件" + filePath$Name + "失败！");
                return false;
            }
        } else {
            Log.e("shell", "删除单个文件失败：" + filePath$Name + "不存在！");
            return false;
        }
    }
}
