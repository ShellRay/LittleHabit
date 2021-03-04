package com.example.frescogif.view.WaterLevel;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
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

import com.example.frescogif.R;
import com.example.frescogif.utils.MediaUtils;
import com.example.frescogif.view.anyshape.PathInfo;
import com.example.frescogif.view.anyshape.PathManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by GG on 2017/12/8.
 */

public class WaveCircleView extends androidx.appcompat.widget.AppCompatImageView {

    private static final String TAG = "WaveLevelView";
    private AnimatorSet animatorSet;

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
    //波浪画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintA = new Paint(Paint.ANTI_ALIAS_FLAG);
    //波浪Path类
    private Path mPath = new Path();
    private Path mPathA = new Path();
    private ValueAnimator animator;
    private List<BeatingTime> beatings;
    public Timer mTimer = new Timer();// 定时器
    private Canvas canvas1;
    //波纹个数
    private int mWaveCount;
    //平移偏移量
    private int mOffset;
    private int mOffsetRight;

    //波纹的中间轴
    private float mCenterY;
    private int standardWaveHeight;
    int maskResId = 0;
    int vWidth = 0;
    int vHeight = 0;

    //一个波浪长度
    private int mWaveLength ;
    private long duration = 5000;
    private double depthOfWater = 0f;
    //波纹的高度
    private  int wave_hight ;
    private boolean isRuning = false;

    public Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch(msg.what){

                case 1:
                    if(animatorSet != null){
                        animatorSet.start();
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
        standardWaveHeight = MediaUtils.dip2px(context, 6);
        mWaveLength = MediaUtils.dip2px(context, 25);
        beatings = new ArrayList<>();
        initAnim();

        mPaint.setColor(context.getResources().getColor(R.color.colorWaveLow));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintA.setColor(context.getResources().getColor(R.color.colorWaveLow));
        mPaintA.setStyle(Paint.Style.FILL_AND_STROKE);

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

    public void initAnim() {

        ArrayList<Animator> animators = new ArrayList<>();
        ValueAnimator animatorRight = ValueAnimator.ofInt(0, 3 * mWaveLength);

        animatorRight.setDuration(duration);
        animatorRight.setRepeatCount(ValueAnimator.RESTART);
        animatorRight.setInterpolator(new LinearInterpolator());
        animatorRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffsetRight = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animators.add(animatorRight);

        animator = ValueAnimator.ofInt(0, 2 * mWaveLength);
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
        animator.addListener(new MyAnimatorListenaer());
        animators.add(animator);
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        vHeight = getHeight();
        vWidth = getWidth();
        //加1.5：至少保证波纹有2个，至少2个才能实现平移效果
        mWaveCount = (int) Math.round(vWidth / mWaveLength + 2.5);
        if (originMaskPath != null) {
            //scale the size of the path to fit the one of this View
            Matrix matrix = new Matrix();
            matrix.setScale(vWidth * 1f / originMaskWidth, vHeight * 1f / originMaskHeight);
            originMaskPath.transform(matrix, realMaskPath);
        }
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
        mCenterY = (float) (vHeight *(1 - depthOfWater));

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas1 = new Canvas(bitmap);
        mPaint.reset();
        mPaintA.reset();

        mPath.reset();
        mPathA.reset();

        //移到屏幕外最左边  ---》 运动方向
        mPath.moveTo( - 2 * mWaveLength + mOffset, mCenterY);
        //移动到皮姆左边   《---- 运动方向
        mPathA.moveTo(  - mOffset , mCenterY );

        //        画先是一条mWaveLength长直线 然后两个周期的正弦曲线 然后 再一条 直线  下同
        //        tips：需要移动的距离是不同的， 前一个2个宽度就可以 而后一个需要3个宽度

        mPath.quadTo((-mWaveLength * 7 / 4)  + mOffset, mCenterY + wave_hight , (-mWaveLength * 6/ 4)  + mOffset, mCenterY);
        mPath.quadTo((-mWaveLength * 5 / 4)  + mOffset, mCenterY - wave_hight,  (-mWaveLength * 4 / 4)  + mOffset, mCenterY);

        mPath.quadTo((-mWaveLength * 3 / 4)  + mOffset, mCenterY + wave_hight, (-mWaveLength / 2)  + mOffset, mCenterY);
        mPath.quadTo((-mWaveLength / 4)  + mOffset, mCenterY - wave_hight, mOffset, mCenterY);

        mPath.quadTo((mWaveLength * 1 / 4)  + mOffset, mCenterY + wave_hight, (mWaveLength / 2) + mOffset, mCenterY);
        mPath.quadTo((mWaveLength* 3/ 4)  + mOffset, mCenterY  - wave_hight,  mWaveLength + mOffset, mCenterY);

        mPath.quadTo((mWaveLength * 5 / 4)  + mOffset, mCenterY + wave_hight , (mWaveLength * 6/ 4)  + mOffset, mCenterY);
        mPath.quadTo((mWaveLength * 7 / 4)  + mOffset, mCenterY - wave_hight,  (mWaveLength * 8 / 4)  + mOffset, mCenterY);

//==========================================================================================================================

        mPathA.quadTo((mWaveLength * 1 / 4)  - mOffset, mCenterY - wave_hight, (mWaveLength / 2) - mOffset, mCenterY);
        mPathA.quadTo((mWaveLength* 3/ 4)    - mOffset, mCenterY + wave_hight,  mWaveLength - mOffset, mCenterY);

        mPathA.quadTo((mWaveLength * 5 / 4)  - mOffset, mCenterY - wave_hight , (mWaveLength * 6/ 4)  - mOffset, mCenterY);
        mPathA.quadTo((mWaveLength * 7 / 4)  - mOffset, mCenterY + wave_hight,  (mWaveLength * 8 / 4) - mOffset, mCenterY);

        mPathA.quadTo((mWaveLength * 9 / 4)  - mOffset, mCenterY - wave_hight , (mWaveLength * 10/ 4)  - mOffset, mCenterY);
        mPathA.quadTo((mWaveLength * 11 /4)  - mOffset, mCenterY + wave_hight,  (mWaveLength * 12 / 4) - mOffset, mCenterY);

        mPathA.quadTo((mWaveLength * 13 /4)  - mOffset, mCenterY - wave_hight, (mWaveLength * 14/ 4) - mOffset, mCenterY);
        mPathA.quadTo((mWaveLength * 15 /4)  - mOffset, mCenterY + wave_hight,  (mWaveLength * 16/4) + mOffset, mCenterY);

        //填充矩形
        mPath.lineTo(vWidth, vHeight);
        mPathA.lineTo(vWidth, vHeight);
        mPath.lineTo(0, vHeight);
        mPathA.lineTo(0, vHeight);
        mPath.close();
        mPathA.close();
        Shader mShader =  new LinearGradient(vWidth /2, mCenterY, vWidth /2, vHeight,
                context.getResources().getColor(R.color.colorWaveLow), context.getResources().getColor(R.color.colorWaveDeep),  Shader.TileMode.MIRROR);

        mPaintA.setShader(mShader);
        mPaint.setShader(mShader);

        canvas1.drawPath(mPathA, mPaintA);
        canvas1.drawPath(mPath, mPaint);

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);

        if (null != bitmap) {
            Bitmap showBitmap = bitmap;
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
            }
            else
            {
                beatings.get(0).updateProgess();
                if(!isRuning && animatorSet != null ){
                    animatorSet.start();
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
        wave_hight = depthOfWater < 0.40d ? (int) (standardWaveHeight * (0.5+depthOfWater)) : standardWaveHeight;
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
        }, 5000, 5000);
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
        animatorSet.cancel();
        mTimer.cancel();
        mTimer = null;
    }
}

