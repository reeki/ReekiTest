package com.forchild000.surface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class SingleEditSetup extends AliveBaseActivity {
	private EditText contentEdit;
	private SetupButton sureBtn;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_text_setup_activity);

		setResult(RESULT_CANCELED);

		contentEdit = (EditText) findViewById(R.id.single_text_content_edit);
		sureBtn = (SetupButton) findViewById(R.id.single_text_sure_button);
		sureBtn.setText(getText(R.string.sure));

		Intent startIntent = getIntent();

		type = startIntent.getIntExtra("type", -1);

		switch (type) {
		case SetupActivity.PERSON_SETUP_NAME:
			contentEdit.setHint(getText(R.string.setup_input_hint_name));
			break;
		case SetupActivity.PERSON_SETUP_NICK:
			contentEdit.setHint(getText(R.string.setup_input_hint_nick));
			break;
		case SetupActivity.PERSON_SETUP_HEIGHT:
			contentEdit.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
			contentEdit.setHint(getText(R.string.setup_input_hint_height));
			break;
		case SetupActivity.PERSON_SETUP_WEIGHT:
			contentEdit.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
			contentEdit.setHint(getText(R.string.setup_input_hint_weight));
			break;
		case SetupActivity.PERSON_SETUP_ADDRESS:
			contentEdit.setHint(getText(R.string.setup_input_hint_address));
			break;
		case SetupActivity.PERSON_SETUP_ALLERGY:
			contentEdit.setHint(getText(R.string.setup_input_hint_allergy));
			break;
		case SetupActivity.PERSON_SETUP_MEDICAL:
			contentEdit.setHint(getText(R.string.setup_input_hint_medical));
			break;
		default:
			finish();
			break;
		}

		sureBtn.setOnClickListener(singleEditListener);
	}

	private OnClickListener singleEditListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Pattern pattern = null;
			Matcher matcher = null;
			String content = contentEdit.getText().toString().trim();
			Intent resultIntent = new Intent();
			switch (type) {
			case SetupActivity.PERSON_SETUP_HEIGHT:
			case SetupActivity.PERSON_SETUP_WEIGHT:
				int num = 0;
				try {
					num = Integer.valueOf(content);
				} catch (NumberFormatException e) {
					Toast.makeText(SingleEditSetup.this, getText(R.string.input_error), Toast.LENGTH_SHORT).show();
					return;
				}
				if (num > 200 || num <= 0) {
					Toast.makeText(SingleEditSetup.this, getText(R.string.input_error), Toast.LENGTH_SHORT).show();
				}

				resultIntent.putExtra("content", num);
				setResult(RESULT_OK, resultIntent);
				finish();
				break;
			case SetupActivity.PERSON_SETUP_NAME:
				pattern = Pattern.compile(SetupActivity.PATTERN_NAME);
				matcher = pattern.matcher(content);
				if (matcher.find()) {
					if (content.length() > 8) {
						Toast.makeText(SingleEditSetup.this, getText(R.string.input_long_error), Toast.LENGTH_SHORT).show();
					} else if (content.length() == 0) {
						Toast.makeText(SingleEditSetup.this, getText(R.string.input_short_error), Toast.LENGTH_SHORT).show();
					} else {
						resultIntent.putExtra("content", content);
						setResult(RESULT_OK, resultIntent);
						finish();
					}
				} else {
					Toast.makeText(SingleEditSetup.this, getText(R.string.input_error), Toast.LENGTH_SHORT).show();
				}
				break;
			case SetupActivity.PERSON_SETUP_NICK:
				if (content.length() > 16) {
					Toast.makeText(SingleEditSetup.this, getText(R.string.input_long_error), Toast.LENGTH_SHORT).show();
				} else {
					resultIntent.putExtra("content", content);
					setResult(RESULT_OK, resultIntent);
					finish();
				}
				break;
			case SetupActivity.PERSON_SETUP_ADDRESS:
				if (content.length() > 128) {
					Toast.makeText(SingleEditSetup.this, getText(R.string.input_long_error), Toast.LENGTH_SHORT).show();
				} else {
					resultIntent.putExtra("content", content);
					setResult(RESULT_OK, resultIntent);
					finish();
				}
				break;
			case SetupActivity.PERSON_SETUP_ALLERGY:
				if (content.length() > 64) {
					Toast.makeText(SingleEditSetup.this, getText(R.string.input_long_error), Toast.LENGTH_SHORT).show();
				} else {
					resultIntent.putExtra("content", content);
					setResult(RESULT_OK, resultIntent);
					finish();
				}
				break;
			case SetupActivity.PERSON_SETUP_MEDICAL:
				if (content.length() > 128) {
					Toast.makeText(SingleEditSetup.this, getText(R.string.input_long_error), Toast.LENGTH_SHORT).show();
				} else {
					resultIntent.putExtra("content", content);
					setResult(RESULT_OK, resultIntent);
					finish();
				}
				break;
			default:
				setResult(RESULT_CANCELED);
				finish();
				break;
			}
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
