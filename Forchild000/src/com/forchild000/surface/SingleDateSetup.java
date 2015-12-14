package com.forchild000.surface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;

public class SingleDateSetup extends AliveBaseActivity {
	private DatePicker datePicker;
	private SetupButton sureBtn;
	private int type;
	private int year, monthOfYear, dayOfMonth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_date_setup_activity);
		setResult(RESULT_CANCELED);

		datePicker = (DatePicker) findViewById(R.id.single_date_picker);
		sureBtn = (SetupButton) findViewById(R.id.single_date_sure_button);
		sureBtn.setText(getText(R.string.sure));

		Intent startIntent = getIntent();
		type = startIntent.getIntExtra("type", -1);

		year = startIntent.getIntExtra("default_year", 1970);
		monthOfYear = startIntent.getIntExtra("default_month", 0);
		dayOfMonth = startIntent.getIntExtra("default_day", 1);
		datePicker.init(year, monthOfYear, dayOfMonth, onDateChangedListener);

		sureBtn.setOnClickListener(singleDateButtonListener);

		switch (type) {
		case SetupActivity.PERSON_SETUP_BIRTH:
			break;
		default:
			finish();
			break;
		}

	}

	private OnDateChangedListener onDateChangedListener = new OnDateChangedListener() {

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			SingleDateSetup.this.year = year;
			SingleDateSetup.this.monthOfYear = monthOfYear;
			SingleDateSetup.this.dayOfMonth = dayOfMonth;
		}

	};

	private OnClickListener singleDateButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (year < 1900) {
				Toast.makeText(SingleDateSetup.this, getText(R.string.birth_early_error), Toast.LENGTH_SHORT).show();
				return;
			}
			Intent resultIntent = new Intent();
			resultIntent.putExtra("year", year);
			resultIntent.putExtra("month", monthOfYear);
			resultIntent.putExtra("day", dayOfMonth);
			setResult(RESULT_OK, resultIntent);
			finish();
		}
	};
}
