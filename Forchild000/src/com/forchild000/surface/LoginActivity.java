package com.forchild000.surface;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.forchild.data.BaseConfiguration;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestLoginChild;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;

public class LoginActivity extends AliveBaseActivity {
	private EditText phoneNum, password;
	private Button loginBtn;
	private TextView registerBtn, getPasswordBtn;
	private Preferences preference;
	private Handler btnHandler, msgHandler;
	private ProgressDialog mDialog;
	protected Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.Login_activity_lable);

		setContentView(R.layout.login_activity);
		preference = new Preferences(this);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		if (!ServiceStates.getForchildServiceState(this)) {
			Intent intent = new Intent(this, ServiceCore.class);
			this.startService(intent);
		}

		if (preference.getLoginState()) {
			Intent intent = new Intent(this, MainActivity.class);
			this.startActivity(intent);
			this.finish();
		}

		btnHandler = new Handler(btnClickable);
		msgHandler = new Handler(responseProcess);
		

		phoneNum = (EditText) findViewById(R.id.login_input);
		password = (EditText) findViewById(R.id.senior_id);
		loginBtn = (Button) findViewById(R.id.login_btn);

		phoneNum.setInputType(InputType.TYPE_CLASS_PHONE);

		registerBtn = (TextView) findViewById(R.id.login_register);
		getPasswordBtn = (TextView) findViewById(R.id.login_obtain_password);

		if (preference.getLoginId() != null) {
			phoneNum.setText(preference.getLoginId());
		}

		if (preference.getPassword() != null) {
			password.setText(preference.getPassword());
		}

		loginBtn.setOnClickListener(loginClick);
		registerBtn.setOnClickListener(loginClick);
		getPasswordBtn.setOnClickListener(loginClick);

		mDialog = new ProgressDialog(this);
		mDialog.setTitle(R.string.login_dialog_netwait_title);
		mDialog.setMessage(getText(R.string.login_dialog_netwait_content));

	}

	private OnClickListener loginClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.login_btn:
				timer = new Timer();
				timer.schedule(new timerTask(), 1);
				loginBtn.setClickable(true);
				String phoneNumStr = phoneNum.getText().toString();
				String passwordStr = password.getText().toString();

				if (!ServiceStates.getForchildServiceState(LoginActivity.this)) {
					Intent intent = new Intent(LoginActivity.this, ServiceCore.class);
					LoginActivity.this.startService(intent);
				}

				RequestLoginChild rlc = new RequestLoginChild(phoneNumStr, passwordStr);
				rlc.addHandler(msgHandler);
				ServiceCore.addNetTask(rlc);

				mDialog.show();
				// Intent loginIntent = new Intent();
				// loginIntent.setAction("com.forolder.sendHttpsBeans");
				// loginIntent.putExtra("bean", rlc);
				// LoginActivity.this.sendBroadcast(loginIntent);
				// preference.setPhoneNumber(phoneNumStr);
				// preference.setPassword(passwordStr);
				break;
			case R.id.login_register:
				Intent regIntent = new Intent(LoginActivity.this, RegisterUserActivity.class);
				regIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				LoginActivity.this.startActivity(regIntent);
				break;
			case R.id.login_obtain_password:
				Intent changepwIntent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
				LoginActivity.this.startActivity(changepwIntent);
				break;
			}

		}

	};

	private Handler.Callback responseProcess = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case ServiceCore.NETWORK_RESPONSE:
				if (msg.obj instanceof RequestLoginChild) {
					RequestLoginChild rlc = (RequestLoginChild) msg.obj;
					Log.e("LoginActivity.responseProcess", "get response, type is RequestLoginChild, req==" + rlc.getReq());
					loginBtn.setClickable(false);
					if (mDialog.isShowing()) {
						mDialog.cancel();
					}
					if (rlc.isResponse()) {
						switch (rlc.getReq()) {
						case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							LoginActivity.this.startActivity(intent);
							if (mDialog != null && mDialog.isShowing()) {
								mDialog.dismiss();
							}
							finish();
							break;
						}
					} else {
						switch (rlc.getReason()) {
						case BaseProtocolFrame.REASON_NORMALITY:
							Toast.makeText(LoginActivity.this, getText(R.string.noresponse_unknown_error), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED:
							Toast.makeText(LoginActivity.this, getText(R.string.noresponse_exceed_maxtask), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_NO_RESPONSE:
							Toast.makeText(LoginActivity.this, getText(R.string.noresponse_no_response), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_LOGOUT:
							Toast.makeText(LoginActivity.this, getText(R.string.noresponse_logout), Toast.LENGTH_SHORT).show();
							break;
						}
					}

				}
				break;
			}

			return true;
		}
	};

	private Handler.Callback btnClickable = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == BaseConfiguration.HANDLER_CLICK_COUNT) {
				loginBtn.setText(msg.arg1 + getText(R.string.time_delay_text).toString());
			}

			if (msg.what == BaseConfiguration.HANDLER_CLICK_MSG) {
				if (msg.arg1 == 1) {
					loginBtn.setClickable(true);
					loginBtn.setText(getText(R.string.login_botton));
				}
			}
			return true;
		}
	};

	private class timerTask extends TimerTask {
		@Override
		public void run() {
			for (int i = 60; i > -1; --i) {
				Message msg = btnHandler.obtainMessage();
				msg.what = BaseConfiguration.HANDLER_CLICK_COUNT;
				msg.arg1 = i;
				msg.sendToTarget();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Message msg = btnHandler.obtainMessage();
			msg.what = BaseConfiguration.HANDLER_CLICK_MSG;
			msg.arg1 = 1;
			msg.sendToTarget();
		}
	}

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
	}

}
