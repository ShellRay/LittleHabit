package com.example.frescogif.view.WaterLevel;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.utils.MediaUtils;
import com.example.frescogif.view.BeatingWaveView;
import com.example.frescogif.view.anyshape.PathInfo;
import com.example.frescogif.view.anyshape.PathManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by GG on 2017/11/29.
 */

public class RealWaveLevelView extends android.support.v7.widget.AppCompatImageView  {

    private static final String TAG = "RealWaveLevelView";
    private Timer timer;


    public static class Beating
    {
        private long       position;
        private long       playLoop;
        private float      progress;
        private long       fromtime;
        private long       duration;
        private boolean    loopmode;

        public Beating(long fromtime, long duration, boolean loopmode)
        {
            this.position = 0;
            this.playLoop = 1;
            this.progress = 0;
            this.fromtime = fromtime;
            this.duration = duration;
            this.loopmode = loopmode;
        }

        public RealWaveLevelView.Beating setPlayLoop(long playLoop)
        {
            this.playLoop = playLoop;
            return this;
        }

        public void cancleLoop()
        {
            if (loopmode)
            {
                long time = AnimationUtils.currentAnimationTimeMillis();
                fromtime = (long) (time - progress * duration);
                loopmode = false;
            }
        }

        public void updateProgess()
        {
            long time = AnimationUtils.currentAnimationTimeMillis();
            long pass = time - fromtime;

            if (loopmode)
            {
                if (pass > duration)
                {
                    pass = pass % duration;
                    fromtime = time - pass;
                }
            }
            else
            {
                position = pass / duration;

                if (pass > duration)
                {
                    pass = pass % duration;
                }
            }

            progress = pass / (float) (duration);
            progress = Math.min(progress, 1);
            progress = Math.max(progress, 0);


        }

        public long finishTime()
        {
            return fromtime + duration * playLoop;
        }

        public boolean finished()
        {
            return !loopmode && position >= playLoop;
        }

    }

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
    private int mWaveLength = 180;
    //波纹个数
    private int mWaveCount;
    //平移偏移量
    private int mOffset;
    //波纹的中间轴
    private float mCenterY;
    //波纹的高度
    private static final int WAVE_HIGHT = 20;

//////////////////////////////////////////////////////////

    int backColor;
    int vWidth = 0;
    int vHeight = 0;
    private Canvas canvas1;
    private double depthOfWater = 0f;
    private ValueAnimator animator;
    private List<RealWaveLevelView.Beating> beatings;

    private long duration = 1000;
    private boolean isRuning = false;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch(msg.what){

                case 1:
                    if(animator != null && !isRuning){
                        animator.start();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    public RealWaveLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        beatings = new ArrayList<>();
        /////////////////////////////////////////////////////////////////////////
        animator = ValueAnimator.ofInt(0, mWaveLength);
        animator.setDuration(duration);
        animator.setRepeatCount(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isRuning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isRuning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        timer = new Timer();
        TimerTask selfAnimationTask = new TimerTask()
        {
            @Override
            public void run()
            {
                Message message = handler.obtainMessage();
                message.what = 1;
                handler.sendEmptyMessage(message.what);
            }
        };
        timer.schedule(selfAnimationTask, 10 * 1000, 10 * 1000);

        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(R.color.white));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPathA = new Path();
        mCyclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCyclePaint.setColor(context.getResources().getColor(R.color.white));
        mCyclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //////////////////////////////////////////////////////

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
        //加1.5：至少保证波纹有2个，至少2个才能实现平移效果
        mWaveCount = (int) Math.round(vWidth / mWaveLength + 1.5);

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
        mCenterY = (float) (vHeight *(1 - depthOfWater));

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas1 = new Canvas(bitmap);
        mPaint.reset();
        mCyclePaint.reset();

        mPath.reset();
        mPathA.reset();
        //移到屏幕外最左边
        mPath.moveTo(-mWaveLength + mOffset, mCenterY);
        mPathA.moveTo(-mWaveLength + mOffset , mCenterY );

        for (int i = 0; i < mWaveCount; i++) {
            //正弦曲线
            mPath.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY + WAVE_HIGHT, (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
            mPath.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY - WAVE_HIGHT, i * mWaveLength + mOffset, mCenterY);

            mPathA.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY-WAVE_HIGHT , (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
            mPathA.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY+WAVE_HIGHT , i * mWaveLength + mOffset, mCenterY);

        }
            //填充矩形
            mPath.lineTo(vWidth, vHeight);
            mPathA.lineTo(vWidth, vHeight);
            mPath.lineTo(0, vHeight);
            mPathA.lineTo(0, vHeight);
        mPath.close();
        mPathA.close();
        Shader mShader = new LinearGradient(vWidth /2, mCenterY, vWidth /2, vHeight,
        //        Shader mShader = new LinearGradient(vWidth /2, 0, vWidth /2, vHeight,
                context.getResources().getColor(R.color.colorWaveLow), context.getResources().getColor(R.color.colorWaveDeep),  Shader.TileMode.MIRROR);
        mCyclePaint.setShader(mShader);
        mPaint.setShader(mShader);
        canvas1.drawPath(mPathA, mCyclePaint);
        canvas1.drawPath(mPath, mPaint);
        ///////////////////////////////////////////////////////////////////

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        //get the drawable to show. if not set the src, will use  backColor
        //Drawable showDrawable = getDrawable();

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

    @Override
    public void computeScroll()
    {
        if (beatings.size() > 0)
        {

            if (beatings.get(0).finished())
            {
                beatings.remove(0);
            }
            else
            {
                 beatings.get(0).updateProgess();
                if(!isRuning && animator != null ){
                    animator.start();
                }
            }
            Log.e(TAG,"computeScroll ===" );
            invalidate();
        }
    }

    public RealWaveLevelView clearBeating()
    {
        beatings.clear();
        return this;
    }

    public long playBeaintg(long loop_count, long heartCount)
    {
        depthOfWater = ((double )heartCount)/10000;
        Log.e(TAG,"depthOfWater====" +depthOfWater );
        if (heartCount >= 8000)
        {
            beatings.clear();
        }

        int size = beatings.size();

        // 如果有新的心跳任务，先取消循环模式
        if(size > 0){
            beatings.get(size-1).cancleLoop();
        }

        long fromtime = size > 0 ? beatings.get(size - 1).finishTime() : AnimationUtils.currentAnimationTimeMillis();

        int length = 1000;//resource.length;

        if (heartCount < 2000)
        {
            duration = 1000;
        }
        else if (heartCount < 4000)
        {
            duration = 1000 * length / (length + 15);
        }
        else if (heartCount < 6000)
        {
            duration = 1000 * length / (length + 30);
        }
        else if (heartCount < 8000)
        {
            duration = 1000 * length / (length + 45);
        }
        else if (heartCount < 10000)
        {
            duration = 1000 * length / (length + 60);
        }
        else
        {
            duration = 1000 * length / (length + 75);
        }

        RealWaveLevelView.Beating beating = new RealWaveLevelView.Beating(fromtime, duration, heartCount >= 8000).setPlayLoop(heartCount < 8000 ? loop_count : 1);
        beatings.add(beating);
        invalidate();
        return fromtime;
    }

    public long playBeating(long heartCount)
    {
        return playBeaintg(1, heartCount);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
        timer = null;
    }
}

