package com.forchild000.surface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.forchild.data.MessageFrame;
import com.forchild.data.SeniorInfoAutoMessage;
import com.forchild.server.AutoMsgHelper;
import com.forchild.server.DatabaseHelper;

public class AutoMsgEditActivity extends AliveBaseActivity {
	protected Spinner userChoices;
	protected EditText contentEdit;
	protected TimePicker timePicker;
	protected DatePicker datePicker;
	protected Button sureBtn, cancelBtn;
	protected ArrayAdapter<String> adapter;
	protected DatabaseHelper dbHelper;
	protected SeniorInfoAutoMessage siam;
	protected int nextAlarmId = -1;
	protected int type = -1;
	protected int msgType = -1;
	protected List<SeniorInfoAutoMessage> siList = new ArrayList<SeniorInfoAutoMessage>(); // 用于保存联系人基本信息
	protected int oid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_message_edit_activity);

		setResult(RESULT_CANCELED);

		dbHelper = new DatabaseHelper(this);

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		userChoices = (Spinner) findViewById(R.id.automsgedit_user_choice_spinner);
		userChoices.setAdapter(adapter);
		userChoices.setVisibility(View.GONE);

		contentEdit = (EditText) findViewById(R.id.automsgedit_content_edit);

		timePicker = (TimePicker) findViewById(R.id.automsgedit_time_timepicker);
		timePicker.setIs24HourView(true);
		datePicker = (DatePicker) findViewById(R.id.automsgedit_date_datepicker);

		sureBtn = (Button) findViewById(R.id.automsgedit_sure_btn);
		cancelBtn = (Button) findViewById(R.id.automsgedit_cancel_btn);
		sureBtn.setOnClickListener(autoMsgEditBtnListener);
		cancelBtn.setOnClickListener(autoMsgEditBtnListener);

		Intent startIntent = getIntent();
		type = startIntent.getIntExtra("type", -1);
		switch (type) {
		case AutoMsgDisplayActivity.NEW_MESSAGE_ACTIVITY_REQUEST:
			oid = startIntent.getIntExtra("oid", -1);
			if (oid == -1) {
				finish();
			}

			msgType = startIntent.getIntExtra("add_type", -1);
			nextAlarmId = startIntent.getIntExtra("next_alarm_id", 0);

			Calendar calendar = Calendar.getInstance();

			timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

			this.refreshUser();
			siam = new SeniorInfoAutoMessage();
			switch (msgType) {
			case MessageFrame.USERMESSAGETYPE_AUTO_CIRCLE_MESSAGE:
				datePicker.setVisibility(View.GONE);
				break;
			case MessageFrame.USERMESSAGETYPE_AUTO_ONEOFF_MESSAGE:
			case MessageFrame.USERMESSAGETYPE_AUTO_MEDICAL_MESSAGE:
				datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
				break;
			}
			break;
		case AutoMsgDisplayActivity.EDIT_MESSAGE_ACTIVITY_REQUEST:
			siam = (SeniorInfoAutoMessage) startIntent.getSerializableExtra("content");
			contentEdit.setText(siam.getContent());
			datePicker.updateDate(siam.getYear(), siam.getMonth(), siam.getDay());
			if (siam.getHour() >= 0 && siam.getHour() < 23) {
				timePicker.setCurrentHour(siam.getHour());
			}
			if (siam.getMinute() >= 0 && siam.getMinute() < 59) {
				timePicker.setCurrentMinute(siam.getMinute());
			}

			userChoices.setEnabled(false);
			adapter.clear();
			if (siam.getNick() != null && siam.getNick().length() > 0) {
				adapter.add(siam.getNick());
			} else if (siam.getName() != null) {
				adapter.add(siam.getName());
			}

			if (adapter.getCount() >= 1) {
				userChoices.setSelection(0);
			}

			if (siam.getType() == MessageFrame.USERMESSAGETYPE_AUTO_CIRCLE_MESSAGE) {
				datePicker.setVisibility(View.GONE);
			}

			break;
		default:
			finish();
			break;
		}

		Log.e("AutoMsgEditActivity", siam.toString());
	}

	private OnClickListener autoMsgEditBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			String content = contentEdit.getText().toString().trim();
			if (content.length() <= 0) {
				return;
			}

			int hour = timePicker.getCurrentHour();
			int minute = timePicker.getCurrentMinute();

			long result = -1;

			switch (v.getId()) {
			case R.id.automsgedit_sure_btn:
				switch (type) {
				case AutoMsgDisplayActivity.NEW_MESSAGE_ACTIVITY_REQUEST:
					for (SeniorInfoAutoMessage seniorBuff : siList) {
						if (seniorBuff.getOid() == oid) {
							siam = seniorBuff;
						}
					}
					switch (msgType) {
					case MessageFrame.USERMESSAGETYPE_AUTO_CIRCLE_MESSAGE:
						break;
					case MessageFrame.USERMESSAGETYPE_AUTO_ONEOFF_MESSAGE:
					case MessageFrame.USERMESSAGETYPE_AUTO_MEDICAL_MESSAGE:
						siam.setYear(datePicker.getYear());
						siam.setMonth(datePicker.getMonth());
						siam.setDay(datePicker.getDayOfMonth());
						Log.e("AutoMsgEditActivity.surebtn",
								"get date:" + datePicker.getYear() + "-" + datePicker.getMonth() + "-" + datePicker.getDayOfMonth());
						Calendar objTime = Calendar.getInstance();
						objTime.set(siam.getYear(), siam.getMonth(), siam.getDay(), hour, minute);
						if (objTime.getTimeInMillis() <= System.currentTimeMillis()) {
							Toast.makeText(AutoMsgEditActivity.this, getText(R.string.automsgedit_time_pass_error), Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					}
					siam.setType(msgType);
					siam.setHour(hour);
					siam.setMinute(minute);
					siam.setContent(content);
					siam.setId(++nextAlarmId);
					siam.setLoginId(ServiceCore.getLoginId());
					result = dbHelper.addAutoMessage(siam);
					if (result >= 0) {
						switch (msgType) {
						case MessageFrame.USERMESSAGETYPE_AUTO_CIRCLE_MESSAGE:
							AutoMsgHelper.setAutoMsgAlarm(AutoMsgEditActivity.this, siam.getId(), hour, minute);
							break;
						case MessageFrame.USERMESSAGETYPE_AUTO_ONEOFF_MESSAGE:
						case MessageFrame.USERMESSAGETYPE_AUTO_MEDICAL_MESSAGE:
							AutoMsgHelper.setOneOffMsgAlarm(AutoMsgEditActivity.this, siam.getId(), siam.getYear(), siam.getMonth(), siam.getDay(),
									hour, minute);
							break;
						}
						Toast.makeText(AutoMsgEditActivity.this, getText(R.string.automsgedit_setup_okay), Toast.LENGTH_SHORT).show();
					}
					setResult(RESULT_OK);
					finish();
					break;
				case AutoMsgDisplayActivity.EDIT_MESSAGE_ACTIVITY_REQUEST:
					switch (siam.getType()) {
					case MessageFrame.USERMESSAGETYPE_AUTO_CIRCLE_MESSAGE:
						break;
					case MessageFrame.USERMESSAGETYPE_AUTO_ONEOFF_MESSAGE:
					case MessageFrame.USERMESSAGETYPE_AUTO_MEDICAL_MESSAGE:
						siam.setYear(datePicker.getYear());
						siam.setMonth(datePicker.getMonth());
						siam.setDay(datePicker.getDayOfMonth());
						Calendar objTime = Calendar.getInstance();
						objTime.set(siam.getYear(), siam.getMonth(), siam.getDay(), hour, minute);
						if (objTime.getTimeInMillis() <= System.currentTimeMillis()) {
							Toast.makeText(AutoMsgEditActivity.this, getText(R.string.automsgedit_time_pass_error), Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					}
					siam.setHour(hour);
					siam.setMinute(minute);
					siam.setContent(content);
					Log.e("AutoMsgEditActivity", siam.toString());
					result = dbHelper.updateAutoMessage(siam);
					if (result >= 0) {
						AutoMsgHelper.cancelAutoMsgAlarm(AutoMsgEditActivity.this, siam.getId());
						if (result >= 0) {
							switch (siam.getType()) {
							case MessageFrame.USERMESSAGETYPE_AUTO_CIRCLE_MESSAGE:
								AutoMsgHelper.setAutoMsgAlarm(AutoMsgEditActivity.this, siam.getId(), hour, minute);
								break;
							case MessageFrame.USERMESSAGETYPE_AUTO_ONEOFF_MESSAGE:
							case MessageFrame.USERMESSAGETYPE_AUTO_MEDICAL_MESSAGE:
								AutoMsgHelper.setOneOffMsgAlarm(AutoMsgEditActivity.this, siam.getId(), siam.getYear(), siam.getMonth(),
										siam.getDay(), hour, minute);
								break;
							}
							Toast.makeText(AutoMsgEditActivity.this, getText(R.string.automsgedit_setup_okay), Toast.LENGTH_SHORT).show();
						}
					}
					setResult(RESULT_OK);
					finish();
					break;
				}
				break;
			case R.id.automsgedit_cancel_btn:
				setResult(RESULT_CANCELED);
				finish();
				break;
			}
		}

	};

	private void refreshUser() {
		Cursor cursor = dbHelper.getSeniorInfo(new String[] { "name", "id", "oid", "nick", "sex" }, null, null);
		while (cursor.moveToNext()) {
			SeniorInfoAutoMessage sis = new SeniorInfoAutoMessage(-1, cursor.getInt(cursor.getColumnIndex("oid")), cursor.getString(cursor
					.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("nick")), new String(), 0, 0);
			sis.setSex(cursor.getInt(cursor.getColumnIndex("sex")));
			siList.add(sis);
		}
		cursor.close();
		dbHelper.close();
		for (SeniorInfoAutoMessage sis : siList) {
			String nick = sis.getNick();
			if (nick != null && nick.length() > 0) {
				adapter.add(nick);
			} else {
				adapter.add(sis.getName());
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
