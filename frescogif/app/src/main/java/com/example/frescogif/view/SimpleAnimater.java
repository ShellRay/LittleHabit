package com.example.frescogif.view;

import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;


/**
 * 通过matrix设置动画 视图动画
 * */
public class SimpleAnimater extends Animation {

    private int mWidth, mHeight;

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        this.mWidth = width;
        this.mHeight = height;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        Log.e("shell","interpolatedTime==" + interpolatedTime);
        Matrix matrix = t.getMatrix();
        matrix.preSkew(interpolatedTime*10,interpolatedTime*10);//偏移
//        matrix.preTranslate(interpolatedTime*100,interpolatedTime*100);//移动
        matrix.preScale(interpolatedTime, interpolatedTime);//缩放
        matrix.preRotate(interpolatedTime * 360);//旋转
        //下面的Translate组合是为了将缩放和旋转的基点移动到整个View的中心，不然系统默认是以View的左上角作为基点
        matrix.preTranslate(-mWidth / 2, -mHeight / 2);
        matrix.postTranslate(mWidth / 2, mHeight / 2);
    }
}
