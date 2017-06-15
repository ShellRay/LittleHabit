package com.example.frescogif.view.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.frescogif.utils.MediaUtils;


/**
 * @author zhy
 * 边框
 */
public class ClipImageBorderView extends View
{
	/**
	 * 边框的宽度 单位dp
	 */
	private int mBorderWidth = 1;

	private Paint mPaint;
	private boolean isHeaderCrop;

	public ClipImageBorderView(Context context)
	{
		this(context, null);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	
		mBorderWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
						.getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setColor(Color.parseColor("#39AEC2"));
		mPaint.setStrokeWidth(2f);
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		int screenWidth = MediaUtils.getScreenWH(getContext()).widthPixels;
		int screenHeight = MediaUtils.getScreenWH(getContext()).heightPixels;
		float left=screenWidth * 0.1f;
		float right=screenWidth * 0.9f;
//		float top=screenHeight * 0.3f;
//		float bottom=screenHeight * 0.7f;
		float top=screenHeight * 0.2f;
		float bottom=screenHeight * 0.8f;
		mPaint.setStyle(Paint.Style.STROKE);
		if(isHeaderCrop){
			canvas.drawRect(0, top, screenWidth, bottom, mPaint);
		}else {
			canvas.drawRect(left, top, right, bottom, mPaint);
		}

	}

	public void setIsHeaderCrop(boolean isHeaderCrop) {
		this.isHeaderCrop = isHeaderCrop;
	}
}
