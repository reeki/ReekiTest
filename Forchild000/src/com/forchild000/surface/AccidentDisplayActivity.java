package com.forchild000.surface;

import java.io.Serializable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.forchild.data.AccidentInfo;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestSendSOS;
import com.forchild.data.SOSInfo;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;

public class AccidentDisplayActivity extends MapBaseActivity implements LocationSource {

	public static final int ACCIDENT_TYPE_ACCIDENT = 1;
	public static final int ACCIDENT_TYPE_SOS = 2;

	protected final long HELP_DELAY = 5 * 60 * 1000;

	private MapView mapView;
	private AMap aMap;
	protected Marker marker;
	protected MarkerOptions markerOptions = new MarkerOptions();
	protected LatLng latlng;
	protected boolean hasMovedMap = false;
	protected MsgPopupWindow msgPopup;
	protected RelativeLayout titleBar;
	protected int type;
	protected TextView leftTitle, rightTitle, centerTitle;
	protected Handler msgHandler;

	protected AccidentInfo ai;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parent_states_activity);

		mapView = (MapView) findViewById(R.id.states_map);
		mapView.onCreate(savedInstanceState);
		aMap = mapView.getMap();

		// aMap.setLocationSource(this);
		// aMap.getUiSettings().setMyLocationButtonEnabled(true);
		// aMap.setMyLocationEnabled(true);
		titleBar = (RelativeLayout) findViewById(R.id.parent_title_bar);
		leftTitle = (TextView) findViewById(R.id.parent_title_left_text);
		rightTitle = (TextView) findViewById(R.id.parent_title_right_text);
		centerTitle = (TextView) findViewById(R.id.parent_title_center_text);
		msgPopup = new MsgPopupWindow(this, titleBar);

		Intent intent = getIntent();
		type = intent.getIntExtra("type", -1);
		Serializable bean = intent.getSerializableExtra("accident_info");
		MarkerOptions marker0 = new MarkerOptions();
		marker0.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		switch (type) {
		case ACCIDENT_TYPE_ACCIDENT:
			if (bean instanceof AccidentInfo) {
				ai = (AccidentInfo) bean;

				if (System.currentTimeMillis() - ai.getDate() > HELP_DELAY) {
					leftTitle.setVisibility(View.GONE);
				} else {
					leftTitle.setText(R.string.accident_display_acc_left_text);
				}
				rightTitle.setText(R.string.accident_display_acc_right_text);

				DatabaseHelper dbHelper = new DatabaseHelper(AccidentDisplayActivity.this);
				Cursor seniorCursor = dbHelper.getSeniorInfo(new String[] { "nick", "name", "oid" }, "oid = ?", new String[] { ai.getOid() + "" });
				String name = new String();
				if (seniorCursor.moveToNext()) {
					String nickBuff = seniorCursor.getString(seniorCursor.getColumnIndex("nick"));
					String nameBuff = seniorCursor.getString(seniorCursor.getColumnIndex("name"));
					if (nickBuff != null && nickBuff.length() > 0) {
						name = nickBuff;
					} else if (name != null) {
						name = nameBuff;
					}
				}
				centerTitle.setText(name);
				msgPopup.setText(ai.getAddress());

				marker0.position(new LatLng(ai.getLatitude(), ai.getLongitude())).visible(true).title(name);
				aMap.addMarker(marker0);
				aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(ai.getLatitude(), ai.getLongitude())));
			} else {
				finish();
			}
			break;
		case ACCIDENT_TYPE_SOS:
			if (bean instanceof AccidentInfo) {
				SOSInfo sosi = (SOSInfo) bean;
				marker0.position(new LatLng(sosi.getLatitude(), sosi.getLongitude())).visible(true).title("求救者");
				aMap.addMarker(marker0);
				aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(sosi.getLatitude(), sosi.getLongitude())));
				leftTitle.setVisibility(View.GONE);
				rightTitle.setText(R.string.accident_display_sos_right_text);
				// centerTitle.setText(sosi.getName());
				centerTitle.setText("跌倒求救");
				msgPopup.setText(sosi.getContent());
			} else {
				finish();
			}
			break;
		default:
			finish();
			break;
		}
		// this.registerReceiver(accidentReceiver, new
		// IntentFilter("com.forchild.location"));
		leftTitle.setOnClickListener(accidentButtonListener);
		rightTitle.setOnClickListener(accidentButtonListener);
	}

	protected Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case ServiceCore.NETWORK_RESPONSE:
				if (msg.obj instanceof RequestSendSOS) {
					RequestSendSOS rss = (RequestSendSOS) msg.obj;
					if (rss.isResponse()) {
						switch (rss.getReq()) {
						case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
							Toast.makeText(AccidentDisplayActivity.this, getText(R.string.accident_display_sendhelp_success), Toast.LENGTH_SHORT)
									.show();
							break;
						default:
							Toast.makeText(AccidentDisplayActivity.this, getText(R.string.accident_display_sendhelp_unsuccess), Toast.LENGTH_SHORT)
									.show();
							break;
						}
					} else {
						switch (rss.getReason()) {
						case BaseProtocolFrame.REASON_NORMALITY:
							Toast.makeText(AccidentDisplayActivity.this, getText(R.string.noresponse_unknown_error), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED:
							Toast.makeText(AccidentDisplayActivity.this, getText(R.string.noresponse_exceed_maxtask), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_NO_RESPONSE:
							Toast.makeText(AccidentDisplayActivity.this, getText(R.string.noresponse_no_response), Toast.LENGTH_SHORT).show();
							break;
						case BaseProtocolFrame.REASON_LOGOUT:
							Toast.makeText(AccidentDisplayActivity.this, getText(R.string.noresponse_logout), Toast.LENGTH_SHORT).show();
							break;
						}
					}
				}
				break;
			}
			return true;
		}
	};

	protected OnClickListener accidentButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.parent_title_left_text:
				switch (type) {
				case ACCIDENT_TYPE_ACCIDENT:
					if (ai != null && System.currentTimeMillis() - ai.getDate() > HELP_DELAY) {
						Toast.makeText(AccidentDisplayActivity.this, getText(R.string.accident_display_exceeds_timedelay_error), Toast.LENGTH_SHORT)
								.show();
						return;
					}
					final EditText helperMsgEdit = new EditText(AccidentDisplayActivity.this);
					Preferences preference = new Preferences(AccidentDisplayActivity.this);
					String helpContent = preference.getEmergencyInfo();
					if (helpContent == null) {
						StringBuffer helpBuff = new StringBuffer();
						helpBuff.append("我的亲人：");
						helpBuff.append(centerTitle.getText());
						helpBuff.append("在");
						helpBuff.append(ai.getAddress());
						helpBuff.append("附近跌倒,请您帮忙借助,我的电话:");
						helpBuff.append(ServiceCore.getLoginId());
						helpBuff.append("如方便请与我联系");
						helpContent = helpBuff.toString();
					}
					helperMsgEdit.setText(helpContent);
					AlertDialog.Builder builder = new AlertDialog.Builder(AccidentDisplayActivity.this);
					builder.setTitle("请输入").setIcon(android.R.drawable.ic_dialog_info);
					builder.setView(helperMsgEdit);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String content = helperMsgEdit.getText().toString().trim();
							if (content.length() > 0 && ai != null) {
								RequestSendSOS rss = new RequestSendSOS(ServiceCore.getSid(), ai.getSosId(), content);
								rss.addHandler(msgHandler);
								ServiceCore.addNetTask(rss);
								dialog.dismiss();
							} else {
								Toast.makeText(AccidentDisplayActivity.this, getText(R.string.accident_display_no_content_error), Toast.LENGTH_SHORT)
										.show();
							}
						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					Dialog helpDialog = builder.create();
					helpDialog.show();
					break;
				}
				break;
			case R.id.parent_title_right_text:
				msgPopup.show();
				break;
			}
		}

	};

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

	public class MsgPopupWindow extends PopupWindow {
		private View parent;
		private TextView msg_popup;
		private Context context;
		private LinearLayout layout;

		public void show() {
			if (layout != null) {
				layout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_in));
			}
			setFocusable(true);
			setOutsideTouchable(true);
			showAsDropDown(parent, 0, 0);
			update();
		}

		public void setText(String content) {
			msg_popup.setText(content);
		}

		public MsgPopupWindow(Context mContext, View parent) {
			this.parent = parent;
			this.context = mContext;
			View view = View.inflate(mContext, R.layout.accident_popup_window, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
			layout = (LinearLayout) view.findViewById(R.id.accidentdisp_msg_parent);
			msg_popup = (TextView) view.findViewById(R.id.accidentdisp_msg_text);
			setContentView(view);

			setWidth(LayoutParams.WRAP_CONTENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// this.unregisterReceiver(accidentReceiver);
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
