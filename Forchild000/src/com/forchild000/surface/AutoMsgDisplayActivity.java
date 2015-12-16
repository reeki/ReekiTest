package com.forchild000.surface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.forchild.data.MessageFrame;
import com.forchild.data.SeniorInfoAutoMessage;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.fortysevendeg.android.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.android.swipelistview.SwipeListView;

public class AutoMsgDisplayActivity extends AliveBaseActivity {
	public static final int NEW_MESSAGE_ACTIVITY_REQUEST = 1;
	public static final int EDIT_MESSAGE_ACTIVITY_REQUEST = 2;

	protected DatabaseHelper dbHelper;
	protected Preferences preference;
	protected int nextAlarmId = 0;

	protected SimpleAdapter adapter;
	protected SwipeListView swipeListView;
	protected List<Map<String, Object>> msgList = new ArrayList<Map<String, Object>>();
	protected List<SeniorInfoAutoMessage> msgContentList = new LinkedList<SeniorInfoAutoMessage>();

	protected SetupButton addCircleMsgBtn, addOneoffMsgBtn, addMedicalMsgBtn;

	protected int isOpenedViewId = -1;

	protected int oid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_msg_display_activity);
		dbHelper = new DatabaseHelper(this);

		preference = new Preferences(this);
		this.oid = getIntent().getIntExtra("oid", -1);

		swipeListView = (SwipeListView) findViewById(R.id.automsgdsp_list);
		adapter = new SimpleAdapter(this, msgList, R.layout.message_list_content, new String[] { "figure", "name", "date", "time", "content",
				"remove" }, new int[] { R.id.automsgdsp_list_figure, R.id.automsgdsp_list_name_text, R.id.automsgdsp_list_date_text,
				R.id.automsgdsp_list_time_text, R.id.automsgdsp_list_content_text, R.id.automsgdsp_list_remove_btn });

		adapter.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, final Object data, String textRepresentation) {
				switch (view.getId()) {
				case R.id.automsgdsp_list_remove_btn:
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							for (int i = 0; i < msgList.size(); ++i) {
								if (msgList.get(i).get("remove").equals(data)) {
									int id = (Integer) msgList.get(i).get("id");
									dbHelper.deleteAutoMessage(id); // 未取消闹钟
									msgList.remove(i);
									adapter.notifyDataSetChanged();
									swipeListView.closeOpenedItems();
									break;
								}
							}
						}
					});
					break;
				case R.id.automsgdsp_list_figure:
					((ImageView) view).setImageResource((Integer) data);
					break;
				case R.id.automsgdsp_list_name_text:
					((TextView) view).setText((String) data);
					break;
				case R.id.automsgdsp_list_content_text:
					((TextView) view).setText((String) data);
					break;
				case R.id.automsgdsp_list_date_text:
					((TextView) view).setText((String) data);
					break;
				case R.id.automsgdsp_list_time_text:
					((TextView) view).setText((String) data);
					break;
				}
				return true;
			}
		});

		swipeListView.setAdapter(adapter);

		swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {

			@Override
			public void onClickFrontView(int position) {
				Log.e("AutoMsgDisplayActivity", "消息被点击" + position + "， 队列长度:" + msgContentList.size());
				if (position >= msgContentList.size()) {
					Log.e("AutoMsgDisplayActivity", "消息被点击,但超出范围" + position + "， 队列长度:" + msgContentList.size());
					return;
				}

				SeniorInfoAutoMessage siam = msgContentList.get(position);
				Calendar cal = Calendar.getInstance();
				cal.set(siam.getYear(), siam.getMonth(), siam.getDay(), siam.getHour(), siam.getMinute());

				if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
					Toast.makeText(AutoMsgDisplayActivity.this, getText(R.string.automsg_message_pasted), Toast.LENGTH_SHORT).show();
					getMsg();
					adapter.notifyDataSetChanged();
					swipeListView.closeOpenedItems();
					return;
				}

				Intent intent = new Intent(AutoMsgDisplayActivity.this, AutoMsgEditActivity.class);
				intent.putExtra("type", EDIT_MESSAGE_ACTIVITY_REQUEST);
				intent.putExtra("content", siam);
				AutoMsgDisplayActivity.this.startActivityForResult(intent, EDIT_MESSAGE_ACTIVITY_REQUEST);
			}

			@Override
			public void onOpened(int position, boolean toRight) {
				isOpenedViewId = position;
			}

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				if (isOpenedViewId != -1) {
					swipeListView.closeOpenedItems();
				}
			}

			@Override
			public void onClosed(int position, boolean fromRight) {
				isOpenedViewId = -1;
			}
		});

		addCircleMsgBtn = (SetupButton) findViewById(R.id.automsgdsp_addcirclemsg_btn);
		addOneoffMsgBtn = (SetupButton) findViewById(R.id.automsgdsp_addoneoffmsg_btn);
		addMedicalMsgBtn = (SetupButton) findViewById(R.id.automsgdsp_addmedmsg_btn);

		addCircleMsgBtn.setText(getText(R.string.automsgdsp_addcirclemsg_text));
		addOneoffMsgBtn.setText(getText(R.string.automsgdsp_addoneoffmsg_text));
		addMedicalMsgBtn.setText(getText(R.string.automsgdsp_addmedicalmsg_text));

		addCircleMsgBtn.setOnClickListener(autoMsgDspListener);
		addOneoffMsgBtn.setOnClickListener(autoMsgDspListener);
		addMedicalMsgBtn.setOnClickListener(autoMsgDspListener);
	}

	private OnClickListener autoMsgDspListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(AutoMsgDisplayActivity.this, AutoMsgEditActivity.class);
			intent.putExtra("type", NEW_MESSAGE_ACTIVITY_REQUEST);
			intent.putExtra("next_alarm_id", nextAlarmId);
			intent.putExtra("oid", oid);
			switch (v.getId()) {
			case R.id.automsgdsp_addcirclemsg_btn:
				intent.putExtra("add_type", MessageFrame.USERMESSAGETYPE_AUTO_CIRCLE_MESSAGE);
				break;
			case R.id.automsgdsp_addoneoffmsg_btn:
				intent.putExtra("add_type", MessageFrame.USERMESSAGETYPE_AUTO_ONEOFF_MESSAGE);
				break;
			case R.id.automsgdsp_addmedmsg_btn:
				intent.putExtra("add_type", MessageFrame.USERMESSAGETYPE_AUTO_MEDICAL_MESSAGE);
				break;
			}
			AutoMsgDisplayActivity.this.startActivityForResult(intent, NEW_MESSAGE_ACTIVITY_REQUEST);
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		this.getMsg();
		adapter.notifyDataSetChanged();
		swipeListView.closeOpenedItems();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected List<Map<String, Object>> getMsg() {
		msgList.clear();
		msgContentList.clear();
		nextAlarmId = 0;
		Cursor messageCursor = dbHelper.getAutoMessage(null, "oid = ?", new String[] { String.valueOf(oid) });
		// List<Map<String, Object>> msgList = new ArrayList<Map<String,
		// Object>>();
		while (messageCursor.moveToNext()) {
			if (!messageCursor.getString(messageCursor.getColumnIndex("login_id")).equals(ServiceCore.getLoginId())) {
				continue;
			}
			Map<String, Object> msg = new HashMap<String, Object>();
			int id = messageCursor.getInt(messageCursor.getColumnIndex("id"));
			msg.put("id", id);
			if (id > nextAlarmId) {
				nextAlarmId = id;
			}
			SeniorInfoAutoMessage siam = new SeniorInfoAutoMessage(id, messageCursor.getInt(messageCursor.getColumnIndex("oid")),
					messageCursor.getString(messageCursor.getColumnIndex("name")), messageCursor.getString(messageCursor.getColumnIndex("nick")),
					messageCursor.getString(messageCursor.getColumnIndex("content")), messageCursor.getInt(messageCursor.getColumnIndex("hour")),
					messageCursor.getInt(messageCursor.getColumnIndex("minute")));
			siam.setLoginId(messageCursor.getString(messageCursor.getColumnIndex("login_id")));
			siam.setSex(messageCursor.getInt(messageCursor.getColumnIndex("sex")));
			siam.setType(messageCursor.getInt(messageCursor.getColumnIndex("type")));
			switch (siam.getType()) {
			case MessageFrame.USERMESSAGETYPE_AUTO_ONEOFF_MESSAGE:
			case MessageFrame.USERMESSAGETYPE_AUTO_MEDICAL_MESSAGE:
				siam.setYear(messageCursor.getInt(messageCursor.getColumnIndex("year")));
				siam.setMonth(messageCursor.getInt(messageCursor.getColumnIndex("month")));
				siam.setDay(messageCursor.getInt(messageCursor.getColumnIndex("day")));

				Calendar calendar = Calendar.getInstance();
				calendar.set(siam.getYear(), siam.getMonth(), siam.getDay(), siam.getHour(), siam.getMinute());
				if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
					dbHelper.deleteAutoMessage(siam.getId());
				}
				break;
			}

			if (siam.getSex() == 1) {
				msg.put("figure", R.drawable.medical_card_figure_male);
			} else if (siam.getSex() == 2) {
				msg.put("figure", R.drawable.medical_card_figure_female);
			}

			if (siam.getNick() != null && siam.getNick().length() > 0) {
				msg.put("name", siam.getNick());
			} else if (siam.getName() != null) {
				msg.put("name", siam.getName());
			}

			msg.put("time", siam.getHour() + ":" + siam.getMinute());

			switch (siam.getType()) {
			case MessageFrame.USERMESSAGETYPE_AUTO_ONEOFF_MESSAGE:
			case MessageFrame.USERMESSAGETYPE_AUTO_MEDICAL_MESSAGE:
				int month = siam.getMonth() + 1;
				msg.put("date", siam.getYear() + "-" + month + "-" + siam.getDay());
				break;
			case MessageFrame.USERMESSAGETYPE_AUTO_CIRCLE_MESSAGE:
				msg.put("date", getText(R.string.automsgdsp_circlemsg_datetext));
				break;
			}

			msg.put("content", siam.getContent());
			msg.put("remove", siam.getId());
			Log.e("AutoMsgDisplayActivity.getMsg", siam.toString());
			msgList.add(msg);
			msgContentList.add(siam);

		}

		messageCursor.close();
		dbHelper.close();
		return msgList;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			this.getMsg();
			adapter.notifyDataSetChanged();
			swipeListView.closeOpenedItems();

			switch (requestCode) {
			case NEW_MESSAGE_ACTIVITY_REQUEST:
				nextAlarmId++;
				break;
			}
		}
	}
}
