package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestRegisterChild extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6474561135028364042L;
	
	protected String phoneNum, pw;
	protected String valid;
	protected String cardId, name;

	public RequestRegisterChild() {
		super.setType(BaseProtocolFrame.REGISTER_CHILD);
		super.url = NetAddress.HOST + NetAddress.REGISTER_CHILD;
	}

	public RequestRegisterChild(String phoneNumStr, String passwordStr, String valid, String name, String cardId) {
		super.setType(BaseProtocolFrame.REGISTER_CHILD);
		super.url = NetAddress.HOST + NetAddress.REGISTER_CHILD;
		this.valid = valid;
		this.name = name;
		this.phoneNum = phoneNumStr;
		this.pw = passwordStr;
		this.cardId = cardId;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getValid() {
		return this.valid;
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

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setCardid(String cardId) {
		this.cardId = cardId;
	}

	public String getCardid() {
		return this.cardId;
	}

	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		try {
			result.put("phone", phoneNum);
			result.put("passwd", pw);
			result.put("valid", valid);
//			result.put("cardid", cardId);
			result.put("name", name);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
