package com.forchild000.surface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.forchild.data.AccidentInfo;
import com.forchild.data.MessageHelp;
import com.forchild.data.SOSInfo;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.TimeFormat;
import com.fortysevendeg.android.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.android.swipelistview.SwipeListView;

public class SOSHistoryActivity extends AliveBaseActivity {
	private SwipeListView contentList;
	private SimpleAdapter adapter;
	private DatabaseHelper dbHelper;
	private List<AccidentInfo> accList = new ArrayList<AccidentInfo>();
	protected List<Map<String, Object>> accDispList = new ArrayList<Map<String, Object>>();
	protected int isOpenedViewId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accident_history_activity);

		contentList = (SwipeListView) findViewById(R.id.accident_list);
		dbHelper = new DatabaseHelper(this);

		adapter = new SimpleAdapter(this, accDispList, R.layout.sos_list_content, new String[] { "name", "content", "time", "remove" }, new int[] {
				R.id.sos_list_name_text, R.id.sos_list_content_text, R.id.sos_list_time_text, R.id.sos_list_remove_btn });

		adapter.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, final Object data, String textRepresentation) {
				switch (view.getId()) {
				case R.id.sos_list_remove_btn:
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							for (int i = 0; i < accList.size(); ++i) {
								if (accDispList.get(i).get("remove").equals(data)) {
									Map<String, Object> accMap = accDispList.get(i);
									dbHelper.deleteSOSMessage("id = ? AND date = ?",
											new String[] { String.valueOf(accMap.get("id")), String.valueOf(accMap.get("date")) });
									accDispList.remove(accMap);
									adapter.notifyDataSetChanged();
									contentList.closeOpenedItems();
									break;
								}
							}
						}
					});
					break;
				case R.id.sos_list_time_text:
					((TextView) view).setText((String) data);
					break;
				case R.id.sos_list_name_text:
					((TextView) view).setText((String) data);
					break;
				case R.id.sos_list_content_text:
					((TextView) view).setText((String) data);
					break;
				}
				return true;
			}
		});

		contentList.setAdapter(adapter);

		contentList.setSwipeListViewListener(new BaseSwipeListViewListener() {

			@Override
			public void onClickFrontView(int position) {
				if (position >= accList.size()) {
					return;
				}
				Intent intent = new Intent(SOSHistoryActivity.this, AccidentDisplayActivity.class);
				intent.putExtra("type", AccidentDisplayActivity.ACCIDENT_TYPE_SOS);
				intent.putExtra("accident_info", accList.get(position));
				SOSHistoryActivity.this.startActivity(intent);
			}

			@Override
			public void onOpened(int position, boolean toRight) {
				isOpenedViewId = position;
			}

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				if (isOpenedViewId != -1) {
					contentList.closeOpenedItems();
				}
			}

			@Override
			public void onClosed(int position, boolean fromRight) {
				isOpenedViewId = -1;
			}
		});

		dbHelper = new DatabaseHelper(this);

		this.registerReceiver(messageReceiver, new IntentFilter("com.forchild.messages.sos.display"));
	}

	private BroadcastReceiver messageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// from ServiceUpdateLocationProcess.java
			Serializable bean = intent.getSerializableExtra("bean");
			if (bean != null && bean instanceof MessageHelp) {
				initData();
				adapter.notifyDataSetChanged();
				contentList.closeOpenedItems();
			}
		}

	};

	protected List<Map<String, Object>> initData() {
		accDispList.clear();
		accList.clear();
		Cursor accCursor = dbHelper.getSOSMessage(null, "belongs = ?", new String[] { ServiceCore.getLoginId() });
//		Cursor accCursor = dbHelper.getSOSMessage(null, null, null);
//		Log.e("SOSHistory", accCursor.getCount() + "");

		while (accCursor.moveToNext()) {
			long date = accCursor.getLong(accCursor.getColumnIndex("date"));
			double la = accCursor.getDouble(accCursor.getColumnIndex("la"));
			double lo = accCursor.getDouble(accCursor.getColumnIndex("lo"));
			String uname = accCursor.getString(accCursor.getColumnIndex("uname"));
			String content = accCursor.getString(accCursor.getColumnIndex("content"));
			int id = accCursor.getInt(accCursor.getColumnIndex("id"));
			int sosId = accCursor.getInt(accCursor.getColumnIndex("sos_id"));

			SOSInfo accBuff = new SOSInfo(id, lo, la, date, uname, content, sosId);
			Map<String, Object> accMap = new HashMap<String, Object>();
			accMap.put("name", uname);
			accMap.put("content", content);
			accMap.put("time", TimeFormat.getDisplayDate(date));
			accMap.put("id", id);
			accMap.put("date", date);
			accDispList.add(accMap);
			accList.add(accBuff);
		}
		return accDispList;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(messageReceiver);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
		adapter.notifyDataSetChanged();
		contentList.closeOpenedItems();
	}
}
