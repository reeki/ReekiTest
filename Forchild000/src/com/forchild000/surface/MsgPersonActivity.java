package com.forchild000.surface;

import java.io.Serializable;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.forchild.data.MessageFrame;
import com.forchild.data.MessageUser;
import com.forchild.data.RequestSendMessage;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;

public class MsgPersonActivity extends AliveBaseActivity {
	protected Button mBtnSend;
	protected EditText mEditTextContent;
	protected ListView mListView;
	protected LinearLayout toolsBar, toolsFirstBtn, toolsSecondBtn;
	protected TextView titleCenterText;
	protected ImageView toolsShowBtn;

	protected MsgViewAdapter mAdapter;
	protected List<MessageFrame> mDataArrays = new ArrayList<MessageFrame>();
	protected boolean isToolsShow = true;
	protected DatabaseHelper dbHelper;
	protected Preferences preference;
	protected Handler msgHandler;
	protected int oid;
	protected int sex;
	protected String name;
	protected long lastTime = 0;
	protected String msgUserName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_person_activity);

		dbHelper = new DatabaseHelper(this);
		preference = new Preferences(this);
		if (preference.getNick() != null && preference.getNick().length() > 0) {
			msgUserName = preference.getNick();
		} else if (preference.getUserName() != null) {
			msgUserName = preference.getUserName();
		}

		initView();

		msgHandler = new Handler(msgCallback);

		IntentFilter receiverFilter = new IntentFilter();
		receiverFilter.addAction("com.forchild.msg.SEND_AUTO_MESSAGE");
		receiverFilter.addAction("com.forchild.msg.SEND_AUTO_MESSAGE_FINISH");
		receiverFilter.addAction("com.forchild.message.commonmessage");
		this.registerReceiver(msgPersonReceiver, receiverFilter);
	}

	public void initView() {
		toolsBar = (LinearLayout) findViewById(R.id.msgperson_tools_bar);
		titleCenterText = (TextView) findViewById(R.id.msgperson_title_center_text);
		toolsFirstBtn = (LinearLayout) findViewById(R.id.msgperson_tools_first_btn);
		toolsSecondBtn = (LinearLayout) findViewById(R.id.msgperson_tools_second_btn);
		toolsShowBtn = (ImageView) findViewById(R.id.msgperson_commonmsg_add_btn);

		Intent startIntent = getIntent();
		oid = startIntent.getIntExtra("oid", -1);
		name = startIntent.getStringExtra("name");
		sex = startIntent.getIntExtra("sex", 1);
		if (oid == -1 || name == null) {
			finish();
		} else {
			titleCenterText.setText(name);
		}

		mListView = (ListView) findViewById(R.id.msgperson_listview);
		mBtnSend = (Button) findViewById(R.id.msgperson_send_btn);
		mBtnSend.setOnClickListener(msgPersonListener);
		toolsShowBtn.setOnClickListener(msgPersonListener);
		toolsFirstBtn.setOnClickListener(msgPersonListener);
		toolsSecondBtn.setOnClickListener(msgPersonListener);

		mEditTextContent = (EditText) findViewById(R.id.msgperson_sendmessage);
		mEditTextContent.setFocusable(true);
		mEditTextContent.requestFocus();

		mAdapter = new MsgViewAdapter(this, mDataArrays, msgFaultListener);
		mListView.setAdapter(mAdapter);
	}

	public void initData() {
		mDataArrays.clear();		
		Cursor msgCursor = dbHelper.getMessage(null, "oid = ? AND login_id = ?", new String[] { String.valueOf(oid), ServiceCore.getLoginId() });

		while (msgCursor.moveToNext()) {
			MessageFrame entity = new MessageFrame();
			entity.setDate(msgCursor.getLong(msgCursor.getColumnIndex("date")));
			entity.setCon(msgCursor.getString(msgCursor.getColumnIndex("content")));
			entity.setLastTime(lastTime);
			entity.setState(msgCursor.getInt(msgCursor.getColumnIndex("send_state")));
			entity.setUserMsgType(msgCursor.getInt(msgCursor.getColumnIndex("type")));
			entity.setOid(msgCursor.getInt(msgCursor.getColumnIndex("oid")));
			entity.setType(MessageFrame.USER_MESSAGE);
			entity.setLoginId(msgCursor.getString(msgCursor.getColumnIndex("login_id")));
			entity.setUname(msgCursor.getString(msgCursor.getColumnIndex("name")));
			lastTime = entity.getDate();
			mDataArrays.add(entity);
			Log.e("MsgPersonActivity.initData", "message entity:" + entity.toString());
		}

		if (msgCursor.getCount() > 0) {
			mAdapter.notifyDataSetChanged();
		}
		msgCursor.close();
		dbHelper.close();

	}

	protected BroadcastReceiver msgPersonReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.forchild.msg.SEND_AUTO_MESSAGE")) {
				Log.e("MsgPersonActivity", "收到自动消息广播");
				Serializable msgBuff = intent.getSerializableExtra("msg");
				if (msgBuff instanceof MessageUser) {
					MessageUser msg = (MessageUser) msgBuff;
					switch (msg.getUserMsgType()) {
					case MessageFrame.USERMESSAGETYPE_AUTO_CIRCLE_MESSAGE:
					case MessageFrame.USERMESSAGETYPE_AUTO_MEDICAL_MESSAGE:
					case MessageFrame.USERMESSAGETYPE_AUTO_ONEOFF_MESSAGE:
						msg.setLastTime(lastTime);

						mDataArrays.add(msg);
						mAdapter.notifyDataSetChanged();
						break;
					default:
						break;
					}
				}
			}

			if (intent.getAction().equals("com.forchild.msg.SEND_AUTO_MESSAGE_FINISH")) {

				mAdapter.notifyDataSetChanged();
			}

			if (intent.getAction().equals("com.forchild.message.commonmessage")) {
				String content = intent.getStringExtra("content");
				if (content != null) {
					mEditTextContent.setText(content);
					mEditTextContent.setSelection(content.length());
				}
			}
		}
	};

	protected Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case ServiceCore.NETWORK_RESPONSE:
				if (msg.obj instanceof RequestSendMessage) {
					Log.e("MsgPersonActivity", "已收到发送结束通知");
					RequestSendMessage rsm = (RequestSendMessage) msg.obj;
					MessageFrame mf = rsm.getMsgEntity();
					if (mf != null && mf.getState() == MessageFrame.SENDSTATE_SENDING) {
						mf.setState(MessageFrame.SENDSTATE_FAULT);
					}
					mAdapter.notifyDataSetChanged();
				}
				break;
			}
			return true;
		}
	};

	protected OnClickListener msgPersonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.msgperson_send_btn:
				send();
				break;
			case R.id.msgperson_commonmsg_add_btn:
				if (toolsBar.getVisibility() != View.VISIBLE) {
					toolsBar.setVisibility(View.VISIBLE);
					toolsBar.startAnimation(AnimationUtils.loadAnimation(MsgPersonActivity.this, R.anim.push_bottom_in_1));
					toolsBar.setFocusable(true);
				} else {
					toolsBar.setVisibility(View.GONE);
				}
				break;
			case R.id.msgperson_tools_first_btn:
				Intent commonMsgIntent = new Intent(MsgPersonActivity.this, CommonMessageDisplayActivity.class);
				MsgPersonActivity.this.startActivity(commonMsgIntent);
				break;
			case R.id.msgperson_tools_second_btn:
				Intent autoMsgIntent = new Intent(MsgPersonActivity.this, AutoMsgDisplayActivity.class);
				autoMsgIntent.putExtra("oid", oid);
				MsgPersonActivity.this.startActivity(autoMsgIntent);
				break;
			}
		}
	};

	protected void send() {
		String contString = mEditTextContent.getText().toString().trim();
		if (contString.length() > 0) {
			MessageUser entity = new MessageUser();
			entity.setCon(contString);
			entity.setDate(System.currentTimeMillis());
			entity.setLastTime(lastTime);
			entity.setLoginId(ServiceCore.getLoginId());
			entity.setState(MessageFrame.SENDSTATE_SENDING);
			entity.setType(MessageFrame.USER_MESSAGE);
			entity.setUname(msgUserName);
			entity.setUserMsgType(MessageFrame.USERMESSAGETYPE_MANUAL_MESSAGE);
			entity.setOid(oid);

			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();

			mEditTextContent.setText("");

			mListView.setSelection(mListView.getCount() - 1);

			if (!ServiceStates.getForchildServiceState(MsgPersonActivity.this)) {
				Intent serviceIntent = new Intent(MsgPersonActivity.this, ServiceCore.class);
				MsgPersonActivity.this.startService(serviceIntent);
			}

			RequestSendMessage rsm = new RequestSendMessage(preference.getSid(), entity);
			rsm.addHandler(msgHandler);
			ServiceCore.addNetTask(rsm);
			lastTime = System.currentTimeMillis();

			dbHelper.addMessage(entity);
			Log.e("MsgPersonActivity", "listview count:" + mListView.getCount() + ", adapter count:" + mAdapter.getCount());
		} else {
			Toast.makeText(MsgPersonActivity.this, getText(R.string.message_edit_short_error), Toast.LENGTH_SHORT).show();
		}
	}

	protected MsgViewAdapter.OnFaultItemClickListener msgFaultListener = new MsgViewAdapter.OnFaultItemClickListener() {

		@Override
		public void onFaultItemClick(View view, MessageFrame msg) {
			mDataArrays.remove(msg);
			dbHelper.deleteMessage(msg.getLoginId(), msg.getDate(), msg.getUserMsgType());

			msg.setDate(System.currentTimeMillis());
			msg.setLastTime(lastTime);
			mDataArrays.add(msg);
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(mListView.getCount() == 0 ? 0 : mListView.getCount() - 1);

			if (!ServiceStates.getForchildServiceState(MsgPersonActivity.this)) {
				Intent serviceIntent = new Intent(MsgPersonActivity.this, ServiceCore.class);
				MsgPersonActivity.this.startService(serviceIntent);
			}

			RequestSendMessage rsm = new RequestSendMessage(preference.getSid(), msg);
			rsm.addHandler(msgHandler);
			ServiceCore.addNetTask(rsm);
			lastTime = System.currentTimeMillis();
			dbHelper.addMessage(msg);
		}

	};

	@Override
	protected void onResume() {
		super.onResume();
		ServiceCore.setMsgActivityOid(oid);
		
		initData();
		Log.e("MsgPersonActivity", "listview count:" + mListView.getCount() + ", adapter count:" + mAdapter.getCount());
		mListView.setSelection(mListView.getCount() == 0 ? 0 : mListView.getCount() - 1);
	}

	@Override
	protected void onPause() {
		super.onPause();
		ServiceCore.setMsgActivityOid(-1);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(msgPersonReceiver);
	}

}
