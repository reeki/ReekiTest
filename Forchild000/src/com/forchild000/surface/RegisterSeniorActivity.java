package com.forchild000.surface;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.forchild.data.BaseConfiguration;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestLoginChild;
import com.forchild.data.RequestRegisterSenior;
import com.forchild.data.RequestSendValidCode;
import com.forchild.data.SeniorInfoComplete;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;

public class RegisterSeniorActivity extends AliveBaseActivity {
	private EditText nameEdit, nickEdit, heightEdit, weightEdit, addressEdit, cardIdEdit, allergicEdit, medicalEdit, codeEdit, verfEdit, phoneEdit;
	private Spinner bloodSpinner, sexSpinner;
	private DatePicker birthDatePicker;
	private LinearLayout phoneLayout;
	private ArrayAdapter<String> bloodAdapter, sexAdapter;
	private Button sureBtn, sendVerfBtn;
	private Handler btnHandler, msgHandler;
	protected Timer btnTimer;
	protected Calendar birth;
	protected SeniorInfoComplete seniorInfo;
	protected String blood;
	protected int sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_sensor_activity);

//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		codeEdit = (EditText) findViewById(R.id.regsenior_code_edit);
		nameEdit = (EditText) findViewById(R.id.regsenior_username_edit);
		nickEdit = (EditText) findViewById(R.id.regsenior_nick_edit);
		heightEdit = (EditText) findViewById(R.id.regsenior_height_edit);
		weightEdit = (EditText) findViewById(R.id.regsenior_weight_edit);
		addressEdit = (EditText) findViewById(R.id.regsenior_address_edit);
		cardIdEdit = (EditText) findViewById(R.id.regsenior_cardid_edit);
		allergicEdit = (EditText) findViewById(R.id.regsenior_allergic_edit);
		medicalEdit = (EditText) findViewById(R.id.regsenior_medical_edit);
		verfEdit = (EditText) findViewById(R.id.regsenior_verification_edit);
		phoneEdit = (EditText) findViewById(R.id.regsenior_phone_edit);

		phoneEdit.setVisibility(View.VISIBLE);

		phoneLayout = (LinearLayout) findViewById(R.id.regsenior_verification_layout);
		phoneLayout.setVisibility(View.VISIBLE);

		bloodSpinner = (Spinner) findViewById(R.id.regsenior_blood_spinner);
		sexSpinner = (Spinner) findViewById(R.id.regsenior_sex_spinner);
		bloodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sexAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		bloodSpinner.setAdapter(bloodAdapter);
		bloodSpinner.setOnItemSelectedListener(seniorsInfoModSpinnerListener);
		sexSpinner.setAdapter(sexAdapter);
		sexSpinner.setOnItemSelectedListener(seniorsInfoModSpinnerListener);

		bloodAdapter.add(getText(R.string.setup_blood_type_a).toString());
		bloodAdapter.add(getText(R.string.setup_blood_type_b).toString());
		bloodAdapter.add(getText(R.string.setup_blood_type_o).toString());
		bloodAdapter.add(getText(R.string.setup_blood_type_ab).toString());

		sexAdapter.add(getText(R.string.personinfo_sex_male).toString());
		sexAdapter.add(getText(R.string.personinfo_sex_female).toString());

		sureBtn = (Button) findViewById(R.id.regsenior_sure_btn);
		sendVerfBtn = (Button) findViewById(R.id.regsenior_sendverification_btn);
		sureBtn.setOnClickListener(seniorsInfoModButtonListener);
		sendVerfBtn.setOnClickListener(seniorsInfoModButtonListener);

		btnHandler = new Handler(btnClickable);
		msgHandler = new Handler(msgCallback);
		btnTimer = new Timer();

		birthDatePicker = (DatePicker) findViewById(R.id.regsenior_birth_datepicker);

		birth = Calendar.getInstance();
		birth.setTimeInMillis(0);
		birthDatePicker.updateDate(1970, 0, 0);

	}

	private Handler.Callback btnClickable = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == BaseConfiguration.HANDLER_CLICK_COUNT) {
				sendVerfBtn.setText(msg.arg1 + getText(R.string.time_second).toString());
			}

			if (msg.what == BaseConfiguration.HANDLER_CLICK_MSG) {
				if (msg.arg1 == 1) {
					sendVerfBtn.setClickable(true);
					sendVerfBtn.setText(getText(R.string.login_botton));
				}
			}
			return true;
		}
	};

	private class btnTimerTask extends TimerTask {
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

	private OnItemSelectedListener seniorsInfoModSpinnerListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			switch (arg0.getId()) {
			case R.id.regsenior_blood_spinner:
				switch (arg2) {
				case 0:
					blood = "A";
					break;
				case 1:
					blood = "B";
					break;
				case 2:
					blood = "O";
					break;
				case 3:
					blood = "AB";
					break;
				default:
					break;
				}
				break;
			case R.id.regsenior_sex_spinner:
				sex = ++arg2;
				break;
			default:
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	};

	private OnClickListener seniorsInfoModButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (!ServiceStates.getForchildServiceState(RegisterSeniorActivity.this)) {
				Intent intent = new Intent(RegisterSeniorActivity.this, ServiceCore.class);
				RegisterSeniorActivity.this.startService(intent);
			}

			String phone = phoneEdit.getText().toString().trim();
			Pattern pattern = Pattern.compile(SetupActivity.PATTERN_PHONE);
			Matcher matcher = pattern.matcher(phone);
			if (!matcher.find()) {
				Toast.makeText(RegisterSeniorActivity.this, getText(R.string.reguser_phone_error), Toast.LENGTH_SHORT).show();
				return;
			}

			switch (v.getId()) {
			case R.id.regsenior_sure_btn:
				String name = nameEdit.getText().toString().trim();
				String nick = nickEdit.getText().toString().trim();
				String address = addressEdit.getText().toString().trim();
				String allergic = allergicEdit.getText().toString().trim();
				String medical = medicalEdit.getText().toString().trim();
				String code = codeEdit.getText().toString().trim();
				String cardId = cardIdEdit.getText().toString().trim();
				String valid = verfEdit.getText().toString().trim();

				pattern = Pattern.compile(SetupActivity.PATTERN_NAME);
				matcher = pattern.matcher(name);

				// name 是否合法及修改的判断
				// if (matcher.find()) {
				if (name.length() > 8) {
					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_name_long_error), Toast.LENGTH_SHORT).show();
					return;
				}

				if (name.length() == 0) {
					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_name_short_error), Toast.LENGTH_SHORT).show();
					return;
				}
				// } else {
				// Toast.makeText(RegisterSeniorActivity.this,
				// getText(R.string.input_name_format_error),
				// Toast.LENGTH_SHORT).show();
				// return;
				// }

				// nick 是否合法的判断
				if (nick.length() > 16) {
					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_nick_long_error), Toast.LENGTH_SHORT).show();
					return;
				}

				// address 是否合法的判断
				if (address.length() > 128) {
					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_address_long_error), Toast.LENGTH_SHORT).show();
					return;
				}

				// allergic 是否合法的判断
				if (allergic.length() > 64) {
					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_allergic_long_error), Toast.LENGTH_SHORT).show();
					return;
				}

				// medical 是否合法的判断
				if (medical.length() > 128) {
					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_medical_long_error), Toast.LENGTH_SHORT).show();
					return;
				}

				// height 和 weight 是否合法及修改的判断
				String heightStr = heightEdit.getText().toString().trim();
				String weightStr = weightEdit.getText().toString().trim();
				int height = 0;
				int weight = 0;
				try {
					height = Integer.parseInt(heightStr);
				} catch (NumberFormatException e) {
					e.printStackTrace();
//					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_height_error), Toast.LENGTH_SHORT).show();
//					return;
				}

				try {
					weight = Integer.parseInt(weightStr);
				} catch (NumberFormatException e) {
					e.printStackTrace();
//					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_weight_error), Toast.LENGTH_SHORT).show();
//					return;
				}

				// blood 是否合法的判断
//				if (blood == null) {
//					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_blood_error), Toast.LENGTH_SHORT).show();
//					return;
//				}

				// sex 是否合法及修改的判断
				if (sex != 1 && sex != 2) {
//					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_sex_error), Toast.LENGTH_SHORT).show();
//					return;
					sex = 1;
				}

				birth.set(Calendar.YEAR, birthDatePicker.getYear());
				birth.set(Calendar.MONTH, birthDatePicker.getMonth());
				birth.set(Calendar.DATE, birthDatePicker.getDayOfMonth());

//				pattern = Pattern.compile(SetupActivity.PATTERN_CARD_ID);
//				matcher = pattern.matcher(cardId);
//
//				if (!matcher.find() && cardId.length() != 15) {
//					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_card_error), Toast.LENGTH_SHORT).show();
//					return;
//				}
				
				if(cardId.length() == 0) {
					cardId = null;
				}

				if (code.length() != 6) {
					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_code_error), Toast.LENGTH_SHORT).show();
					return;
				} else if (!Pattern.matches(SetupActivity.PATTERN_ONLY_NUMBERORCHARTS, code)) {
					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_code_type_error), Toast.LENGTH_SHORT).show();
				}

				if (valid.length() != 6) {
					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_valid_error), Toast.LENGTH_SHORT).show();
					return;
				}

				// 杨峥登陆码是否存在
				DatabaseHelper dbHelper = new DatabaseHelper(RegisterSeniorActivity.this);
				Cursor seniorOidCursor = dbHelper.getSeniorInfo(new String[] { "oid" }, "code = ?", new String[] { code });
				if (seniorOidCursor.getCount() > 0) {
					Toast.makeText(RegisterSeniorActivity.this, getText(R.string.input_code_repeated_error), Toast.LENGTH_SHORT).show();
					return;
				}

				Preferences preference = new Preferences(RegisterSeniorActivity.this);
				RequestRegisterSenior rrc = new RequestRegisterSenior(preference.getSid(), code, name, nick, cardId, allergic, medical, blood, phone,
						valid, address, sex, height, weight, birth.getTimeInMillis());
				rrc.addHandler(msgHandler);
				ServiceCore.addNetTask(rrc);

				finish();
				break;
			case R.id.regsenior_sendverification_btn:
				sendVerfBtn.setClickable(false);
				btnTimer.schedule(new btnTimerTask(), 1);

				RequestSendValidCode rsvc = new RequestSendValidCode(phone);
				rsvc.addHandler(msgHandler);
				ServiceCore.addNetTask(rsvc);

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
							Toast.makeText(RegisterSeniorActivity.this, getText(R.string.noresponse_unknown_error), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED:
							Toast.makeText(RegisterSeniorActivity.this, getText(R.string.noresponse_exceed_maxtask), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_NO_RESPONSE:
							Toast.makeText(RegisterSeniorActivity.this, getText(R.string.noresponse_no_response), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_LOGOUT:
							Toast.makeText(RegisterSeniorActivity.this, getText(R.string.noresponse_logout), Toast.LENGTH_SHORT).show();
							break;
						}
					}
				}

				if (msg.obj instanceof RequestRegisterSenior) {
					RequestRegisterSenior rrs = (RequestRegisterSenior) msg.obj;
					if (rrs.isResponse()) {
					} else {
						switch (rrs.getReason()) {
						case BaseProtocolFrame.REASON_NORMALITY:
							Toast.makeText(RegisterSeniorActivity.this, getText(R.string.noresponse_unknown_error), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED:
							Toast.makeText(RegisterSeniorActivity.this, getText(R.string.noresponse_exceed_maxtask), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_NO_RESPONSE:
							Toast.makeText(RegisterSeniorActivity.this, getText(R.string.noresponse_no_response), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_LOGOUT:
							Toast.makeText(RegisterSeniorActivity.this, getText(R.string.noresponse_logout), Toast.LENGTH_SHORT).show();
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
