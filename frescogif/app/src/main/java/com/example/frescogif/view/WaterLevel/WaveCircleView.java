package com.example.frescogif.view.WaterLevel;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.frescogif.R;
import com.example.frescogif.view.anyshape.PathInfo;
import com.example.frescogif.view.anyshape.PathManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by GG on 2017/12/8.
 */

public class WaveCircleView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = "WaveLevelView";
    private AnimatorSet mAnimatorSet;
    private Bitmap bitmap;
    private BitmapShader mWaveShader;

    public static class BeatingTime
    {
        private long       position;
        private long       playLoop;
        private float      progress;
        private long       fromtime;
        private long       duration;
        private boolean    loopmode;

        public BeatingTime(long fromtime, long duration, boolean loopmode)
        {
            this.position = 0;
            this.playLoop = 1;
            this.progress = 0;
            this.fromtime = fromtime;
            this.duration = duration;
            this.loopmode = loopmode;
        }

        public BeatingTime setPlayLoop(long playLoop)
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

    private List<BeatingTime> beatings;
    public Timer mTimer = new Timer();// 定时器
    private Canvas canvas1;
    Paint wavePaint = new Paint();
    Matrix mShaderMatrix = new Matrix();
    int maskResId = 0;
    int vWidth = 0;
    int vHeight = 0;

    private long duration = 1000;
    private double depthOfWater = 0f;

    private boolean isRuning = false;

    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 1;//0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 1.0f;//0.0f;

    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#4Dff60ab");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#ffff60ab");//ffff291e");

    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaveLength;
    private double mDefaultAngularFrequency;

    private float mAmplitudeRatio = 0.0001f;//DEFAULT_AMPLITUDE_RATIO;
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

    private int mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
    private int mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;


    public Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch(msg.what){

                case 1:
                    if(mAnimatorSet != null){
                        mAnimatorSet.start();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    public WaveCircleView(Context context) {
        this(context, null);
    }

    public WaveCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        beatings = new ArrayList<>();
        initAnimation();
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
            }
        }
        a.recycle();
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
    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();

        ValueAnimator waveShiftAnim = ObjectAnimator.ofFloat( 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.RESTART);
        waveShiftAnim.setDuration(duration);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveShiftAnim);
        waveShiftAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWaveShiftRatio = (float) animation.getAnimatedValue();
            }
        });

        ValueAnimator amplitudeAnim = ObjectAnimator.ofFloat( 0.0001f, 0.05f);
        amplitudeAnim.setRepeatCount(ValueAnimator.RESTART);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(duration);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        amplitudeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAmplitudeRatio = (float) animation.getAnimatedValue();
            }
        });
        animators.add(amplitudeAnim);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
        mAnimatorSet.addListener(new MyAnimatorListenaer());
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
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (null == originMaskPath) {
            super.onDraw(canvas);
            return;
        }
        if (vWidth == 0 || vHeight == 0) {
            return;
        }

        mDefaultAngularFrequency = 4.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / vWidth;
        mDefaultAmplitude = vHeight * DEFAULT_AMPLITUDE_RATIO;
        mDefaultWaterLevel = vHeight * DEFAULT_WATER_LEVEL_RATIO;
        mDefaultWaveLength = vWidth;

        wavePaint.reset();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas1 = new Canvas(bitmap);



        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];

        //center 第二个参数
        /*Shader mShader =  new LinearGradient(vWidth /2, vHeight, vWidth /2, vHeight,
                context.getResources().getColor(R.color.colorWaveLow), context.getResources().getColor(R.color.colorWaveDeep),  Shader.TileMode.MIRROR);
        wavePaint.setShader(mShader);*/

        wavePaint.setColor(mBehindWaveColor);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas1.drawLine(beginX, beginY, beginX, endY, wavePaint);

            waveY[beginX] = beginY;
        }

        wavePaint.setColor(mFrontWaveColor);
        final int wave2Shift = (int) (mDefaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas1.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }

        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        wavePaint.setShader(mWaveShader);


        /*if (wavePaint.getShader() == null) {
            wavePaint.setShader(mWaveShader);
        }*/
        if(mWaveShader != null) {
            mShaderMatrix.setScale(
                    mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                    mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO,
                    0,
                    mDefaultWaterLevel);

            mShaderMatrix.postTranslate(
                    mWaveShiftRatio * getWidth(),
                    (float) ((DEFAULT_WATER_LEVEL_RATIO - depthOfWater) * getHeight()));

            mWaveShader.setLocalMatrix(mShaderMatrix);
            canvas1.drawRect(0, 0, vWidth,
                    vHeight, wavePaint);
        }
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);

        if (null != bitmap) {
            Bitmap showBitmap = bitmap;//((BitmapDrawable) showDrawable).getBitmap();
            Shader shader = new BitmapShader(showBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix shaderMatrix = new Matrix();
            float scaleX = vWidth * 1.0f / showBitmap.getWidth();
            float scaleY = vHeight * 1.0f / showBitmap.getHeight();
            shaderMatrix.setScale(scaleX, scaleY);
            shader.setLocalMatrix(shaderMatrix);
            paint.setShader(shader);
        }
        canvas.drawPath(realMaskPath, paint);

    }


    @Override
    public void computeScroll()
    {
        if (beatings.size() > 0)
        {

            if (beatings.get(0).finished())
            {
                beatings.remove(0);
                if(mAnimatorSet != null) {
                    mAnimatorSet.end();
                }
            }
            else
            {
                beatings.get(0).updateProgess();
                if(!isRuning && mAnimatorSet != null ){
                    mAnimatorSet.start();
                }
            }
            invalidate();
        }
    }

    public WaveCircleView clearBeating()
    {
        beatings.clear();
        return this;
    }

    public long playBeaintg(long loop_count, long heartCount)
    {
        depthOfWater = ((double )heartCount)/10000;
//        wave_hight = depthOfWater < 0.40d ? (int) (standardWaveHeight * (0.5+depthOfWater)) : standardWaveHeight;
        int size = beatings.size();

        // 如果有新的心跳任务，先取消循环模式
        if(size > 0){
            beatings.get(size-1).cancleLoop();
        }

        long fromtime = size > 0 ? beatings.get(size - 1).finishTime() : AnimationUtils.currentAnimationTimeMillis();

        BeatingTime beating = new BeatingTime(fromtime, duration, heartCount >= 10000)
                .setPlayLoop(heartCount < 10000 ? loop_count : 1);
        beatings.add(beating);
        invalidate();
        return fromtime;
    }

    public long playBeating(long heartCount)
    {
        return playBeaintg(1, heartCount);
    }

    public void startWaveTimeTask() {
        mTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Message message = handler.obtainMessage();
                message.what = 1;
                if(!isRuning) {
                    handler.sendEmptyMessage(message.what);
                }
            }
        }, 20000, 20000);
    }

    class MyAnimatorListenaer implements Animator.AnimatorListener{
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
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTimer.cancel();
        mTimer = null;
    }
}


