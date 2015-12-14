package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestModifySeniorPhone extends RequestModifyChildPhone {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3977607010390866198L;

	protected int oid = BaseProtocolFrame.INT_INITIATION;

	public RequestModifySeniorPhone() {
		super.setType(BaseProtocolFrame.MODIFY_SENIOR_PHONE);
		super.url = NetAddress.HOST + NetAddress.MODIFY_SENIOR_PHONE;
	}

	public RequestModifySeniorPhone(String sid, int oid, String pPhone, String code, String phone, String validCode) {
		this();
		
		super.sid = sid;
		super.pPhone = pPhone;
		super.code = code;
		super.phone = phone;
		super.validCode = validCode;
		
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
			}
		}
		return result;
	}

}
