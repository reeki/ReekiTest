package com.forchild.frame.servicenet.imple;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.MessageFrame;
import com.forchild.data.RequestSendMessage;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild000.surface.LoginActivity;
import com.forchild000.surface.R;

public class ServiceSendMessageProcess implements ServiceNetworkResultHandler {

	protected Context context;
	protected Preferences preference;

	public ServiceSendMessageProcess() {

	}

	public ServiceSendMessageProcess(Context context, Preferences preference) {
		this.context = context;
		this.preference = preference;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setPreferences(Preferences preferences) {
		this.preference = preferences;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.SEND_MESSAGE;
	}

	@Override
	public int process(BaseProtocolFrame source) {
		if (source != null && source instanceof RequestSendMessage) {
			RequestSendMessage rsm = (RequestSendMessage) source;
			MessageFrame msg = rsm.getMsgEntity();
			
			switch (source.getReq()) {
			case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
				break;
			case BaseProtocolFrame.RESPONSE_TYPE_NO_LOGIN:
				Toast.makeText(context, context.getText(R.string.response_error_no_login), Toast.LENGTH_SHORT).show();
				if (preference == null) {
					preference = new Preferences(context);
				}
				preference.setLoginState(false);
				Intent logoutIntent = new Intent();
				logoutIntent.setAction("com.forolder.logout.activity");
				context.sendBroadcast(logoutIntent);
				Intent intent = new Intent(context, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);

				break;
			case BaseProtocolFrame.RESPONSE_TYPE_OTHER_LOGIN:
				Toast.makeText(context, context.getText(R.string.response_error_login_other_phone), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			
			if (msg != null) {
				if(msg.getState() == MessageFrame.SENDSTATE_SENDING) {
					msg.setState(MessageFrame.SENDSTATE_FAULT);
				}
				DatabaseHelper dbHelper = new DatabaseHelper(context);
				ContentValues values = new ContentValues();
				values.put("send_state", msg.getState());
				dbHelper.updateMessage(values, msg.getLoginId(), msg.getDate(), msg.getUserMsgType());
			}
		} else {
			return -100;
		}
		return 0;
	}
}
