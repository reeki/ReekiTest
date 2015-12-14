package com.forchild.server;

import java.util.List;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

public class ServiceStates {
	public static boolean getSerivceState(Context context, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(100);
		for (int i = 0; i < serviceList.size(); i++) {
			Log.e("Test47 Servicelist No.", +i + ": " + serviceList.get(i).service.getClassName());
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	public static boolean getActivityState(Context context, String className) {
		boolean result = false;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskList = activityManager.getRunningTasks(1000);
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).baseActivity.getClassName().equals("com.forchild000.surface.MainActivity")) {
				result = true;
			}
			Log.e("Test 59", "activity name:" + taskList.get(i).baseActivity.getClassName());
		}
		return result;
	}

	public static List<ActivityManager.RunningServiceInfo> getServiceList(Context context, int maxNum) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getRunningServices(maxNum);
	}

	public static List<ActivityManager.RunningServiceInfo> getServiceList(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getRunningServices(100);
	}

	public static boolean getForchildServiceState(Context context) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(100);
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals("com.forchild000.surface.ServiceCore") == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
}
