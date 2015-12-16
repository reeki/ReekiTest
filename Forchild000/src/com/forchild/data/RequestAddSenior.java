package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestAddSenior extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8634547910954997232L;

	protected String mPhone;
	protected String phone;

	public RequestAddSenior() {
		super(BaseProtocolFrame.ADD_SENIOR);
		super.url = NetAddress.HOST + NetAddress.ADD_SENIOR;
	}

	public RequestAddSenior(String sid, String phone, String mPhone) {
		this();
		this.sid = sid;
		this.mPhone = mPhone;
		this.phone = phone;
	}

	public void setMPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	public String getMPhone() {
		return this.mPhone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		try {
			result.put("sid", sid);
			result.put("phone", phone);
			result.put("mphone", mPhone);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
