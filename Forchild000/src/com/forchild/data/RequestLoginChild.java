package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestLoginChild extends BaseProtocolFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3540488262286198896L;

	protected String phoneNum, pw, sid;

	public RequestLoginChild() {
		super(BaseProtocolFrame.LOGIN_CHILD);
		super.url = NetAddress.HOST + NetAddress.LOGIN;
	}

	public RequestLoginChild(String phoneNumStr, String passwordStr) {
		super(BaseProtocolFrame.LOGIN_CHILD);
		super.url = NetAddress.HOST + NetAddress.LOGIN;
		this.phoneNum = phoneNumStr;
		this.pw = passwordStr;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSid() {
		return this.sid;
	}

	public void setPhoneNumber(String phone) {
		this.phoneNum = phone;
	}

	public void setPassword(String password) {
		this.pw = password;
	}

	public String getPhoneNumber() {
		return this.phoneNum;
	}

	public String getPassword() {
		return this.pw;
	}

	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		try {
			result.put("uname", phoneNum);
			result.put("passwd", pw);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
