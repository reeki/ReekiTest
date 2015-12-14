package com.forchild000.surface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild.server.TimeFormat;

public class MsgActivity extends AliveBaseActivity {
	protected ListView userListView;
	protected SimpleAdapter adapter;
	protected List<Map<String, Object>> msgList = new ArrayList<Map<String, Object>>();
	protected TextView titleCenterText;

	protected DatabaseHelper dbHelper;
	protected Preferences preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_activity);

		titleCenterText = (TextView) findViewById(R.id.msg_title_center_text);
		titleCenterText.setText(R.string.msg_centertitle_text);

		dbHelper = new DatabaseHelper(this);
		preference = new Preferences(this);

		userListView = (ListView) findViewById(R.id.msg_user_list);
		adapter = new SimpleAdapter(this, getUserMessage(), R.layout.message_list_content, new String[] { "figure", "name", "time", "content" },
				new int[] { R.id.automsgdsp_list_figure, R.id.automsgdsp_list_name_text, R.id.automsgdsp_list_time_text,
						R.id.automsgdsp_list_content_text });
		userListView.setAdapter(adapter);
		userListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Map<String, Object> seniorBuff = msgList.get(position);
				Intent intent = new Intent(MsgActivity.this, MsgPersonActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("oid", (Integer) seniorBuff.get("oid"));
				intent.putExtra("name", (String) seniorBuff.get("name"));
				intent.putExtra("sex", (Integer) seniorBuff.get("sex"));
				MsgActivity.this.startActivity(intent);
			}
		});
	}

	protected List<Map<String, Object>> getUserMessage() {
		msgList.clear();
		Log.e("MsgActivity", "login_id:" + ServiceCore.getLoginId());
		// Cursor seniorsCursor = dbHelper.getSeniorInfo(new String[] { "oid",
		// "sex", "name", "nick", "id" }, "belongs = ?",
		// new String[] { ServiceCore.getLoginId() });
		Cursor seniorsCursor = dbHelper.getSeniorInfo(new String[] { "oid", "sex", "name", "nick", "id", "belongs" }, null, null);
		while (seniorsCursor.moveToNext()) {
			Map<String, Object> seniorsBuff = new HashMap<String, Object>();
			seniorsBuff.put("oid", seniorsCursor.getInt(seniorsCursor.getColumnIndex("oid")));
			int sex = seniorsCursor.getInt(seniorsCursor.getColumnIndex("sex"));
			if (sex == 1) {
				seniorsBuff.put("figure", R.drawable.medical_card_figure_male);
			} else if (sex == 2) {
				seniorsBuff.put("figure", R.drawable.medical_card_figure_female);
			} else {
				seniorsBuff.put("figure", R.drawable.ic_launcher);
			}
			String nick = seniorsCursor.getString(seniorsCursor.getColumnIndex("nick"));
			String name = seniorsCursor.getString(seniorsCursor.getColumnIndex("name"));
			if (nick != null && nick.length() > 0) {
				seniorsBuff.put("name", nick);
			} else if (name != null) {
				seniorsBuff.put("name", name);
			}
			seniorsBuff.put("sex", sex);
			msgList.add(seniorsBuff);
			Log.e("MsgActivity", "查询到老人信息:" + name + ", oid:" + seniorsBuff.get("oid") + ", login_id:" + seniorsCursor.getString(seniorsCursor.getColumnIndex("belongs")));
		}
		seniorsCursor.close();
		dbHelper.close();
		Cursor messageCursor = dbHelper.getLastMessage(ServiceCore.getLoginId());
		while (messageCursor.moveToNext()) {
			int oid = messageCursor.getInt(messageCursor.getColumnIndex("oid"));
			for (Map<String, Object> msgBuff : msgList) {
				if (((Integer) msgBuff.get("oid")).intValue() == oid) {
					msgBuff.put("time", TimeFormat.getDisplayDate(messageCursor.getLong(messageCursor.getColumnIndex("Max(date)"))));
					msgBuff.put("content", messageCursor.getString(messageCursor.getColumnIndex("content")));
					break;
				}
			}
		}

		return msgList;
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.getUserMessage();
		adapter.notifyDataSetChanged();
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
