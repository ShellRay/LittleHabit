package com.example.frescogif.adapter;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.view.emoji.FaceViewPager;


public class FaceAdapter extends ArrayListAdapter<String> {
	public static final String TAG = "FaceAdapter";
	public static final String DELETE = "20080";
	
	private EditText edit;
	public FaceAdapter(Context context, EditText edit) {
		super(context);
		this.edit=edit;
	}

	int mCount;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position == 0) {
			mCount++;
		}
		else {
			mCount = 0;
		}
		if (mCount > 1 && convertView != null) {
			Log.i(TAG, TAG + " <getView> drop");
			return convertView;
		}
		Log.i(TAG, TAG + " getView position " + position);
		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			holder = new ViewHolder();
			row = View.inflate(mContext, R.layout.face_item, null);
			holder.faceImg = (TextView) row.findViewById(R.id.faceImg);
			holder.btnLayout = (RelativeLayout) row.findViewById(R.id.btnLayout);
			holder.myOnClickListener=new MyOnClickListener();
			holder.myOnLongClickListener=new MyOnLongClickListener();
			holder.faceImg.setOnClickListener(holder.myOnClickListener);
			holder.btnLayout.setOnClickListener(holder.myOnClickListener);
			holder.faceImg.setOnLongClickListener(holder.myOnLongClickListener);
			row.setTag(holder);
		}
		else {
			holder = (ViewHolder) row.getTag();
		}
		String imageName = mList.get(position);
		if (imageName.equals(DELETE)) {
			holder.faceImg.setVisibility(View.GONE);
			holder.btnLayout.setVisibility(View.VISIBLE);
		}
		else {
			holder.faceImg.setVisibility(View.VISIBLE);
			holder.btnLayout.setVisibility(View.GONE);
			holder.faceImg.setText(FaceViewPager.replaceEmotionStrToImg(imageName));
		}
		holder.myOnClickListener.imageName=imageName;
		holder.myOnLongClickListener.imageName=imageName;
		return row;
	}

	public static class ViewHolder {
		TextView faceImg;
		RelativeLayout btnLayout;
		MyOnClickListener myOnClickListener;
		MyOnLongClickListener myOnLongClickListener;
	}
	
	class MyOnClickListener implements View.OnClickListener {
		private String imageName;

		@Override
		public void onClick(View v) {
			if (edit != null) {
				int start = edit.getSelectionStart();
				int end = edit.getSelectionEnd();

				Editable editable = edit.getEditableText();
				editable.delete(start, end);

				if (imageName.equals(DELETE)) {
					final KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
					edit.onKeyDown(KeyEvent.KEYCODE_DEL, keyEventDown);
				}
				else {
					editable.insert(start, FaceViewPager.replaceEmotionStrToImg(imageName));
				}
			}
		}
	}

	class MyOnLongClickListener implements View.OnLongClickListener{

		public String imageName;

		@Override
		public boolean onLongClick(View v) {

			if (imageName.equals(DELETE)) {
				final KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
				edit.onKeyDown(KeyEvent.KEYCODE_DEL, keyEventDown);
			}
			else {
				Toast toast = Toast.makeText(mContext, FaceViewPager.replaceEmotionStrToImg(imageName), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER,0,0);
				toast.show();
			}
			return true;
		}
	}


}
