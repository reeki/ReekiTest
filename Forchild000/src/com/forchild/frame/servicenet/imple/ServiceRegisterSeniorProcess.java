package com.forchild.frame.servicenet.imple;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestChildInformation;
import com.forchild.data.RequestRegisterSenior;
import com.forchild.server.Preferences;
import com.forchild000.surface.LoginActivity;
import com.forchild000.surface.R;
import com.forchild000.surface.ServiceCore;

public class ServiceRegisterSeniorProcess implements ServiceNetworkResultHandler {
	protected Preferences preference;
	protected Context context;

	public ServiceRegisterSeniorProcess() {

	}

	public ServiceRegisterSeniorProcess(Context context, Preferences preference) {
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
		return BaseProtocolFrame.REGISTER_SENIOR;
	}

	@Override
	public int process(BaseProtocolFrame source) {
		if (source != null && source instanceof RequestRegisterSenior) {
			RequestRegisterSenior rlc = (RequestRegisterSenior) source;
			switch (rlc.getReq()) {
			case BaseProtocolFrame.RESPONSE_TYPE_OKAY:

				if (preference == null) {
					preference = new Preferences(context);
				}

				if (rlc.getSid() == null) {
					return -1;
				}

				ServiceCore.addPriorNetTask(new RequestChildInformation(preference.getSid()));

				Toast.makeText(context, context.getText(R.string.response_error_register_senior_okay), Toast.LENGTH_SHORT).show();

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
			case 100:
				Toast.makeText(context, context.getText(R.string.response_error_register_repetitive_phone), Toast.LENGTH_SHORT).show();
				break;
			case 101:
				Toast.makeText(context, context.getText(R.string.response_error_register_incorrect_valid), Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(context, context.getText(R.string.response_error_register_fault), Toast.LENGTH_SHORT).show();
				break;
			}
		}
		return 0;
	}
}
