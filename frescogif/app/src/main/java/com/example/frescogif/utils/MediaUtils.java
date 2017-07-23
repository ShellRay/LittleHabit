package com.example.frescogif.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera.Size;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.widget.Toast;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MediaUtils {
	private static final String TAG = "MediaUtils";
	private static final String QIQI_VIDEO = "video/";
	private static final String QIQI_DIRECTOTY = "/KeLe/";
	public final static String KELE_PHOTOS_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Atest/photos";
	public final static int REQUESTCODE_GALLERY = 101;
	public final static int REQUESTCODE_CAPTURE = 102;
	public final static int REQUESTCODE_CROP = 103;
	public final static int RESULTCODE_CROP = 203;
	private static final String CAMERA_ACTION_CROP = "com.android.camera.action.CROP";
	private static final long NO_STORAGE_ERROR = -1L;
	private static final long CANNOT_STAT_ERROR = -2L;
	// Orientation hysteresis amount used in rounding, in degrees
	public static final int ORIENTATION_HYSTERESIS = 5;


	public static void Assert(boolean cond) {
		if(!cond) {
			throw new AssertionError();
		}
	}

	/**
	 * 获取屏幕的旋转角度
	 */
	public static int getDisplayRotation(Activity activity) {
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		switch (rotation) {
			case Surface.ROTATION_0:
				return 0;
			case Surface.ROTATION_90:
				return 90;
			case Surface.ROTATION_180:
				return 180;
			case Surface.ROTATION_270:
				return 270;
		}
		return 0;
	}

	public static int roundOrientation(int orientation, int orientationHistory) {
		boolean changeOrientation = false;
		if(orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
			changeOrientation = true;
		} else {
			int dist = Math.abs(orientation - orientationHistory);
			dist = Math.min(dist, 360 - dist);
			changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
		}
		if(changeOrientation) {
			return ((orientation + 45) / 90 * 90) % 360;
		}
		return orientationHistory;
	}

	/**
	 * 检测sdcard是否存在,剩余内存
	 */
	@SuppressWarnings("deprecation")
	public static boolean isSDcardMemeryAvailable() {
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			long availCount = sf.getAvailableBlocks();

			long sdMemery = (availCount * blockSize / 1024) / 1024;
			if(sdMemery < 30) {
				return false;
			}
		}
		return true;
	}

	public static String createVideoPath(String folderName) {
		long dateTaken = System.currentTimeMillis();
		String title = getVideoFileTitleByTime(dateTaken);
		String filename = title + ".mp4";
		String filePath = getAppVideoDirectory(folderName) + filename;
		if(!new File(filePath).exists()) {
			try {
				File file = new File(filePath);
				file.createNewFile();
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}

		return filePath;
	}

	public static String getVideoFileTitleByTime(long milliseconds) {
		return new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA).format(new Date(milliseconds));
	}

	public static String getAppVideoDirectory(String folderName) {
		String videoPath = Environment.getExternalStorageDirectory().getPath() + QIQI_DIRECTOTY;
		if(!isDirExist(videoPath)) {
			File file = new File(videoPath);
			file.mkdir();
		}
		videoPath = videoPath + QIQI_VIDEO + folderName + File.separator;
		if(!isDirExist(videoPath)) {
			File file = new File(videoPath);
			file.mkdir();
		}
		return videoPath;
	}

	public static boolean hasStorage() {
		boolean requireWriteAccess = true;
		String state = Environment.getExternalStorageState();

		if(Environment.MEDIA_MOUNTED.equals(state)) {
			if(requireWriteAccess) {
				boolean writable = checkFsWritable();
				return writable;
			} else {
				return true;
			}
		} else if(!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	private static boolean checkFsWritable() {
		String directoryName = getAppDirectory() + QIQI_VIDEO;
		File directory = new File(directoryName);
		if(!directory.isDirectory()) {
			if(!directory.mkdirs()) {
				return false;
			}
		}
		return directory.canWrite();
	}

	public static String getAppDirectory() {
		String appPath = getExternalStorageDirectory() + QIQI_DIRECTOTY;
		if(!isDirExist(appPath)) {
			File file = new File(appPath);
			file.mkdirs();
		}
		return appPath;
	}

	public static boolean isDirExist(String path) {
		if(TextUtils.isEmpty(path)) {
			return false;
		}
		File file = new File(path);
		return file.exists() && file.isDirectory();
	}

	public static String getExternalStorageDirectory() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	@SuppressWarnings("deprecation")
	public static long getAvailableStorage() {
		try {
			if(!hasStorage()) {
				return NO_STORAGE_ERROR;
			} else {
				String storageDirectory = Environment.getExternalStorageDirectory().toString();
				StatFs stat = new StatFs(storageDirectory);
				return (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
			}
		} catch ( Exception ex ) {
			return CANNOT_STAT_ERROR;
		}
	}

	/**
	 * 录制异常时候删除产生的文件
	 * @param path
	 */
	public static void deleteVideoFile(final String path) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				if(TextUtils.isEmpty(path)) {
					return;
				}
				File f = new File(path);
				if(!f.delete()) {
					//TODO
				}
			}
		}).start();
	}

	public static boolean isSupported(String value, List<String> supported) {
		return supported == null ? false : supported.indexOf(value) >= 0;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree 旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		if(TextUtils.isEmpty(path)) {
			return degree;
		}
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch ( IOException e ) {
			e.printStackTrace();
			return degree;
		}
		return degree;
	}

	/**
	 * 旋转图片，使图片保持正确的方向。
	 * @param degrees 原始图片的角度
	 * @return Bitmap 旋转后的图片
	 */
	public static Bitmap rotateBitmap(String path, int degrees) {
		BitmapFactory.Options  opt = new BitmapFactory.Options();
		opt.outHeight = 640;
		opt.outWidth = 640 ;
		Bitmap bitmap = BitmapFactory.decodeFile(path,opt);
		if(degrees == 0 || null == bitmap) {
			return bitmap;
		}
		Matrix matrix = new Matrix();
		matrix.setRotate(degrees);
		float scaleWidth = ((float) 480) / bitmap.getWidth();
		matrix.postScale(scaleWidth, scaleWidth);
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		if(null != bitmap) {
			bitmap.recycle();
		}
		return bmp;
	}

	/**
	 * Bitmap输出到文件
	 * @param bitmap
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static boolean writeBitmapToPath(Bitmap bitmap, String filePath, String fileName) {
		if(bitmap == null || TextUtils.isEmpty(filePath) || TextUtils.isEmpty(fileName)) {
			return false;
		}
		FileOutputStream fos = null;
		ByteArrayOutputStream out = null;
		try {
			File pathFile = new File(filePath);
			if(!pathFile.exists()) {
				pathFile.mkdirs();
			}

			File file = new File(filePath + File.separator + fileName);
			if(file.exists()) {
				file.delete();
				file.createNewFile();
			}

			out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			byte[] array = out.toByteArray();

			fos = new FileOutputStream(file);
			fos.write(array);

		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
			return false;
		} catch ( IOException e ) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				fos.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 打开系统的图片裁剪页面
	 * @param isHeader 如果时候头像则进行裁决方形，非头像则随意裁剪
	 * @throws IOException 
	 */
	public static void startPhotoCrop(Context context, Uri uri, int aspectX, int aspectY, int outputX, int outputY, String filePath, String fileName,boolean isHeader)
			throws IOException {
		Intent intent = new Intent(CAMERA_ACTION_CROP);
		intent.setDataAndType(getImageUri(context, uri), "image/*");
		if(isHeader) {
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", aspectX);
			intent.putExtra("aspectY", aspectY);
		}
			intent.putExtra("outputX", outputX);
			intent.putExtra("outputY", outputY);
			intent.putExtra("crop", true);
			intent.putExtra("scale", true);
			intent.putExtra("scaleUpIfNeeded", true);
			intent.putExtra("return-data", false);

		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		Uri outUri = Uri.fromFile(new File(filePath + File.separator + fileName));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
		((Activity) context).startActivityForResult(intent, REQUESTCODE_CROP);
	}

	public static Uri getImageUri(Context context, Uri data) throws IOException {
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			return data;
		}

		// convert "file://" from "content://" for 4.4+
		ParcelFileDescriptor parcelFileDescriptor = null;
		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			parcelFileDescriptor = context.getContentResolver().openFileDescriptor(data, "r");
			FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
			from = new FileInputStream(fileDescriptor);
			File tempOutFile = new File(context.getExternalFilesDir(null), "crop_temp");
			to = new FileOutputStream(tempOutFile);

			byte[] buffer = new byte[4096]; // To hold file contents
			int bytes_read; // How many bytes in buffer
			while ((bytes_read = from.read(buffer)) != -1) {
				to.write(buffer, 0, bytes_read);
			}

			return Uri.fromFile(tempOutFile);
		} finally {
				to.close();
				from.close();
			if(parcelFileDescriptor != null) {
				parcelFileDescriptor.close();
			}
		}
	}

	/**
	 * 启动系统拍照页面
	 * @param context
	 * @param filePath
	 * @param fileName
	 */
	public static Uri startTakePhoto(Context context, String filePath, String fileName) {
		Uri uri = null;
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            PackageManager pm = context.getPackageManager();

            final ResolveInfo mInfo = pm.resolveActivity(i, 0);

            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.addCategory(Intent.CATEGORY_DEFAULT);

    		File file = new File(filePath);
    		if(!file.exists()) {
    			file.mkdirs();
    		}
    		
    		if(Build.MODEL.equals("HM 1SW")) {
    			
    		} else {
    			uri = Uri.fromFile(new File(filePath + File.separator + fileName));
    		    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    		}

    		((Activity) context).startActivityForResult(intent, REQUESTCODE_CAPTURE);
        } catch (Exception e){ 
        	e.printStackTrace();
        }
		return uri;
	
	}

	/**
	 * 从相册选择照片
	 * @param context
	 */
	@SuppressLint("InlinedApi")
	public static void pickFromGallery(Context context) {
		Intent intent = null;

		if(Build.MODEL.equals("HM 1SW")) {
			 intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		} else {
			intent = new Intent();
			intent.setType("image/*");
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
				intent.setAction(Intent.ACTION_GET_CONTENT);
			} else {
				intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
			}
		}

		// 判断是否存在能处理该intent 的activity
		if(intent.resolveActivity(context.getPackageManager()) != null){
			((Activity) context).startActivityForResult(intent, REQUESTCODE_GALLERY);
		}else{
			Toast.makeText(context,"打开相册失败",Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 获取视频封面图
	 * @param filePath 视频的路径
	 * @param timeUs 视频的时间戳(-1的话交给系统来处理)，根据时间戳来获取对应时间戳的视频截图
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	public static Bitmap getVideoThumbs(String filePath, long timeUs) {
		Bitmap bitmap = null;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			try {
				retriever.setDataSource(filePath);
				bitmap = retriever.getFrameAtTime(timeUs);
			} catch ( IllegalArgumentException ex ) {
				// Assume this is a corrupt video file
			} catch ( RuntimeException ex ) {
				// Assume this is a corrupt video file.
			} catch ( OutOfMemoryError ex ) {
				// Assume this is a corrupt video file.
			} finally {
				try {
					retriever.release();
				} catch ( RuntimeException ex ) {
					// Ignore failures while cleaning up.
				}
			}

			if(bitmap == null)
				return null;
			// Scale down the bitmap if it's too large.
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int max = Math.max(width, height);
			if(max > 512) {
				float scale = 512f / max;
				int w = Math.round(scale * width);
				int h = Math.round(scale * height);
				bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
			}
		} else {
			bitmap = ThumbnailUtils.createVideoThumbnail(filePath, Thumbnails.MICRO_KIND);
		}
		return bitmap;
	}

	@SuppressWarnings("deprecation")
	public static Size getOptimalPreviewSize(Activity currentActivity, List<Size> sizes, double targetRatio) {
		// Use a very small tolerance because we want an exact match.
		final double ASPECT_TOLERANCE = 0.001;
		if(sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		// Because of bugs of overlay and layout, we sometimes will try to
		// layout the viewfinder in the portrait orientation and thus get the
		// wrong size of mSurfaceView. When we change the preview size, the
		// new overlay will be created before the old one closed, which causes
		// an exception. For now, just get the screen size

		Display display = currentActivity.getWindowManager().getDefaultDisplay();
		int targetHeight = Math.min(display.getHeight(), display.getWidth());

		if(targetHeight <= 0) {
			// We don't know the size of SurfaceView, use screen height
			targetHeight = display.getHeight();
		}

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if(Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if(Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio. This should not happen.
		// Ignore the requirement.
		if(optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if(Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static String getSmallBitmapPath(String filePath,int width,int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		FileOutputStream fos = null;
		ByteArrayOutputStream out = null;

		try{
			File file = new File(filePath);
			out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
			byte[] array = out.toByteArray();
			fos = new FileOutputStream(file);
			fos.write(array);
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filePath;
	}

	public static Bitmap getSmallBitmap(String filePath,int width,int height) {
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static DisplayMetrics getScreenWH(Context context) {
		DisplayMetrics dMetrics = new DisplayMetrics();
		dMetrics = context.getResources().getDisplayMetrics();
		return dMetrics;
	}

	/** 保存方法 */
	public static boolean saveBitmap(Bitmap bm, String mCropPath) {
		boolean compress = false;
		File f = new File(mCropPath);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			 compress =  bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return compress;
	}
	/**
	 * 图片按比例大小压缩方法（根据Bitmap图片压缩）
	 * */
	public static Bitmap comp(Bitmap image) {
		Bitmap bitmap;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if( baos.toByteArray().length / 1024>2048) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}
	//  压缩好比例大小后再进行质量压缩
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>200) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 获取照片的真实路径
	 * */
	public static String getRealFilePath( final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			cursor.moveToFirst();
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}
}
