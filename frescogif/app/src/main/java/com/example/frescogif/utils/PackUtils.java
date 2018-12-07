package com.example.frescogif.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static android.content.Context.KEYGUARD_SERVICE;

/**
 *  
 * @ClassName:    PackUtils 
 * @Author:       Ying Guohuo
 * @CreateDate:   2013-10-11 下午3:53:52    
 */
public class PackUtils {
	public static boolean packIsRunning(Context context) {
		List<RunningTaskInfo> taskInfos = getTaskInfos(context, 1000);
		if (taskInfos == null || taskInfos.isEmpty())
			return false;
		for (RunningTaskInfo info : taskInfos) {
			if (context.getPackageName().equals(info.baseActivity.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public static List<RunningTaskInfo> getTaskInfos(Context context, int num) {
		try {//需要catch异常，用户可能会限制权限
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> taskInfos = activityManager.getRunningTasks(num);
			if (taskInfos != null && !taskInfos.isEmpty()) {
				return taskInfos;
			}
		}
		catch (Exception e) {
//			LogUtils.printStackTrace(e);
		}
		return null;
	}
	/***
	 * 判断应用是否是在后台
	 */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);

		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (TextUtils.equals(appProcess.processName, context.getPackageName())) {
				boolean isBackground = (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE);
				boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
				return isBackground || isLockedState;
			}
		}
		return false;
	}
	public static boolean isOnForeground(Context context) {
		List<RunningTaskInfo> taskInfos = getTaskInfos(context, 2);
		if (taskInfos == null || taskInfos.isEmpty())
			return false;
		RunningTaskInfo runningTaskInfo = taskInfos.get(0);
		ComponentName topActivity = runningTaskInfo.topActivity;
		if (context.getPackageName().equals(topActivity.getPackageName())) {
			return true;
		}
		return false;
	}

	public static boolean isRunningBackground(Context context) {
		if (!isOnForeground(context) && packIsRunning(context)) {
			return true;
		}
		return false;
	}

	private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
	private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

	/**判断是否开启了通知*/
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static boolean isNotificationEnabled(Context context) {

		AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

		ApplicationInfo appInfo = context.getApplicationInfo();

		String pkg = context.getApplicationContext().getPackageName();

		int uid = appInfo.uid;

		Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

		try {

			appOpsClass = Class.forName(AppOpsManager.class.getName());

			Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

			Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
			int value = (int)opPostNotificationValue.get(Integer.class);

			return ((int)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

}
