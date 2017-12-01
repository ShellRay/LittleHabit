package com.example.frescogif.view.WaterLevel;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.utils.MediaUtils;
import com.example.frescogif.view.anyshape.PathInfo;
import com.example.frescogif.view.anyshape.PathManager;

/**
 * Created by GG on 2017/11/29.
 */

public class RealWaveLevelView extends android.support.v7.widget.AppCompatImageView{

    private static final String TAG = "RealWaveLevelView";
    Context context;
    Path originMaskPath = null;
    int originMaskWidth = 0;
    int originMaskHeight = 0;
    Path realMaskPath = new Path();
    Paint paint = new Paint();
    int maskResId = 0;

/////////////////////////////////////////////////////////

    // 波纹颜色
    private static final int WAVE_PAINT_COLOR = 0xff663D4E;
    // y = Asin(wx+b)+h
    private static final float STRETCH_FACTOR_A = 20;
    private static final int OFFSET_Y = 0;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 3;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 1;

    private static final int TRANSLATE_X_SPEED_THREE = 3;

    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private float[] mResetThreeYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOffsetSpeedThree;
    private int mXOneOffset;
    private int mXTwoOffset;
    private int mXThreeOffset;

    private Paint mWavePaint;
    private DrawFilter mDrawFilter;

//////////////////////////////////////////////////////////

    int backColor;
    int vWidth = 0;
    int vHeight = 0;
    private Canvas canvas1;
    private float depthOfWater = 0.9f;

    public RealWaveLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /////////////////////////////////////////////////////////////////////////
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = MediaUtils.dip2px(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = MediaUtils.dip2px(context, TRANSLATE_X_SPEED_TWO);
        mXOffsetSpeedThree = MediaUtils.dip2px(context, TRANSLATE_X_SPEED_THREE);

        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
//        mWavePaint.setColor(WAVE_PAINT_COLOR);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        //////////////////////////////////////////////////////
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AnyShapeImageView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            final int attr = a.getIndex(i);
            if (attr == R.styleable.AnyShapeImageView_anyshapeMask) {
                maskResId = a.getResourceId(attr, 0);
                if (0 == maskResId) {
                    //did not set mask
                    continue;
                }

            } else if (attr == R.styleable.AnyShapeImageView_anyshapeBackColor) {
                backColor = a.getColor(attr, Color.TRANSPARENT);
            }
        }
        a.recycle();
    }

    public RealWaveLevelView(Context context) {
        this(context, null);
    }

    public RealWaveLevelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        vHeight = getHeight();
        vWidth = getWidth();
        if (originMaskPath != null) {
            //scale the size of the path to fit the one of this View
            Matrix matrix = new Matrix();
            matrix.setScale(vWidth * 1f / originMaskWidth, vHeight * 1f / originMaskHeight);
            originMaskPath.transform(matrix, realMaskPath);
        }

        // 记录下view的宽高
        mTotalWidth = vWidth;
        mTotalHeight = vHeight;//MediaUtils.dip2px(context,20);
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth];
        // 用于保存波纹二的y值
        mResetTwoYPositions = new float[mTotalWidth];
        //用于保存波纹三的y值
        mResetThreeYPositions = new float[mTotalWidth];

        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);

        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }

        Log.d(TAG,"onSizeChanged");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        if (mWidth != 0 && mHeight != 0) {
            if (maskResId <= 0) {
                return;
            }
            PathInfo pi = PathManager.getInstance().getPathInfo(maskResId);
            if (null != pi) {
                originMaskPath = pi.path;
                originMaskWidth = pi.width;
                originMaskHeight = pi.height;
                Log.d(TAG,"PathInfo maskResId 不为空" +"originMaskWidth"+originMaskWidth +"originMaskHeight"+originMaskHeight);
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(context.getResources(), maskResId, options);
                int widthRatio = (int)(options.outWidth * 1f / mWidth);
                int heightRatio = (int)(options.outHeight * 1f / mHeight);
                if (widthRatio > heightRatio) {
                    options.inSampleSize = widthRatio;
                } else {
                    options.inSampleSize = heightRatio;
                }
                if (options.inSampleSize == 0) {
                    options.inSampleSize = 1;
                }
                options.inJustDecodeBounds = false;
                Bitmap maskBitmap = BitmapFactory.decodeResource(context.getResources(), maskResId, options);
                originMaskPath = PathManager.getInstance().getPathFromBitmap(maskBitmap);
                originMaskWidth = maskBitmap.getWidth();
                originMaskHeight = maskBitmap.getHeight();
                pi = new PathInfo();
                pi.height = originMaskHeight;
                pi.width = originMaskWidth;
                pi.path = originMaskPath;
                PathManager.getInstance().addPathInfo(maskResId, pi);
                maskBitmap.recycle();
            }
        }
        Log.d(TAG,"onMeasure");
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (null == originMaskPath) {
            // if the mask is null, the view will work as a normal ImageView
            super.onDraw(canvas);
            return;
        }
        if (vWidth == 0 || vHeight == 0) {
            return;
        }
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas1 = new Canvas(bitmap);

        // 从canvas层面去除绘制时锯齿
        canvas1.setDrawFilter(mDrawFilter);
        resetPositonY();

//        mWavePaint.setColor(0xff00ffff);
        int top = (int) (mTotalHeight * (1 - depthOfWater) + MediaUtils.dip2px(context,10));
//        canvas1.drawRect(new Rect(0,top,mTotalWidth,mTotalHeight),mWavePaint);//绘制矩形，并设置矩形框显示的位置
        /* 设置渐变色 颜色是改变的 */
        Shader mShader = new LinearGradient(mTotalWidth/2, 0, mTotalWidth/2,  mTotalHeight,
                 Color.GREEN, Color.BLUE,  Shader.TileMode.MIRROR);
        mWavePaint.setShader(mShader);

//        mWavePaint.setColor(0xff0000ff);
        int top1 = 100;
        for (int i = 0; i < mTotalWidth; i++) {

            // 减400只是为了控制波纹绘制的y的在屏幕的位置，大家可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
            // 绘制第一条水波纹
            canvas1.drawLine(i , (mTotalHeight - mResetOneYPositions[i] ) * (1 - depthOfWater)  ,i ,
                    mTotalHeight,
                    mWavePaint);


            // 绘制第二条水波纹
            canvas1.drawLine(i  , (mTotalHeight - mResetTwoYPositions[i]) * (1 - depthOfWater) ,i ,
                    mTotalHeight,
                    mWavePaint);
        }

        // 改变两条波纹的移动点
        mXOneOffset += mXOffsetSpeedOne;
        mXTwoOffset += mXOffsetSpeedTwo;
        mXThreeOffset += mXOffsetSpeedThree;

        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }
        if (mXThreeOffset > mTotalWidth) {
            mXThreeOffset = 0;
        }
        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        ///////////////////////////////////////////////////////////////////

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        //get the drawable to show. if not set the src, will use  backColor

//        Drawable showDrawable = getDrawable();

        if (null != bitmap) {//这里就是水波纹的背景颜色
            Bitmap showBitmap = bitmap;//((BitmapDrawable) showDrawable).getBitmap();
            Shader shader = new BitmapShader(showBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix shaderMatrix = new Matrix();
            float scaleX = vWidth * 1.0f / showBitmap.getWidth();
            float scaleY = vHeight * 1.0f / showBitmap.getHeight();
            shaderMatrix.setScale(scaleX, scaleY);
            shader.setLocalMatrix(shaderMatrix);
            paint.setShader(shader);
        } else {
            //no src , use the backColor to fill the path
            paint.setColor(backColor);
        }
        canvas.drawPath(realMaskPath, paint);

        /////////////////////////////////////////////////////////////////

        postInvalidate();

    }

    /**
     * allow coder to set the backColor
     * @param color
     */
    public void setBackColor(int color) {
        backColor = color;
        postInvalidate();
    }

    /**
     *
     * @param depthOfWater
     */
    public void setDepthOfWater(float depthOfWater) {
        this.depthOfWater = depthOfWater;
        Toast.makeText(context,depthOfWater + "百分比",Toast.LENGTH_SHORT).show();
        postInvalidate();
    }


    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

        int yTwoInterval = mYPositions.length - mXTwoOffset;
        System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0,
                yTwoInterval);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);

        int yThreeInterval = mYPositions.length - mXThreeOffset;
        System.arraycopy(mYPositions, mXThreeOffset, mResetThreeYPositions, 0,
                yThreeInterval);
        System.arraycopy(mYPositions, 0, mResetThreeYPositions, yThreeInterval, mXThreeOffset);
    }


    public static int parse(String s) throws NumberFormatException
    {
        if(!s.startsWith("0x"))
            throw new NumberFormatException();
        int number=0,n=0;
        for(int i=2;i<s.length();i++)
        {
            char c=s.charAt(i);
            switch(c)
            {
                case '1':
                    n=1;break;
                case '2':
                    n=2;break;
                case '3':
                    n=3;break;
                case '4':
                    n=4;break;
                case '5':
                    n=5;break;
                case '6':
                    n=6;break;
                case '7':
                    n=7;break;
                case '8':
                    n=8;break;
                case '9':
                    n=9;break;
                case '0':
                    n=0;break;
                case 'a':
                case 'A':
                    n=10;break;
                case 'b':
                case 'B':
                    n=11;break;
                case 'c':
                case 'C':
                    n=12;break;
                case 'd':
                case 'D':
                    n=13;break;
                case 'e':
                case 'E':
                    n=14;break;
                case 'f':
                case 'F':
                    n=15;break;
                default:
                    throw new NumberFormatException();
            }
            number=number*16+n;
        }
        return number;
    }
}

