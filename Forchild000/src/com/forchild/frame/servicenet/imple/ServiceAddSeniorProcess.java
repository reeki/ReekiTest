package com.forchild.frame.servicenet.imple;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestAddSenior;
import com.forchild.data.RequestRegisterSenior;
import com.forchild.server.Preferences;
import com.forchild000.surface.R;

public class ServiceAddSeniorProcess implements ServiceNetworkResultHandler {
	protected Context context;
	protected Preferences preference;

	public ServiceAddSeniorProcess() {

	}

	public ServiceAddSeniorProcess(Context context, Preferences preference) {
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
		return BaseProtocolFrame.ADD_SENIOR;
	}

	@Override
	public int process(BaseProtocolFrame source) {
		if (source != null && source instanceof RequestAddSenior) {
			if (source.isResponse()) {
				switch (source.getReq()) {
				case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
					break;
				// case BaseProtocolFrame.RESPONSE_TYPE_NO_LOGIN:
				// Toast.makeText(context,
				// context.getText(R.string.response_error_no_login),
				// Toast.LENGTH_SHORT).show();
				// if (preference == null) {
				// preference = new Preferences(context);
				// }
				// preference.setLoginState(false);
				// Intent logoutIntent = new Intent();
				// logoutIntent.setAction("com.forolder.logout.activity");
				// context.sendBroadcast(logoutIntent);
				// Intent intent = new Intent(context, LoginActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// context.startActivity(intent);
				//
				// break;
				case BaseProtocolFrame.RESPONSE_TYPE_OTHER_LOGIN:
					Toast.makeText(context, context.getText(R.string.response_error_login_other_phone), Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(context, context.getText(R.string.response_error_add_senior_fault), Toast.LENGTH_SHORT).show();
					break;
				}
			} else {
				switch (source.getReason()) {
				case BaseProtocolFrame.REASON_NORMALITY:
					break;
				case BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED:
					break;
				case BaseProtocolFrame.REASON_NO_RESPONSE:
					break;
				case BaseProtocolFrame.REASON_LOGOUT:
					break;
				}
			}
		}
		return 0;
	}

}
