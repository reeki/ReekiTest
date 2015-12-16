package com.forchild000.surface;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestSeniorLocation;
import com.forchild.data.SeniorInfoSimple;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;
import com.forchild.server.StatesIntent;

public class ParentStates extends MapBaseActivity implements LocationSource {
	protected static final double LAT_LNG_EXCURSION_MIN = 0.00001d;

	private Spinner choiseSpinner;
	private ArrayAdapter<String> adapter;
	private MapView mapView;
	private AMap aMap;
	private TextView centerText, leftText, rightText;

	protected int nowSenior = -1;
	protected int locationSerialNumber = 0;
	protected DatabaseHelper databaseHelper;
	protected List<SeniorInfoSimple> seniorsInfo = new ArrayList<SeniorInfoSimple>();
	protected Preferences preference;
	protected Handler msgHandler;
	protected Timer btnTimer = new Timer();
	protected boolean hasMovedMap = false;
	protected Marker marker;
	protected MarkerOptions markerOptions = new MarkerOptions();
	protected LatLng latlng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parent_states_activity);

		ServiceCore.aliveActivityList.remove(this);

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		choiseSpinner = (Spinner) findViewById(R.id.parent_title_center_spinner);
		choiseSpinner.setAdapter(adapter);
		choiseSpinner.setOnItemSelectedListener(choiseListener);

		mapView = (MapView) findViewById(R.id.states_map);
		mapView.onCreate(savedInstanceState);
		aMap = mapView.getMap();

//		aMap.setLocationSource(this);
//		aMap.getUiSettings().setMyLocationButtonEnabled(true);
//		aMap.setMyLocationEnabled(true);

		centerText = (TextView) findViewById(R.id.parent_title_center_text);
		leftText = (TextView) findViewById(R.id.parent_title_left_text);
		rightText = (TextView) findViewById(R.id.parent_title_right_text);

		rightText.setText(R.string.parent_rightbtn_text);
		centerText.setText(R.string.parent_centerbtn_text);
		leftText.setText(R.string.parent_state_unknown);
		rightText.setOnClickListener(parentBtnListener);
		centerText.setOnClickListener(parentBtnListener);

		databaseHelper = new DatabaseHelper(this);
		preference = new Preferences(this);

		msgHandler = new Handler(messageCallback);

		markerOptions.visible(true);
	}

	protected OnClickListener parentBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.parent_title_center_text:
				choiseSpinner.performClick();
				break;
			case R.id.parent_title_right_text:
				Intent intent = new Intent(ParentStates.this, ParentsTrackActivity.class);
				if (nowSenior != -1 && nowSenior < seniorsInfo.size()) {
					intent.putExtra("senior_info", seniorsInfo.get(nowSenior));
				} else {
					Toast.makeText(ParentStates.this, getText(R.string.parent_noselectsenior_error), Toast.LENGTH_SHORT).show();
					return;
				}
				ParentStates.this.startActivity(intent);
				break;
			}
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
					latlng = new LatLng(la, lo);
					markerOptions.position(latlng);
					// markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
					marker = aMap.addMarker(markerOptions);
					// if (!hasMovedMap) {
					// aMap.moveCamera(CameraUpdateFactory.changeLatLng(new
					// LatLng(la, lo)));
					// }
				}
			}
		}

	};

	private OnItemSelectedListener choiseListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg2 != nowSenior) {
				preference.setDefaultSenior(arg2);
				getOneLocation(arg2);
				if (arg2 < seniorsInfo.size()) {
					SeniorInfoSimple sis = seniorsInfo.get(arg2);
					nowSenior = arg2;
					if (sis.getNick() != null && sis.getNick().length() > 0) {
						centerText.setText(sis.getNick());
					} else if (sis.getName() != null) {
						centerText.setText(sis.getName());
					}
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		aMap.clear();

		// 查看service是否在运行，如果不在，则将其运行
		if (!ServiceStates.getForchildServiceState(this)) {
			Intent intent = new Intent(this, ServiceCore.class);
			this.startService(intent);
		}

		// 添加老人名字到spinner
		Cursor cursor = databaseHelper.getSeniorInfo(new String[] { "name", "id", "oid", "nick" }, null, null);
		seniorsInfo.clear();
		adapter.clear();
		while (cursor.moveToNext()) {
			SeniorInfoSimple sis = new SeniorInfoSimple(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")),
					cursor.getInt(cursor.getColumnIndex("oid")), cursor.getString(cursor.getColumnIndex("nick")));
			seniorsInfo.add(sis);
		}
		cursor.close();
		databaseHelper.close();

		for (SeniorInfoSimple sis : seniorsInfo) {
			if (sis.getNick() != null && sis.getNick().length() > 0) {
				adapter.add(sis.getNick());
			} else if (sis.getName() != null) {
				adapter.add(sis.getName());
			} else {
				continue;
			}

		}

		// 设置光标的默认位置
		if (seniorsInfo.size() > 0) {
			int defaultSenior = preference.getDefaultSenior();
			if (defaultSenior <= seniorsInfo.size() && defaultSenior != -1) {
				choiseSpinner.setSelection(defaultSenior);
				getOneLocation(defaultSenior);
			} else if (defaultSenior == -1) {
				choiseSpinner.setSelection(0);
			}
			rightText.setVisibility(View.VISIBLE);
		} else {
			rightText.setVisibility(View.GONE);
		}

		if (latlng != null) {
			if (marker != null) {
				marker.remove();
			}
			markerOptions.position(latlng);
			marker = aMap.addMarker(markerOptions);
		}
	}

	private Handler.Callback messageCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case ServiceCore.NETWORK_RESPONSE:

				if (msg.obj != null && msg.obj instanceof RequestSeniorLocation) {
					RequestSeniorLocation rsl = (RequestSeniorLocation) msg.obj;
					if (rsl.isResponse()) {
						switch (rsl.getReq()) {
						case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
							LatLng oldLocation = new LatLng(rsl.getLatitude(), rsl.getLongitude());
							MarkerOptions marker = new MarkerOptions();
							marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
							marker.position(oldLocation).visible(true);
							aMap.addMarker(marker);
							aMap.moveCamera(CameraUpdateFactory.changeLatLng(oldLocation));

							switch (rsl.getState()) {
							case RequestSeniorLocation.STATE_RUN:
								leftText.setText(R.string.parent_state_run);
								break;
							case RequestSeniorLocation.STATE_WALK:
								leftText.setText(R.string.parent_state_walk);
								break;
							case RequestSeniorLocation.STATE_STAND:
								leftText.setText(R.string.parent_state_stand);
								break;
							default:
								leftText.setText(R.string.parent_state_stand);
								break;
							}

							break;
						}
					} else {
						switch (rsl.getReason()) {
						case BaseProtocolFrame.REASON_NORMALITY:
							Toast.makeText(ParentStates.this, getText(R.string.noresponse_unknown_error), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED:
							Toast.makeText(ParentStates.this, getText(R.string.noresponse_exceed_maxtask), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_NO_RESPONSE:
							Toast.makeText(ParentStates.this, getText(R.string.noresponse_no_response), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_LOGOUT:
							Toast.makeText(ParentStates.this, getText(R.string.noresponse_logout), Toast.LENGTH_SHORT).show();
							break;
						}
					}

				}
				break;
			}
			return true;
		}
	};

	private void getOneLocation(int id) {
		Cursor cursor = databaseHelper.getSeniorInfo(new String[] { "oid" }, "id = ?", new String[] { String.valueOf(id) });
		int oid = BaseProtocolFrame.INT_INITIATION;
		if (cursor.moveToNext()) {
			oid = cursor.getInt(cursor.getColumnIndex("oid"));
		}
		cursor.close();
		databaseHelper.close();

		if (oid != BaseProtocolFrame.INT_INITIATION) {
			if (!ServiceStates.getForchildServiceState(ParentStates.this)) {
				Intent intent = new Intent(ParentStates.this, ServiceCore.class);
				ParentStates.this.startService(intent);
			}

			RequestSeniorLocation rsl = new RequestSeniorLocation(preference.getSid(), oid);
			rsl.addHandler(msgHandler);

			ServiceCore.addNetTask(rsl);
		}
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
