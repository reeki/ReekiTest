package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestSendValidCode extends BaseProtocolFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6535872553717330417L;
	protected String phone;

	public RequestSendValidCode() {
		super(BaseProtocolFrame.SEND_VALID_CODE);
		super.url = NetAddress.HOST + NetAddress.SEND_VALID_CODE;
	}

	public RequestSendValidCode(String phone) {
		super(BaseProtocolFrame.SEND_VALID_CODE);
		super.url = NetAddress.HOST + NetAddress.SEND_VALID_CODE;
		this.phone = phone;
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
			result.put("phone", phone);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
