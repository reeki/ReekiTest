package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestBaseSidFrame extends BaseProtocolFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -571664770388213496L;

	protected String sid;

	public RequestBaseSidFrame() {

	}
	
	public RequestBaseSidFrame(int type) {
		super(type);
	}

	public RequestBaseSidFrame(int type, String url) {
		super(type);
		super.url = url;
	}

	public RequestBaseSidFrame(int type, String url, String sid) {
		this(type, url);
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
		try {
			result.put("sid", sid);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
