package com.forchild.frame.servicenet.imple;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestModifySeniorPhone;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild000.surface.LoginActivity;
import com.forchild000.surface.R;

public class ServiceModifySeniorPhoneProcess implements ServiceNetworkResultHandler {

	protected Preferences preference;
	protected Context context;

	public ServiceModifySeniorPhoneProcess() {

	}

	public ServiceModifySeniorPhoneProcess(Context context, Preferences preference) {
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
		return BaseProtocolFrame.MODIFY_SENIOR_PHONE;
	}

	@Override
	public int process(BaseProtocolFrame source) {
		if (source != null && source instanceof RequestModifySeniorPhone) {
			switch (source.getReq()) {
			case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
				RequestModifySeniorPhone rsp = (RequestModifySeniorPhone) source;
				DatabaseHelper dbHelper = new DatabaseHelper(context);
				ContentValues cv = new ContentValues();
				cv.put("phone", rsp.getPhone());
				dbHelper.updateSeniorInfo(cv, "oid = ?", new String[] { rsp.getOid() + "" });
				
				Toast.makeText(context, context.getText(R.string.response_error_modphone_okay), Toast.LENGTH_SHORT).show();

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
			case 101:
				Toast.makeText(context, context.getText(R.string.response_error_modphone_pphone_error), Toast.LENGTH_SHORT).show();
				break;
			case 100:
				Toast.makeText(context, context.getText(R.string.response_error_register_incorrect_valid), Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(context, context.getText(R.string.response_error_modphone_fault), Toast.LENGTH_SHORT).show();
				break;
			}
		}
		return 0;
	}

}
