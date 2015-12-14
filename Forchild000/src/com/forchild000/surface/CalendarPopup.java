package com.forchild000.surface;

import java.util.ArrayList;
import java.util.List;

import com.forchild000.surface.KCalendar.OnCalendarClickListener;
import com.forchild000.surface.KCalendar.OnCalendarDateChangedListener;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalendarPopup extends PopupWindow {
	protected String date = null;
	protected KCalendar kCalendar;
	
	private View parent;
	private LinearLayout ll_popup;
	private Context context;

	public void show() {
		if (ll_popup != null) {
			ll_popup.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_in));
		}
		setFocusable(true);
		setOutsideTouchable(true);
		showAsDropDown(parent, 0, 0);
		update();
	}

	public CalendarPopup(Context mContext, View parent, OnCalendarClickListener calendarListner) {

		View view = View.inflate(mContext, R.layout.popupwindow_calendar, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		this.parent = parent;
		this.context = mContext;
		
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new BitmapDrawable());
		setContentView(view);

		final TextView popupwindow_calendar_month = (TextView) view.findViewById(R.id.popupwindow_calendar_month);
		final KCalendar calendar = (KCalendar) view.findViewById(R.id.popupwindow_calendar);
		kCalendar = calendar;
		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年" + calendar.getCalendarMonth() + "月");
		final OnCalendarClickListener listner = calendarListner;
		
		if (null != date) {

			int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
			popupwindow_calendar_month.setText(years + "年" + month + "月");

			calendar.showCalendar(years, month);
			calendar.setCalendarDayBgColor(date, R.drawable.calendar_date_focused);
		}

		// 监听所选中的日期
		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			public void onCalendarClick(int row, int col, String dateFormat) {
				int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-")));

				Log.e("test_calendar_weight", dateFormat);

				if (calendar.getCalendarMonth() - month == 1// 跨年跳转
						|| calendar.getCalendarMonth() - month == -11) {
					calendar.lastMonth();

				} else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();

				} else {
					calendar.removeAllBgColor();
					calendar.setCalendarDayBgColor(dateFormat, R.drawable.calendar_date_focused);
					date = dateFormat;// 最后返回给全局 date
				}
				
				if(listner != null) {
					listner.onCalendarClick(row, col, dateFormat);
				}
			}
		});

		// 监听当前月份
		calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			public void onCalendarDateChanged(int year, int month) {
				popupwindow_calendar_month.setText(year + "年" + month + "月");
			}
		});

		// 上月监听按钮
		RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view.findViewById(R.id.popupwindow_calendar_last_month);
		popupwindow_calendar_last_month.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				calendar.lastMonth();
			}

		});

		// 下月监听按钮
		RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view.findViewById(R.id.popupwindow_calendar_next_month);
		popupwindow_calendar_next_month.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				calendar.nextMonth();
			}
		});
	}
	
	public int getYear() {
		if(kCalendar != null) {
			return kCalendar.getCalendarYear();
		} else {
			return -1;
		}
	}
	
	public int getMonth() {
		if(kCalendar != null) {
			return kCalendar.getCalendarMonth() - 1;
		} else {
			return -1;
		}
	}
	
	public int getDate() {
		if(kCalendar != null) {
			return kCalendar.getCalendarDate();
		} else {
			return -1;
		}
	}
}
