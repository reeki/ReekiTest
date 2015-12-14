package com.forchild000.surface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.forchild.data.SeniorInfoComplete;
import com.forchild.server.DatabaseHelper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ChangeSeniorPhoneActivity extends AliveBaseActivity {
	protected DatabaseHelper dbHelper;
	protected List<SeniorInfoComplete> seniorsList = new ArrayList<SeniorInfoComplete>();
	private List<ContentButton> seniorBtnList = new LinkedList<ContentButton>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seniors_info_activity);

		dbHelper = new DatabaseHelper(this);

		findViewById(R.id.seniors_add_seniors).setVisibility(View.GONE);
		findViewById(R.id.seniors_reg_seniors).setVisibility(View.GONE);

		seniorBtnList.add((ContentButton) findViewById(R.id.seniors_first_btn));
		seniorBtnList.add((ContentButton) findViewById(R.id.seniors_second_btn));
		seniorBtnList.add((ContentButton) findViewById(R.id.seniors_third_btn));
		seniorBtnList.add((ContentButton) findViewById(R.id.seniors_fourth_btn));
		seniorBtnList.add((ContentButton) findViewById(R.id.seniors_fifth_btn));
		seniorBtnList.add((ContentButton) findViewById(R.id.seniors_sixth_btn));

		for (ContentButton seniorBtnBuf : seniorBtnList) {
			seniorBtnBuf.setOnClickListener(seniorsInfoButtonListener);
		}

	}

	private OnClickListener seniorsInfoButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.seniors_first_btn:
				if (seniorsList.size() > 0) {
					intent.setClass(ChangeSeniorPhoneActivity.this, ChangeSeniorPhoneEditActivity.class);
					intent.putExtra("content", seniorsList.get(0));
					ChangeSeniorPhoneActivity.this.startActivity(intent);
				} else {
					seniorBtnList.get(0).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_second_btn:
				if (seniorsList.size() > 1) {
					intent.setClass(ChangeSeniorPhoneActivity.this, ChangeSeniorPhoneEditActivity.class);
					intent.putExtra("content", seniorsList.get(1));
					ChangeSeniorPhoneActivity.this.startActivity(intent);
				} else {
					seniorBtnList.get(1).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_third_btn:
				if (seniorsList.size() > 2) {
					intent.setClass(ChangeSeniorPhoneActivity.this, ChangeSeniorPhoneEditActivity.class);
					intent.putExtra("content", seniorsList.get(2));
					ChangeSeniorPhoneActivity.this.startActivity(intent);
				} else {
					seniorBtnList.get(2).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_fourth_btn:
				if (seniorsList.size() > 3) {
					intent.setClass(ChangeSeniorPhoneActivity.this, ChangeSeniorPhoneEditActivity.class);
					intent.putExtra("content", seniorsList.get(3));
					ChangeSeniorPhoneActivity.this.startActivity(intent);
				} else {
					seniorBtnList.get(3).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_fifth_btn:
				if (seniorsList.size() > 4) {
					intent.setClass(ChangeSeniorPhoneActivity.this, ChangeSeniorPhoneEditActivity.class);
					intent.putExtra("content", seniorsList.get(4));
					ChangeSeniorPhoneActivity.this.startActivity(intent);
				} else {
					seniorBtnList.get(4).setVisibility(View.GONE);
				}
				break;
			case R.id.seniors_sixth_btn:
				if (seniorsList.size() > 5) {
					intent.setClass(ChangeSeniorPhoneActivity.this, ChangeSeniorPhoneEditActivity.class);
					intent.putExtra("content", seniorsList.get(5));
					ChangeSeniorPhoneActivity.this.startActivity(intent);
				} else {
					seniorBtnList.get(5).setVisibility(View.GONE);
				}
				break;
			default:
				break;
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
				} else if (seniorInfoBuffer.getOid() != -1) {
					dbHelper.deleteSeniorInfo("oid = ?", new String[] { seniorInfoBuffer.getOid() + "" });
					seniorBtnList.get(i).setVisibility(View.GONE);
					continue;
				} else {
					Log.e("ChangeSeniorPhoneActivity.reflushUI", "add seniors button, seniors information error, id:" + seniorBtnBuffer.getId());
					seniorBtnList.get(i).setVisibility(View.GONE);
					continue;
				}

				if (code != null && code.length() > 0) {
					seniorBtnBuffer.setText(code);
					seniorBtnBuffer.setVisibility(View.VISIBLE);
				} else {
					seniorBtnBuffer.setVisibility(View.GONE);
				}
			} else {
				seniorBtnList.get(i).setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		reflushUI();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
