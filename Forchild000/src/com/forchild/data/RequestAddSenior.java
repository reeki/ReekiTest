package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;


public class RequestAddSenior extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8634547910954997232L;

	protected String cardId;
	protected String phone;

	public RequestAddSenior() {
		super(BaseProtocolFrame.ADD_SENIOR);
		super.url = NetAddress.HOST + NetAddress.ADD_SENIOR;
	}

	public RequestAddSenior(String sid, String cardId, String phone) {
		this();
		this.sid = sid;
		this.cardId = cardId;
		this.phone = phone;
	}

	public void setCardid(String cardId) {
		this.cardId = cardId;
	}

	public String getCardid() {
		return this.cardId;
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
			result.put("cardid", cardId);
			result.put("mphone", phone);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
