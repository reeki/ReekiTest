package com.forchild000.surface;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.forchild.data.BaseConfiguration;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestModifySeniorPhone;
import com.forchild.data.RequestRegisterChild;
import com.forchild.data.RequestSendValidCode;
import com.forchild.data.SeniorInfoComplete;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;

public class ChangeSeniorPhoneEditActivity extends AliveBaseActivity {
	private EditText phoneEdit, validEdit;
	private Button sendVerfBtn, sureBtn;
	protected SeniorInfoComplete sic;

	private Handler btnClickableHandler, msgHandler;
	private Timer btnTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_phone_edit_activity);

		phoneEdit = (EditText) findViewById(R.id.changephone_phone_edit);
		validEdit = (EditText) findViewById(R.id.changephone_verification_edit);
		sendVerfBtn = (Button) findViewById(R.id.changephone_sendverification_btn);
		sureBtn = (Button) findViewById(R.id.changephone_sure_btn);

		sendVerfBtn.setOnClickListener(changePhoneButtonListener);
		sureBtn.setOnClickListener(changePhoneButtonListener);

		Intent startIntent = getIntent();
		Serializable bean = startIntent.getSerializableExtra("content");
		if (bean instanceof SeniorInfoComplete) {
			sic = (SeniorInfoComplete) bean;
			if (sic.getPhone() != null) {
				phoneEdit.setText(sic.getPhone());
			}
		}
		btnClickableHandler = new Handler(btnTimerCallback);
		msgHandler = new Handler(msgCallback);
		btnTimer = new Timer();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private OnClickListener changePhoneButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String phone = phoneEdit.getText().toString().trim();
			Pattern pattern = Pattern.compile(SetupActivity.PATTERN_PHONE);
			Matcher matcher = pattern.matcher(phone);
			if (!matcher.find()) {
				Toast.makeText(ChangeSeniorPhoneEditActivity.this, getText(R.string.reguser_phone_error), Toast.LENGTH_SHORT).show();
				return;
			}

			if (!ServiceStates.getForchildServiceState(ChangeSeniorPhoneEditActivity.this)) {
				Intent intent = new Intent(ChangeSeniorPhoneEditActivity.this, ServiceCore.class);
				ChangeSeniorPhoneEditActivity.this.startService(intent);
			}

			switch (v.getId()) {
			case R.id.changephone_sendverification_btn:
				btnTimer.schedule(new btnTimerTask(R.id.changephone_sendverification_btn), 1);
				sendVerfBtn.setClickable(false);
				RequestSendValidCode rsvc = new RequestSendValidCode(phone);
				rsvc.addHandler(msgHandler);
				ServiceCore.addNetTask(rsvc);

				break;
			case R.id.changephone_sure_btn:
				
				String valid = validEdit.getText().toString().trim();

				Preferences preference = new Preferences(ChangeSeniorPhoneEditActivity.this);
				if (sic != null && sic.getCode() != null && sic.getCode().length() > 0) {
					RequestModifySeniorPhone rmsp = new RequestModifySeniorPhone(preference.getSid(), sic.getOid(), sic.getPhone(), sic.getCode(),
							phone, valid);
					rmsp.addHandler(msgHandler);
					ServiceCore.addNetTask(rmsp);
				} else {
					finish();
				}
				break;
			}
		}
	};

	private Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case ServiceCore.NETWORK_RESPONSE:
				
				break;
			}

			return true;
		}

	};

	private Handler.Callback btnTimerCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == BaseConfiguration.HANDLER_CLICK_COUNT) {
				switch (msg.arg2) {
				case R.id.changephone_sendverification_btn:
					sendVerfBtn.setText(msg.arg1 + getText(R.string.time_delay_text).toString());
					break;
				case R.id.changephone_sure_btn:
					sureBtn.setText(msg.arg1 + getText(R.string.time_delay_text).toString());
					break;
				default:
					break;
				}
			}

			if (msg.what == BaseConfiguration.HANDLER_CLICK_MSG) {
				if (msg.arg1 == BaseConfiguration.ENSURE) {
					switch (msg.arg2) {
					case R.id.changephone_sendverification_btn:
						sendVerfBtn.setClickable(true);
						sendVerfBtn.setText(getText(R.string.regsenior_sendverification_btntitle));
						break;
					case R.id.changephone_sure_btn:
						sureBtn.setClickable(true);
						sureBtn.setText(R.string.reguser_register_btn_text);
						break;
					default:
						break;
					}
				}
			}
			return true;
		}
	};

	private class btnTimerTask extends TimerTask {
		private int resId;

		public btnTimerTask(int resId) {
			this.resId = resId;
		}

		@Override
		public void run() {
			for (int i = 60; i > -1; --i) {
				Message msg = btnClickableHandler.obtainMessage();
				msg.what = BaseConfiguration.HANDLER_CLICK_COUNT;
				msg.arg1 = i;
				msg.arg2 = resId;
				msg.sendToTarget();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			Message msg = btnClickableHandler.obtainMessage();
			msg.what = BaseConfiguration.HANDLER_CLICK_MSG;
			msg.arg1 = BaseConfiguration.ENSURE; // È·¶¨¿ªÆô
			msg.arg2 = resId;
			msg.sendToTarget();
		}
	};
}
