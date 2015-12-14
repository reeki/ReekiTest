package com.forchild.frame.servicenet.imple;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestAllowAddSenior;
import com.forchild.data.RequestDeleteSenior;
import com.forchild.server.Preferences;
import com.forchild000.surface.LoginActivity;
import com.forchild000.surface.R;

public class ServiceAllowAddSeniorProcess implements ServiceNetworkResultHandler {
	protected Preferences preference;
	protected Context context;

	public ServiceAllowAddSeniorProcess() {

	}

	public ServiceAllowAddSeniorProcess(Context context, Preferences preference) {
		this.preference = preference;
		this.context = context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setPreferences(Preferences preferences) {
		this.preference = preferences;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.ALLOW_ADD_SENIOR;
	}

	@Override
	public int process(BaseProtocolFrame source) {
		if (source != null && source instanceof RequestAllowAddSenior) {
			switch (source.getReq()) {
			case BaseProtocolFrame.RESPONSE_TYPE_OKAY:

				Toast.makeText(context, context.getText(R.string.response_error_delete_senior_okay), Toast.LENGTH_SHORT).show();

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
			default:
				Toast.makeText(context, context.getText(R.string.response_error_delete_fault), Toast.LENGTH_SHORT).show();
				break;
			}
		}
		return 0;
	}

}
