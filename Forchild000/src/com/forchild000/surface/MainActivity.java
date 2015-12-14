package com.forchild000.surface;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

import com.forchild.server.ServiceStates;

public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */
	private TabHost tabHost;
	private TextView newMessageCount;
	protected boolean isAlive = false;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		ServiceCore.aliveMainActivity = this;

		newMessageCount = (TextView) findViewById(R.id.main_tab_new_message);
		newMessageCount.setVisibility(View.INVISIBLE);
		// newMessageCount.setText("10");

		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, ParentStates.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		spec = tabHost.newTabSpec(getText(R.string.btn_page_0).toString()).setIndicator(getText(R.string.btn_page_0)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MsgActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		spec = tabHost.newTabSpec(getText(R.string.btn_page_1).toString()).setIndicator(getText(R.string.btn_page_1)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, HistoryActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		spec = tabHost.newTabSpec(getText(R.string.btn_page_2).toString()).setIndicator(getText(R.string.btn_page_2)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, SetupActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		spec = tabHost.newTabSpec(getText(R.string.btn_page_3).toString()).setIndicator(getText(R.string.btn_page_3)).setContent(intent);
		tabHost.addTab(spec);
//		tabHost.setCurrentTab(0);

		RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.main_tab_group);
		radioGroup.check(R.id.main_tab_btn_page0);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.main_tab_btn_page0:// 关注人状态
					tabHost.setCurrentTabByTag(getText(R.string.btn_page_0).toString());
					break;
				case R.id.main_tab_btn_page1:// 历史
					tabHost.setCurrentTabByTag(getText(R.string.btn_page_1).toString());
					break;
				case R.id.main_tab_btn_page2:// 消息
					tabHost.setCurrentTabByTag(getText(R.string.btn_page_2).toString());
					break;
				case R.id.main_tab_btn_page3:// 设置
					tabHost.setCurrentTabByTag(getText(R.string.btn_page_3).toString());
					break;
				default:
					break;
				}
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ServiceCore.aliveMainActivity = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.isAlive = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.isAlive = false;
	}
	
	public boolean isAlive() {
		return this.isAlive;
	}

}
