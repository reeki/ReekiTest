package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestModifyChildPhone extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2171731276587768147L;

	protected String pPhone, code, phone, validCode;

	public RequestModifyChildPhone() {
		super(BaseProtocolFrame.MODIFY_CHILD_PHONE, NetAddress.HOST + NetAddress.MODIFY_CHILD_PHONE);
	}

	public RequestModifyChildPhone(String sid, String pPhone, String code, String phone, String validCode) {
		super(BaseProtocolFrame.MODIFY_CHILD_PHONE, NetAddress.HOST + NetAddress.MODIFY_CHILD_PHONE, sid);

		this.pPhone = pPhone;
		this.code = code;
		this.phone = phone;
		this.validCode = validCode;
	}

	public void setPPhone(String pPhone) {
		this.pPhone = pPhone;
	}

	public String getPPhone() {
		return this.pPhone;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public String getValidCode() {
		return this.validCode;
	}

	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		try {
			result.put("sid", super.sid);
			result.put("pphone", pPhone);
			result.put("code", code);
			result.put("phone", phone);
			result.put("valid", validCode);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
