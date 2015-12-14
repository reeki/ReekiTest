package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestModifySeniorInformation extends RequestModifyChildInformation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6946080694867478952L;

	protected int oid = BaseProtocolFrame.INT_INITIATION;

	public RequestModifySeniorInformation() {
		super.setType(BaseProtocolFrame.MODIFY_SENIOR_INFORMATION);
		super.url = NetAddress.HOST + NetAddress.MODIFY_SENIOR_INFORMATION;
	}

	public RequestModifySeniorInformation(String sid, int oid, int sex, long birth, String allergic, String medical, int height, int weight,
			String blood, String nick, String address) {
		super.setType(BaseProtocolFrame.MODIFY_SENIOR_INFORMATION);
		super.url = NetAddress.HOST + NetAddress.MODIFY_SENIOR_INFORMATION;
		super.sid = sid;
		super.nick = nick;
		super.allergic = allergic;
		super.medical = medical;
		if (blood != null) {
			super.blood = blood.toUpperCase().trim();
		}
		super.address = address;
		super.sex = sex;
		super.height = height;
		super.weight = weight;
		super.birth = birth;
		this.oid = oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public int getOid() {
		return this.oid;
	}

	@Override
	public JSONObject toRequestJson() {
		JSONObject result = super.toRequestJson();
		if (result != null) {
			try {
				result.put("oid", oid);
			} catch (JSONException e) {
				e.printStackTrace();
				result = null;
			}
		}
		return result;
	}
}
