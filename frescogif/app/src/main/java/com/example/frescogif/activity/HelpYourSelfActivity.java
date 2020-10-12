package com.example.frescogif.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.utils.ResourceTask;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGADynamicEntity;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import androidx.annotation.Nullable;
import kotlin.jvm.functions.Function2;

/**
 * Created by GG on 2018/12/3.
 */

public class HelpYourSelfActivity extends BaseActivity {

    private SVGAImageView scga;
    private SVGAImageView scgaFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_your_self);
        scga = (SVGAImageView) findViewById(R.id.scga);
        scgaFile = (SVGAImageView) findViewById(R.id.scgaFile);

        loadAnimation();

        ResourceTask.intialize();
    }

    private void loadAnimation() {
        final SVGAParser parser = new SVGAParser(this);

        try {
            URL url = new URL("https://github.com/yyued/SVGA-Samples/blob/master/kingset.svga?raw=true");
            parser.decodeFromURL(url, new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete( SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem, requestDynamicItemWithSpannableText("Pony 送了一打风油精给主播"));
                    scga.setImageDrawable(drawable);
                    scga.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }
    }

    /**
     * 你可以设置富文本到 ImageKey 相关的元素上
     * 富文本是会自动换行的，不要设置过长的文本
     *
     * @return
     */
    private SVGADynamicEntity requestDynamicItemWithSpannableText(String msg) {
        SVGADynamicEntity dynamicEntity = new SVGADynamicEntity();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(msg);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(28);
        dynamicEntity.setDynamicText(new StaticLayout(
                spannableStringBuilder,
                0,
                spannableStringBuilder.length(),
                textPaint,
                0,
                Layout.Alignment.ALIGN_CENTER,
                1.0f,//间隔
                0.0f,
                false
        ), "banner");
        dynamicEntity.setDynamicImage("http://res.img002.com/pic//7202_9.gif", "99");
        dynamicEntity.setDynamicText(msg, textPaint,"banner");

        dynamicEntity.setDynamicDrawer(new Function2<Canvas, Integer, Boolean>() {
            @Override
            public Boolean invoke(Canvas canvas, Integer frameIndex) {
                Paint aPaint = new Paint();
                aPaint.setColor(Color.RED);
                aPaint.setAntiAlias(true);
                canvas.drawCircle(55, 55, frameIndex % 5, aPaint);//闪烁
//                canvas.drawCircle(55, 55, 7, aPaint);
                return false;
            }
        }, "banner");//banner 是素材中的一个属性，只有存在才会将自定义的数字展示到其中
        return dynamicEntity;
    }

    public void onOpenFile(View view){
        final SVGAParser parser = new SVGAParser(this);
        String  filePath= ResourceTask.getInstance().getFilePath("1");
        File file = new File(filePath);
        if(!file.exists()){
            Log.e("shell","素材文件未下载");
            return;
        }
        InputStream is = null;
        try {
            is = new FileInputStream(file);

            parser.decodeFromInputStream(is, "banner", new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete( SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity, requestDynamicItemWithSpannableText("Pony 送了一打风油精给主播"));
                    scgaFile.getClearsAfterStop();
                    scgaFile.setImageDrawable(drawable);
//                    scgaFile.setLoops(1);
                    scgaFile.startAnimation();
                }

                @Override
                public void onError() {

                }
            }, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
