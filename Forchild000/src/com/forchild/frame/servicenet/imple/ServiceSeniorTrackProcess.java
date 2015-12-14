package com.forchild.frame.servicenet.imple;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestSeniorTrack;
import com.forchild.server.Preferences;
import com.forchild000.surface.LoginActivity;
import com.forchild000.surface.R;

public class ServiceSeniorTrackProcess implements ServiceNetworkResultHandler {

	protected Context context;
	protected Preferences preference;

	public ServiceSeniorTrackProcess() {

	}

	public ServiceSeniorTrackProcess(Context context, Preferences preference) {
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
		return BaseProtocolFrame.REQUEST_SENIOR_TRACK;
	}

	@Override
	public int process(BaseProtocolFrame source) {
		if (source != null && source instanceof RequestSeniorTrack) {
			switch (source.getReq()) {
			case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
				Toast.makeText(context, context.getText(R.string.response_error_senior_track_okay), Toast.LENGTH_SHORT).show();
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
			case BaseProtocolFrame.RESPONSE_TYPE_NULL_DATA:
				Toast.makeText(context, context.getText(R.string.response_error_senior_track_nodata), Toast.LENGTH_SHORT).show();
				break;
			case BaseProtocolFrame.RESPONSE_TYPE_OTHER_LOGIN:
				Toast.makeText(context, context.getText(R.string.response_error_login_other_phone), Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(context, context.getText(R.string.response_error_senior_track_fault), Toast.LENGTH_SHORT).show();
				break;
			}
		} else {
			return -100;
		}
		return 0;
	}

}
