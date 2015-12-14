package com.forchild000.surface;

import java.sql.Timestamp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.forchild.server.Preferences;

public class EmergencyVeterinaryActivity extends AliveBaseActivity {
	private TextView userHeight;
	private TextView userWeight;
	private TextView userBlood;
	private TextView userAddress;
	private TextView emergencyPhone;
	private TextView medicalInfo;
	private TextView allergic;
	private TextView userName;
	private TextView userSex;
	private TextView userAge;
	private ImageView userPortrait;
	private Preferences preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergency_veterinary_activity);

		userHeight = (TextView) findViewById(R.id.emergencysetup_user_height);
		userWeight = (TextView) findViewById(R.id.emergencysetup_user_weight);
		userBlood = (TextView) findViewById(R.id.emergencysetup_user_blood);
		userAddress = (TextView) findViewById(R.id.emergencysetup_user_address);
		emergencyPhone = (TextView) findViewById(R.id.emergencysetup_emergency_phone);
		medicalInfo = (TextView) findViewById(R.id.emergencysetup_medical_info);
		allergic = (TextView) findViewById(R.id.emergencysetup_user_allergic);
		userName = (TextView) findViewById(R.id.emergencysetup_user_name);
		userSex = (TextView) findViewById(R.id.emergencysetup_user_sex);
		userAge = (TextView) findViewById(R.id.emergencysetup_user_age);
		userPortrait = (ImageView) findViewById(R.id.emergencysetup_portrait);

		emergencyPhone.setVisibility(View.GONE);

		preference = new Preferences(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.refresh();
	}

	private void refresh() {
		int heightInt = preference.getHeight();
		int weightInt = preference.getWeight();
		String bloodStr = preference.getBlood();
		String addressStr = preference.getAddress();
		// String ePhoneStr = preference.getEmergencyTel(); // 紧急联系电话
		String medInfoStr = preference.getMedicalInfo();
		String allergicStr = preference.getAllergic();
		String nameStr = preference.getUserName();
		int sexInt = preference.getSex();
		// int ageInt = preference.getAge();
		Timestamp birth = new Timestamp(preference.getBirth());

		if (heightInt != 0) {
			userHeight.setText(heightInt + " cm");
		}

		if (weightInt != 0) {
			userWeight.setText(weightInt + " kg");
		}

		if (bloodStr != null) {
			userBlood.setText(bloodStr);
		}

		if (addressStr != null) {
			userAddress.setText(addressStr);
		}

		// if(ePhoneStr != null) {
		// emergencyPhone.setText(ePhoneStr);
		// }

		if (medInfoStr != null) {
			medicalInfo.setText(medInfoStr);
		}

		if (allergicStr != null) {
			allergic.setText(allergicStr);
		}

		if (nameStr != null) {
			userName.setText(nameStr);
		}

		if (sexInt == 1) {
			userSex.setText("男");
			userPortrait.setImageResource(R.drawable.medical_card_figure_male);
		} else if (sexInt == 2) {
			userSex.setText("女");
			userPortrait.setImageResource(R.drawable.medical_card_figure_female);
		}

		if (birth != null) {
			String ageArray[] = birth.toString().split(" ");
			userAge.setText(ageArray[0]);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
