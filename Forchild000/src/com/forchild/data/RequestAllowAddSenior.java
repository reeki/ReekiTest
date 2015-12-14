package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestAllowAddSenior extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6867393969614277452L;
	
	protected int fid;

	public RequestAllowAddSenior() {
		super(BaseProtocolFrame.ALLOW_ADD_SENIOR);
		super.url = NetAddress.HOST + NetAddress.ALLOW_ADD_SENIOR;
	}

	public RequestAllowAddSenior(String sid, int fid) {
		this();
		super.sid = sid;
		this.fid = fid;
	}
	
	
	public void setFid(int fid) {
		this.fid = fid;
	}
	
	public int getFid() {
		return this.fid;
	}
	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		try {
			result.put("sid", sid);
			result.put("fid", fid);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
