package com.forchild000.surface;

import android.app.Activity;
import android.os.Bundle;

public class AliveBaseActivity extends Activity {
	protected boolean isAlive = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ServiceCore.aliveActivityList.add(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isAlive = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		isAlive = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ServiceCore.aliveActivityList.remove(this);
	}

	public boolean isFrontActivity() {
		return this.isAlive;
	}

}
