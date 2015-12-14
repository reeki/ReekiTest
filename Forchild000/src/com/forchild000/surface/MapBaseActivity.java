package com.forchild000.surface;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.LocationManagerProxy;

import android.os.Bundle;

public class MapBaseActivity extends AliveBaseActivity {
	protected AMapLocation aMapLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ServiceCore.mapActivityList.add(this);
		this.aMapLocation = ServiceCore.getAMapLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ServiceCore.mapActivityList.remove(this);
	}

	public void setAMapLocation(AMapLocation arg0) {
		this.aMapLocation = arg0;

	}

}
