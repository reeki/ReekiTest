package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestLogoutChild extends BaseProtocolFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5402587546672489923L;
	
	protected String sid;

	public RequestLogoutChild() {
		super(BaseProtocolFrame.LOGOUT_CHILD);
		super.url = NetAddress.HOST + NetAddress.LOGOUT;
	}

	public RequestLogoutChild(String sid) {
		super(BaseProtocolFrame.LOGOUT_CHILD);
		super.url = NetAddress.HOST + NetAddress.LOGOUT;
		this.sid = sid;
	}
	
	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		try {
			result.put("sid", sid);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
