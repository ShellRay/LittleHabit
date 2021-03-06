package com.example.frescogif.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.anyshape.AnyshapeImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 *
 * 获取
 * Created by GG on 2017/11/29.
 */

public class AnyShapeActivity extends BaseActivity{

    int colors[] = {Color.BLUE, Color.WHITE, Color.YELLOW, Color.LTGRAY, Color.RED, Color.GREEN, Color.CYAN};
    View root;
    AnyshapeImageView iv_rings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_any_shape);
        root = findViewById(R.id.root);
        iv_rings = (AnyshapeImageView) findViewById(R.id.iv_rings);
        findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int)(colors.length * Math.random());
                iv_rings.setBackColor(colors[index]);
            }
        });

        SimpleDraweeView sdv = (SimpleDraweeView) findViewById(R.id.sdv);
        sdv.setController(Fresco.newDraweeControllerBuilder()
                .setImageRequest(ImageRequestBuilder.newBuilderWithSource(
                        Uri.parse("http://image5.tuku.cn/pic/wallpaper/fengjing/menghuandaziranmeijingbizhi/001.jpg"))
                        .setProgressiveRenderingEnabled(true)
                        .build())
                .setOldController(sdv.getController())
                .build());
    }


}
