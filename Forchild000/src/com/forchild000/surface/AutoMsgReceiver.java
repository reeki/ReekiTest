package com.forchild000.surface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.forchild.server.ServiceStates;

public class AutoMsgReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("AutoMsgReceiver", "get auto message");
		int id = intent.getIntExtra("id", -1);
		if (id < 0) {
			Log.e("AutoMsgReceiver", "id error: " + id);
		} else {
			if(!ServiceStates.getForchildServiceState(context)) {
				Intent serviceIntent = new Intent(context, ServiceCore.class);
				context.startService(serviceIntent);
			}
			
			Intent beanIntent = new Intent();
			beanIntent.setAction(ServiceCore.REQUEST_FLAGS_AUTO_SEND_MESSAGE);
			beanIntent.putExtra("id", id);
			context.sendBroadcast(beanIntent);
			
		}
	}

}
