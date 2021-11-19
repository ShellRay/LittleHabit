package com.example.frescogif.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * author : ShellRay
 * date : 2021/10/20 13:23
 * description :
 */
public class SportlightRoomView extends FrameLayout {

    private Path pathMasterIn;
    private Path pathMaster;
    private Paint paint;
    boolean showGuestView;
    boolean showMasterView;

    private final int topOffsetWidth = 110;// 左右的时候 偏移的距离
    private final int topWidth = 150;//上边的宽度
    private final int topWidthHalf = topWidth/2;//上边的宽度
    private final int lightWidthOut = 240;// 外部 头像的控件宽度 + 空出的距离
    private final int lightWidthHalfOut = lightWidthOut/2;//头像的控件宽度 + 空出的距离 一半
    private final int lightWidthIn = lightWidthOut - 60;//头像的控件宽度 + 空出的距离 一半 内部
    private final int lightWidthHalfIn = lightWidthIn /2;//头像的控件宽度 + 空出的距离 一半 内部
    private float masterX;
    private float masterY;
    private float guestX;
    private float guestY;
    private boolean isLeft;

    public SportlightRoomView(Context context) {
        super(context);
    }

    public SportlightRoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1.0f);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 这是绘图方法
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        int width = getWidth();
        int halfWidth = width / 2;

        if(showMasterView) {
            pathMaster = new Path();
            pathMaster.moveTo(halfWidth - topWidthHalf, 0.0f);
            pathMaster.lineTo(halfWidth + topWidthHalf, 0.0f);
            pathMaster.lineTo(halfWidth + lightWidthHalfOut, masterY);
            pathMaster.lineTo(halfWidth - lightWidthHalfOut, masterY);
            pathMaster.lineTo(halfWidth - topWidthHalf, 0.0f);
            //倒叙
            int[] colors = {0x00FFFFFF, 0x57FFFFFF,0x4AFFFFFF, 0x00FFFFFF};
            float[] position = {0.0f, 0.2f, 0.8f, 1.0f};
            LinearGradient gradient = new LinearGradient(halfWidth , 0, masterX, masterY, colors, position, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            canvas.drawPath(pathMaster, paint);

            pathMasterIn = new Path();
            pathMasterIn.moveTo(halfWidth - topWidthHalf, 0.0f);
            pathMasterIn.lineTo(halfWidth + topWidthHalf, 0.0f);
            pathMasterIn.lineTo(halfWidth + lightWidthHalfIn, masterY );
            pathMasterIn.lineTo(halfWidth - lightWidthHalfIn, masterY);
            pathMasterIn.lineTo(halfWidth - topWidthHalf, 0.0f);
            //倒叙
            int[] colors1 = {0x00FFFFFF, 0x57FFFFFF,0x4AFFFFFF, 0x00FFFFFF};
            float[] position1 = {0.0f, 0.2f,0.8f, 1.0f};
            LinearGradient gradient1 = new LinearGradient(halfWidth , 0, masterX, masterY, colors1, position1, Shader.TileMode.CLAMP);
            paint.setShader(gradient1);
            canvas.drawPath(pathMasterIn, paint);
        }


        ///////////////////////嘉宾位///////////////////////////

        if(showGuestView) {
            //右边
            Path pathGuestOut = new Path();
            pathGuestOut.moveTo(halfWidth + (isLeft ? - topOffsetWidth : topOffsetWidth), 0.0f);
            pathGuestOut.lineTo(halfWidth + (isLeft ? - topOffsetWidth - topWidth : topOffsetWidth + topWidth), 0.0f);
            pathGuestOut.lineTo( guestX   + (isLeft ?  - lightWidthHalfOut : lightWidthHalfOut ), guestY );
            pathGuestOut.lineTo( guestX   + (isLeft ?  + lightWidthHalfOut : - lightWidthHalfOut ), guestY );
            pathGuestOut.lineTo(halfWidth + (isLeft ? - topOffsetWidth : topOffsetWidth), 0.0f);
            //倒叙
            int[] colorsGuestOut = {0x00FFFFFF, 0x57FFFFFF,0x4AFFFFFF, 0x00FFFFFF};
            float[] positionGuestOut = {0.0f, 0.2f,0.8f, 1.0f};
            int x0 = halfWidth + (isLeft ? - topOffsetWidth - lightWidthOut : topOffsetWidth + lightWidthOut);
            LinearGradient gradientGuestOut = new LinearGradient(x0, 0, guestX, guestY, colorsGuestOut, positionGuestOut, Shader.TileMode.CLAMP);
            paint.setShader(gradientGuestOut);
            canvas.drawPath(pathGuestOut, paint);

            Path pathGuestIn = new Path();
            pathGuestIn.moveTo(halfWidth + (isLeft ? - topOffsetWidth : topOffsetWidth), 0.0f);
            pathGuestIn.lineTo(halfWidth + (isLeft ? - topOffsetWidth - topWidth : topOffsetWidth + topWidth), 0.0f);
            pathGuestIn.lineTo(guestX    + (isLeft ?  - lightWidthHalfIn : lightWidthHalfIn), guestY);
            pathGuestIn.lineTo(guestX   + (isLeft ?  + lightWidthHalfIn : - lightWidthHalfIn), guestY);
            pathGuestIn.lineTo(halfWidth + (isLeft ? - topOffsetWidth : topOffsetWidth), 0.0f);
            //倒叙
            int[] colorsGuestIn = {0x00FFFFFF, 0x57FFFFFF,0x4AFFFFFF, 0x00FFFFFF};
            float[] positionGuestIn = {0.0f, 0.2f,0.8f, 1.0f};
            int x0In = halfWidth + (isLeft ? -topOffsetWidth - lightWidthIn : topOffsetWidth + lightWidthIn);
            LinearGradient gradientGuestIn = new LinearGradient(x0In, 0, guestX, guestY, colorsGuestIn, positionGuestIn, Shader.TileMode.CLAMP);
            paint.setShader(gradientGuestIn);
            canvas.drawPath(pathGuestIn, paint);
        }

    }


    public void showMasterView(float masterX, float masterY) {
        this.masterX = masterX;
        this.masterY = masterY;
        showMasterView = true;
        invalidate();
    }

    public void showGuestView(boolean isLeft,float guestX, float guestY) {
        this.guestX = guestX;
        this.guestY = guestY;
        this.isLeft = isLeft;
        showGuestView = true;
        invalidate();
    }

    public void goneMaseter() {
        showMasterView = false;
        invalidate();
    }

    public void goneGuest() {
        showGuestView = false;
        invalidate();
    }
}

