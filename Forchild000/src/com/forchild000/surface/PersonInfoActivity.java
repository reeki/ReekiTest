package com.forchild000.surface;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.DateInfo;
import com.forchild.data.RequestModifyChildInformation;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;
import com.forchild.server.StatesIntent;

public class PersonInfoActivity extends AliveBaseActivity {
	private ContentButton nameBtn, nickBtn, birthBtn, sexBtn, heightBtn, weightBtn, addressBtn, allergyBtn, medicalBtn, bloodBtn;
	private SetupButton medCardBtn;
	protected Preferences preference;
	protected String name, nick, address, allergy, medical, blood;
	protected int height, weight, sex;
	protected long birth;
	protected DateInfo birthDate;
	protected boolean updateOption = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_info_activity);

		preference = new Preferences(this);

		nameBtn = (ContentButton) findViewById(R.id.person_name_btn);
		nickBtn = (ContentButton) findViewById(R.id.person_nick_btn);
		birthBtn = (ContentButton) findViewById(R.id.person_birth_btn);
		sexBtn = (ContentButton) findViewById(R.id.person_sex_btn);
		heightBtn = (ContentButton) findViewById(R.id.person_height_btn);
		weightBtn = (ContentButton) findViewById(R.id.person_weight_btn);
		addressBtn = (ContentButton) findViewById(R.id.person_address_btn);
		allergyBtn = (ContentButton) findViewById(R.id.person_allergy_btn);
		medicalBtn = (ContentButton) findViewById(R.id.person_medical_btn);
		bloodBtn = (ContentButton) findViewById(R.id.person_blood_btn);
		medCardBtn = (SetupButton) findViewById(R.id.person_med_card_btn);

		nameBtn.setFrame(ContentButton.BOTTOM_FRAME);
		nickBtn.setFrame(ContentButton.BOTTOM_FRAME);
		birthBtn.setFrame(ContentButton.BOTTOM_FRAME);
		sexBtn.setFrame(ContentButton.BOTTOM_FRAME);
		heightBtn.setFrame(ContentButton.BOTTOM_FRAME);
		weightBtn.setFrame(ContentButton.BOTTOM_FRAME);
		allergyBtn.setFrame(ContentButton.BOTTOM_FRAME);
		medicalBtn.setFrame(ContentButton.BOTTOM_FRAME);

		nameBtn.setTitle(getText(R.string.personinfo_name_title));
		nickBtn.setTitle(getText(R.string.personinfo_nick_title));
		birthBtn.setTitle(getText(R.string.personinfo_birth_title));
		sexBtn.setTitle(getText(R.string.personinfo_sex_title));
		heightBtn.setTitle(getText(R.string.personinfo_height_title));
		weightBtn.setTitle(getText(R.string.personinfo_weight_title));
		addressBtn.setTitle(getText(R.string.personinfo_address_title));
		allergyBtn.setTitle(getText(R.string.personinfo_allergy_title));
		medicalBtn.setTitle(getText(R.string.personinfo_medical_title));
		bloodBtn.setTitle(getText(R.string.personinfo_blood_title));

		medCardBtn.setText(getText(R.string.personinfo_emergence_card_title));

		nameBtn.setOnClickListener(personInfoButtonListener);
		nickBtn.setOnClickListener(personInfoButtonListener);
		birthBtn.setOnClickListener(personInfoButtonListener);
		sexBtn.setOnClickListener(personInfoButtonListener);
		heightBtn.setOnClickListener(personInfoButtonListener);
		weightBtn.setOnClickListener(personInfoButtonListener);
		addressBtn.setOnClickListener(personInfoButtonListener);
		allergyBtn.setOnClickListener(personInfoButtonListener);
		medicalBtn.setOnClickListener(personInfoButtonListener);
		bloodBtn.setOnClickListener(personInfoButtonListener);
		medCardBtn.setOnClickListener(personInfoButtonListener);
		this.registerReceiver(personInfoReceiver, new IntentFilter("com.forchild.personinfo.refreshui"));

		refreshUI();
	}

	protected void refreshUI() {
		name = preference.getUserName();

		nick = preference.getNick();
		birth = preference.getBirth();
		sex = preference.getSex();
		height = preference.getHeight();
		weight = preference.getWeight();
		address = preference.getAddress();
		allergy = preference.getAllergic();
		medical = preference.getMedicalInfo();
		blood = preference.getBlood();

		if (blood != null) {
			blood = preference.getBlood().toUpperCase();
		}
		nameBtn.setText(name);
		nickBtn.setText(nick);
		String birthText = getText(R.string.no_info).toString();
		birthDate = new DateInfo(birth);

		if (birth != 0) {
			birthText = birthDate.getYYYYMMDD();
		}
		birthBtn.setText(birthText);
		switch (sex) {
		case 1:
			sexBtn.setText(getText(R.string.personinfo_sex_male));
			break;
		case 2:
			sexBtn.setText(getText(R.string.personinfo_sex_female));
			break;
		default:
			break;
		}

		if (height != BaseProtocolFrame.INT_INITIATION) {
			heightBtn.setText(height + "cm");
		}

		if (weight != BaseProtocolFrame.INT_INITIATION) {
			weightBtn.setText(weight + "kg");
		}

		addressBtn.setText(address);
		allergyBtn.setText(allergy);
		medicalBtn.setText(medical);
		bloodBtn.setText(blood);
	}

	private BroadcastReceiver personInfoReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			PersonInfoActivity.this.refreshUI();
		}

	};

	private OnClickListener personInfoButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.person_name_btn:
				// intent.setClass(PersonInfoActivity.this,
				// SingleEditSetup.class);
				// intent.putExtra("type", SetupActivity.PERSON_SETUP_NAME);
				// PersonInfoActivity.this.startActivityForResult(intent,
				// SetupActivity.PERSON_SETUP_NAME);
				Toast.makeText(PersonInfoActivity.this, getText(R.string.personinfo_name_no_changing), Toast.LENGTH_SHORT).show();
				break;
			case R.id.person_nick_btn:
				intent.setClass(PersonInfoActivity.this, SingleEditSetup.class);
				intent.putExtra("type", SetupActivity.PERSON_SETUP_NICK);
				PersonInfoActivity.this.startActivityForResult(intent, SetupActivity.PERSON_SETUP_NICK);
				break;
			case R.id.person_birth_btn:
				intent.setClass(PersonInfoActivity.this, SingleDateSetup.class);
				intent.putExtra("type", SetupActivity.PERSON_SETUP_BIRTH);
				PersonInfoActivity.this.startActivityForResult(intent, SetupActivity.PERSON_SETUP_BIRTH);
				break;
			case R.id.person_sex_btn:
				intent.setClass(PersonInfoActivity.this, SingleChoiceSetup.class);
				intent.putExtra("type", SetupActivity.PERSON_SETUP_SEX);
				PersonInfoActivity.this.startActivityForResult(intent, SetupActivity.PERSON_SETUP_SEX);
				break;
			case R.id.person_height_btn:
				intent.setClass(PersonInfoActivity.this, SingleEditSetup.class);
				intent.putExtra("type", SetupActivity.PERSON_SETUP_HEIGHT);
				PersonInfoActivity.this.startActivityForResult(intent, SetupActivity.PERSON_SETUP_HEIGHT);
				break;
			case R.id.person_weight_btn:
				intent.setClass(PersonInfoActivity.this, SingleEditSetup.class);
				intent.putExtra("type", SetupActivity.PERSON_SETUP_WEIGHT);
				PersonInfoActivity.this.startActivityForResult(intent, SetupActivity.PERSON_SETUP_WEIGHT);
				break;
			case R.id.person_address_btn:
				intent.setClass(PersonInfoActivity.this, SingleEditSetup.class);
				intent.putExtra("type", SetupActivity.PERSON_SETUP_ADDRESS);
				PersonInfoActivity.this.startActivityForResult(intent, SetupActivity.PERSON_SETUP_ADDRESS);
				break;
			case R.id.person_allergy_btn:
				intent.setClass(PersonInfoActivity.this, SingleEditSetup.class);
				intent.putExtra("type", SetupActivity.PERSON_SETUP_ALLERGY);
				PersonInfoActivity.this.startActivityForResult(intent, SetupActivity.PERSON_SETUP_ALLERGY);
				break;
			case R.id.person_medical_btn:
				intent.setClass(PersonInfoActivity.this, SingleEditSetup.class);
				intent.putExtra("type", SetupActivity.PERSON_SETUP_MEDICAL);
				PersonInfoActivity.this.startActivityForResult(intent, SetupActivity.PERSON_SETUP_MEDICAL);
				break;
			case R.id.person_blood_btn:
				intent.setClass(PersonInfoActivity.this, SingleChoiceSetup.class);
				intent.putExtra("type", SetupActivity.PERSON_SETUP_BLOOD);
				PersonInfoActivity.this.startActivityForResult(intent, SetupActivity.PERSON_SETUP_BLOOD);
				break;
			case R.id.person_med_card_btn:
				if (name != null && nick != null && birth != 0 && address != null && blood != null && height != 0 && weight != 0 && sex != 0) {
					intent.setClass(PersonInfoActivity.this, EmergencyVeterinaryActivity.class);
					PersonInfoActivity.this.startActivity(intent);
				} else {
					Toast.makeText(PersonInfoActivity.this, getText(R.string.personinfo_need_complete_information), Toast.LENGTH_SHORT).show();
				}
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
			case SetupActivity.PERSON_SETUP_NAME:
				// String nameBuffer = data.getStringExtra("content");
				// if (nameBuffer != null && !nameBuffer.equals(name)) {
				// updateOption = true;
				// name = nameBuffer;
				// preference.setUserName(name);
				// nameBtn.setText(name);
				// }
				break;
			case SetupActivity.PERSON_SETUP_NICK:
				String nickBuffer = data.getStringExtra("content");
				if (nickBuffer != null && !nickBuffer.equals(name)) {
					updateOption = true;
					nick = nickBuffer;
					preference.setNick(nick);
					nickBtn.setText(nick);
				}
				break;
			case SetupActivity.PERSON_SETUP_BIRTH:
				int year = data.getIntExtra("year", 1970);
				int month = data.getIntExtra("month", 0);
				int day = data.getIntExtra("day", 0);
				if (birthDate.getYear() != year || birthDate.getMonth() != month || birthDate.getDay() != day) {
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.MILLISECOND, 0);
					calendar.set(year, month, day, 0, 0, 0);
					long birthInMillis = calendar.getTimeInMillis();
					preference.setBirth(birthInMillis);
					if (birthInMillis != 0) {
						birthDate.setYear(year);
						birthDate.setMonth(month);
						birthDate.setDate(day);
						birthBtn.setText(birthDate.getYYYYMMDD());
						updateOption = true;
					} else {
						birthBtn.setText(getText(R.string.no_info));
					}
				}
				break;
			case SetupActivity.PERSON_SETUP_SEX:
				int sexBuffer = data.getIntExtra("content", 0);
				if (sexBuffer != sex) {
					if (sexBuffer == 1) {
						sexBtn.setText(getText(R.string.personinfo_sex_male));
						sex = 1;
						preference.setSex(sex);
						updateOption = true;
					}

					if (sexBuffer == 2) {
						sexBtn.setText(getText(R.string.personinfo_sex_female));
						sex = 1;
						preference.setSex(sex);
						updateOption = true;
					}
				}
				break;
			case SetupActivity.PERSON_SETUP_HEIGHT:
				int heightBuffer = data.getIntExtra("content", 0);
				if (heightBuffer != height && heightBuffer != 0) {
					height = heightBuffer;
					heightBtn.setText(height + "cm");
					preference.setHeight(height);
					updateOption = true;
				}
				break;
			case SetupActivity.PERSON_SETUP_WEIGHT:
				int weightBuffer = data.getIntExtra("content", 0);
				if (weightBuffer != weight && weightBuffer != 0) {
					weight = weightBuffer;
					weightBtn.setText(weight + "cm");
					preference.setWeight(weight);
					updateOption = true;
				}
				break;
			case SetupActivity.PERSON_SETUP_ADDRESS:
				String addressBuffer = data.getStringExtra("content");
				if (addressBuffer != null && !addressBuffer.equals(address)) {
					updateOption = true;
					address = addressBuffer;
					preference.setAddress(address);
					addressBtn.setText(address);
				}
				break;
			case SetupActivity.PERSON_SETUP_ALLERGY:
				String allergyBuffer = data.getStringExtra("content");
				if (allergyBuffer != null && !allergyBuffer.equals(allergy)) {
					updateOption = true;
					allergy = allergyBuffer;
					preference.setAllergic(allergy);
					allergyBtn.setText(allergy);
				}
				break;
			case SetupActivity.PERSON_SETUP_MEDICAL:
				String medicalBuffer = data.getStringExtra("content");
				if (medicalBuffer != null && !medicalBuffer.equals(medical)) {
					updateOption = true;
					medical = medicalBuffer;
					preference.setMedicalInfo(medical);
					medicalBtn.setText(medical);
				}
				break;
			case SetupActivity.PERSON_SETUP_BLOOD:
				String bloodBuffer = data.getStringExtra("content");
				if (bloodBuffer != null && !bloodBuffer.equals(blood)) {
					updateOption = true;
					blood = bloodBuffer;
					preference.setBlood(blood);
					bloodBtn.setText(blood);
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (updateOption) {
			if (!ServiceStates.getForchildServiceState(PersonInfoActivity.this)) {
				Intent intent = new Intent(PersonInfoActivity.this, ServiceCore.class);
				PersonInfoActivity.this.startService(intent);
			}

			RequestModifyChildInformation rlc = new RequestModifyChildInformation(preference.getSid(), preference.getSex(), preference.getBirth(),
					preference.getAllergic(), preference.getMedicalInfo(), preference.getHeight(), preference.getWeight(), preference.getBlood(),
					preference.getNick(), preference.getAddress());

			Log.e("PersonInfoActivity.onDestroy", rlc.toString());
			ServiceCore.addNetTask(rlc);
		}

		this.unregisterReceiver(personInfoReceiver);
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
