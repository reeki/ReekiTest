package com.forchild.server;

import java.util.Calendar;

import android.text.format.Time;

public class TimeFormat {
	private Time time;

	public TimeFormat() {
		time = new Time();
	}

	public int monthDay() {
		return time.monthDay;
	}

	// 2012-12-21 12:12:12
	public String getAllTime() {
		time.setToNow();
		int month = time.month + 1;
		String result = time.year + "-" + month + "-" + time.monthDay + " " + time.hour + ":" + time.minute + ":" + time.second;
		return result;

	}

	public String getDate() {
		time.setToNow();
		int month = time.month + 1;
		String result = time.year + "-" + month + "-" + time.monthDay;
		return result;
	}

	public String getTime() {
		time.setToNow();
		String result = time.hour + ":" + time.minute + ":" + time.second;
		return result;
	}

	public String getTimeHM() {
		time.setToNow();
		String result = time.hour + ":" + time.minute;
		return result;
	}

	/** 将毫秒转化为时分秒的格式 */
	public static String msToTime(long mtime) {
		mtime = mtime / 1000;
		long second = mtime % 60;
		long minute = mtime / 60;
		minute = minute % 60;
		long hour = mtime / 3600;
		hour = hour % 24;
		StringBuffer time = new StringBuffer();
		if (hour < 10) {
			time.append("0");
			time.append(hour);
		} else {
			time.append(hour);
		}
		if (minute < 10) {
			time.append(":0");
			time.append(minute);
		} else {
			time.append(":");
			time.append(minute);
		}
		if (second < 10) {
			time.append(":0");
			time.append(second);
		} else {
			time.append(":");
			time.append(second);
		}
		return time.toString();
	}

	public static String getDisplayDate(long mTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mTime);
		return getDisplayDate(calendar);
	}

	public static String getDisplayTime(long mTime) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mTime);

		return getDisplayTime(calendar);
	}

	public static String getDisplayDate(Calendar calendar) {
		StringBuffer time = new StringBuffer();
		int month = calendar.get(Calendar.MONTH) + 1;
		time.append(calendar.get(Calendar.YEAR)).append("-").append(month).append("-").append(calendar.get(Calendar.DATE)).append(" ");
		if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
			time.append("0").append(calendar.get(Calendar.HOUR_OF_DAY));
		} else {
			time.append(calendar.get(Calendar.HOUR_OF_DAY));
		}

		time.append(":");

		if (calendar.get(Calendar.MINUTE) < 10) {
			time.append("0").append(calendar.get(Calendar.MINUTE));
		} else {
			time.append(calendar.get(Calendar.MINUTE));
		}

		return time.toString();
	}

	public static String getDisplayTime(Calendar calendar) {
		StringBuffer time = new StringBuffer();
		if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
			time.append("0").append(calendar.get(Calendar.HOUR_OF_DAY));
		} else {
			time.append(calendar.get(Calendar.HOUR_OF_DAY));
		}

		time.append(":");

		if (calendar.get(Calendar.MINUTE) < 10) {
			time.append("0").append(calendar.get(Calendar.MINUTE));
		} else {
			time.append(calendar.get(Calendar.MINUTE));
		}

		return time.toString();
	}
}
