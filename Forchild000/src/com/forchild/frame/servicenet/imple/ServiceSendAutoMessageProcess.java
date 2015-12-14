package com.forchild.frame.servicenet.imple;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.MessageFrame;
import com.forchild.data.RequestSendAutoMessage;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild000.surface.LoginActivity;
import com.forchild000.surface.R;
import com.forchild000.surface.ServiceCore;

public class ServiceSendAutoMessageProcess extends ServiceSendMessageProcess {

	public ServiceSendAutoMessageProcess() {
		super();
	}

	public ServiceSendAutoMessageProcess(Context context, Preferences preference) {
		super(context, preference);
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.REQUEST_SEND_AUTO_MESSAGE;
	}

	@Override
	public int process(BaseProtocolFrame source) {
		if (source != null && source instanceof RequestSendAutoMessage) {
			RequestSendAutoMessage rsm = (RequestSendAutoMessage) source;
			MessageFrame msg = rsm.getMsgEntity();
			switch (source.getReq()) {
			case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
				if (msg != null) {
					DatabaseHelper dbHelper = new DatabaseHelper(context);
					ContentValues values = new ContentValues();
					values.put("send_state", MessageFrame.SENDSTATE_FINISHED);
					dbHelper.updateMessage(values, msg.getLoginId(), msg.getDate(), msg.getUserMsgType());
				}
				break;
			case BaseProtocolFrame.RESPONSE_TYPE_NO_LOGIN:
				if (msg != null) {
					DatabaseHelper dbHelper = new DatabaseHelper(context);
					ContentValues values = new ContentValues();
					values.put("send_state", MessageFrame.SENDSTATE_FAULT);
					dbHelper.updateMessage(values, msg.getLoginId(), msg.getDate(), msg.getUserMsgType());
				}
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
				if (msg != null) {
					DatabaseHelper dbHelper = new DatabaseHelper(context);
					ContentValues values = new ContentValues();
					values.put("send_state", MessageFrame.SENDSTATE_FAULT);
					dbHelper.updateMessage(values, msg.getLoginId(), msg.getDate(), msg.getUserMsgType());
				}
				Toast.makeText(context, context.getText(R.string.response_error_login_other_phone), Toast.LENGTH_SHORT).show();
				break;
			default:
				if (msg != null) {
					DatabaseHelper dbHelper = new DatabaseHelper(context);
					ContentValues values = new ContentValues();
					values.put("send_state", MessageFrame.SENDSTATE_FAULT);
					dbHelper.updateMessage(values, msg.getLoginId(), msg.getDate(), msg.getUserMsgType());
				}
				break;
			}

			if (msg.getOid() == ServiceCore.getMsgActivityOid()) {
				Intent msgNotice = new Intent();
				msgNotice.setAction("com.forchild.msg.SEND_AUTO_MESSAGE_FINISH");
				context.sendBroadcast(msgNotice);
			}

		} else {
			return -100;
		}
		return 0;
	}
}
