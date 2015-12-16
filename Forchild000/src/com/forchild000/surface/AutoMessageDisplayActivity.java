package com.forchild000.surface;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.forchild.data.SeniorInfoAutoMessage;
import com.forchild.frame.SlideButtonListener;
import com.forchild.server.AutoMsgHelper;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild.server.StatesIntent;

/***
 * @deprecated
 * @author Reeki
 * 
 */
public class AutoMessageDisplayActivity extends AliveBaseActivity {
	public static final int DISPLAY_LIST_VIEWID = 60000;
	public static final int NEW_MESSAGE_ACTIVITY_REQUEST = 1;
	public static final int EDIT_MESSAGE_ACTIVITY_REQUEST = 2;

	private ScrollView frameUI;
	private SlideListView displayList;
	protected DatabaseHelper dbHelper;
	protected int nextAlarmId = 0;
	protected Preferences preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_message_display_activity);

		frameUI = (ScrollView) findViewById(R.id.auto_msg_dsp_frame);

		dbHelper = new DatabaseHelper(this);

		displayList = new SlideListView(this);
		displayList.setId(DISPLAY_LIST_VIEWID);
		displayList.setBottomButtonText(getText(R.string.automsgdsp_add_new_message_text));
		displayList.setPublicListener(new SlideButtonAction(this, null));

		frameUI.addView(displayList, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));

	}

	protected class SlideButtonAction implements SlideButtonListener {
		protected Activity context;
		protected SeniorInfoAutoMessage seniorInfoMsg;

		public SlideButtonAction(Activity context, SeniorInfoAutoMessage siam) {
			this.context = context;
			this.seniorInfoMsg = siam;
		}

		@Override
		public void onClick(SlideButton v) {
			Intent intent = new Intent(context, AutoMessageEditActivity.class);
			intent.putExtra("type", EDIT_MESSAGE_ACTIVITY_REQUEST);
			intent.putExtra("senior_info", seniorInfoMsg);
			intent.putExtra("from", "AutoMessageDisplayActivity.SlideButtonAction.onClick");
			intent.putExtra("view_id", v.getViewId());
			context.startActivityForResult(intent, EDIT_MESSAGE_ACTIVITY_REQUEST);
		}

		@Override
		public void onScroll(SlideButton v) {

		}

		@Override
		public void onButtonClick(SlideButton v) {
			if (seniorInfoMsg != null) {
				DatabaseHelper db = new DatabaseHelper(context);
				db.deleteAutoMessage(seniorInfoMsg.getId());
				Log.e("AutoMessageDisplayActivity.SlideButtonAction.onButtonClick", "msg id:" + seniorInfoMsg.getId());
				AutoMsgHelper.cancelAutoMsgAlarm(context, seniorInfoMsg.getId());
			} else {
				Log.e("AutoMessageDisplayActivity.SlideButtonAction.onButtonClick", "seniorInfoMsg is null");
			}
		}

		@Override
		public void onBottomButtonClick(SlideListView v) {
			Intent intent = new Intent(context, AutoMessageEditActivity.class);
			intent.putExtra("type", NEW_MESSAGE_ACTIVITY_REQUEST);
			intent.putExtra("from", "AutoMessageDisplayActivity.SlideButtonAction.onBottomButtonClick");
			intent.putExtra("next_alarm_id", nextAlarmId);
			context.startActivityForResult(intent, NEW_MESSAGE_ACTIVITY_REQUEST);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			SeniorInfoAutoMessage siam = (SeniorInfoAutoMessage) data.getSerializableExtra("senior_info");
			if (siam == null)
				return;
			switch (requestCode) {
			case NEW_MESSAGE_ACTIVITY_REQUEST:
				if (siam.getNick() != null && siam.getNick().length() > 0) {
					displayList.addSlideButton(siam.getNick(), siam.getContent(), getText(R.string.automsgdsp_remark_title_text), siam.getHour()
							+ ":" + siam.getMinute(), new SlideButtonAction(this, siam));
				} else if (siam.getName() != null) {
					displayList.addSlideButton(siam.getName(), siam.getContent(), getText(R.string.automsgdsp_remark_title_text), siam.getHour()
							+ ":" + siam.getMinute(), new SlideButtonAction(this, siam));
				}
				break;
			case EDIT_MESSAGE_ACTIVITY_REQUEST:
				int viewId = data.getIntExtra("view_id", -1);
				if (siam.getNick() != null && siam.getNick().length() > 0) {
					displayList.setSlideButton(viewId, siam.getNick(), siam.getContent(), getText(R.string.automsgdsp_remark_title_text),
							siam.getHour() + ":" + siam.getMinute());
				} else if (siam.getName() != null) {
					displayList.setSlideButton(viewId, siam.getName(), siam.getContent(), getText(R.string.automsgdsp_remark_title_text),
							siam.getHour() + ":" + siam.getMinute());
				}

				displayList.setSlideButtonListener(viewId, new SlideButtonAction(this, siam));
				break;
			}
		}
	}

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


		displayList.clearAllSlideButton();
		nextAlarmId = 0;
		// `id` int, `to` int, `name` varchar(16), `nick` varchar(32),
		// `content` varchar(512), `hour` int, `minute` int
		Cursor messageCursor = dbHelper.getAutoMessage();
		while (messageCursor.moveToNext()) {
			int id = messageCursor.getInt(messageCursor.getColumnIndex("id"));
			if (id > nextAlarmId) {
				nextAlarmId = id;
			}
			SeniorInfoAutoMessage siam = new SeniorInfoAutoMessage(id, messageCursor.getInt(messageCursor.getColumnIndex("oid")),
					messageCursor.getString(messageCursor.getColumnIndex("name")), messageCursor.getString(messageCursor.getColumnIndex("nick")),
					messageCursor.getString(messageCursor.getColumnIndex("content")), messageCursor.getInt(messageCursor.getColumnIndex("hour")),
					messageCursor.getInt(messageCursor.getColumnIndex("minute")));
			if (siam.getNick() != null && siam.getNick().length() > 0) {
				displayList.addSlideButton(siam.getNick(), siam.getContent(), getText(R.string.automsgdsp_remark_title_text), siam.getHour() + ":"
						+ siam.getMinute(), new SlideButtonAction(this, siam));
			} else if (siam.getName() != null) {
				displayList.addSlideButton(siam.getName(), siam.getContent(), getText(R.string.automsgdsp_remark_title_text), siam.getHour() + ":"
						+ siam.getMinute(), new SlideButtonAction(this, siam));
			}

			Log.e("AutoMessageDisplayActivity.onResume", "has add button, info:" + siam.toString());
		}
		messageCursor.close();
		dbHelper.close();

	}

}
