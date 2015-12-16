package com.forchild000.surface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestSeniorSport;
import com.forchild.data.SeniorInfoSimple;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;
import com.forchild.server.TimeFormat;
import com.forchild000.surface.KCalendar.OnCalendarClickListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

public class HistoryActivity extends Activity {
	protected String calUnit;
	protected String msgUnit;

	private Spinner choiseSpinner;
	private ArrayAdapter<String> adapter;
	private TextView runText, walkText, standText, calText;
	private TextView centerText, leftText, rightText;

	private PieChart mChart;
	private CalendarPopup calendarPopup;
	private RelativeLayout titleBar;

	protected int nowSenior = -1;
	protected Calendar calendar;
	protected List<SeniorInfoSimple> seniorsInfo = new ArrayList<SeniorInfoSimple>();
	protected Preferences preference;
	protected DatabaseHelper databaseHelper;
	protected Handler msgHandler;
	protected int fallCount, msgCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_activity);

		runText = (TextView) findViewById(R.id.history_run_time_display);
		walkText = (TextView) findViewById(R.id.history_walk_time_display);
		standText = (TextView) findViewById(R.id.history_stand_time_display);
		calText = (TextView) findViewById(R.id.history_calorie_count);

		centerText = (TextView) findViewById(R.id.history_title_center_text);
		leftText = (TextView) findViewById(R.id.history_title_left_text);
		rightText = (TextView) findViewById(R.id.history_title_right_text);

		rightText.setText(R.string.parenttrack_rightbtn_text);
		leftText.setText(R.string.parenttrack_leftbtn_text);
		centerText.setText(R.string.parent_centerbtn_text);

		titleBar = (RelativeLayout) findViewById(R.id.history_title_bar);
		calendarPopup = new CalendarPopup(this, titleBar, calendarListner);

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		choiseSpinner = (Spinner) findViewById(R.id.history_title_center_spinner);
		choiseSpinner.setAdapter(adapter);
		choiseSpinner.setOnItemSelectedListener(choiseListener);

		calendar = Calendar.getInstance();

		leftText = (TextView) findViewById(R.id.history_title_left_text);
		leftText.setOnClickListener(historyClickListener);
		rightText.setOnClickListener(historyClickListener);
		centerText.setOnClickListener(historyClickListener);

		msgHandler = new Handler(msgCallback);

		databaseHelper = new DatabaseHelper(this);
		preference = new Preferences(this);

		calUnit = getText(R.string.history_calorie_unit).toString();
		msgUnit = getText(R.string.history_message_unit).toString();

		mChart = (PieChart) findViewById(R.id.history_pie_chart);
		mChart.setVisibility(View.GONE);
		mChart.setHoleRadius(50f);
		mChart.setTransparentCircleRadius(54f);
		mChart.setDescription(null);
		mChart.setDrawCenterText(true);
		mChart.setDrawHoleEnabled(true);
		mChart.setRotationAngle(90);
		mChart.setRotationEnabled(true);
		mChart.setUsePercentValues(true);
		mChart.getLegend().setEnabled(false); // 设置比例图
	}

	protected OnCalendarClickListener calendarListner = new OnCalendarClickListener() {

		@Override
		public void onCalendarClick(int row, int col, String dateFormat) {
			if (!ServiceStates.getForchildServiceState(HistoryActivity.this)) {
				Intent intent = new Intent(HistoryActivity.this, ServiceCore.class);
				HistoryActivity.this.startService(intent);
			}

			if (calendarPopup != null && seniorsInfo.size() > 0 && nowSenior < seniorsInfo.size()) {
				Calendar now = Calendar.getInstance();
				int years = Integer.parseInt(dateFormat.substring(0, dateFormat.indexOf("-")));
				int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-"))) - 1;
				int date = Integer.parseInt(dateFormat.substring(dateFormat.lastIndexOf("-") + 1));
				Log.e("History", "已选择日期" + years + "-" + month + "-" + date);
				if (now.get(Calendar.YEAR) == years && now.get(Calendar.MONTH) == month && now.get(Calendar.DATE) == date) {
					calendar = now;
				} else {
					Log.e("ParentTrack", calendarPopup.getYear() + "-" + calendarPopup.getMonth() + "-" + calendarPopup.getDate());
					calendar.set(Calendar.MILLISECOND, 999);
					calendar.set(years, month, date, 23, 59, 59);
				}

				if (ServiceCore.getSid() != null && seniorsInfo.get(nowSenior).getOid() != BaseProtocolFrame.INT_INITIATION) {
					getOneState(seniorsInfo.get(nowSenior).getOid(), calendar);
				}

				calendarPopup.dismiss();
			}

		}

	};

	protected OnClickListener historyClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.history_title_left_text:
				if (nowSenior >= 0 && nowSenior < seniorsInfo.size()) {
					getOneState(seniorsInfo.get(nowSenior).getOid());
				} else {
					Toast.makeText(HistoryActivity.this, getText(R.string.history_refresh_nouser_error), Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			case R.id.history_title_right_text:
				calendarPopup.show();
				break;
			case R.id.history_title_center_text:
				choiseSpinner.performClick();
				break;
			}
		}

	};

	private Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {

			switch (msg.what) {
			case ServiceCore.NETWORK_RESPONSE:

				if (msg.obj instanceof RequestSeniorSport) {
					RequestSeniorSport rlc = (RequestSeniorSport) msg.obj;
					if (rlc.isResponse()) {
						switch (rlc.getReq()) {
						case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
							runText.setText(TimeFormat.msToTime(rlc.getRun() * 1000));
							walkText.setText(TimeFormat.msToTime(rlc.getWalk() * 1000));
							standText.setText(TimeFormat.msToTime(rlc.getStand() * 1000));
							calText.setText(rlc.getCal() + calUnit);
							HistoryActivity.this.showPie((float) rlc.getRun(), (float) rlc.getWalk(), (float) rlc.getStand());
							break;
						}
					} else {
						switch (rlc.getReason()) {
						case BaseProtocolFrame.REASON_NORMALITY:
							Toast.makeText(HistoryActivity.this, getText(R.string.noresponse_unknown_error), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED:
							Toast.makeText(HistoryActivity.this, getText(R.string.noresponse_exceed_maxtask), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_NO_RESPONSE:
							Toast.makeText(HistoryActivity.this, getText(R.string.noresponse_no_response), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_LOGOUT:
							Toast.makeText(HistoryActivity.this, getText(R.string.noresponse_logout), Toast.LENGTH_SHORT).show();
							break;
						}
					}
				}
				break;
			}
			return true;
		}

	};

	private void showPie(float runTime, float walkTime, float standTime) {
		if (runTime == 0f && walkTime == 0f && standTime == 0f) {
			if (mChart.getVisibility() != View.GONE) {
				mChart.setVisibility(View.GONE);
			}
			return;
		}

		double totalTime = runTime + walkTime + standTime;
		
		ArrayList<String> xValues = new ArrayList<String>();
		ArrayList<Entry> yValues = new ArrayList<Entry>();
		ArrayList<Integer> colors = new ArrayList<Integer>();

		if (runTime / totalTime > 0.1) {
			xValues.add(getText(R.string.history_runtime_title).toString() + TimeFormat.msToTime((long) runTime * 1000));
			yValues.add(new Entry(runTime, 0));
			colors.add(Color.rgb(255, 123, 124));
		}

		if (walkTime / totalTime > 0.1) {
			xValues.add(getText(R.string.history_walktime_title).toString() + TimeFormat.msToTime((long) walkTime * 1000));
			yValues.add(new Entry(walkTime, 1));
			colors.add(Color.rgb(205, 205, 205));
		}

		if (standTime / totalTime > 0.1) {
			xValues.add(getText(R.string.history_standtime_title).toString() + TimeFormat.msToTime((long) standTime * 1000));
			yValues.add(new Entry(standTime, 2));
			colors.add(Color.rgb(114, 188, 223));
		}

		String suggestion = new String();
		if (walkTime + runTime * 2 > 7200) {
			suggestion = getText(R.string.history_suggest_much).toString();
		} else if (walkTime + runTime * 2 < 3600) {
			suggestion = getText(R.string.history_suggest_less).toString();
		} else {
			suggestion = getText(R.string.history_suggest_normal).toString();
		}

		PieDataSet pieDataSet = new PieDataSet(yValues, getText(R.string.history_sport_time).toString());
		pieDataSet.setSliceSpace(0f);

		pieDataSet.setColors(colors);

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = 5 * (metrics.densityDpi / 160f);
		pieDataSet.setSelectionShift(px);

		mChart.setCenterText(suggestion);
		mChart.setData(new PieData(xValues, pieDataSet));
		if (mChart.getVisibility() != View.VISIBLE) {
			mChart.setVisibility(View.VISIBLE);
			mChart.animateXY(1000, 1000);
		}
	}

	private OnItemSelectedListener choiseListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg2 != nowSenior) {
				preference.setDefaultSenior(arg2);
				getOneState(seniorsInfo.get(arg2).getOid());

				if (arg2 < seniorsInfo.size()) {
					SeniorInfoSimple sis = seniorsInfo.get(arg2);
					nowSenior = arg2;
					if (sis.getNick() != null && sis.getNick().length() > 0) {
						centerText.setText(sis.getNick());
					} else if (sis.getName() != null) {
						centerText.setText(sis.getName());
					}
				}

			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 查看service是否在运行，如果不在，则将其运行
		if (!ServiceStates.getForchildServiceState(this)) {
			Intent intent = new Intent(this, ServiceCore.class);
			this.startService(intent);
		}

		// 添加老人名字到spinner
		Cursor cursor = databaseHelper.getSeniorInfo(new String[] { "name", "id", "oid", "nick" }, null, null);
		seniorsInfo.clear();
		while (cursor.moveToNext()) {
			SeniorInfoSimple sis = new SeniorInfoSimple(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")),
					cursor.getInt(cursor.getColumnIndex("oid")), cursor.getString(cursor.getColumnIndex("nick")));
			seniorsInfo.add(sis);
		}
		cursor.close();
		databaseHelper.close();

		adapter.clear();
		for (SeniorInfoSimple sis : seniorsInfo) {
			if (sis.getNick() != null && sis.getNick().length() > 0) {
				adapter.add(sis.getNick());
			} else if (sis.getName() != null) {
				adapter.add(sis.getName());
			} else {
				continue;
			}
		}

		// 设置光标的默认位置
		if (seniorsInfo.size() > 0) {
			int defaultSenior = preference.getDefaultSenior();
			if (defaultSenior <= seniorsInfo.size() && defaultSenior != -1) {
				choiseSpinner.setSelection(defaultSenior);
				getOneState(seniorsInfo.get(defaultSenior).getOid());
			} else if (defaultSenior == -1) {
				choiseSpinner.setSelection(0);
			}

			if (defaultSenior < seniorsInfo.size()) {
				SeniorInfoSimple sis = seniorsInfo.get(defaultSenior);
				nowSenior = defaultSenior;
				if (sis.getNick() != null && sis.getNick().length() > 0) {
					centerText.setText(sis.getNick());
				} else if (sis.getName() != null) {
					centerText.setText(sis.getName());
				}
			}

			rightText.setVisibility(View.VISIBLE);
		} else {
			rightText.setVisibility(View.GONE);
		}

	}

	private void getOneState(int oid) {
		if (!ServiceStates.getForchildServiceState(this)) {
			Intent intent = new Intent(this, ServiceCore.class);
			this.startService(intent);
		}

		if (ServiceCore.getSid() != null && oid != BaseProtocolFrame.INT_INITIATION) {
			RequestSeniorSport rss = new RequestSeniorSport(preference.getSid(), oid, calendar.getTimeInMillis());
			rss.addHandler(msgHandler);
			ServiceCore.addNetTask(rss);
			 Log.e("History",
			 calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH)
			 + "-" + calendar.get(Calendar.DATE) + " "
			 + calendar.get(Calendar.HOUR_OF_DAY) + ":" +
			 calendar.get(Calendar.MINUTE) + ":" +
			 calendar.get(Calendar.SECOND));
		}
	}

	private void getOneState(int oid, Calendar calendar) {
		if (!ServiceStates.getForchildServiceState(this)) {
			Intent intent = new Intent(this, ServiceCore.class);
			this.startService(intent);
		}

		if (ServiceCore.getSid() != null && oid != BaseProtocolFrame.INT_INITIATION) {
			RequestSeniorSport rss = new RequestSeniorSport(preference.getSid(), oid, calendar.getTimeInMillis());
			rss.addHandler(msgHandler);
			ServiceCore.addNetTask(rss);
			Log.e("History",
					calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DATE) + " "
							+ calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));
		}
	}

}
