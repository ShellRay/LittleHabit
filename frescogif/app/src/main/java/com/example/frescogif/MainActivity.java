package com.example.frescogif;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.frescogif.activity.AddSecretActivity;
import com.example.frescogif.activity.AnimationActivity;
import com.example.frescogif.activity.AppBarActivity;
import com.example.frescogif.activity.ChatGatherActivity;
import com.example.frescogif.activity.CircleSolidActivity;
import com.example.frescogif.activity.CityActivity;
import com.example.frescogif.activity.CustomPhotoActivity;
import com.example.frescogif.activity.DesignActivity;
import com.example.frescogif.activity.EmojiActivity;
import com.example.frescogif.activity.GuideImageActivity;
import com.example.frescogif.activity.HelpYourSelfActivity;
import com.example.frescogif.activity.LayoutMangerActivity;
import com.example.frescogif.activity.LoadingActivity;
import com.example.frescogif.activity.LoadingAnimActivity;
import com.example.frescogif.activity.PopWindowActivity;
import com.example.frescogif.activity.PullScaleActivity;
import com.example.frescogif.activity.PullToRefreshActivity;
import com.example.frescogif.activity.PullToRefreshPlantActivity;
import com.example.frescogif.activity.RadioButtonActivity;
import com.example.frescogif.activity.RecycleViewActivity;
import com.example.frescogif.activity.ScrollVerfityActivity;
import com.example.frescogif.activity.ShapeActivity;
import com.example.frescogif.activity.SlidMenuActivity;
import com.example.frescogif.activity.SomeAnimationActivity;
import com.example.frescogif.activity.StickinessActivity;
import com.example.frescogif.activity.VideoCompressActivity;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.bean.GiftDialogBean;
import com.example.frescogif.utils.GlideLoadUtils;
import com.example.frescogif.utils.PackUtils;
import com.example.frescogif.utils.ToastUtils;
import com.example.frescogif.utils.Utils;
import com.example.frescogif.view.GiftAnimView.CustomRoundView;
import com.example.frescogif.view.GiftAnimView.MagicTextView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

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
    private Button lastview;
    private Button xunfei;
    private LinearLayout llgiftcontent;
    private TranslateAnimation inAnim;
    private TranslateAnimation outAnim;
    private NumAnim giftNumAnim;
    private Timer timer;
    private Button btnScrollVerify;
    private Button constrict;
    private Button compress;

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
        lastview = (Button) findViewById(R.id.lastview);
        xunfei = (Button) findViewById(R.id.xunfei);
        llgiftcontent = (LinearLayout) findViewById(R.id.llgiftcontent);
        btnScrollVerify = (Button) findViewById(R.id.btn_scroll_verify);
        constrict = (Button) findViewById(R.id.constrict);
        compress = (Button) findViewById(R.id.compress);

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
        lastview.setOnClickListener(this);
        xunfei.setOnClickListener(this);
        btnScrollVerify.setOnClickListener(this);
        constrict.setOnClickListener(this);
        compress.setOnClickListener(this);

        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_out);
        giftNumAnim = new NumAnim();
        clearTiming();

        ArrayList<GiftDialogBean> list = new ArrayList<GiftDialogBean>();
        int path = R.drawable.a;//"asset:///a.gif";
        int path1 = R.drawable.b;//"asset:///b.gif";
        int path2 = R.drawable.c;//"asset:///c.gif";
        int path3 = R.drawable.d;//"asset:///d.gif";
        int path4 = R.drawable.e;//"asset:///e.gif";


        list.add(new GiftDialogBean("http://res.img002.com/pic//7202_9.gif", 10, "蜜桃"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//7204_9.gif", 20, "口红"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//7205_9.gif", 30, "轿车"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//7207_9.gif", 15, "冰雕"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//6187_9.gif", 5, "摇钱树"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//6195_9.gif", 7, "内衣"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//6197_9.gif", 7, "高跟鞋"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//6194_9.gif", 7, "对影两人"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//9708.gif", 7, "气球"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//8044.gif", 7, "666"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//8048.gif", 7, "冰淇淋"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//8029.gif", 7, "啤酒"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//8040.gif", 30, "蜜桃"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//8036.gif", 15, "王冠"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//9742.gif", 5, "汽车"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//9739.gif", 7, "蜜桃"));
        list.add(new GiftDialogBean("http://res.img002.com/pic//9609.gif", 7, "天使"));


        giftDialog = new GiftDialog(this, list);

        giftDialog.setOnClickListener(new GiftDialog.OnGiftClickListener() {
            @Override
            public void onItemSelect(GiftDialogBean bean, int num) {
                showGift(bean.path + "", num);
            }

        });

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

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NotificationManagerCompat manager = NotificationManagerCompat.from(MainActivity.this);
                boolean isOpened = manager.areNotificationsEnabled();
                if (!isOpened) {
                    showConflictDialog(MainActivity.this, "请打开您的通知权限");
                }
            }
        }, 5000);
*/
        if (Build.VERSION.SDK_INT >= 23) {
            AndPermission.with(this)
                    .runtime()
                    .permission(
                            Permission.READ_EXTERNAL_STORAGE,
                            Permission.WRITE_EXTERNAL_STORAGE
                    )
                    .onDenied(new Action<List<String>>() {

                        @Override
                        public void onAction(List<String> data) {
                            ToastUtils.showShort(MainActivity.this, "您已禁止读取存储空间权限");
                        }
                    })
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
//                            ToastUtils.showShort(MainActivity.this, "开启读取存储空间权限");
                        }
                    })
                    .start();
        }
        ImageView viewById = (ImageView) findViewById(R.id.ivImage);
        GlideLoadUtils.getInstance().loadImageAsGif(MainActivity.this, path3, viewById);
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
            case R.id.lastview:
                startActivity(new Intent(this, HelpYourSelfActivity.class));
                break;
            case R.id.xunfei:
                startActivity(new Intent(this, AnimationActivity.class));
                break;
            case R.id.btn_scroll_verify:
                startActivity(new Intent(this, ScrollVerfityActivity.class));
                break;
            case R.id.constrict:
                startActivity(new Intent(this, GuideImageActivity.class));
                break;
            case R.id.compress:
                startActivity(new Intent(this, VideoCompressActivity.class));
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

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (alertDialog != null && alertDialog.isShowing()) {

                    alertDialog.dismiss();

                }
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(MainActivity.this) || !PackUtils.isNotificationEnabled(MainActivity.this)) {
                        getPermissonForHand();//获取权限
                    }
                }
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

    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissonForHand() {
        if (!Settings.canDrawOverlays(MainActivity.this)) {
            String systemName = Utils.getSystemName();
            switch (systemName) {
                case Utils.SYS_MIUI:
                    Intent intentMi = new Intent();
                    intentMi.setAction("miui.intent.action.APP_PERM_EDITOR");
                    intentMi.addCategory(Intent.CATEGORY_DEFAULT);
                    intentMi.putExtra("extra_pkgname", getApplication().getPackageName());
                    intentMi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        startActivity(intentMi);
                    } catch (Exception e) {
                        Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent1);
                    }
                    break;
                case Utils.SYS_EMUI:
                    Intent intentHua = new Intent();
                    intentHua.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
                    intentHua.setComponent(comp);
                    try {
                        startActivity(intentHua);
                    } catch (Exception e) {
                        Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent1);
                    }
                    break;
                case Utils.SYS_FLYME:
                    Intent intentMei = new Intent("com.meizu.safe.security.SHOW_APPSEC");
                    intentMei.addCategory(Intent.CATEGORY_DEFAULT);
                    intentMei.putExtra("packageName", getApplication().getPackageName());
                    try {
                        startActivity(intentMei);
                    } catch (Exception e) {
                        Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent1);
                    }
                    break;
                default:
                    Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivity(intent1);
                    break;
            }

        }
        if (!PackUtils.isNotificationEnabled(MainActivity.this)) {
            getNotificationPermission();
        }
    }

    public void getNotificationPermission() {
        //android 8.0引导
        try {

            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= 26) {
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(EXTRA_APP_PACKAGE, getApplication().getPackageName());
                intent.putExtra(EXTRA_CHANNEL_ID, getApplication().getApplicationInfo().uid);
            }

            if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 26) {
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", getApplication().getPackageName());
                intent.putExtra("app_uid", getApplication().getApplicationInfo().uid);
            }
            if (Build.VERSION.SDK_INT < 21) {
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getApplication().getPackageName(), null));
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    /**
     * 数字放大动画
     */
    public class NumAnim {
        private Animator lastAnimator = null;

        public void start(View view) {
            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.end();
                lastAnimator.cancel();
            }
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1.0f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            lastAnimator = animSet;
            animSet.setDuration(200);
            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(anim1, anim2);
            animSet.start();
        }
    }

    private List<View> giftViewCollection = new ArrayList<View>();

    /**
     * 添加礼物view,(考虑垃圾回收)
     */
    public View addGiftView() {
        View view = null;
        if (giftViewCollection.size() <= 0) {
            /*如果垃圾回收中没有view,则生成一个*/
            view = LayoutInflater.from(this).inflate(R.layout.item_gift, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            view.setLayoutParams(lp);
            llgiftcontent.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                }

                @Override
                public void onViewDetachedFromWindow(View view) {
                    giftViewCollection.add(view);
                }
            });
        } else {
            view = giftViewCollection.get(0);
            giftViewCollection.remove(view);
        }
        return view;
    }

    /**
     * 删除礼物view
     */
    public void removeGiftView(final int index) {
        final View removeView = llgiftcontent.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llgiftcontent.removeViewAt(index);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeView.startAnimation(outAnim);
            }
        });
    }

    /**
     * 显示礼物的方法
     */
    public void showGift(String tag, int num) {
        View giftView = llgiftcontent.findViewWithTag(tag);
        if (giftView == null) {/*该用户不在礼物显示列表*/

            if (llgiftcontent.getChildCount() > 2) {/*如果正在显示的礼物的个数超过两个，那么就移除最后一次更新时间比较长的*/
                View giftView1 = llgiftcontent.getChildAt(0);
                CustomRoundView picTv1 = (CustomRoundView) giftView1.findViewById(R.id.crvheadimage);
                long lastTime1 = (Long) picTv1.getTag();
                View giftView2 = llgiftcontent.getChildAt(1);
                CustomRoundView picTv2 = (CustomRoundView) giftView2.findViewById(R.id.crvheadimage);
                long lastTime2 = (Long) picTv2.getTag();
                if (lastTime1 > lastTime2) {/*如果第二个View显示的时间比较长*/
                    removeGiftView(1);
                } else {/*如果第一个View显示的时间长*/
                    removeGiftView(0);
                }
            }

            giftView = addGiftView();/*获取礼物的View的布局*/
            giftView.setTag(tag);/*设置view标识*/

            CustomRoundView crvheadimage = (CustomRoundView) giftView.findViewById(R.id.crvheadimage);
            final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/

            ImageView ivgift = (ImageView) giftView.findViewById(R.id.ivgift);


            giftNum.setText("x" + num);/*设置礼物数量*/
            crvheadimage.setTag(System.currentTimeMillis());/*设置时间标记*/
            giftNum.setTag(num);/*给数量控件设置标记*/

            llgiftcontent.addView(giftView);/*将礼物的View添加到礼物的ViewGroup中*/
            llgiftcontent.invalidate();/*刷新该view*/
            giftView.startAnimation(inAnim);/*开始执行显示礼物的动画*/
            String url = "https://res.guagua.cn/pic//6897_9.gif";

            GlideLoadUtils.getInstance().loadImageAsGif(MainActivity.this, tag, ivgift);
            inAnim.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumAnim.start(giftNum);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {/*该用户在礼物显示列表*/
            CustomRoundView crvheadimage = (CustomRoundView) giftView.findViewById(R.id.crvheadimage);/*找到头像控件*/
            MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
//            ImageView ivgift = (ImageView) giftView.findViewById(R.id.ivgift);
//            GlideLoadUtils.getInstance().loadImageAsGif(this,tag,ivgift);
            int showNum = (Integer) giftNum.getTag() + num;
            giftNum.setText("x" + showNum);
            giftNum.setTag(showNum);
            crvheadimage.setTag(System.currentTimeMillis());
            giftNumAnim.start(giftNum);
        }
    }

    /**
     * 定时清除礼物
     */
    private void clearTiming() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int count = llgiftcontent.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = llgiftcontent.getChildAt(i);
                    CustomRoundView crvheadimage = (CustomRoundView) view.findViewById(R.id.crvheadimage);
                    long nowtime = System.currentTimeMillis();
                    long upTime = (Long) crvheadimage.getTag();
                    if ((nowtime - upTime) >= 3000) {
                        removeGiftView(i);
                        return;
                    }
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
