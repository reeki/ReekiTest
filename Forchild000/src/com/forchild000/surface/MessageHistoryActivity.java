package com.forchild000.surface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.forchild.data.DateInfo;
import com.forchild.data.MessageAccident;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.StatesIntent;

public class MessageHistoryActivity extends AliveBaseActivity {
	private ListView contentList;
	private ArrayAdapter<String> adapter;
	private DatabaseHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_history_activity);

		contentList = (ListView) findViewById(R.id.msghistory_content_list);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
		contentList.setAdapter(adapter);

		dbHelper = new DatabaseHelper(this);

		this.registerReceiver(messageReceiver, new IntentFilter("com.forchild.messages.history.display"));
	}

	private BroadcastReceiver messageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(messageReceiver);
	}

	@Override
	protected void onPause() {
		super.onPause();

		StatesIntent.sendCloseState(this, ServiceCore.ACTIVITY_TYPE_MESSAGEHISTORY);
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.clear();
		
		Cursor message = dbHelper.getMessage(new String[] { "date", "name", "content" });
		message.moveToPosition(message.getCount());
		while (message.moveToPrevious()) {
			DateInfo dateInfo = new DateInfo(message.getLong(message.getColumnIndex("date")));
			adapter.add(dateInfo.getRTime() + "\n" + message.getString(message.getColumnIndex("name")) + "\n"
					+ message.getString(message.getColumnIndex("content")));
		}
		message.close();
		dbHelper.close();

		StatesIntent.sendAliveState(this, ServiceCore.ACTIVITY_TYPE_MESSAGEHISTORY);
	}

}
