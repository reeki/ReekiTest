package com.forchild000.surface;

import java.io.Serializable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.forchild.data.AccidentInfo;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.MessageAccident;
import com.forchild.data.MessageAttention;
import com.forchild.data.MessageHelp;
import com.forchild.data.RequestAllowAddSenior;
import com.forchild.data.SOSInfo;
import com.forchild.data.SeniorInfoComplete;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;

public class WarningActivity extends AliveBaseActivity {
	public static final int WARNING_TYPE_DELETE_SENIOR = 100;
	public static final int WARNING_TYPE_RESTORE_FACTORY_SETTINGS = 101;
	public static final int WARNING_TYPE_LOGOUT = 102;
	public static final int WARNING_TYPE_ACCIDENT_MESSAGE = 103;
	public static final int WARNING_TYPE_SOS_MESSAGE = 104;
	public static final int WARNING_TYPE_REQUEST_ATTENTION = 105;
	public static final int WARNING_TYPE_SYSTEM_MESSAGE = 106;

	private TextView content;
	private Button sureBtn, cancelBtn;
	private int type;
	private Intent startIntent;

	private MessageAttention msgAttention;
	private AccidentInfo accidentInfo;
	private SOSInfo sosInfo;
	private int fid = BaseProtocolFrame.INT_INITIATION;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.warning_activity);

		setResult(RESULT_CANCELED);

		content = (TextView) findViewById(R.id.warning_content_text);

		sureBtn = (Button) findViewById(R.id.warning_sure_btn);
		cancelBtn = (Button) findViewById(R.id.warning_cancel_btn);

		startIntent = getIntent();
		type = startIntent.getIntExtra("type", -1);

		sureBtn.setOnClickListener(warningButtonListener);
		cancelBtn.setOnClickListener(warningButtonListener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private OnClickListener warningButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.warning_sure_btn:
				switch (type) {
				case WARNING_TYPE_DELETE_SENIOR:
					Intent intent = new Intent();
					intent.putExtra("senior_info", startIntent.getSerializableExtra("senior_info"));
					setResult(RESULT_OK, intent);
					break;
				case WARNING_TYPE_RESTORE_FACTORY_SETTINGS:
					setResult(RESULT_OK);
					break;
				case WARNING_TYPE_SYSTEM_MESSAGE:
					break;
				case WARNING_TYPE_ACCIDENT_MESSAGE:
					if (accidentInfo != null) {
						Intent accidentIntent = new Intent(WarningActivity.this, AccidentDisplayActivity.class);
						accidentIntent.putExtra("accident_info", accidentInfo);
						WarningActivity.this.startActivity(accidentIntent);
					} else {
						finish();
					}
					break;
				case WARNING_TYPE_SOS_MESSAGE:
					if (sosInfo != null) {
						Intent sosIntent = new Intent(WarningActivity.this, AccidentDisplayActivity.class);
						sosIntent.putExtra("accident_info", sosInfo);
						WarningActivity.this.startActivity(sosIntent);
					} else {
						finish();
					}
					break;
				case WARNING_TYPE_REQUEST_ATTENTION:
					if (!ServiceStates.getForchildServiceState(WarningActivity.this)) {
						Intent serviceIntent = new Intent(WarningActivity.this, ServiceCore.class);
						WarningActivity.this.startService(serviceIntent);
					}
					if (fid != BaseProtocolFrame.INT_INITIATION) {
						Preferences preference = new Preferences(WarningActivity.this);
						ServiceCore.addNetTask(new RequestAllowAddSenior(preference.getSid(), fid));

					}
					break;
				case WARNING_TYPE_LOGOUT:
					setResult(RESULT_OK);
					break;
				default:
					break;
				}
				break;
			case R.id.warning_cancel_btn:
				setResult(RESULT_CANCELED);
				break;

			default:
				break;
			}
			WarningActivity.this.finish();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();

		switch (type) {
		case WARNING_TYPE_DELETE_SENIOR:
			setTitle(R.string.warning_delete_seniors_title);
			Serializable bean = startIntent.getSerializableExtra("senior_info");
			if (bean instanceof SeniorInfoComplete) {
				SeniorInfoComplete sic = (SeniorInfoComplete) bean;
				if (sic.getNick() != null) {
					if (sic.getNick().length() > 0) {
						content.setText(getText(R.string.warning_delete_seniors_content_head_text) + sic.getNick()
								+ getText(R.string.warning_delete_seniors_content_end_text));
					} else if (sic.getName() != null) {
						content.setText(getText(R.string.warning_delete_seniors_content_head_text) + sic.getName()
								+ getText(R.string.warning_delete_seniors_content_end_text));
					}
				} else if (sic.getName() != null) {
					content.setText(getText(R.string.warning_delete_seniors_content_head_text) + sic.getName()
							+ getText(R.string.warning_delete_seniors_content_end_text));
				}
			} else {
				finish();
			}
			sureBtn.setText(R.string.sure);
			cancelBtn.setText(R.string.cancel);
			break;
		case WARNING_TYPE_RESTORE_FACTORY_SETTINGS:
			setTitle(R.string.warning_resotre_factory_title);
			content.setText(R.string.warning_resotre_factory_content);
			sureBtn.setText(R.string.sure);
			cancelBtn.setText(R.string.cancel);
			break;
		case WARNING_TYPE_SYSTEM_MESSAGE:
			setTitle(R.string.warning_system_message_title);
			content.setText(startIntent.getStringExtra("content"));
			sureBtn.setText(R.string.sure);
			cancelBtn.setText(R.string.cancel);
			break;
		case WARNING_TYPE_ACCIDENT_MESSAGE:
			setTitle(R.string.warning_accident_message_title);
			Serializable msg = startIntent.getSerializableExtra("msg");
			if (msg != null && msg instanceof MessageAccident) {
				MessageAccident ma = (MessageAccident) msg;
				content.setText(getText(R.string.warning_accident_message_content_front).toString() + startIntent.getStringExtra("name")
						+ getText(R.string.warning_accident_message_content_mid).toString() + ma.getAddress()
						+ getText(R.string.warning_accident_message_content_behind).toString());
				accidentInfo = new AccidentInfo(0, ma.getLo(), ma.getLa(), ma.getDate(), startIntent.getStringExtra("name"));
			}
			break;
		case WARNING_TYPE_SOS_MESSAGE:
			setTitle(R.string.warning_sos_message_title);
			Serializable msgHelp = startIntent.getSerializableExtra("msg");
			if (msgHelp != null && msgHelp instanceof MessageHelp) {
				MessageHelp mh = (MessageHelp) msgHelp;

				StringBuffer contentText = new StringBuffer(getText(R.string.warning_sos_message_content));
				if (mh.getCon() != null && mh.getCon().length() > 0) {
					contentText.append("\n");
					contentText.append(getText(R.string.warning_sos_message_user_content));
					contentText.append("\n");
					contentText.append(mh.getCon());
				}

				sosInfo = new SOSInfo(0, mh.getLo(), mh.getLa(), mh.getDate(), mh.getUname(), mh.getCon());
				content.setText(contentText);
			}
			break;
		case WARNING_TYPE_REQUEST_ATTENTION:
			setTitle(R.string.warning_attention_message_title);
			Serializable msgAtt = startIntent.getSerializableExtra("msg");
			if (msgAtt != null && msgAtt instanceof MessageAttention) {
				msgAttention = (MessageAttention) msgAtt;
				StringBuffer contentText = new StringBuffer();
				if (msgAttention.getNick() != null && msgAttention.getNick().length() > 0) {
					contentText.append(msgAttention.getNick());
					contentText.append("(");
					contentText.append(msgAttention.getPhone());
					contentText.append(")");
				}
				contentText.append(getText(R.string.warning_attention_message_content));
				content.setText(startIntent.getStringExtra("name"));
			}
			sureBtn.setText(R.string.agree_to);
			cancelBtn.setText(R.string.refuse_to);
			break;
		case WARNING_TYPE_LOGOUT:
			setTitle(R.string.warning_logout_title);
			content.setText(R.string.warning_logout_text);
			sureBtn.setText(R.string.warning_logout_sure_btn);
			cancelBtn.setText(R.string.cancel);
			cancelBtn.setVisibility(View.VISIBLE);
			break;
		default:
			finish();
			break;
		}
	}
}
