package com.forchild.server;

import android.content.Context;
import android.content.Intent;

public class StatesIntent {
	public static final String ALIVE_TAG = "com.forchild.isAlive";
	public static final String CLOSE_TAG = "com.forchild.isClose";
	
	public static void sendAliveState(Context context, int activityId) {
		Intent toLiveIntent = new Intent();
		toLiveIntent.setAction(ALIVE_TAG);
		toLiveIntent.putExtra("type", activityId);
		context.sendBroadcast(toLiveIntent);
	}
	
	
	public static void sendCloseState(Context context, int activityId) {
		Intent toCloseIntent = new Intent();
		toCloseIntent.setAction(CLOSE_TAG);
		toCloseIntent.putExtra("type", activityId);
		context.sendBroadcast(toCloseIntent);
	}
}
