package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestDeleteSenior extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2315756178920420498L;
	
	protected int oid = BaseProtocolFrame.INT_INITIATION;
	
	public RequestDeleteSenior() {
		super(BaseProtocolFrame.DELETE_SENIOR);
		super.url = NetAddress.HOST + NetAddress.DELETE_SENIOR;
	}
	
	public RequestDeleteSenior(String sid, int oid) {
		this();
		super.sid = sid;
		this.oid = oid;
	}
	
	public void setOid(int oid) {
		this.oid = oid;
	}
	
	public int getOid() {
		return this.oid;
	}

	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		try {
			result.put("sid", sid);
			result.put("oid", oid);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
	
	
}
