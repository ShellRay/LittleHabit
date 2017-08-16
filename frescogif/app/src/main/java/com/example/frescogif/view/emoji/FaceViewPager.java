package com.example.frescogif.view.emoji;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.example.frescogif.R;
import com.example.frescogif.adapter.FaceAdapter;
import com.example.frescogif.bean.FaceNames;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class FaceViewPager extends ViewPager {
	public static final String TAG = "FaceViewPager";
	
	public static final String PNG = ".png";
	public static final String FACE_DIR = "emoji";
	public static final int FACE_ROW = 4;//表情行数
	public static int faceColumn = 7;//表情列数
	private static HashMap<String, Drawable> faceMap = new HashMap<String, Drawable>();
	private static Context context;

	private ArrayList<View> gridViews;
	private EditText edit;
	private LayoutInflater infalter;

	public FaceViewPager(Context context) {
		super(context);
		this.context = context;
	}

	public FaceViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void setEditText(EditText edit) {
		this.edit = edit;
	}

	public int init() {
		gridViews = new ArrayList<View>();
		infalter = LayoutInflater.from(getContext());
		int screenWidth = context.getWallpaperDesiredMinimumWidth();
		int faceImageWidth =  200;
		faceColumn = screenWidth / faceImageWidth;

		int pageSize = FACE_ROW * faceColumn;
		int pageCount = 0;

		List<String> faceList = Arrays.asList(FaceNames.faceImagesName);
		ArrayList<String> facePageList = new ArrayList<String>();

		Iterator<String> it = faceList.iterator();
		while (it.hasNext()) {
			if ((facePageList.size() + 1) == pageSize) {
				facePageList.add(FaceAdapter.DELETE);
				createGridView(facePageList);
				facePageList = new ArrayList<String>();
				pageCount++;
			}
			else {
				facePageList.add(it.next());
			}
		}
		if (facePageList.size() > 0) {
			facePageList.add(FaceAdapter.DELETE);
			createGridView(facePageList);
			pageCount++;
		}

		ViewPageAdapter adapter = new ViewPageAdapter(gridViews);
		this.setAdapter(adapter);

		new FaceLoadTask().execute();//加载本地表情
		return pageCount;
	}

	private void createGridView(ArrayList<String> list) {
		GridView gridView = (GridView) infalter.inflate(R.layout.face_gridview, null);
		FaceAdapter faceAdapter = new FaceAdapter(getContext(),edit);
		faceAdapter.setList(list);
		gridView.setAdapter(faceAdapter);
		gridView.setNumColumns(faceColumn);
		gridViews.add(gridView);
	}

	public static SpannableString replaceEmotionStrToImg(String faceName) {
		String emid = "[emid:" + faceName + "]";
		SpannableString spannable = new SpannableString(emid);
		Drawable drawable = getDrawable(faceName);
		int faceWidth = 100;
		drawable.setBounds(0, 5, faceWidth, faceWidth + 5);
		ImageSpan imagespan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
		spannable.setSpan(imagespan, 0, emid.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	private static Drawable getDrawable(String faceName) {
		faceName = faceName + PNG;
		Drawable drawable = faceMap.get(faceName);
		String fileName = FACE_DIR + File.separator + faceName;
		InputStream in = null;
		try {
			if (drawable == null) {
				in = context.getAssets().open(fileName);
				drawable = BitmapDrawable.createFromStream(in, null);
			}
			else {
				Log.i(TAG, "getDrawable from cache:" + fileName);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return drawable;
	}

	class FaceLoadTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... paras) {

			try {
				String[] faceList = context.getResources().getAssets().list(FaceViewPager.FACE_DIR);
				for (int i = 0; i < faceList.length; i++) {
					String fileName = faceList[i];
					Drawable drawable = null;
					InputStream in = null;
					try {
						String filePath = FaceViewPager.FACE_DIR + File.separator + fileName;
						in = context.getAssets().open(filePath);
						drawable = BitmapDrawable.createFromStream(in, null);
						faceMap.put(fileName, drawable);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					finally {
						if (in != null) {
							try {
								in.close();
							}
							catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

		}
	}
}
