package com.forchild.frame.servicenet.imple;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.forchild.data.BaseConfiguration;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestChildInformation;
import com.forchild.data.RequestLoginChild;
import com.forchild.server.Preferences;
import com.forchild.server.ServiceStates;
import com.forchild000.surface.R;
import com.forchild000.surface.ServiceCore;

public class ServiceLoginProcess implements ServiceNetworkResultHandler {
	protected Preferences preference;
	protected Context context;
	protected int loginFaultTol = BaseConfiguration.LOGIN_FAULT_TOLERATE;

	public ServiceLoginProcess() {

	}

	public ServiceLoginProcess(Context context, Preferences preference) {
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
		return BaseProtocolFrame.LOGIN_CHILD;
	}

	@Override
	public int process(BaseProtocolFrame source) {
		if (source != null && source instanceof RequestLoginChild) {
			RequestLoginChild rlc = (RequestLoginChild) source;
			switch (rlc.getReq()) {
			case BaseProtocolFrame.RESPONSE_TYPE_OKAY:

				if (preference == null) {
					preference = new Preferences(context);
				}

				if (rlc.getSid() == null) {
					return -1;
				}

				preference.setSid(rlc.getSid());
				preference.setLoginState(true);
				ServiceCore.setLoginId(rlc.getPhoneNumber());

				if (!rlc.getPassword().equals(preference.getPassword()) || rlc.getPhoneNumber().equals(preference.getLoginId())) {
					preference.setPassword(rlc.getPassword());
					preference.setLoginId(rlc.getPhoneNumber());
				}
				
				RequestChildInformation rci = new RequestChildInformation(rlc.getSid());
				ServiceCore.addPriorNetTask(rci);
				
				if(context instanceof ServiceCore) {
					ServiceCore sc = (ServiceCore) context;
					sc.startLocation();
				}
				Toast.makeText(context, context.getText(R.string.response_error_login_success), Toast.LENGTH_SHORT).show();
				break;
			case BaseProtocolFrame.RESPONSE_TYPE_NULL_DATA:
				Toast.makeText(context, context.getText(R.string.response_error_login_null_data), Toast.LENGTH_SHORT).show();
				break;
			case 100:
				Toast.makeText(context, context.getText(R.string.response_error_login_incorrect_message), Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(context, context.getText(R.string.response_error_login_fault), Toast.LENGTH_SHORT).show();
				break;
			}
		}
		return 0;
	}
}
