package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestRenew extends BaseProtocolFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -193382908786536872L;

	protected String sid;

	public RequestRenew() {
		super(BaseProtocolFrame.REQUEST_RENEW_SID);
		super.setUrl(NetAddress.HOST + NetAddress.RENEW);
	}

	public RequestRenew(String sid) {
		this();
		this.sid = sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSid() {
		return this.sid;
	}

	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		if (result != null) {
			try {
				result.put("sid", sid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
