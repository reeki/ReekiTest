package com.forchild000.surface;

import java.io.Serializable;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.LocationInfo;
import com.forchild.data.RequestSeniorTrack;
import com.forchild.data.SeniorInfoSimple;
import com.forchild.server.ServiceStates;
import com.forchild000.surface.KCalendar.OnCalendarClickListener;

public class ParentsTrackActivity extends MapBaseActivity implements LocationSource {
	protected static final double LAT_LNG_EXCURSION_MIN = 0.00001d;
	private MapView mapView;
	private AMap aMap;
	private TextView centerText, leftText, rightText;
	private CalendarPopup calendarPopup;
	private RelativeLayout titleBar;

	protected Marker marker;
	protected MarkerOptions markerOptions = new MarkerOptions();
	protected LatLng latlng;

	protected SeniorInfoSimple seniorInfo;
	protected Calendar calendar;
	protected Handler msgHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parent_states_activity);

		if (!ServiceStates.getForchildServiceState(ParentsTrackActivity.this)) {
			Intent intent = new Intent(ParentsTrackActivity.this, ServiceCore.class);
			ParentsTrackActivity.this.startService(intent);
		}

		msgHandler = new Handler(msgCallback);

		mapView = (MapView) findViewById(R.id.states_map);
		mapView.onCreate(savedInstanceState);
		aMap = mapView.getMap();

		// aMap.setLocationSource(this);
		// aMap.getUiSettings().setMyLocationButtonEnabled(true);
		// aMap.setMyLocationEnabled(true);

		titleBar = (RelativeLayout) findViewById(R.id.parent_title_bar);

		calendarPopup = new CalendarPopup(this, titleBar, calendarListner);

		centerText = (TextView) findViewById(R.id.parent_title_center_text);
		leftText = (TextView) findViewById(R.id.parent_title_left_text);
		rightText = (TextView) findViewById(R.id.parent_title_right_text);

		rightText.setText(R.string.parenttrack_rightbtn_text);
		leftText.setText(R.string.parenttrack_leftbtn_text);
		centerText.setText(R.string.parent_centerbtn_text);

		rightText.setOnClickListener(parentTrackBtnListener);
		leftText.setOnClickListener(parentTrackBtnListener);

		calendar = Calendar.getInstance();

		Serializable seniorInfoBuffer = getIntent().getSerializableExtra("senior_info");
		if (seniorInfoBuffer instanceof SeniorInfoSimple) {
			seniorInfo = (SeniorInfoSimple) seniorInfoBuffer;
		}

		if (seniorInfo == null) {
			Toast.makeText(this, getText(R.string.parenttrack_noseniorinfo_error), Toast.LENGTH_SHORT).show();
			finish();
		} else {
			if (seniorInfo.getNick() != null && seniorInfo.getNick().length() > 0) {
				centerText.setText(seniorInfo.getNick());
			} else if (seniorInfo.getName() != null) {
				centerText.setText(seniorInfo.getName());
			}
		}

		if (ServiceCore.getSid() != null && seniorInfo.getOid() != BaseProtocolFrame.INT_INITIATION) {
			RequestSeniorTrack rst = new RequestSeniorTrack(ServiceCore.getSid(), seniorInfo.getOid(), calendar.getTimeInMillis());
			rst.addHandler(msgHandler);
			ServiceCore.addNetTask(rst);
		}

		this.registerReceiver(parentStateReceiver, new IntentFilter("com.forchild.location"));
	}

	protected OnClickListener parentTrackBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.parent_title_left_text:
				if (!ServiceStates.getForchildServiceState(ParentsTrackActivity.this)) {
					Intent intent = new Intent(ParentsTrackActivity.this, ServiceCore.class);
					ParentsTrackActivity.this.startService(intent);
				}

				if (ServiceCore.getSid() != null && seniorInfo.getOid() != BaseProtocolFrame.INT_INITIATION) {
					RequestSeniorTrack rst = new RequestSeniorTrack(ServiceCore.getSid(), seniorInfo.getOid(), calendar.getTimeInMillis());
					rst.addHandler(msgHandler);
					ServiceCore.addNetTask(rst);
				}
				break;
			case R.id.parent_title_right_text:
				calendarPopup.show();
				break;
			}
		}
	};

	protected OnCalendarClickListener calendarListner = new OnCalendarClickListener() {

		@Override
		public void onCalendarClick(int row, int col, String dateFormat) {
			if (!ServiceStates.getForchildServiceState(ParentsTrackActivity.this)) {
				Intent intent = new Intent(ParentsTrackActivity.this, ServiceCore.class);
				ParentsTrackActivity.this.startService(intent);
			}

			if (calendarPopup != null) {
				Calendar now = Calendar.getInstance();
				int years = Integer.parseInt(dateFormat.substring(0, dateFormat.indexOf("-")));
				int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-"))) - 1;
				int date = Integer.parseInt(dateFormat.substring(dateFormat.lastIndexOf("-") + 1));
				if (now.get(Calendar.YEAR) == years && now.get(Calendar.MONTH) == month && now.get(Calendar.DATE) == date) {
					calendar = now;
				} else {
					Log.e("ParentTrack", calendarPopup.getYear() + "-" + calendarPopup.getMonth() + "-" + calendarPopup.getDate());
					calendar.set(Calendar.MILLISECOND, 9999);
					calendar.set(years, month, date, 23, 59, 59);
				}

				if (ServiceCore.getSid() != null && seniorInfo.getOid() != BaseProtocolFrame.INT_INITIATION) {
					RequestSeniorTrack rst = new RequestSeniorTrack(ServiceCore.getSid(), seniorInfo.getOid(), calendar.getTimeInMillis());
					Log.e("ParentTrack", "RequestSeniorTrack:" + rst.toJsonString());
					rst.addHandler(msgHandler);
					ServiceCore.addNetTask(rst);
				}
				calendarPopup.dismiss();
			}

		}

	};

	protected Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case ServiceCore.NETWORK_RESPONSE:
				if (msg.obj instanceof RequestSeniorTrack) {
					RequestSeniorTrack rst = (RequestSeniorTrack) msg.obj;

					if (rst.isResponse()) {
						switch (rst.getReq()) {
						case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
							if (rst != null && rst.getLocationList().size() > 0) {
								PolylineOptions polyLine = new PolylineOptions();
								double la = 0.0d;
								double lo = 0.0d;
								MarkerOptions lastMarker = new MarkerOptions();
								for (LocationInfo info : rst.getLocationList()) {
									if (Math.abs(la - info.getLatitude()) > LAT_LNG_EXCURSION_MIN
											|| Math.abs(lo - info.getLongitude()) > LAT_LNG_EXCURSION_MIN) {
										polyLine.add(new LatLng(info.getLatitude(), info.getLongitude()));
										la = info.getLatitude();
										lo = info.getLongitude();
									}

									lastMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
									lastMarker.position(new LatLng(la, lo)).visible(true);
								}
								polyLine.width(10).geodesic(true).color(Color.RED);
								aMap.addPolyline(polyLine);
								aMap.addMarker(lastMarker);
								aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(la, lo)));
							}
							break;
						}
					} else {
						switch (rst.getReason()) {
						case BaseProtocolFrame.REASON_NORMALITY:
							Toast.makeText(ParentsTrackActivity.this, getText(R.string.noresponse_unknown_error), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED:
							Toast.makeText(ParentsTrackActivity.this, getText(R.string.noresponse_exceed_maxtask), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_NO_RESPONSE:
							Toast.makeText(ParentsTrackActivity.this, getText(R.string.noresponse_no_response), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_LOGOUT:
							Toast.makeText(ParentsTrackActivity.this, getText(R.string.noresponse_logout), Toast.LENGTH_SHORT).show();
							break;
						}
					}

				}
				break;
			}
			return true;
		}
	};

	private BroadcastReceiver parentStateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("com.forchild.location")) {
				double la = intent.getDoubleExtra("la", BaseProtocolFrame.DOUBLE_INITIATION);
				double lo = intent.getDoubleExtra("lo", BaseProtocolFrame.DOUBLE_INITIATION);
				if (la != BaseProtocolFrame.DOUBLE_INITIATION && lo != BaseProtocolFrame.DOUBLE_INITIATION) {
					if (marker != null) {
						marker.remove();
					}
					// latlng = new LatLng(la, lo);
					// markerOptions.position(latlng);
					// marker = aMap.addMarker(markerOptions);
				}
			}
		}

	};

	@Override
	protected void onResume() {
		super.onResume();
		if (!ServiceStates.getForchildServiceState(ParentsTrackActivity.this)) {
			Intent intent = new Intent(ParentsTrackActivity.this, ServiceCore.class);
			ParentsTrackActivity.this.startService(intent);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(parentStateReceiver);
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
