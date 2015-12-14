package com.forchild000.surface;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.forchild.data.CommonMsgFrame;
import com.forchild.server.DatabaseHelper;

public class CommonMessageDisplayActivity extends AliveBaseActivity {
	protected ListView contentList;
	protected ArrayAdapter<String> adapter;
	protected EditText contentEdit;
	protected ImageView addBtn;
	protected DatabaseHelper dbHelper;
	protected List<CommonMsgFrame> msgList = new ArrayList<CommonMsgFrame>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_msg_activity);

		setResult(RESULT_CANCELED);

		dbHelper = new DatabaseHelper(this);

		contentList = (ListView) findViewById(R.id.commonmsg_content_list);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
		contentList.setAdapter(adapter);

		contentList.setOnItemClickListener(commonListClickListener);

		this.refresh();

		contentEdit = (EditText) findViewById(R.id.commonmsg_content_edit);
		addBtn = (ImageView) findViewById(R.id.commonmsg_add_btn);

		addBtn.setOnClickListener(commonBtnListener);

		contentEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					String content = contentEdit.getText().toString().trim();
					if (content.length() > 256) {
						Toast.makeText(CommonMessageDisplayActivity.this, getText(R.string.commonmsg_input_long_error), Toast.LENGTH_SHORT).show();
						return true;
					} else if (content.length() <= 0) {
						return true;
					} else {
						int id = dbHelper.insertCommonMessage(content);
						if (id > 0) {
							msgList.add(new CommonMsgFrame(id, content));
							adapter.add(content);
						}
					}
					refresh();
					return true;
				}
				return false;
			}

		});

		contentEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
		contentEdit.setInputType(EditorInfo.TYPE_CLASS_TEXT);

		// contentEdit.setOnKeyListener(new OnKeyListener() {
		//
		// @Override
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// if (keyCode == KeyEvent.KEYCODE_ENTER) {
		// String content = contentEdit.getText().toString().trim();
		// if (content.length() > 256) {
		// Toast.makeText(CommonMessageDisplayActivity.this,
		// getText(R.string.commonmsg_input_long_error),
		// Toast.LENGTH_SHORT).show();
		// return true;
		// } else if (content.length() <= 0) {
		// return true;
		// } else {
		// int id = dbHelper.insertCommonMessage(content);
		// if (id > 0) {
		// msgList.add(new CommonMsgFrame(id, content));
		// adapter.add(content);
		// }
		// }
		// refresh();
		// return true;
		// }
		// return false;
		// }
		// });
	}

	protected void refresh() {
		adapter.clear();
		msgList.clear();
		Cursor msgCursor = dbHelper.getCommonMessage();
		while (msgCursor.moveToNext()) {
			msgList.add(new CommonMsgFrame(msgCursor.getInt(msgCursor.getColumnIndex("id")), msgCursor.getString(msgCursor.getColumnIndex("content"))));
		}

		for (CommonMsgFrame msg : msgList) {
			if (msg.getContent() != null) {
				adapter.add(msg.getContent());
			} else {
				msgList.remove(msg);
				dbHelper.deleteCommonMessage("id = ?", new String[] { msg.getId() + "" });
			}
		}
		msgCursor.close();
		dbHelper.close();
	}

	private OnItemClickListener commonListClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position < msgList.size()) {
				String content = msgList.get(position).getContent();
				
				Intent intent = new Intent();
				intent.setAction("com.forchild.message.commonmessage");
				intent.putExtra("content", content);
				CommonMessageDisplayActivity.this.sendBroadcast(intent);
				
				finish();
			} else {
				adapter.clear();
				for (CommonMsgFrame msg : msgList) {
					if (msg.getContent() != null) {
						adapter.add(msg.getContent());
					} else {
						msgList.remove(msg);
						dbHelper.deleteCommonMessage("id = ?", new String[] { msg.getId() + "" });
					}
				}
			}
		}
	};

	private OnClickListener commonBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String content = contentEdit.getText().toString().trim();
			if (content.length() > 256) {
				Toast.makeText(CommonMessageDisplayActivity.this, getText(R.string.commonmsg_input_long_error), Toast.LENGTH_SHORT).show();
				return;
			} else if (content.length() <= 0) {
				return;
			} else {
				int id = dbHelper.insertCommonMessage(content);
				if (id > 0) {
					msgList.add(new CommonMsgFrame(id, content));
					adapter.add(content);
				}
			}
			refresh();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
