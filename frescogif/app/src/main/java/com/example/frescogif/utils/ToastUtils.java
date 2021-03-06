package com.example.frescogif.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @modify
 * @version 1.0.0
 */
public class ToastUtils {
	private static Toast mToast;

	/**
	 * 多次弹出信息只显示最新的一次
	 */
	public static void showShort(Context context, CharSequence text) {
		if (context==null )
			return;

		if (TextUtils.isEmpty(text))
			return;

//		Activity topActivity = ActivityStack.getTopActivity();
//		if (topActivity != context && context instanceof ContextThemeWrapper) {
//				Context baseContext = ((ContextThemeWrapper)context).getBaseContext();
//				if (baseContext != topActivity) {
//					return;
//				}
//				context = baseContext;
//		}
		
		if (mToast == null) {
			mToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		
		mToast.show();
	}
	

}
