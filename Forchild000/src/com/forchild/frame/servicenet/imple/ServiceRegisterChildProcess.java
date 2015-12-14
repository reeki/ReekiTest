package com.forchild.frame.servicenet.imple;

import android.content.Context;
import android.widget.Toast;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestRegisterChild;
import com.forchild.server.Preferences;
import com.forchild000.surface.R;

public class ServiceRegisterChildProcess implements ServiceNetworkResultHandler {
	protected Preferences preference;
	protected Context context;

	public ServiceRegisterChildProcess() {

	}

	public ServiceRegisterChildProcess(Context context, Preferences preference) {
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
		return BaseProtocolFrame.REGISTER_CHILD;
	}

	@Override
	public int process(BaseProtocolFrame source) {
		if (source != null && source instanceof RequestRegisterChild) {
			RequestRegisterChild rlc = (RequestRegisterChild) source;
			switch (rlc.getReq()) {
			case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
				if (preference == null) {
					preference = new Preferences(context);
				}

				if (rlc.getSid() == null) {
					return -1;
				}

				if (!rlc.getPassword().equals(preference.getPassword()) || rlc.getPhoneNumber().equals(preference.getLoginId())) {
					preference.setPassword(rlc.getPassword());
					preference.setLoginId(rlc.getPhoneNumber());
				}
				
				Toast.makeText(context, context.getText(R.string.response_error_register_okay), Toast.LENGTH_SHORT).show();

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
