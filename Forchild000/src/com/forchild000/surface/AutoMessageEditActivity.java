package com.forchild000.surface;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.forchild.data.SeniorInfoAutoMessage;
import com.forchild.server.AutoMsgHelper;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.StatesIntent;

/**
 * @deprecated
 * @author Reeki
 * 
 */
public class AutoMessageEditActivity extends AliveBaseActivity {
	private Spinner userChoices;
	private EditText contentEdit;
	private TimePicker timePicker;
	private Button sureBtn, cancelBtn;
	private ArrayAdapter<String> adapter;
	protected DatabaseHelper dbHelper;
	protected SeniorInfoAutoMessage siam;
	protected int type = -1;
	protected int viewId = -1;
	protected int nextAlarmId = -1;
	protected List<SeniorInfoAutoMessage> siList = new ArrayList<SeniorInfoAutoMessage>();

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
		userChoices.setOnItemSelectedListener(userChoicesListener);

		Cursor cursor = dbHelper.getSeniorInfo(new String[] { "name", "id", "oid", "nick" }, null, null);
		while (cursor.moveToNext()) {
			SeniorInfoAutoMessage sis = new SeniorInfoAutoMessage(-1, cursor.getInt(cursor.getColumnIndex("oid")), cursor.getString(cursor
					.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("nick")), new String(), 0, 0);
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

		contentEdit = (EditText) findViewById(R.id.automsgedit_content_edit);

		timePicker = (TimePicker) findViewById(R.id.automsgedit_time_timepicker);
		timePicker.setIs24HourView(true);

		sureBtn = (Button) findViewById(R.id.automsgedit_sure_btn);
		cancelBtn = (Button) findViewById(R.id.automsgedit_cancel_btn);
		sureBtn.setOnClickListener(AutoMessageEditButtonListener);
		cancelBtn.setOnClickListener(AutoMessageEditButtonListener);

		Intent startIntent = getIntent();
		type = startIntent.getIntExtra("type", -1);
		switch (type) {
		case AutoMessageDisplayActivity.NEW_MESSAGE_ACTIVITY_REQUEST:
			nextAlarmId = startIntent.getIntExtra("next_alarm_id", -1);
			if (nextAlarmId == -1) {
				Log.e("AutoMessageEditActivity", "alarm id error: " + nextAlarmId);
				finish();
			} else {
				siam = new SeniorInfoAutoMessage();
			}
			break;

		case AutoMessageDisplayActivity.EDIT_MESSAGE_ACTIVITY_REQUEST:
			siam = (SeniorInfoAutoMessage) startIntent.getSerializableExtra("senior_info");
			viewId = startIntent.getIntExtra("view_id", -1);
			contentEdit.setText(siam.getContent());
			if (siam.getHour() >= 0 && siam.getHour() < 23) {
				timePicker.setCurrentHour(siam.getHour());
			}
			if (siam.getMinute() >= 0 && siam.getMinute() < 59) {
				timePicker.setCurrentMinute(siam.getMinute());
			}
			userChoices.setVisibility(View.GONE);
			break;

		default:
			Log.e("AutoMessageEditActivity", "type error: " + type);
			finish();
			break;
		}

	}

	private OnClickListener AutoMessageEditButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.automsgedit_sure_btn:
				String content = contentEdit.getText().toString();
				DatabaseHelper dbHelper = new DatabaseHelper(AutoMessageEditActivity.this);
				Intent resultIntent = new Intent();
				if (content != null && content.length() > 0) {
					siam.setContent(content);
				} else {
					Toast.makeText(AutoMessageEditActivity.this, getText(R.string.automsgedit_edittext_empty_reminder), Toast.LENGTH_SHORT).show();
					return;
				}

				switch (type) {
				case AutoMessageDisplayActivity.NEW_MESSAGE_ACTIVITY_REQUEST:

					if (userChoices.getSelectedItemPosition() <= siList.size()) {
						siam = siList.get(userChoices.getSelectedItemPosition());
					} else {
						Toast.makeText(AutoMessageEditActivity.this, getText(R.string.automsgedit_user_choise_error), Toast.LENGTH_SHORT).show();
						return;
					}

					siam.setHour(timePicker.getCurrentHour());
					siam.setMinute(timePicker.getCurrentMinute());
					siam.setContent(content);
					siam.setId(nextAlarmId + 1);
					AutoMsgHelper.setAutoMsgAlarm(AutoMessageEditActivity.this, siam.getId(), siam.getHour(), siam.getMinute());

					ContentValues newAlarmValues = new ContentValues();
					newAlarmValues.put("oid", siam.getOid());
					newAlarmValues.put("id", siam.getId());
					newAlarmValues.put("name", siam.getName());
					newAlarmValues.put("nick", siam.getNick());
					newAlarmValues.put("content", siam.getContent());
					newAlarmValues.put("hour", siam.getHour());
					newAlarmValues.put("minute", siam.getMinute());
					long insertResult = dbHelper.addAutoMessage(newAlarmValues, siam.getId());
					// `id` int, `to` int, `name` varchar(16), `nick`
					// varchar(32), `content` varchar(512), `hour` int, `minute`
					// int
					if (insertResult == -1) {
						Toast.makeText(AutoMessageEditActivity.this, getText(R.string.automsgedit_addnewmsg_fault), Toast.LENGTH_SHORT).show();
						setResult(RESULT_CANCELED);
						finish();
					}
					break;
				case AutoMessageDisplayActivity.EDIT_MESSAGE_ACTIVITY_REQUEST:
					if (siam.getHour() != timePicker.getCurrentHour() || siam.getMinute() != timePicker.getCurrentMinute()) {
						siam.setHour(timePicker.getCurrentHour());
						siam.setMinute(timePicker.getCurrentMinute());
						AutoMsgHelper.setAutoMsgAlarm(AutoMessageEditActivity.this, siam.getId(), siam.getHour(), siam.getMinute());
					}

					if (!siam.getContent().equals(content)) {
						siam.setContent(content);
					}

					ContentValues updateAlarmValues = new ContentValues();
					updateAlarmValues.put("oid", siam.getOid());
					updateAlarmValues.put("id", siam.getId());
					updateAlarmValues.put("name", siam.getName());
					updateAlarmValues.put("nick", siam.getNick());
					updateAlarmValues.put("content", siam.getContent());
					updateAlarmValues.put("hour", siam.getHour());
					updateAlarmValues.put("minute", siam.getMinute());
					long udpateResult = dbHelper.updateAutoMessage(updateAlarmValues, siam.getId());
					if (udpateResult == -1) {
						Toast.makeText(AutoMessageEditActivity.this, getText(R.string.automsgedit_addnewmsg_fault), Toast.LENGTH_SHORT).show();
						setResult(RESULT_CANCELED);
						finish();
					}
					resultIntent.putExtra("view_id", viewId);
					break;
				}
				resultIntent.putExtra("senior_info", siam);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				break;
			case R.id.automsgedit_cancel_btn:
				setResult(Activity.RESULT_CANCELED);
				finish();
				break;
			default:
				break;
			}
		}

	};

	private OnItemSelectedListener userChoicesListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// siam = siList.get(arg2);
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
		StatesIntent.sendCloseState(this, ServiceCore.ACTIVITY_TYPE_AUTOMESSAGEEDIT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatesIntent.sendAliveState(this, ServiceCore.ACTIVITY_TYPE_AUTOMESSAGEEDIT);

	}

}
