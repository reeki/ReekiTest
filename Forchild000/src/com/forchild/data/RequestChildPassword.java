package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestChildPassword extends BaseProtocolFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4008077596915965666L;

	
	protected String valid;
	protected String phone;
	protected String npass;
	
	public RequestChildPassword() {
		super(BaseProtocolFrame.REQUEST_CHANGE_CHILD_PASSWORD);
		super.url = NetAddress.HOST + NetAddress.MODIFY_CHILD_PASSWORD;
	}
	
	public RequestChildPassword(String phone, String npass, String valid) {
		this();
		this.phone = phone;
		this.npass = npass;
		this.valid = valid;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getPhone() {
		return this.phone;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getNpass() {
		return npass;
	}

	public void setNpass(String npass) {
		this.npass = npass;
	}
	
	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		try {
			result.put("phone", phone);
			result.put("valid", valid);
			result.put("npass", npass);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
}
