package com.example.frescogif;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.frescogif.activity.AddSecretActivity;
import com.example.frescogif.activity.AppBarActivity;
import com.example.frescogif.activity.ChatGatherActivity;
import com.example.frescogif.activity.CircleSolidActivity;
import com.example.frescogif.activity.CustomPhotoActivity;
import com.example.frescogif.activity.DesignActivity;
import com.example.frescogif.activity.EmojiActivity;
import com.example.frescogif.activity.LayoutMangerActivity;
import com.example.frescogif.activity.LoadingActivity;
import com.example.frescogif.activity.PullScaleActivity;
import com.example.frescogif.activity.PullToRefreshActivity;
import com.example.frescogif.activity.PullToRefreshPlantActivity;
import com.example.frescogif.activity.RecycleViewActivity;
import com.example.frescogif.activity.ShapeActivity;
import com.example.frescogif.activity.SlidMenuActivity;
import com.example.frescogif.activity.SomeAnimationActivity;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.bean.GiftDialogBean;
import com.facebook.drawee.backends.pipeline.Fresco;

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
        btn_pull = (Button)  findViewById(R.id.btn_pull);
        btn_solid = (Button)  findViewById(R.id.btn_solid);
        btn_I = (Button)  findViewById(R.id.btn_I);
        btn_emoji = (Button)  findViewById(R.id.btn_emoji);
        btn_chat_gather = (Button)  findViewById(R.id.btn_chat_gather);
        addMi = (Button)  findViewById(R.id.addMi);


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

        ArrayList<GiftDialogBean> list = new ArrayList<GiftDialogBean>();
        String path = "asset:///a.gif";
        String path1 = "asset:///b.gif";
        String path2 = "asset:///c.gif";
        String path3 = "asset:///d.gif";
        String path4 = "asset:///e.gif";
        list.add(new GiftDialogBean(path,10));
        list.add(new GiftDialogBean(path1,20));
        list.add(new GiftDialogBean(path2,30));
        list.add(new GiftDialogBean(path3,15));
        list.add(new GiftDialogBean(path4,5));
        list.add(new GiftDialogBean(path1,7));
      /*  for (int x = 0;x<2;x++){
            list.add(x,new GiftDialogBean(path,10));
        }
        for (int x = 0;x<2;x++){

            list.add(x,path1);
        }
        for (int x = 0;x<2;x++){
            list.add(x,path2);
        }
        for (int x = 0;x<2;x++){
            list.add(x,path3);
        }
        for (int x = 0;x<2;x++){
            list.add(x,path4);
        }*/
        giftDialog = new GiftDialog(this, list);
      /*
       这个是可以使用的
       String url = "https://res.guagua.cn/pic//6897_9.gif";
            Glide.with(MainActivity.this)
                .load(url.toString())
                .asGif()
                .into(iv_gif);*/
    }

    public void click(View view){
        if(giftDialog.isShowing()){
            giftDialog.dismiss();
        }else {
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
        switch (v.getId()){
            case R.id.btn_bg:
                startActivity(new Intent(this,ImageBackgroundResilience.class));
                break;
            case R.id.btn_login://slidmenue界面
                startActivity(new Intent(this,SlidMenuActivity.class));
                break;
            case R.id.btn_recycle:
                startActivity(new Intent(this,RecycleViewActivity.class));
                break;
            case R.id.btn_gif:
                startActivity(new Intent(this,PullToRefreshActivity.class));
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

        }
    }
}
