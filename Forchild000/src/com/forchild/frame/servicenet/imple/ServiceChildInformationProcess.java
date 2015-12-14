package com.forchild.frame.servicenet.imple;

import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestChildInformation;
import com.forchild.data.SeniorInfoCommunication;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild000.surface.LoginActivity;
import com.forchild000.surface.R;
import com.forchild000.surface.ServiceCore;

public class ServiceChildInformationProcess implements ServiceNetworkResultHandler {
	protected Preferences preference;
	protected Context context;

	public ServiceChildInformationProcess() {

	}

	public ServiceChildInformationProcess(Context context, Preferences preference) {
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
		return BaseProtocolFrame.REQUEST_CHILD_INFORMATION;
	}

	@Override
	public int process(BaseProtocolFrame source) {
		if (source != null && source instanceof RequestChildInformation) {
			RequestChildInformation rlc = (RequestChildInformation) source;
			switch (rlc.getReq()) {
			case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
				preference.setSex(rlc.getSex());
				preference.setNick(rlc.getNick());
				preference.setUserName(rlc.getName());
				preference.setAddress(rlc.getAddress());
				preference.setBirth(rlc.getBirth());
				preference.setAllergic(rlc.getAllergic());
				preference.setMedicalInfo(rlc.getMedical());
				preference.setHeight(rlc.getHeight());
				preference.setWeight(rlc.getWeight());
				preference.setBlood(rlc.getBlood());
				List<SeniorInfoCommunication> communit = rlc.getSeniorList();
				DatabaseHelper dbHelper = new DatabaseHelper(context);
				SeniorInfoCommunication info = null;
				dbHelper.deleteSeniorInfo();
				for (int i = 0; i < communit.size(); ++i) {
					info = communit.get(i);

					if (info.getOid() == BaseProtocolFrame.INT_INITIATION) {
						continue;
					}

					ContentValues values = new ContentValues();
					values.put("id", i);
					values.put("oid", info.getOid());
					values.put("sex", info.getSex());
					values.put("name", info.getName());
					values.put("medical", info.getMedical());
					values.put("allergic", info.getAllergic());
					values.put("height", info.getHeight());
					values.put("weight", info.getWeight());
					values.put("weight", info.getWeight());
					values.put("blood", info.getBlood());
					values.put("phone", info.getPhone());
					values.put("nick", info.getNick());
					values.put("address", info.getAddress());
					values.put("birth", info.getBirth());
					values.put("code", info.getCode());
					values.put("belongs", ServiceCore.getLoginId());
					dbHelper.insertSeniorInfo(values);
				}

				// 发向SeniorsInfoActivity。java
				Intent refreshIntent = new Intent();
				refreshIntent.setAction("com.forchild.seniorinfo.refreshui");
				context.sendBroadcast(refreshIntent);

				// 发向PersonInfoActivity。java
				refreshIntent = new Intent();
				refreshIntent.setAction("com.forchild.personinfo.refreshui");
				context.sendBroadcast(refreshIntent);

				// 发向SetupActivity。java
				refreshIntent = new Intent();
				refreshIntent.setAction("com.forchild.setupactivity.refreshui");
				context.sendBroadcast(refreshIntent);

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
				context.startActivity(intent);
				break;
			default:
				break;
			}
		}
		return 0;
	}
}
