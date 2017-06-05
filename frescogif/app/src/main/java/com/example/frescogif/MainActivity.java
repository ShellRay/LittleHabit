package com.example.frescogif;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.frescogif.activity.RecycleViewActivity;
import com.example.frescogif.activity.SlidMenuActivity;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GiftDialog giftDialog;
    private Button btn_bg;
    private Button btn_login;
    private Button btn_recycle;
    private Button iv_gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        btn_bg = (Button) findViewById(R.id.btn_bg);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_recycle = (Button) findViewById(R.id.btn_recycle);
        iv_gif = (Button) findViewById(R.id.btn_gif);


        btn_bg.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_recycle.setOnClickListener(this);
        iv_gif.setOnClickListener(this);

        ArrayList<String> list = new ArrayList<>();
        String path = "asset:///a.gif";
        String path1 = "asset:///b.gif";
        AssetManager assets = getAssets();
        for (int x = 0;x<8;x++){
                list.add(x,path);
            }
        for (int x = 0;x<8;x++){
            list.add(x,path1);
        }
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
//                startActivity(new Intent(this,RecycleViewActivity.class));

                break;
        }
    }
}
