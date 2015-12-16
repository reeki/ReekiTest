package com.forchild000.surface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.forchild.server.DBFrame;
import com.forchild.server.DatabaseHelper;

public class SetupActivity extends Activity {
	public static final int PERSON_SETUP_NAME = 1;
	public static final int PERSON_SETUP_NICK = 2;
	public static final int PERSON_SETUP_HEIGHT = 3;
	public static final int PERSON_SETUP_WEIGHT = 4;
	public static final int PERSON_SETUP_ADDRESS = 5;
	public static final int PERSON_SETUP_ALLERGY = 6;
	public static final int PERSON_SETUP_MEDICAL = 7;

	public static final int PERSON_SETUP_BIRTH = 8;

	public static final int PERSON_SETUP_SEX = 9;
	public static final int PERSON_SETUP_BLOOD = 10;

	public static final String PATTERN_NAME = "[\\u0041-\\u0056]";
	public static final String PATTERN_ONLY_NUMBERORCHARTS = "^[\\d[a-zA-Z]]*$";
	public static final String PATTERN_PHONE = "^1[3|4|5|8][0-9]\\d{8}$";
	public static final String PATTERN_CARD_ID = "^(\\d{6})(19|20)?(\\d{2})([01]\\d)([0123]\\d)(\\d{3})(\\d|X|x)?$";
	public static final String PATTERN_ONLY_NUMBER = "\\d";

	protected SetupButton assistCareBtn, personInfoBtn, seniorsInfoBtn, emeInfoBtn, commonSetBtn, aboutBtn, addSeniorBtn, regSeniorBtn, accidentBtn,
			sosBtn;
	protected TextView titleCenterText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_activity);

		// ActionBar actionBar = getActionBar();
		// actionBar.setCustomView(R.layout.public_title_bar_layout);
		// actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		titleCenterText = (TextView) findViewById(R.id.setup_title_center_text);
		titleCenterText.setText(R.string.setup_title);

		assistCareBtn = (SetupButton) findViewById(R.id.setup_assist_care_btn);
		personInfoBtn = (SetupButton) findViewById(R.id.setup_person_info_btn);
		seniorsInfoBtn = (SetupButton) findViewById(R.id.setup_olders_info_btn);
		emeInfoBtn = (SetupButton) findViewById(R.id.setup_emergence_info_btn);
		commonSetBtn = (SetupButton) findViewById(R.id.setup_common_info_btn);
		// aboutBtn = (SetupButton) findViewById(R.id.setup_about_btn);
		addSeniorBtn = (SetupButton) findViewById(R.id.setup_add_senior_btn);
		regSeniorBtn = (SetupButton) findViewById(R.id.setup_register_seniors_btn);
		accidentBtn = (SetupButton) findViewById(R.id.setup_accident_info_btn);
		sosBtn = (SetupButton) findViewById(R.id.setup_sos_info_btn);
		// aboutBtn.setVisibility(View.GONE);
		assistCareBtn.setVisibility(View.GONE);

		personInfoBtn.setFrame(SetupButton.BOTTOM_FRAME);
		seniorsInfoBtn.setFrame(SetupButton.BOTTOM_FRAME);
		addSeniorBtn.setFrame(SetupButton.BOTTOM_FRAME);
		regSeniorBtn.setFrame(SetupButton.BOTTOM_FRAME);
		accidentBtn.setFrame(SetupButton.BOTTOM_FRAME);
		commonSetBtn.setFrame(SetupButton.BOTTOM_FRAME);
		// seniorsInfoBtn.setFrame(SetupButton.NO_FRAME);

		assistCareBtn.setOnClickListener(setupButtonListener);
		personInfoBtn.setOnClickListener(setupButtonListener);
		seniorsInfoBtn.setOnClickListener(setupButtonListener);
		emeInfoBtn.setOnClickListener(setupButtonListener);
		commonSetBtn.setOnClickListener(setupButtonListener);
//		aboutBtn.setOnClickListener(setupButtonListener);
		addSeniorBtn.setOnClickListener(setupButtonListener);
		regSeniorBtn.setOnClickListener(setupButtonListener);
		accidentBtn.setOnClickListener(setupButtonListener);
		sosBtn.setOnClickListener(setupButtonListener);

		assistCareBtn.setText(getText(R.string.setup_assist_care_btntext));
		personInfoBtn.setText(getText(R.string.setup_person_info_btntext));
		seniorsInfoBtn.setText(getText(R.string.setup_seniors_info_btntext));
		emeInfoBtn.setText(getText(R.string.setup_emergence_info_btntext));
		commonSetBtn.setText(getText(R.string.setup_common_info_btntext));
//		aboutBtn.setText(getText(R.string.setup_about_btntext));
		addSeniorBtn.setText(getText(R.string.seniorinfo_add_senior_btn_text));
		regSeniorBtn.setText(getText(R.string.seniorinfo_reg_senior_btn_text));
		accidentBtn.setText(getText(R.string.setup_accident_btntext));
		sosBtn.setText(getText(R.string.setup_sos_btntext));

		this.initUI();

		this.registerReceiver(setupReceiver, new IntentFilter("com.forchild.setupactivity.refreshui"));

		// !!!! 测试功能
		Button catchLogcat = (Button) findViewById(R.id.setup_catch_logcat);
		catchLogcat.setVisibility(View.GONE);
		
		catchLogcat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				StringBuffer log = new StringBuffer();
				DBFrame dbFrame = new DBFrame(SetupActivity.this);
				SQLiteDatabase db = dbFrame.getWritableDatabase();
				try {
					ArrayList<String> commandLine = new ArrayList<String>();
					commandLine.add("logcat");
					commandLine.add("-d");
					commandLine.add("-v");
					commandLine.add("time");
					commandLine.add("-s");
					commandLine.add("*:w");
					Process process = Runtime.getRuntime().exec(commandLine.toArray(new String[commandLine.size()]));
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						ContentValues values = new ContentValues();
						values.put("content", line);
						db.insert("FORCHILD000_LOG", null, values);
					}

					ContentValues values = new ContentValues();
					values.put("content", "finish:" + (new Date(System.currentTimeMillis())).toString());
					db.insert("FORCHILD000_LOG", null, values);
					db.close();
					Toast.makeText(SetupActivity.this, "log存储完成", Toast.LENGTH_SHORT).show();

					commandLine.clear();
					commandLine.add("logcat");
					commandLine.add("-c");
					process = Runtime.getRuntime().exec(commandLine.toArray(new String[commandLine.size()]));
					bufferedReader.close();
					process.destroy();
				} catch (IOException e) {
				}
			}
		});
	}

	public void initUI() {
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		Cursor seniorInfo = dbHelper.getSeniorInfo(new String[] { "oid" }, null, null);
		if (seniorInfo.getCount() > 5) {
			addSeniorBtn.setVisibility(View.GONE);
			regSeniorBtn.setVisibility(View.GONE);
		} else if (seniorInfo.getCount() <= 0) {
			seniorsInfoBtn.setVisibility(View.GONE);
		}
	}

	private BroadcastReceiver setupReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("com.forchild.setupactivity.refreshui")) {
				SetupActivity.this.initUI();
			}
		}

	};

	private OnClickListener setupButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.setup_assist_care_btn:
				break;
			case R.id.setup_person_info_btn:
				intent.setClass(SetupActivity.this, PersonInfoActivity.class);
				break;
			case R.id.setup_olders_info_btn:
				intent.setClass(SetupActivity.this, SeniorsDisplayActivity.class);
				break;
			case R.id.setup_emergence_info_btn:
				intent.setClass(SetupActivity.this, EmergenceInfoActivity.class);
				break;
			case R.id.setup_common_info_btn:
				intent.setClass(SetupActivity.this, CommonSetupActivity.class);
				break;
			// case R.id.setup_about_btn:
			// intent.setClass(SetupActivity.this, AboutActivity.class);
			// break;
			case R.id.setup_add_senior_btn:
				intent.setClass(SetupActivity.this, AddSeniorActivity.class);
				break;
			case R.id.setup_register_seniors_btn:
				intent.setClass(SetupActivity.this, RegisterSeniorActivity.class);
				break;
			case R.id.setup_accident_info_btn:
				intent.setClass(SetupActivity.this, SOSHistoryActivity.class);
				break;
			case R.id.setup_sos_info_btn:
				intent.setClass(SetupActivity.this, AccidentHistoryActivity.class);
				break;
			}
			SetupActivity.this.startActivity(intent);
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(setupReceiver);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
