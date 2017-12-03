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
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.utils.MediaUtils;
import com.example.frescogif.view.anyshape.PathInfo;
import com.example.frescogif.view.anyshape.PathManager;

/**
 * Created by GG on 2017/11/29.
 */

public class RealWaveLevelView extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener {

    private static final String TAG = "RealWaveLevelView";
    Context context;
    Path originMaskPath = null;
    int originMaskWidth = 0;
    int originMaskHeight = 0;
    Path realMaskPath = new Path();
    Paint paint = new Paint();
    int maskResId = 0;

/////////////////////////////////////////////////////////
//波浪画笔
private Paint mPaint;
    //测试红点画笔
    private Paint mCyclePaint;

    //波浪Path类
    private Path mPath;
    private Path mPathA;
    //一个波浪长度
    private int mWaveLength = 100;
    //波纹个数
    private int mWaveCount;
    //平移偏移量
    private int mOffset;
    private int mOffset1;
    //波纹的中间轴
    private int mCenterY;

    //屏幕高度
    private int mScreenHeight;
    //屏幕宽度
    private int mScreenWidth;

    private static final int WAVE_HIGHT = 15;

//////////////////////////////////////////////////////////

    int backColor;
    int vWidth = 0;
    int vHeight = 0;
    private Canvas canvas1;
    private float depthOfWater = 0.9f;

    public RealWaveLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /////////////////////////////////////////////////////////////////////////

        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0x88FF4081);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        setOnClickListener(this);

        mPathA = new Path();
        mCyclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCyclePaint.setColor(0x88FF4081);
        mCyclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
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
        mScreenHeight = h;
        mScreenWidth = w;
        //加1.5：至少保证波纹有2个，至少2个才能实现平移效果
        mWaveCount = (int) Math.round(mScreenWidth / mWaveLength + 1.5);

        if (originMaskPath != null) {
            //scale the size of the path to fit the one of this View
            Matrix matrix = new Matrix();
            matrix.setScale(vWidth * 1f / originMaskWidth, vHeight * 1f / originMaskHeight);
            originMaskPath.transform(matrix, realMaskPath);
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
        mCenterY = (int) (mScreenHeight *(1-depthOfWater)+WAVE_HIGHT);

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas1 = new Canvas(bitmap);

        mPath.reset();
        mPathA.reset();
        //移到屏幕外最左边
        mPath.moveTo(-mWaveLength + mOffset, mCenterY);
        mPathA.moveTo(-mWaveLength + mOffset , mCenterY );

        if(depthOfWater > 0){
        for (int i = 0; i < mWaveCount; i++) {
            //正弦曲线
            mPath.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY + WAVE_HIGHT, (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
            mPath.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY - WAVE_HIGHT, i * mWaveLength + mOffset, mCenterY);

            mPathA.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset1, mCenterY+WAVE_HIGHT , (-mWaveLength / 2) + (i * mWaveLength) + mOffset1, mCenterY);
            mPathA.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset1, mCenterY-WAVE_HIGHT , i * mWaveLength + mOffset1, mCenterY);

        }
            //填充矩形
            mPath.lineTo(mScreenWidth, mScreenHeight);
            mPathA.lineTo(mScreenWidth, mScreenHeight);
            mPath.lineTo(0, mScreenHeight);
            mPathA.lineTo(0, mScreenHeight);
        }else {
            mPath.addRect(new RectF(0,0,mScreenWidth,mScreenHeight), Path.Direction.CW);
            mPathA.addRect(new RectF(0,0,mScreenWidth,mScreenHeight), Path.Direction.CW);
        }

        mPath.close();
        mPathA.close();
        Shader mShader = new LinearGradient(mScreenWidth/2, mCenterY, mScreenWidth/2,  mScreenHeight,
                Color.GREEN, Color.BLUE,  Shader.TileMode.MIRROR);
        mCyclePaint.setShader(mShader);
        mPaint.setShader(mShader);
            canvas1.drawPath(mPathA, mCyclePaint);
            canvas1.drawPath(mPath, mPaint);
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

    @Override
    public void onClick(View v) {
        ValueAnimator animator = ValueAnimator.ofInt(0, mWaveLength);
        animator.setDuration(500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
        ValueAnimator animator1 = ValueAnimator.ofInt(0, mWaveLength);
        animator1.setDuration(1000);
        animator1.setRepeatCount(ValueAnimator.INFINITE);
        animator1.setInterpolator(new LinearInterpolator());
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset1 = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator1.start();
    }
}

