package com.forchild000.surface;

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

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestAddSenior;
import com.forchild.data.RequestSeniorSport;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;
import com.forchild.server.TimeFormat;

public class AddSeniorActivity extends AliveBaseActivity {
	private EditText cardIdEdit, phoneEdit;
	private SetupButton sureBtn;
	protected Preferences preference;
	protected Handler msgHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_senior_activity);

		cardIdEdit = (EditText) findViewById(R.id.addsenior_cardid_edit);
		phoneEdit = (EditText) findViewById(R.id.addsenior_mainchild_phone_edit);
		sureBtn = (SetupButton) findViewById(R.id.addsenior_sure_btn);
		sureBtn.setText(getText(R.string.sure));

		preference = new Preferences(this);
		msgHandler = new Handler(msgCallback);

		sureBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cardId = cardIdEdit.getText().toString().trim();
				String phone = phoneEdit.getText().toString().trim();

				Pattern pattern = Pattern.compile(SetupActivity.PATTERN_CARD_ID);
				Matcher matcher = pattern.matcher(cardId);

				if (!matcher.find() && cardId.length() != 15) {
					Toast.makeText(AddSeniorActivity.this, getText(R.string.setup_input_cardid_error), Toast.LENGTH_SHORT).show();
					return;
				}

				pattern = Pattern.compile(SetupActivity.PATTERN_PHONE);
				matcher = pattern.matcher(phone);

				if (!matcher.find()) {
					Toast.makeText(AddSeniorActivity.this, getText(R.string.setup_input_phone_error), Toast.LENGTH_SHORT).show();
					return;
				}

				if (!ServiceStates.getForchildServiceState(AddSeniorActivity.this)) {
					Intent intent = new Intent(AddSeniorActivity.this, ServiceCore.class);
					AddSeniorActivity.this.startService(intent);
				}

				RequestAddSenior ras = new RequestAddSenior(preference.getSid(), cardId, phone);

				ServiceCore.addNetTask(ras);
			}

		});

	}

	private Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {

			switch (msg.what) {
			case ServiceCore.NETWORK_RESPONSE:

				if (msg.obj instanceof RequestAddSenior) {
					RequestAddSenior rlc = (RequestAddSenior) msg.obj;
					if (rlc.isResponse()) {
					} else {
						switch (rlc.getReason()) {
						case BaseProtocolFrame.REASON_NORMALITY:
							Toast.makeText(AddSeniorActivity.this, getText(R.string.noresponse_unknown_error), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED:
							Toast.makeText(AddSeniorActivity.this, getText(R.string.noresponse_exceed_maxtask), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_NO_RESPONSE:
							Toast.makeText(AddSeniorActivity.this, getText(R.string.noresponse_no_response), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_LOGOUT:
							Toast.makeText(AddSeniorActivity.this, getText(R.string.noresponse_logout), Toast.LENGTH_SHORT).show();
							break;
						}
					}
				}

				break;
			}

			return true;
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
	}

}
