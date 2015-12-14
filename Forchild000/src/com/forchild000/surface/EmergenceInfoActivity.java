package com.forchild000.surface;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.forchild.server.Preferences;

public class EmergenceInfoActivity extends AliveBaseActivity {
	private EditText contentEdit;
	private TextView titleText;
	private SetupButton sureBtn;
	protected Preferences preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signle_title_text_setup_activity);

		preference = new Preferences(this);

		contentEdit = (EditText) findViewById(R.id.singlet_text_content_edit);
		titleText = (TextView) findViewById(R.id.singlet_title_content_edit);
		sureBtn = (SetupButton) findViewById(R.id.singlet_text_sure_button);
		titleText.setText(R.string.emergencyinfo_title_text);
		sureBtn.setText(getText(R.string.sure));

		if (preference.getEmergencyInfo() != null) {
			contentEdit.setText(preference.getEmergencyInfo());
		}

		sureBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = contentEdit.getText().toString().trim();

				if (content.length() == 0) {
					Toast.makeText(EmergenceInfoActivity.this, getText(R.string.input_nothing_error), Toast.LENGTH_SHORT).show();
					return;
				}

				preference.setEmergencyInfo(content);
				finish();
			}

		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
