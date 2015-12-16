package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestAllowAddSenior extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6867393969614277452L;
	
	protected int fid;
	protected int agree;

	public RequestAllowAddSenior() {
		super(BaseProtocolFrame.ALLOW_ADD_SENIOR);
		super.url = NetAddress.HOST + NetAddress.ALLOW_ADD_SENIOR;
	}

	public RequestAllowAddSenior(String sid, int fid, int agree) {
		this();
		super.sid = sid;
		this.fid = fid;
		this.agree = agree;
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
			result.put("agree", agree);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public int getAgree() {
		return agree;
	}

	public void setAgree(int agree) {
		this.agree = agree;
	}

}
