package com.forchild000.surface;

import java.util.Timer;
import java.util.TimerTask;

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
import com.forchild.data.RequestRegisterChild;
import com.forchild.data.RequestSendValidCode;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;

public class ChangePasswordActivity extends AliveBaseActivity {
	private EditText pwEdit, pwAgainEdit, verfEdit;
	private Button sendVerfBtn, sureBtn;
	private Handler btnClickableHandler, msgHandler;
	private Timer btnTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password_activity);

		pwEdit = (EditText) findViewById(R.id.changepw_pasword_edit);
		pwAgainEdit = (EditText) findViewById(R.id.changepw_pasword_again_edit);
		verfEdit = (EditText) findViewById(R.id.changepw_verification_edit);
		sendVerfBtn = (Button) findViewById(R.id.changepw_sendverification_btn);
		sureBtn = (Button) findViewById(R.id.changepw_sure_btn);

		sendVerfBtn.setOnClickListener(changePWButtonListener);
		sureBtn.setOnClickListener(changePWButtonListener);

		btnClickableHandler = new Handler(btnTimerCallback);
		msgHandler = new Handler(msgCallback);
		btnTimer = new Timer();
	}

	private OnClickListener changePWButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (!ServiceStates.getForchildServiceState(ChangePasswordActivity.this)) {
				Intent intent = new Intent(ChangePasswordActivity.this, ServiceCore.class);
				ChangePasswordActivity.this.startService(intent);
			}
			Preferences preference = new Preferences(ChangePasswordActivity.this);

			switch (v.getId()) {
			case R.id.changepw_sendverification_btn:
				btnTimer.schedule(new btnTimerTask(R.id.changepw_verification_edit), 1);
				sendVerfBtn.setClickable(false);
				RequestSendValidCode rsvc = new RequestSendValidCode(preference.getLoginId());
				ServiceCore.addNetTask(rsvc);

				break;
			case R.id.changepw_sure_btn:
				String pw = pwEdit.getText().toString();
				String pwA = pwAgainEdit.getText().toString();

				if (!pw.equals(pwA)) {
					Toast.makeText(ChangePasswordActivity.this, getText(R.string.reguser_password_different_error), Toast.LENGTH_SHORT).show();
					return;
				}

				String verf = verfEdit.getText().toString();

				// TODO
				// Requestm rlc = new RequestLoginChild(phoneNumStr,
				// passwordStr);
				// rlc.addHandler(msgHandler);
				// ServiceCore.addNetTask(rlc);

				finish();
				break;
			default:
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
				case R.id.changepw_sendverification_btn:
					sendVerfBtn.setText(msg.arg1 + getText(R.string.time_delay_text).toString());
					break;
				case R.id.changepw_sure_btn:
					sureBtn.setText(msg.arg1 + getText(R.string.time_delay_text).toString());
					break;
				default:
					break;
				}
			}

			if (msg.what == BaseConfiguration.HANDLER_CLICK_MSG) {
				if (msg.arg1 == BaseConfiguration.ENSURE) {
					switch (msg.arg2) {
					case R.id.changepw_sendverification_btn:
						sendVerfBtn.setClickable(true);
						sendVerfBtn.setText(getText(R.string.regsenior_sendverification_btntitle));
						break;
					case R.id.changepw_sure_btn:
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
