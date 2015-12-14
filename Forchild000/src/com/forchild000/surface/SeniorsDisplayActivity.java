package com.forchild000.surface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.forchild.data.RequestDeleteSenior;
import com.forchild.data.SeniorInfoAutoMessage;
import com.forchild.data.SeniorInfoComplete;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;
import com.forchild.server.TimeFormat;
import com.fortysevendeg.android.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.android.swipelistview.SwipeListView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;

public class SeniorsDisplayActivity extends AliveBaseActivity {
	protected SwipeListView mListView;
	protected SimpleAdapter adapter;
	protected List<Map<String, Object>> msgList = new ArrayList<Map<String, Object>>();
	protected List<SeniorInfoComplete> seniorsList = new LinkedList<SeniorInfoComplete>();
	protected DatabaseHelper dbHelper;
	protected int isOpenedViewId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seniors_display_activity);

		dbHelper = new DatabaseHelper(this);

		mListView = (SwipeListView) findViewById(R.id.seniorsdisplay_list);
		adapter = new SimpleAdapter(this, msgList, R.layout.message_list_content, new String[] { "figure", "name", "content", "remove" }, new int[] {
				R.id.automsgdsp_list_figure, R.id.automsgdsp_list_name_text, R.id.automsgdsp_list_content_text, R.id.automsgdsp_list_remove_btn });

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
									Intent intent = new Intent(SeniorsDisplayActivity.this, WarningActivity.class);
									intent.putExtra("type", WarningActivity.WARNING_TYPE_DELETE_SENIOR);
									intent.putExtra("senior_info", seniorsList.get(i));
									SeniorsDisplayActivity.this.startActivityForResult(intent, WarningActivity.WARNING_TYPE_DELETE_SENIOR);
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
				}
				return true;
			}
		});

		mListView.setAdapter(adapter);

		mListView.setSwipeListViewListener(new BaseSwipeListViewListener() {

			@Override
			public void onClickFrontView(int position) {
				if (position >= seniorsList.size()) {
					return;
				}
				Intent intent = new Intent(SeniorsDisplayActivity.this, SeniorsInfoModifyActivity.class);
				intent.putExtra("content", seniorsList.get(position));
				SeniorsDisplayActivity.this.startActivity(intent);
			}

			@Override
			public void onOpened(int position, boolean toRight) {
				isOpenedViewId = position;
			}

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				if (isOpenedViewId != -1) {
					mListView.closeOpenedItems();
				}
			}

			@Override
			public void onClosed(int position, boolean fromRight) {
				isOpenedViewId = -1;
			}
		});

		this.registerReceiver(seniorsDisplayReceiver, new IntentFilter("com.forchild.seniorinfo.refreshui"));
	}

	protected BroadcastReceiver seniorsDisplayReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("com.forchild.seniorinfo.refreshui")) {
				getSeniorsInfo();
				adapter.notifyDataSetChanged();
				mListView.closeOpenedItems();
			}
		}

	};

	protected List<Map<String, Object>> getSeniorsInfo() {
		msgList.clear();
		seniorsList.clear();
		Cursor seniorsCursor = dbHelper.getSeniorInfo(null, "belongs = ?", new String[] { ServiceCore.getLoginId() });
		while (seniorsCursor.moveToNext()) {
			SeniorInfoComplete seniorInfo = new SeniorInfoComplete(seniorsCursor);
			seniorsList.add(seniorInfo);
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

			String code = seniorsCursor.getString(seniorsCursor.getColumnIndex("code"));
			if (code != null && code.length() > 0) {
				seniorsBuff.put("content", "µÇÂ½Âë:" + code);
			}

			msgList.add(seniorsBuff);
		}
		seniorsCursor.close();
		dbHelper.close();

		return msgList;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case WarningActivity.WARNING_TYPE_DELETE_SENIOR:
				Serializable seniorBean = data.getSerializableExtra("senior_info");
				if (seniorBean instanceof SeniorInfoComplete) {
					SeniorInfoComplete seniorInfo = (SeniorInfoComplete) seniorBean;
					if (!ServiceStates.getForchildServiceState(SeniorsDisplayActivity.this)) {
						Intent intent = new Intent(SeniorsDisplayActivity.this, ServiceCore.class);
						SeniorsDisplayActivity.this.startService(intent);
					}
					RequestDeleteSenior rds = new RequestDeleteSenior(ServiceCore.getSid(), seniorInfo.getOid());
					ServiceCore.addNetTask(rds);
				} else {
					Log.e("SeniorsInfoActivity.onActivityResult", "delete senior exception, intent without senior information");
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.getSeniorsInfo();
		adapter.notifyDataSetChanged();
		mListView.closeOpenedItems();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(seniorsDisplayReceiver);
	}

}
