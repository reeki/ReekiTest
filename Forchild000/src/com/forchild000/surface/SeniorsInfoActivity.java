package com.forchild000.surface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestDeleteSenior;
import com.forchild.data.SeniorInfoComplete;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;
import com.forchild.server.StatesIntent;

public class SeniorsInfoActivity extends AliveBaseActivity {
	private SetupButton addSeniorBtn, regSeniorBtn;
	protected DatabaseHelper dbHelper;
	protected List<SeniorInfoComplete> seniorsList = new ArrayList<SeniorInfoComplete>();

	private List<ContentButton> seniorBtnList = new LinkedList<ContentButton>();
	private List<View> blockList = new LinkedList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seniors_info_activity);

		dbHelper = new DatabaseHelper(this);

		seniorBtnList.add((ContentButton) findViewById(R.id.seniors_first_btn));
		seniorBtnList.add((ContentButton) findViewById(R.id.seniors_second_btn));
		seniorBtnList.add((ContentButton) findViewById(R.id.seniors_third_btn));
		seniorBtnList.add((ContentButton) findViewById(R.id.seniors_fourth_btn));
		seniorBtnList.add((ContentButton) findViewById(R.id.seniors_fifth_btn));
		seniorBtnList.add((ContentButton) findViewById(R.id.seniors_sixth_btn));

		for (ContentButton seniorBtnBuf : seniorBtnList) {
			seniorBtnBuf.setOnClickListener(seniorsInfoButtonListener);
			seniorBtnBuf.setOnLongClickListener(seniorsInfoLongListener);
		}

		blockList.add(findViewById(R.id.seniors_block_0));
		blockList.add(findViewById(R.id.seniors_block_1));
		blockList.add(findViewById(R.id.seniors_block_2));
		blockList.add(findViewById(R.id.seniors_block_3));
		blockList.add(findViewById(R.id.seniors_block_4));
		blockList.add(findViewById(R.id.seniors_block_5));

		addSeniorBtn = (SetupButton) findViewById(R.id.seniors_add_seniors);
		regSeniorBtn = (SetupButton) findViewById(R.id.seniors_reg_seniors);
		addSeniorBtn.setVisibility(View.GONE);
		regSeniorBtn.setVisibility(View.GONE);

		// addSeniorBtn.setText(getText(R.string.seniorinfo_add_senior_btn_text));
		// regSeniorBtn.setText(getText(R.string.seniorinfo_reg_senior_btn_text));
		//
		// addSeniorBtn.setOnClickListener(seniorsInfoButtonListener);
		// regSeniorBtn.setOnClickListener(seniorsInfoButtonListener);
		this.registerReceiver(seniorsInfoReceiver, new IntentFilter("com.forchild.seniorinfo.refreshui"));
	}

	private BroadcastReceiver seniorsInfoReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			// from ServiceChildInformationProcess¡£java
			if (intent.getAction().equals("com.forchild.seniorinfo.refreshui")) {
				SeniorsInfoActivity.this.reflushUI();
			}
		}

	};

	protected void reflushUI() {
		seniorsList.clear();

		Cursor seniorsInfo = dbHelper.getSeniorInfo(null, null, null);
		while (seniorsInfo.moveToNext()) {
			seniorsList.add(new SeniorInfoComplete(seniorsInfo));
		}
		seniorsInfo.close();
		dbHelper.close();
		//
		// if (seniorsList.size() >= 6) {
		// addSeniorBtn.setVisibility(View.GONE);
		// regSeniorBtn.setVisibility(View.GONE);
		// }

		for (int i = 0; i < 6; ++i) {
			if (i < seniorsList.size()) {
				ContentButton seniorBtnBuffer = seniorBtnList.get(i);
				SeniorInfoComplete seniorInfoBuffer = seniorsList.get(i);
				String nick = seniorInfoBuffer.getNick();
				String name = seniorInfoBuffer.getName();
				String code = seniorInfoBuffer.getCode();
				if (nick != null && nick.length() > 0) {
					seniorBtnBuffer.setTitle(nick);
				} else if (name != null) {
					seniorBtnBuffer.setTitle(name);
				} else if (seniorInfoBuffer.getOid() != BaseProtocolFrame.INT_INITIATION) {
					dbHelper.deleteSeniorInfo("oid = ?", new String[] { seniorInfoBuffer.getOid() + "" });
					seniorBtnList.get(i).setVisibility(View.GONE);
					blockList.get(i).setVisibility(View.GONE);
					continue;
				} else {
					Log.e("SeniorsInfoActivity.reflushUI", "add seniors button, seniors information error, id:" + seniorBtnBuffer.getId());
					seniorBtnList.get(i).setVisibility(View.GONE);
					blockList.get(i).setVisibility(View.GONE);
					continue;
				}
				seniorBtnBuffer.setText(seniorInfoBuffer.getCode());
				seniorBtnBuffer.setVisibility(View.VISIBLE);
				blockList.get(i).setVisibility(View.VISIBLE);

				if (code != null) {
					seniorBtnBuffer.setText(code);
				}
			} else {
				seniorBtnList.get(i).setVisibility(View.GONE);
				blockList.get(i).setVisibility(View.GONE);
			}
		}
	}

	private OnClickListener seniorsInfoButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.seniors_first_btn:
				if (seniorsList.size() > 0) {
					intent.setClass(SeniorsInfoActivity.this, SeniorsInfoModifyActivity.class);
					intent.putExtra("content", seniorsList.get(0));
					SeniorsInfoActivity.this.startActivity(intent);
				} else {
					seniorBtnList.get(0).setVisibility(View.GONE);
					blockList.get(0).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_second_btn:
				if (seniorsList.size() > 1) {
					intent.setClass(SeniorsInfoActivity.this, SeniorsInfoModifyActivity.class);
					intent.putExtra("content", seniorsList.get(1));
					SeniorsInfoActivity.this.startActivity(intent);
				} else {
					seniorBtnList.get(1).setVisibility(View.GONE);
					blockList.get(1).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_third_btn:
				if (seniorsList.size() > 2) {
					intent.setClass(SeniorsInfoActivity.this, SeniorsInfoModifyActivity.class);
					intent.putExtra("content", seniorsList.get(2));
					SeniorsInfoActivity.this.startActivity(intent);
				} else {
					seniorBtnList.get(2).setVisibility(View.GONE);
					blockList.get(2).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_fourth_btn:
				if (seniorsList.size() > 3) {
					intent.setClass(SeniorsInfoActivity.this, SeniorsInfoModifyActivity.class);
					intent.putExtra("content", seniorsList.get(3));
					SeniorsInfoActivity.this.startActivity(intent);
				} else {
					seniorBtnList.get(3).setVisibility(View.GONE);
					blockList.get(3).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_fifth_btn:
				if (seniorsList.size() > 4) {
					intent.setClass(SeniorsInfoActivity.this, SeniorsInfoModifyActivity.class);
					intent.putExtra("content", seniorsList.get(4));
					SeniorsInfoActivity.this.startActivity(intent);
				} else {
					seniorBtnList.get(4).setVisibility(View.GONE);
					blockList.get(4).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_sixth_btn:
				if (seniorsList.size() > 5) {
					intent.setClass(SeniorsInfoActivity.this, SeniorsInfoModifyActivity.class);
					intent.putExtra("content", seniorsList.get(5));
					SeniorsInfoActivity.this.startActivity(intent);
				} else {
					seniorBtnList.get(5).setVisibility(View.GONE);
					blockList.get(5).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_add_seniors:
				intent.setClass(SeniorsInfoActivity.this, AddSeniorActivity.class);
				SeniorsInfoActivity.this.startActivity(intent);
				break;
			case R.id.seniors_reg_seniors:
				intent.setClass(SeniorsInfoActivity.this, RegisterSeniorActivity.class);
				SeniorsInfoActivity.this.startActivity(intent);
				break;
			default:
				break;
			}
		}

	};

	private OnLongClickListener seniorsInfoLongListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			Intent intent = new Intent(SeniorsInfoActivity.this, WarningActivity.class);
			intent.putExtra("type", WarningActivity.WARNING_TYPE_DELETE_SENIOR);
			switch (v.getId()) {
			case R.id.seniors_first_btn:
				if (seniorsList.size() > 0) {
					intent.putExtra("senior_info", seniorsList.get(0));
					SeniorsInfoActivity.this.startActivityForResult(intent, WarningActivity.WARNING_TYPE_DELETE_SENIOR);
				} else {
					seniorBtnList.get(0).setVisibility(View.GONE);
					blockList.get(0).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_second_btn:
				if (seniorsList.size() > 1) {
					intent.putExtra("senior_info", seniorsList.get(0));
					SeniorsInfoActivity.this.startActivityForResult(intent, WarningActivity.WARNING_TYPE_DELETE_SENIOR);
				} else {
					seniorBtnList.get(1).setVisibility(View.GONE);
					blockList.get(1).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_third_btn:
				if (seniorsList.size() > 2) {
					intent.putExtra("senior_info", seniorsList.get(0));
					SeniorsInfoActivity.this.startActivityForResult(intent, WarningActivity.WARNING_TYPE_DELETE_SENIOR);
				} else {
					seniorBtnList.get(2).setVisibility(View.GONE);
					blockList.get(2).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_fourth_btn:
				if (seniorsList.size() > 3) {
					intent.putExtra("senior_info", seniorsList.get(0));
					SeniorsInfoActivity.this.startActivityForResult(intent, WarningActivity.WARNING_TYPE_DELETE_SENIOR);
				} else {
					seniorBtnList.get(3).setVisibility(View.GONE);
					blockList.get(3).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_fifth_btn:
				if (seniorsList.size() > 4) {
					intent.putExtra("senior_info", seniorsList.get(0));
					SeniorsInfoActivity.this.startActivityForResult(intent, WarningActivity.WARNING_TYPE_DELETE_SENIOR);
				} else {
					seniorBtnList.get(4).setVisibility(View.GONE);
					blockList.get(4).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_sixth_btn:
				if (seniorsList.size() > 5) {
					intent.putExtra("senior_info", seniorsList.get(0));
					SeniorsInfoActivity.this.startActivityForResult(intent, WarningActivity.WARNING_TYPE_DELETE_SENIOR);
				} else {
					seniorBtnList.get(5).setVisibility(View.GONE);
					blockList.get(5).setVisibility(View.GONE);
				}
				break;
			default:
				break;
			}
			return false;
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case WarningActivity.WARNING_TYPE_DELETE_SENIOR:
				Serializable seniorBean = data.getSerializableExtra("senior_info");
				if (seniorBean instanceof SeniorInfoComplete) {
					SeniorInfoComplete seniorInfo = (SeniorInfoComplete) seniorBean;
					for (int i = 0; i < seniorsList.size(); ++i) {
						if (seniorInfo.equals(seniorsList.get(i))) {
							// dbHelper.deleteSeniorInfo("oid = ? AND id = ?",
							// new String[] { seniorInfo.getOid() + "",
							// seniorInfo.getId() + "" });
							// this.reflushUI();
							if (!ServiceStates.getForchildServiceState(SeniorsInfoActivity.this)) {
								Intent intent = new Intent(SeniorsInfoActivity.this, ServiceCore.class);
								SeniorsInfoActivity.this.startService(intent);
							}
							Preferences preference = new Preferences(SeniorsInfoActivity.this);
							RequestDeleteSenior rds = new RequestDeleteSenior(preference.getSid(), seniorInfo.getOid());
							ServiceCore.addNetTask(rds);
						}
					}
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
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(seniorsInfoReceiver);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.reflushUI();

	}

}
