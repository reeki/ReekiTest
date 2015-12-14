package com.forchild000.surface;

import java.io.Serializable;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestChildInformation;
import com.forchild.data.RequestModifySeniorInformation;
import com.forchild.data.SeniorInfoComplete;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;

public class SeniorsInfoModifyActivity extends AliveBaseActivity {
	private EditText nameEdit, nickEdit, heightEdit, weightEdit, addressEdit, cardIdEdit, allergicEdit, medicalEdit, codeEdit;
	private Spinner bloodSpinner, sexSpinner;
	private DatePicker birthDatePicker;
	private ArrayAdapter<String> bloodAdapter, sexAdapter;
	private Button sureBtn;
	protected Calendar birth;
	protected SeniorInfoComplete seniorInfo;
	protected String name, nick, address, cardId, allergic, medical, blood, code;
	protected int height, weight, sex;
	protected boolean updateOption = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_sensor_activity);

		Intent startIntent = getIntent();
		Serializable seniorInfoBuf = startIntent.getSerializableExtra("content");

		if (seniorInfoBuf != null && seniorInfoBuf instanceof SeniorInfoComplete) {
			seniorInfo = (SeniorInfoComplete) seniorInfoBuf;
		} else {
			finish();
		}

		if (seniorInfo.getOid() == BaseProtocolFrame.INT_INITIATION) {

			if (!ServiceStates.getForchildServiceState(SeniorsInfoModifyActivity.this)) {
				Intent intent = new Intent(SeniorsInfoModifyActivity.this, ServiceCore.class);
				SeniorsInfoModifyActivity.this.startService(intent);
			}

			Preferences preference = new Preferences(SeniorsInfoModifyActivity.this);

			ServiceCore.addNetTask(new RequestChildInformation(preference.getSid()));

			Toast.makeText(this, getText(R.string.senior_info_getinfo_error), Toast.LENGTH_SHORT).show();
		}

		name = seniorInfo.getName();
		nick = seniorInfo.getNick();
		address = seniorInfo.getAddress();
		// cardId = seniorInfo.getCardId();
		allergic = seniorInfo.getAllergic();
		medical = seniorInfo.getMedical();
		height = seniorInfo.getHeight();
		weight = seniorInfo.getWeight();
		sex = seniorInfo.getSex();
		blood = seniorInfo.getBlood();
		code = seniorInfo.getCode();

		codeEdit = (EditText) findViewById(R.id.regsenior_code_edit);
		codeEdit.setEnabled(false);

		nameEdit = (EditText) findViewById(R.id.regsenior_name_edit);
		nickEdit = (EditText) findViewById(R.id.regsenior_nick_edit);
		heightEdit = (EditText) findViewById(R.id.regsenior_height_edit);
		weightEdit = (EditText) findViewById(R.id.regsenior_weight_edit);
		addressEdit = (EditText) findViewById(R.id.regsenior_address_edit);
		cardIdEdit = (EditText) findViewById(R.id.regsenior_cardid_edit);
		allergicEdit = (EditText) findViewById(R.id.regsenior_allergic_edit);
		medicalEdit = (EditText) findViewById(R.id.regsenior_medical_edit);

		nameEdit.setEnabled(false);
		cardIdEdit.setEnabled(false);
		cardIdEdit.setVisibility(View.INVISIBLE);

		nameEdit.setHint(R.string.seniorinfomod_nameeidt_hint_text);
		nickEdit.setHint(R.string.seniorinfomod_nickeidt_hint_text);
		heightEdit.setHint(R.string.seniorinfomod_heighteidt_hint_text);
		weightEdit.setHint(R.string.seniorinfomod_weighteidt_hint_text);
		addressEdit.setHint(R.string.seniorinfomod_addresseidt_hint_text);
		allergicEdit.setHint(R.string.seniorinfomod_allergiceidt_hint_text);
		medicalEdit.setHint(R.string.seniorinfomod_medicaleidt_hint_text);

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

		bloodAdapter.add(getText(R.string.setup_blood_type_na).toString());
		bloodAdapter.add(getText(R.string.setup_blood_type_a).toString());
		bloodAdapter.add(getText(R.string.setup_blood_type_b).toString());
		bloodAdapter.add(getText(R.string.setup_blood_type_o).toString());
		bloodAdapter.add(getText(R.string.setup_blood_type_ab).toString());

		sexAdapter.add(getText(R.string.personinfo_sex_male).toString());
		sexAdapter.add(getText(R.string.personinfo_sex_female).toString());

		sureBtn = (Button) findViewById(R.id.regsenior_sure_btn);
		sureBtn.setOnClickListener(seniorsInfoModButtonListener);

		birthDatePicker = (DatePicker) findViewById(R.id.regsenior_birth_datepicker);

		birth = Calendar.getInstance();
		birth.setTimeInMillis(seniorInfo.getBirth());
		birthDatePicker.updateDate(birth.get(Calendar.YEAR), birth.get(Calendar.MONTH), birth.get(Calendar.DATE));

		if (name != null) {
			nameEdit.setText(name);
		}
		if (nick != null) {
			nickEdit.setText(nick);
		}
		if (sex > 0 && sex < 3) {
			sexSpinner.setSelection(sex - 1);
		} else {
			sexSpinner.setSelection(0);
		}
		if (height != BaseProtocolFrame.INT_INITIATION) {
			heightEdit.setText(String.valueOf(height));
		}
		if (weight != BaseProtocolFrame.INT_INITIATION) {
			weightEdit.setText(String.valueOf(weight));
		}
		if (address != null) {
			addressEdit.setText(address);
		}
		if (allergic != null) {
			allergicEdit.setText(allergic);
		}
		if (medical != null) {
			medicalEdit.setText(medical);
		}

		if (blood != null) {
			if (blood.toUpperCase().equals("未知")) {
				bloodSpinner.setSelection(0);
			} else if (blood.toUpperCase().equals("A")) {
				bloodSpinner.setSelection(1);
			} else if (blood.toUpperCase().equals("B")) {
				bloodSpinner.setSelection(2);
			} else if (blood.toUpperCase().equals("O")) {
				bloodSpinner.setSelection(3);
			} else if (blood.toUpperCase().equals("AB")) {
				bloodSpinner.setSelection(4);
			} else {
				bloodSpinner.setSelection(0);
			}
		}

		if (code != null) {
			if (code.length() > 0) {
				codeEdit.setText(code);
			} else {
				codeEdit.setVisibility(View.GONE);
			}
		} else {
			codeEdit.setVisibility(View.GONE);
		}

		// if (seniorInfo.getCardId() != null) {
		// cardIdEdit.setText(seniorInfo.getCardId());
		// }

	}

	private OnItemSelectedListener seniorsInfoModSpinnerListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			switch (arg0.getId()) {
			case R.id.regsenior_blood_spinner:
				switch (arg2) {
				case 0:
					blood = "未知";
					break;
				case 1:
					blood = "A";
					break;
				case 2:
					blood = "B";
					break;
				case 3:
					blood = "O";
					break;
				case 4:
					blood = "AB";
					break;
				default:
					blood = "未知";
					break;
				}
				break;
			case R.id.regsenior_sex_spinner:
				sex = arg2;
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

			name = nameEdit.getText().toString().trim();
			nick = nickEdit.getText().toString().trim();
			address = addressEdit.getText().toString().trim();
			allergic = allergicEdit.getText().toString().trim();
			medical = medicalEdit.getText().toString().trim();

			// 可以重新构造一个SeniorInfoComplete类，然后与intent传过来的seniorinfo做equals，如果非，则证明有修改。
			// 但需要在SeniorInfoComplete的构造方法和set方法中对输入进行限制，同时不便于不同的数据做出不同的报错提示。因此使用了这种逐步判断的额方法。
			Pattern pattern = Pattern.compile(SetupActivity.PATTERN_NAME);
			Matcher matcher = pattern.matcher(name);

			// name 是否合法及修改的判断
			if (name.length() > 8) {
				Toast.makeText(SeniorsInfoModifyActivity.this, getText(R.string.input_name_long_error), Toast.LENGTH_SHORT).show();
				return;
			}

			if (name.length() == 0) {
				Toast.makeText(SeniorsInfoModifyActivity.this, getText(R.string.input_name_short_error), Toast.LENGTH_SHORT).show();
				return;
			}

			if (!name.equals(seniorInfo.getName())) {
				updateOption = true;
			}

			// nick 是否合法及修改的判断
			if (nick.length() > 16) {
				Toast.makeText(SeniorsInfoModifyActivity.this, getText(R.string.input_nick_long_error), Toast.LENGTH_SHORT).show();
				return;
			}

			if (seniorInfo.getNick() == null) {
				if (nick.length() != 0) {
					updateOption = true;
				}
			} else if (!nick.equals(seniorInfo.getNick())) {
				updateOption = true;
			}

			// address 是否合法及修改的判断
			if (address.length() > 128) {
				Toast.makeText(SeniorsInfoModifyActivity.this, getText(R.string.input_address_long_error), Toast.LENGTH_SHORT).show();
				return;
			}

			if (seniorInfo.getAddress() == null) {
				if (address.length() != 0) {
					updateOption = true;
				}
			} else if (!address.equals(seniorInfo.getAddress())) {
				updateOption = true;
			}

			// allergic 是否合法及修改的判断
			if (allergic.length() > 64) {
				Toast.makeText(SeniorsInfoModifyActivity.this, getText(R.string.input_allergic_long_error), Toast.LENGTH_SHORT).show();
				return;
			}

			if (seniorInfo.getAllergic() == null) {
				if (address.length() != 0) {
					updateOption = true;
				}
			} else if (!address.equals(seniorInfo.getAllergic())) {
				updateOption = true;
			}

			// medical 是否合法及修改的判断
			if (medical.length() > 128) {
				Toast.makeText(SeniorsInfoModifyActivity.this, getText(R.string.input_medical_long_error), Toast.LENGTH_SHORT).show();
				return;
			}

			if (seniorInfo.getMedical() == null) {
				if (medical.length() != 0) {
					updateOption = true;
				}
			} else if (!medical.equals(seniorInfo.getMedical())) {
				updateOption = true;
			}

			// height 和 weight 是否合法及修改的判断
			String heightStr = heightEdit.getText().toString().trim();
			String weightStr = weightEdit.getText().toString().trim();

			try {
				height = Integer.valueOf(heightStr).intValue();
			} catch (NumberFormatException e) {
				e.printStackTrace();
				Toast.makeText(SeniorsInfoModifyActivity.this, getText(R.string.input_height_error), Toast.LENGTH_SHORT).show();
				return;
			}

			try {
				weight = Integer.valueOf(weightStr).intValue();
			} catch (NumberFormatException e) {
				e.printStackTrace();
				Toast.makeText(SeniorsInfoModifyActivity.this, getText(R.string.input_weight_error), Toast.LENGTH_SHORT).show();
				return;
			}

			if (height != seniorInfo.getHeight() || weight != seniorInfo.getWeight()) {
				updateOption = true;
			}

			// blood 是否合法及修改的判断

			int bloodInt = bloodSpinner.getSelectedItemPosition();
			switch (bloodInt) {
			case 0:
				blood = "未知";
				break;
			case 1:
				blood = "A";
				break;
			case 2:
				blood = "B";
				break;
			case 3:
				blood = "O";
				break;
			case 4:
				blood = "AB";
				break;
			default:
				blood = "未知";
				break;
			}

			if (seniorInfo.getBlood() == null) {
				updateOption = true;
			} else if (!blood.equals(seniorInfo.getBlood())) {
				updateOption = true;
			}

			// sex 是否合法及修改的判断
			sex = sexSpinner.getSelectedItemPosition() + 1;

			if ((sex == 1 || sex == 2) && sex != seniorInfo.getSex()) {
				updateOption = true;
			}

			// birth 是否合法及修改的判断
			if (birthDatePicker.getYear() != birth.get(Calendar.YEAR) || birthDatePicker.getMonth() != birth.get(Calendar.MONTH)
					|| birthDatePicker.getDayOfMonth() != birth.get(Calendar.DATE)) {
				updateOption = true;
				birth.set(Calendar.YEAR, birthDatePicker.getYear());
				birth.set(Calendar.MONTH, birthDatePicker.getMonth());
				birth.set(Calendar.DATE, birthDatePicker.getDayOfMonth());
			}

			if (updateOption) {
				updateOption = false;
				seniorInfo.setName(name);
				seniorInfo.setNick(nick);
				seniorInfo.setAddress(address);
				seniorInfo.setAllergic(allergic);
				seniorInfo.setBirth(birth.getTimeInMillis());
				seniorInfo.setBlood(blood);
				seniorInfo.setHeight(height);
				seniorInfo.setWeight(weight);
				seniorInfo.setMedical(medical);

				seniorInfo.setSex(sex);
				DatabaseHelper dbHelper = new DatabaseHelper(SeniorsInfoModifyActivity.this);
				dbHelper.updateSeniorInfo(seniorInfo.toContentValues(), "oid = ? AND id = ?",
						new String[] { seniorInfo.getOid() + "", seniorInfo.getId() + "" });

				if (!ServiceStates.getForchildServiceState(SeniorsInfoModifyActivity.this)) {
					Intent intent = new Intent(SeniorsInfoModifyActivity.this, ServiceCore.class);
					SeniorsInfoModifyActivity.this.startService(intent);
				}

				Preferences preference = new Preferences(SeniorsInfoModifyActivity.this);

				RequestModifySeniorInformation rlc = new RequestModifySeniorInformation(preference.getSid(), seniorInfo.getOid(), sex,
						birth.getTimeInMillis(), allergic, medical, height, weight, blood, nick, address);
				ServiceCore.addNetTask(rlc);
			}

			finish();
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
