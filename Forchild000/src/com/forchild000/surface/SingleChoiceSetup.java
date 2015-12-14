package com.forchild000.surface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SingleChoiceSetup extends AliveBaseActivity {
	private RadioGroup group;
	private RadioButton btn0, btn1, btn2, btn3;
	protected int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_choice_setup_activity);

		setResult(RESULT_CANCELED);

		group = (RadioGroup) findViewById(R.id.signle_choice_group);

		btn0 = (RadioButton) findViewById(R.id.signle_choice_btn0);
		btn1 = (RadioButton) findViewById(R.id.signle_choice_btn1);
		btn2 = (RadioButton) findViewById(R.id.signle_choice_btn2);
		btn3 = (RadioButton) findViewById(R.id.signle_choice_btn3);

		Intent startIntent = getIntent();
		type = startIntent.getIntExtra("type", -1);

		switch (type) {
		case SetupActivity.PERSON_SETUP_BLOOD:
			btn0.setText(getText(R.string.setup_blood_type_a));
			btn1.setText(getText(R.string.setup_blood_type_b));
			btn2.setText(getText(R.string.setup_blood_type_o));
			btn3.setText(getText(R.string.setup_blood_type_ab));
			break;
		case SetupActivity.PERSON_SETUP_SEX:
			btn0.setText(getText(R.string.personinfo_sex_male));
			btn3.setText(getText(R.string.personinfo_sex_female));
			btn1.setVisibility(View.GONE);
			btn2.setVisibility(View.GONE);
			break;
		default:
			finish();
			break;
		}

		group.setOnCheckedChangeListener(singleCheckedChangeListener);
	}

	private OnCheckedChangeListener singleCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			Log.e("SingleChoiceSetip checkedId", checkedId + "");
			if (checkedId == -1) {
				return;
			}
			Intent resultIntent = new Intent();
			switch (type) {
			case SetupActivity.PERSON_SETUP_BLOOD:
				resultIntent.putExtra("content", ((RadioButton) findViewById(checkedId)).getText());
				break;
			case SetupActivity.PERSON_SETUP_SEX:
				if (checkedId == btn0.getId()) {
					resultIntent.putExtra("content", 1);
				} else if (checkedId == btn3.getId()) {
					resultIntent.putExtra("content", 2);
				} else {
					return;
				}
				break;
			default:
				finish();
				break;
			}
			setResult(RESULT_OK, resultIntent);
			SingleChoiceSetup.this.finish();
		}

	};
}
