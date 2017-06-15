package com.example.frescogif.view.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 自定义控件裁剪
 * 
 * @author Shell Ray
 *
 * 2016-2-28
 */
public class ClipImageLayout extends RelativeLayout
{
	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;
	
	private Bitmap mBitmap;
	private Uri imageUri;

	public ClipImageLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip()
	{
		return mZoomImageView.clip();
	}
	
	 public void setImageBitmap(Bitmap bitmap, ExifInterface exif) {

	        if (bitmap == null) {
	            return;
	        }

	        if (exif == null) {
	            setImageBitmap(bitmap);
	            return;
	        }

	        final Matrix matrix = new Matrix();
	        final int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
	        int rotate = -1;

	        switch (orientation) {
	            case ExifInterface.ORIENTATION_ROTATE_270:
	                rotate = 270;
	                break;
	            case ExifInterface.ORIENTATION_ROTATE_180:
	                rotate = 180;
	                break;
	            case ExifInterface.ORIENTATION_ROTATE_90:
	                rotate = 90;
	                break;
	        }

	        if (rotate == -1) {
	            setImageBitmap(bitmap);
	        } else {
	            matrix.postRotate(rotate);
	            final Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap,
	                                                             0,
	                                                             0,
	                                                             bitmap.getWidth(),
	                                                             bitmap.getHeight(),
	                                                             matrix,
	                                                             true);
	            setImageBitmap(rotatedBitmap);
	            bitmap.recycle();
	        }
	    }
	 /**
	     * Sets a Bitmap as the content of the CropImageView.
	     * 
	     * @param bitmap the Bitmap to set
	     */
	    public void setImageBitmap(Bitmap bitmap) {
	    	
	        mBitmap = bitmap;
	        mZoomImageView.setImageBitmap(mBitmap);
	    }

	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
		mZoomImageView.setImageURI(imageUri);
	}

	public void setIsHeaderCrop(boolean isHeaderCrop) {
				mZoomImageView.setIsHeaderCrop(isHeaderCrop);
				mClipImageView.setIsHeaderCrop(isHeaderCrop);
	}

	public void setPostInvite() {
		mZoomImageView.postInvalidate();
		mClipImageView.postInvalidate();
		postInvalidate();
	}
}
