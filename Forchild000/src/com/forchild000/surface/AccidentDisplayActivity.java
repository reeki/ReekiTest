package com.forchild000.surface;

import java.io.Serializable;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.forchild.data.AccidentInfo;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.SOSInfo;

public class AccidentDisplayActivity extends MapBaseActivity implements LocationSource {

	public static final int ACCIDENT_TYPE_ACCIDENT = 1;
	public static final int ACCIDENT_TYPE_SOS = 2;

	private MapView mapView;
	private AMap aMap;
	// private TextView msgText;
	protected Marker marker;
	protected MarkerOptions markerOptions = new MarkerOptions();
	protected LatLng latlng;
	protected boolean hasMovedMap = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parent_states_activity);

		mapView = (MapView) findViewById(R.id.states_map);
		mapView.onCreate(savedInstanceState);
		aMap = mapView.getMap();

		aMap.setLocationSource(this);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		aMap.setMyLocationEnabled(true);

		// msgText = (TextView) findViewById(R.id.states_message_0);
		Intent intent = getIntent();
		int type = intent.getIntExtra("type", -1);
		Serializable bean = intent.getSerializableExtra("accident_info");
		MarkerOptions marker0 = new MarkerOptions();
		marker0.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		switch (type) {
		case ACCIDENT_TYPE_ACCIDENT:
			if (bean instanceof AccidentInfo) {
				AccidentInfo ai = (AccidentInfo) bean;
				marker0.position(new LatLng(ai.getLatitude(), ai.getLongitude())).visible(true).title("被关注人");
				aMap.addMarker(marker0);
				// msgText.setText(ai.getName() + " 在标注点附近，疑似跌倒");
			} else {
				finish();
			}
			break;
		case ACCIDENT_TYPE_SOS:
			if (bean instanceof AccidentInfo) {
				SOSInfo sosi = (SOSInfo) bean;
				marker0.position(new LatLng(sosi.getLatitude(), sosi.getLongitude())).visible(true).title("被关注人");
				aMap.addMarker(marker0);
				// msgText.setText(sosi.getContent());
			} else {
				finish();
			}
			break;
		default:
			finish();
			break;
		}
		this.registerReceiver(accidentReceiver, new IntentFilter("com.forchild.location"));
	}

	private BroadcastReceiver accidentReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("com.forchild.location")) {
				double la = intent.getDoubleExtra("la", BaseProtocolFrame.DOUBLE_INITIATION);
				double lo = intent.getDoubleExtra("lo", BaseProtocolFrame.DOUBLE_INITIATION);
				if (la != BaseProtocolFrame.DOUBLE_INITIATION && lo != BaseProtocolFrame.DOUBLE_INITIATION) {
					if (marker != null) {
						marker.remove();
					}
					latlng = new LatLng(la, lo);
					markerOptions.position(latlng);
					marker = aMap.addMarker(markerOptions);
					if (!hasMovedMap) {
						aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(la, lo)));
					}
				}
			}
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(accidentReceiver);
	}

	@Override
	public void activate(OnLocationChangedListener arg0) {
		if (arg0 != null) {
			arg0.onLocationChanged(super.aMapLocation);// 显示系统小蓝点
		}
	}

	@Override
	public void deactivate() {

	}

}
