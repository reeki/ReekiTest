package com.forchild.server;

import java.sql.Timestamp;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.forchild000.surface.AutoMsgReceiver;

public class AutoMsgHelper {
	public static final long ONE_DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

	public static void setAutoMsgAlarm(Context context, int id, int hour, int minute) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AutoMsgReceiver.class);
		intent.setAction("com.forchild.automsg.broadcast");
		intent.putExtra("id", id);
		PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);

		long time = cal.getTimeInMillis();

		if (time < System.currentTimeMillis()) {
			time += ONE_DAY_IN_MILLIS;
		}

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, ONE_DAY_IN_MILLIS, pi);
		Log.e("AutoMsgHelper", "已设置循环消息, id = " + id + "时间:" + hour + ":" + minute);
	}

	public static boolean setOneOffMsgAlarm(Context context, int id, int year, int month, int day, int hour, int minute) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AutoMsgReceiver.class);
		intent.setAction("com.forchild.automsg.broadcast");
		intent.putExtra("id", id);
		PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);

		long time = cal.getTimeInMillis();

		if (time <= System.currentTimeMillis()) {
			return false;
		}

		long a = System.currentTimeMillis() - time;

		Log.e("AutoMsgHelper", "time:" + a);

		alarmManager.set(AlarmManager.RTC, time, pi);
		Timestamp ts = new Timestamp(time);
		Log.e("AutoMsgHelper", "已设置一次性消息, id = " + id + "时间:" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ", cal" + ts.toString());
		return true;
	}

	public static void cancelAutoMsgAlarm(Context context, int id) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AutoMsgReceiver.class);
		intent.setAction("com.forchild.automsg.broadcast");
		intent.putExtra("id", id);
		PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pi);
	}

}
