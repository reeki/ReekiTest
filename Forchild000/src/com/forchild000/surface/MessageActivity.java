package com.forchild000.surface;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.forchild.data.RequestSendMessage;
import com.forchild.data.SeniorInfoAutoMessage;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;
import com.forchild.server.StatesIntent;

public class MessageActivity extends Activity {
	public static final int HANDLER_COMMON_MESSAGE = 1;

	private ArrayAdapter<String> adapter;
	private Spinner userChoiceSpinner;
	// private RadioGroup commonMsgGroup;
	// private RadioButton commonMsg;
	private EditText msgEdit;
	private Button sendBtn, msgHistoryBtn, autoMsgBtn;
	private ImageView commonMsgBtn;
	private int spinnerSelected = -1;
	protected List<SeniorInfoAutoMessage> siList = new ArrayList<SeniorInfoAutoMessage>();
	protected DatabaseHelper dbHelper;
	protected Preferences preference;
	protected boolean isRadioChanged = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_activity);

		dbHelper = new DatabaseHelper(this);
		preference = new Preferences(this);

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		userChoiceSpinner = (Spinner) findViewById(R.id.message_choice_user);
		userChoiceSpinner.setAdapter(adapter);
		userChoiceSpinner.setOnItemSelectedListener(userChoicesListener);

		Cursor cursor = dbHelper.getSeniorInfo(new String[] { "name", "id", "oid", "nick" }, null, null);
		while (cursor.moveToNext()) {
			SeniorInfoAutoMessage sis = new SeniorInfoAutoMessage(cursor.getInt(cursor.getColumnIndex("id")), cursor.getInt(cursor
					.getColumnIndex("oid")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("nick")),
					new String(), 0, 0);
			siList.add(sis);
		}
		cursor.close();
		dbHelper.close();
		adapter.clear();
		for (SeniorInfoAutoMessage sis : siList) {
			String nick = sis.getNick();
			if (nick != null && nick.length() > 0) {
				adapter.add(nick);
			} else {
				adapter.add(sis.getName());
			}
		}

		if (userChoiceSpinner.getCount() >= preference.getDefaultSenior()) {
			userChoiceSpinner.setSelection(preference.getDefaultSenior());
		} else if (userChoiceSpinner.getCount() > 0) {
			userChoiceSpinner.setSelection(0);
		}

		// commonMsgGroup = (RadioGroup)
		// findViewById(R.id.message_common_msg_group);
		//
		// commonMsgGroup.setOnCheckedChangeListener(new
		// OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// if (checkedId != -1) {
		// commonMsg = (RadioButton)
		// MessageActivity.this.findViewById(checkedId);
		// if (commonMsg.isChecked()) {
		// isRadioChanged = true;
		// msgEdit.setText(commonMsg.getText());
		// msgEdit.setSelection(commonMsg.getText().length());
		// }
		// }
		// }
		//
		// });
		//
		// msgEdit = (EditText) findViewById(R.id.message_edit_msg);
		// msgEdit.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// if (isRadioChanged) {
		// isRadioChanged = false;
		// } else {
		// commonMsgGroup.clearCheck();
		// }
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		//
		// }
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before, int
		// count) {
		//
		// }
		// });
		msgEdit = (EditText) findViewById(R.id.message_edit_msg);
		sendBtn = (Button) findViewById(R.id.message_send);
		msgHistoryBtn = (Button) findViewById(R.id.message_histoty);
		autoMsgBtn = (Button) findViewById(R.id.message_auto_msg);
		commonMsgBtn = (ImageView) findViewById(R.id.message_common_message);

		autoMsgBtn.setText(R.string.message_automsg_button);

		sendBtn.setOnClickListener(messageBtnListener);
		msgHistoryBtn.setOnClickListener(messageBtnListener);
		autoMsgBtn.setOnClickListener(messageBtnListener);
		commonMsgBtn.setOnClickListener(messageBtnListener);

		msgEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					if (!ServiceStates.getForchildServiceState(MessageActivity.this)) {
						Intent serviceIntent = new Intent(MessageActivity.this, ServiceCore.class);
						MessageActivity.this.startService(serviceIntent);
					}

					String content = msgEdit.getText().toString().trim();

					if (content.length() < 1) {
						Toast.makeText(MessageActivity.this, getText(R.string.message_edit_short_error), Toast.LENGTH_SHORT).show();
					}

					RequestSendMessage rsm = new RequestSendMessage(preference.getSid(), siList.get(spinnerSelected).getOid(), preference
							.getUserName(), content);
					ServiceCore.addNetTask(rsm);
					return true;
				}
				return false;
			}

		});

		msgEdit.setImeOptions(EditorInfo.IME_ACTION_SEND);
		msgEdit.setInputType(EditorInfo.TYPE_CLASS_TEXT);

		this.registerReceiver(msgReceiver, new IntentFilter("com.forchild.message.commonmessage"));
	}

	protected BroadcastReceiver msgReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("com.forchild.message.commonmessage")) {
				String content = intent.getStringExtra("content");
				if (content != null) {
					msgEdit.setText(content);
					msgEdit.setSelection(content.length());
				}
			}
		}
	};

	private OnClickListener messageBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.message_send:
				if (!ServiceStates.getForchildServiceState(MessageActivity.this)) {
					Intent serviceIntent = new Intent(MessageActivity.this, ServiceCore.class);
					MessageActivity.this.startService(serviceIntent);
				}

				String content = msgEdit.getText().toString().trim();

				if (content.length() < 1) {
					Toast.makeText(MessageActivity.this, getText(R.string.message_edit_short_error), Toast.LENGTH_SHORT).show();
				}

				RequestSendMessage rsm = new RequestSendMessage(preference.getSid(), siList.get(spinnerSelected).getOid(), preference.getUserName(),
						content);
				ServiceCore.addNetTask(rsm);
				break;
			case R.id.message_histoty:
				Intent intent = new Intent(MessageActivity.this, MessageHistoryActivity.class);
				MessageActivity.this.startActivity(intent);
				break;
			case R.id.message_auto_msg:
				Intent autoMsgIntent = new Intent(MessageActivity.this, AutoMsgDisplayActivity.class);
				MessageActivity.this.startActivity(autoMsgIntent);
				break;
			case R.id.message_common_message:
				Intent commonMsgIntent = new Intent(MessageActivity.this, CommonMessageDisplayActivity.class);
				MessageActivity.this.startActivity(commonMsgIntent);
				break;
			}

		}

	};

	private OnItemSelectedListener userChoicesListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			spinnerSelected = arg2;
			preference.setDefaultSenior(arg2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(msgReceiver);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatesIntent.sendCloseState(this, ServiceCore.ACTIVITY_TYPE_MESSAGE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatesIntent.sendAliveState(this, ServiceCore.ACTIVITY_TYPE_MESSAGE);
	}

}
