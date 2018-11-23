package com.example.frescogif;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.frescogif.activity.AddSecretActivity;
import com.example.frescogif.activity.AppBarActivity;
import com.example.frescogif.activity.ChatGatherActivity;
import com.example.frescogif.activity.CircleSolidActivity;
import com.example.frescogif.activity.CityActivity;
import com.example.frescogif.activity.CustomPhotoActivity;
import com.example.frescogif.activity.DesignActivity;
import com.example.frescogif.activity.EmojiActivity;
import com.example.frescogif.activity.LayoutMangerActivity;
import com.example.frescogif.activity.LoadingActivity;
import com.example.frescogif.activity.LoadingAnimActivity;
import com.example.frescogif.activity.PopWindowActivity;
import com.example.frescogif.activity.PullScaleActivity;
import com.example.frescogif.activity.PullToRefreshActivity;
import com.example.frescogif.activity.PullToRefreshPlantActivity;
import com.example.frescogif.activity.RadioButtonActivity;
import com.example.frescogif.activity.RecycleViewActivity;
import com.example.frescogif.activity.ShapeActivity;
import com.example.frescogif.activity.SlidMenuActivity;
import com.example.frescogif.activity.SomeAnimationActivity;
import com.example.frescogif.activity.StickinessActivity;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.bean.GiftDialogBean;
import com.example.frescogif.utils.PermissionUtils;
import com.example.frescogif.utils.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private GiftDialog giftDialog;
    private Button btn_bg;
    private Button btn_login;
    private Button btn_recycle;
    private Button iv_gif;
    private Button btn_refresh_plant;
    private Button btn_loading;
    private Button btn_edit;
    private Button btn_layoutmanager;
    private Button btn_photo;
    private Button btn_app_bar;
    private Button btn_shape;
    private Button btn_pull;
    private Button btn_I;
    private Button btn_solid;
    private Button btn_emoji;
    private Button btn_chat_gather;
    private Button addMi;
    private Button rceStickiness;
    private Button loadingAni;
    private Button city;
    private Button radiobutton;
    private Button popWindow;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btn_bg = (Button) findViewById(R.id.btn_bg);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_recycle = (Button) findViewById(R.id.btn_recycle);
        iv_gif = (Button) findViewById(R.id.btn_gif);
        btn_refresh_plant = (Button) findViewById(R.id.btn_refresh_plant);
        btn_loading = (Button) findViewById(R.id.btn_loading);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_layoutmanager = (Button) findViewById(R.id.btn_layoutmanager);
        btn_photo = (Button) findViewById(R.id.btn_photo);
        btn_app_bar = (Button) findViewById(R.id.btn_app_bar);
        btn_shape = (Button) findViewById(R.id.btn_shape);
        btn_pull = (Button) findViewById(R.id.btn_pull);
        btn_solid = (Button) findViewById(R.id.btn_solid);
        btn_I = (Button) findViewById(R.id.btn_I);
        btn_emoji = (Button) findViewById(R.id.btn_emoji);
        btn_chat_gather = (Button) findViewById(R.id.btn_chat_gather);
        addMi = (Button) findViewById(R.id.addMi);
        rceStickiness = (Button) findViewById(R.id.rce_stickiness);
        loadingAni = (Button) findViewById(R.id.loading_ani);
        city = (Button) findViewById(R.id.city);
        radiobutton = (Button) findViewById(R.id.radiobutton);
        popWindow = (Button) findViewById(R.id.popWindow);


        btn_bg.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_recycle.setOnClickListener(this);
        iv_gif.setOnClickListener(this);
        btn_refresh_plant.setOnClickListener(this);
        btn_loading.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_layoutmanager.setOnClickListener(this);
        btn_photo.setOnClickListener(this);
        btn_app_bar.setOnClickListener(this);
        btn_shape.setOnClickListener(this);
        btn_pull.setOnClickListener(this);
        btn_solid.setOnClickListener(this);
        btn_I.setOnClickListener(this);
        btn_emoji.setOnClickListener(this);
        btn_chat_gather.setOnClickListener(this);
        addMi.setOnClickListener(this);
        rceStickiness.setOnClickListener(this);
        loadingAni.setOnClickListener(this);
        city.setOnClickListener(this);
        radiobutton.setOnClickListener(this);
        popWindow.setOnClickListener(this);


        ArrayList<GiftDialogBean> list = new ArrayList<GiftDialogBean>();
        String path = "asset:///a.gif";
        String path1 = "asset:///b.gif";
        String path2 = "asset:///c.gif";
        String path3 = "asset:///d.gif";
        String path4 = "asset:///e.gif";
        list.add(new GiftDialogBean(path, 10));
        list.add(new GiftDialogBean(path1, 20));
        list.add(new GiftDialogBean(path2, 30));
        list.add(new GiftDialogBean(path3, 15));
        list.add(new GiftDialogBean(path4, 5));
        list.add(new GiftDialogBean(path1, 7));

        giftDialog = new GiftDialog(this, list);
      /*
       这个是可以使用的
       String url = "https://res.guagua.cn/pic//6897_9.gif";
            Glide.with(MainActivity.this)
                .load(url.toString())
                .asGif()
                .into(iv_gif);*/

        /*
        //进入设置界面
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NotificationManagerCompat manager = NotificationManagerCompat.from(MainActivity.this);
                boolean isOpened = manager.areNotificationsEnabled();
                if(!isOpened) {
                    showConflictDialog(MainActivity.this, "请打开您的通知权限");
                }
            }
        },5000);


       /* AndPermission.with(this)
                .overlay()
                .onGranted(new Action<Void>() {
                    @Override
                    public void onAction(Void data) {
                        ToastUtils.showShort(MainActivity.this, "有权限--=");
                        *//*Dialog dialog = new Dialog(MainActivity.this);
                        Window window = dialog.getWindow();

                        int overlay = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                        int alertWindow = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                        int type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? overlay : alertWindow;
                        window.setType(type);*//*

//                        dialog.show();
//                        showAlertWindow();
                    }
                })
                .onDenied(new Action<Void>() {
                    @Override
                    public void onAction(Void data) {
                        ToastUtils.showShort(MainActivity.this, "无权限--=");
                        // TODO ...
                    }
                })
                .start();*/

    }

    public void click(View view) {
        if (giftDialog.isShowing()) {
            giftDialog.dismiss();
        } else {
            giftDialog.show();
        }

    }

    private byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bg:
                startActivity(new Intent(this, ImageBackgroundResilience.class));
                break;
            case R.id.btn_login://slidmenue界面
                startActivity(new Intent(this, SlidMenuActivity.class));
                break;
            case R.id.btn_recycle:
                startActivity(new Intent(this, RecycleViewActivity.class));
                break;
            case R.id.btn_gif:
                startActivity(new Intent(this, PullToRefreshActivity.class));
                break;
            case R.id.btn_refresh_plant:
                startActivity(new Intent(this, PullToRefreshPlantActivity.class));
                break;
            case R.id.btn_loading:
                startActivity(new Intent(this, LoadingActivity.class));
                break;
            case R.id.btn_edit:
                startActivity(new Intent(this, DesignActivity.class));
                break;
            case R.id.btn_layoutmanager:
                startActivity(new Intent(this, LayoutMangerActivity.class));
                break;
            case R.id.btn_photo:
                startActivity(new Intent(this, CustomPhotoActivity.class));
                break;
            case R.id.btn_app_bar:
                startActivity(new Intent(this, AppBarActivity.class));
                break;
            case R.id.btn_shape:
                startActivity(new Intent(this, ShapeActivity.class));
                break;
            case R.id.btn_pull:
                startActivity(new Intent(this, PullScaleActivity.class));
                break;
            case R.id.btn_solid:
                startActivity(new Intent(this, CircleSolidActivity.class));
                break;
            case R.id.btn_I:
                startActivity(new Intent(this, SomeAnimationActivity.class));
                break;
            case R.id.btn_emoji:
                startActivity(new Intent(this, EmojiActivity.class));
                break;
            case R.id.btn_chat_gather:
                startActivity(new Intent(this, ChatGatherActivity.class));
                break;
            case R.id.addMi:
                startActivity(new Intent(this, AddSecretActivity.class));
                break;
            case R.id.rce_stickiness:
                startActivity(new Intent(this, StickinessActivity.class));
                break;
            case R.id.loading_ani:
                startActivity(new Intent(this, LoadingAnimActivity.class));
                break;
            case R.id.city:
                startActivity(new Intent(this, CityActivity.class));
                break;
            case R.id.radiobutton:
                startActivity(new Intent(this, RadioButtonActivity.class));
                break;
            case R.id.popWindow:
                startActivity(new Intent(this, PopWindowActivity.class));
                break;

        }
    }
    int LAYOUT_FLAG;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showConflictDialog(final Context context, final String text) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

             LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;//Android O版本适配
        } else {

            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        View view = View.inflate(context, R.layout.item_post, null);
        view.setFocusableInTouchMode(true);

        view.measure(View.MeasureSpec.makeMeasureSpec(0,

                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec

                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        Button button = view.findViewById(R.id.main_dialog);

        button.setFocusable(true);

        TextView textView = view.findViewById(R.id.main_tv_jpush);

        textView.setText(text);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(

                WindowManager.LayoutParams.WRAP_CONTENT,

                WindowManager.LayoutParams.WRAP_CONTENT, LAYOUT_FLAG,//WindowManager.LayoutParams控制悬浮窗特性，比如点击窗体外面是否取消悬浮窗

                WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,

                PixelFormat.TRANSLUCENT);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        dialogBuilder.setTitle("提示");

        dialogBuilder.setView(view);

        dialogBuilder.setCancelable(false);

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.getWindow().setAttributes(params);

        if (!alertDialog.isShowing()) {

            alertDialog.show();

        }

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (alertDialog != null && alertDialog.isShowing()) {

                    alertDialog.dismiss();

                }

//                boolean isConflictDialogShow = false;

//                conflictBuilder = null;

//                Intent intent = new Intent(context, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                intent.putExtra("type", "exit");
//                context.startActivity(intent);

            }

        });

        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

                    if (alertDialog.isShowing()) {

                        alertDialog.dismiss();

//                        isConflictDialogShow = false;
//
//                        conflictBuilder = null;

                        Intent intent = new Intent(context, LoginActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                        intent.putExtra("type", "exit");

                        context.startActivity(intent);

                    }

                }

                return false;

            }

        });

    }

}
