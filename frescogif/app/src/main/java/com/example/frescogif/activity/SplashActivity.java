package com.example.frescogif.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.frescogif.LoginActivity;
import com.example.frescogif.MainActivity;
import com.example.frescogif.R;
import com.example.frescogif.view.loadingview.Titanic;
import com.example.frescogif.view.loadingview.TitanicTextView;
import com.example.frescogif.view.loadingview.Typefaces;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by GG on 2017/6/5.
 */
public class SplashActivity extends AppCompatActivity {

    private SimpleDraweeView splashImage;
    private Handler handler;
    private TitanicTextView my_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_splash);
        splashImage = (SimpleDraweeView) findViewById(R.id.splash_Image);
        my_text = (TitanicTextView) findViewById(R.id.my_text);

        splashImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });
        // set fancy typeface
        my_text.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));

        // start animation
        new Titanic().start(my_text);

        String path1 = "asset:///b.gif";
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setOldController(splashImage.getController())
                .setUri(Uri.parse(path1))
                .build();
        splashImage.setController(controller);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        },8000);
    }




}
