package com.forchild000.surface;

import java.util.Timer;
import java.util.TimerTask;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.forchild.data.BaseConfiguration;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestRegisterChild;
import com.forchild.data.RequestSendValidCode;
import com.forchild.server.ServiceStates;
import com.forchild.server.StatesIntent;

public class RegisterUserActivity extends AliveBaseActivity {
	private EditText phoneEdit, verificationEdit, passwordEdit, pwAgainEdit, cardIdEdit, nickEdit, nameEdit;
	private Button sendVerifBtn, sureBtn;
	private Handler btnClickableHandler, msgHandler;
	private Timer btnTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_user_activity);

		phoneEdit = (EditText) findViewById(R.id.reguser_phone_edit);
		verificationEdit = (EditText) findViewById(R.id.reguser_verification_edit);
		passwordEdit = (EditText) findViewById(R.id.reguser_pasword_edit);
		pwAgainEdit = (EditText) findViewById(R.id.reguser_pasword_again_edit);
		cardIdEdit = (EditText) findViewById(R.id.reguser_cardid_edit);
		nickEdit = (EditText) findViewById(R.id.reguser_nick_edit);
		nameEdit = (EditText) findViewById(R.id.reguser_name_edit);
		sendVerifBtn = (Button) findViewById(R.id.reguser_sendverification_btn);
		sureBtn = (Button) findViewById(R.id.reguser_sure_btn);

		// cardIdEdit.setVisibility(View.INVISIBLE);
		nickEdit.setVisibility(View.INVISIBLE);

		passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		pwAgainEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		sendVerifBtn.setOnClickListener(registerBtnListener);
		sureBtn.setOnClickListener(registerBtnListener);

		btnClickableHandler = new Handler(btnTimerCallback);
		msgHandler = new Handler(msgCallback);
		btnTimer = new Timer();

	}

	private OnClickListener registerBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!ServiceStates.getForchildServiceState(RegisterUserActivity.this)) {
				Intent intent = new Intent(RegisterUserActivity.this, ServiceCore.class);
				RegisterUserActivity.this.startService(intent);
			}

			String phoneText = phoneEdit.getText().toString();
			if (phoneText.length() != 11) {
				Toast.makeText(RegisterUserActivity.this, getText(R.string.reguser_phone_error), Toast.LENGTH_SHORT).show();
				return;
			}

			switch (v.getId()) {
			case R.id.reguser_sendverification_btn:
				btnTimer.schedule(new btnTimerTask(R.id.reguser_sendverification_btn), 1);
				sendVerifBtn.setClickable(false);
				RequestSendValidCode rsvc = new RequestSendValidCode(phoneText);
				rsvc.addHandler(msgHandler);
				ServiceCore.addNetTask(rsvc);

				break;
			case R.id.reguser_sure_btn:

				String verificationText = verificationEdit.getText().toString();
				String pwText = passwordEdit.getText().toString();
				String pwAgainText = pwAgainEdit.getText().toString();
				String cardIdText = cardIdEdit.getText().toString();
				// String nickText = nickEdit.getText().toString();
				String nameText = nameEdit.getText().toString();

				if (verificationText.length() != 6) {
					Toast.makeText(RegisterUserActivity.this, getText(R.string.reguser_verification_error), Toast.LENGTH_SHORT).show();
					break;
				}

				if (pwText.length() < 5 || pwText.length() > 16) {
					Toast.makeText(RegisterUserActivity.this, getText(R.string.reguser_password_length_error), Toast.LENGTH_SHORT).show();
					break;
				}

				if (!pwText.equals(pwAgainText)) {
					Toast.makeText(RegisterUserActivity.this, getText(R.string.reguser_password_different_error), Toast.LENGTH_SHORT).show();
					break;
				}

				if (cardIdText.length() != 18 && cardIdText.length() != 15) {
					Toast.makeText(RegisterUserActivity.this, getText(R.string.reguser_cardId_short_error), Toast.LENGTH_SHORT).show();
					break;
				}

				// if (nickText.length() > 16) {
				// Toast.makeText(RegisterUserActivity.this,
				// getText(R.string.reguser_nick_long_error),
				// Toast.LENGTH_SHORT).show();
				// break;
				// }

				if (nameText.length() < 2) {
					Toast.makeText(RegisterUserActivity.this, getText(R.string.reguser_name_error), Toast.LENGTH_SHORT).show();
					break;
				}
				sureBtn.setClickable(false);
				RequestRegisterChild rrc = new RequestRegisterChild(phoneText, pwText, verificationText, nameText, cardIdText);
				rrc.addHandler(msgHandler);
				ServiceCore.addNetTask(rrc);
				break;
			}
		}

	};

	private Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case ServiceCore.NETWORK_RESPONSE:
				if (msg.obj instanceof RequestSendValidCode) {
					RequestSendValidCode rlc = (RequestSendValidCode) msg.obj;
					if (rlc.isResponse()) {
						switch (rlc.getReq()) {
						case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
							break;
						}
					} else {
						switch (rlc.getReason()) {
						case BaseProtocolFrame.REASON_NORMALITY:
							Toast.makeText(RegisterUserActivity.this, getText(R.string.noresponse_unknown_error), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED:
							Toast.makeText(RegisterUserActivity.this, getText(R.string.noresponse_exceed_maxtask), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_NO_RESPONSE:
							Toast.makeText(RegisterUserActivity.this, getText(R.string.noresponse_no_response), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_LOGOUT:
							Toast.makeText(RegisterUserActivity.this, getText(R.string.noresponse_logout), Toast.LENGTH_SHORT).show();
							break;
						}
					}

				}
				
				if (msg.obj instanceof RequestRegisterChild) {
					RequestRegisterChild rrc = (RequestRegisterChild) msg.obj;
					sureBtn.setClickable(true);
					if (rrc.isResponse()) {
						switch (rrc.getReq()) {
						case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
							// Intent intent = new
							// Intent(RegisterUserActivity.this,
							// MainActivity.class);
							// RegisterUserActivity.this.startActivity(intent);
							finish();
							break;
						}
					} else {
						switch (rrc.getReason()) {
						case BaseProtocolFrame.REASON_NORMALITY:
							Toast.makeText(RegisterUserActivity.this, getText(R.string.noresponse_unknown_error), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED:
							Toast.makeText(RegisterUserActivity.this, getText(R.string.noresponse_exceed_maxtask), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_NO_RESPONSE:
							Toast.makeText(RegisterUserActivity.this, getText(R.string.noresponse_no_response), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_LOGOUT:
							Toast.makeText(RegisterUserActivity.this, getText(R.string.noresponse_logout), Toast.LENGTH_SHORT).show();
							break;
						}
					}
				}

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
				case R.id.reguser_sendverification_btn:
					sendVerifBtn.setText(msg.arg1 + getText(R.string.time_delay_text).toString());
					break;
				case R.id.reguser_sure_btn:
					sureBtn.setText(msg.arg1 + getText(R.string.time_delay_text).toString());
					break;
				default:
					break;
				}
			}

			if (msg.what == BaseConfiguration.HANDLER_CLICK_MSG) {
				if (msg.arg1 == BaseConfiguration.ENSURE) {
					switch (msg.arg2) {
					case R.id.reguser_sendverification_btn:
						sendVerifBtn.setClickable(true);
						sendVerifBtn.setText(getText(R.string.regsenior_sendverification_btntitle));
						break;
					case R.id.reguser_sure_btn:
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

	@Override
	protected void onPause() {
		super.onPause();

		StatesIntent.sendCloseState(this, ServiceCore.ACTIVITY_TYPE_REGISTER_USER);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatesIntent.sendAliveState(this, ServiceCore.ACTIVITY_TYPE_REGISTER_USER);
	}

}
