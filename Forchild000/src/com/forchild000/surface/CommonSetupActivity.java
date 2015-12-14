package com.forchild000.surface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.forchild.data.RequestLogoutChild;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;

public class CommonSetupActivity extends AliveBaseActivity {
	private SetupButton passwordChangeBtn, seniorPhoneChangeBtn, clearBtn, logoutBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_setup_activity);

		passwordChangeBtn = (SetupButton) findViewById(R.id.common_password_change_btn);
		seniorPhoneChangeBtn = (SetupButton) findViewById(R.id.common_seniorphone_change_btn);
		clearBtn = (SetupButton) findViewById(R.id.common_clean_btn);
		logoutBtn = (SetupButton) findViewById(R.id.common_logout_btn);

		passwordChangeBtn.setFrame(SetupButton.BOTTOM_FRAME);

		clearBtn.setVisibility(View.GONE);

		passwordChangeBtn.setText(getText(R.string.common_password_change_button_text));
		seniorPhoneChangeBtn.setText(getText(R.string.common_seniorphone_change_button_text));
		clearBtn.setText(getText(R.string.common_clear_button_text));
		logoutBtn.setText(getText(R.string.logout));

		passwordChangeBtn.setOnClickListener(commonSetupButtonListener);
		seniorPhoneChangeBtn.setOnClickListener(commonSetupButtonListener);
		clearBtn.setOnClickListener(commonSetupButtonListener);
		logoutBtn.setOnClickListener(commonSetupButtonListener);

	}

	private OnClickListener commonSetupButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();

			switch (v.getId()) {
			case R.id.common_password_change_btn:
				intent.setClass(CommonSetupActivity.this, ChangePasswordActivity.class);
				CommonSetupActivity.this.startActivity(intent);
				break;
			case R.id.common_seniorphone_change_btn:
				intent.setClass(CommonSetupActivity.this, ChangeSeniorPhoneActivity.class);
				CommonSetupActivity.this.startActivity(intent);
				break;
			case R.id.common_clean_btn:
				intent.setClass(CommonSetupActivity.this, WarningActivity.class);
				intent.putExtra("type", WarningActivity.WARNING_TYPE_RESTORE_FACTORY_SETTINGS);
				CommonSetupActivity.this.startActivityForResult(intent, WarningActivity.WARNING_TYPE_RESTORE_FACTORY_SETTINGS);
				break;
			case R.id.common_logout_btn:
				intent.setClass(CommonSetupActivity.this, WarningActivity.class);
				intent.putExtra("type", WarningActivity.WARNING_TYPE_LOGOUT);
				CommonSetupActivity.this.startActivityForResult(intent, WarningActivity.WARNING_TYPE_LOGOUT);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case WarningActivity.WARNING_TYPE_RESTORE_FACTORY_SETTINGS:
				if (!ServiceStates.getForchildServiceState(CommonSetupActivity.this)) {
					Intent serviceIntent = new Intent(CommonSetupActivity.this, ServiceCore.class);
					CommonSetupActivity.this.startService(serviceIntent);
				}
				DatabaseHelper dbHelper = new DatabaseHelper(this);
				dbHelper.deleteAll();
				Preferences preference = new Preferences(CommonSetupActivity.this);
				ServiceCore.addNetTask(new RequestLogoutChild(preference.getSid()));
				preference.clear();
				Intent logoutIntent = new Intent();
				logoutIntent.setAction("com.forolder.logout.activity");
				CommonSetupActivity.this.sendBroadcast(logoutIntent);

				Intent intent = new Intent(this, LoginActivity.class);
				this.startActivity(intent);
				break;
			case WarningActivity.WARNING_TYPE_LOGOUT:
				if (!ServiceStates.getForchildServiceState(CommonSetupActivity.this)) {
					Intent serviceIntent = new Intent(CommonSetupActivity.this, ServiceCore.class);
					CommonSetupActivity.this.startService(serviceIntent);
				}

				Intent logout = new Intent();
				logout.setAction("com.forchild.logout.activity");
				CommonSetupActivity.this.sendBroadcast(logout);
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
