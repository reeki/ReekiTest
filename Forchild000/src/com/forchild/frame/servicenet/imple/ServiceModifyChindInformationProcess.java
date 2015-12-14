package com.forchild.frame.servicenet.imple;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.forchild.data.BaseProtocolFrame;import com.forchild.data.RequestModifyChildInformation;
import com.forchild.server.Preferences;
import com.forchild000.surface.LoginActivity;
import com.forchild000.surface.R;

public class ServiceModifyChindInformationProcess implements ServiceNetworkResultHandler {

	protected Preferences preference;
	protected Context context;

	public ServiceModifyChindInformationProcess() {

	}

	public ServiceModifyChindInformationProcess(Context context, Preferences preference) {
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
		return BaseProtocolFrame.MODIFY_CHILD_INFORMATION;
	}

	@Override
	public int process(BaseProtocolFrame source) {
		if (source != null && source instanceof RequestModifyChildInformation) {
			switch (source.getReq()) {
			case BaseProtocolFrame.RESPONSE_TYPE_OKAY:

				Toast.makeText(context, context.getText(R.string.response_error_modify_child_infomation_okay), Toast.LENGTH_SHORT).show();

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
				Toast.makeText(context, context.getText(R.string.response_error_modify_child_infomation_error), Toast.LENGTH_SHORT).show();
				break;
			}
		}
		return 0;
	}

}
