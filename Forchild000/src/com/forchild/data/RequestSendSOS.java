package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestSendSOS extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5334839155969632155L;
	protected int oid = BaseProtocolFrame.INT_INITIATION;
	protected long date;
	protected String content;
	
	public RequestSendSOS() {
		super(BaseProtocolFrame.SEND_SOS, NetAddress.HOST + NetAddress.SEND_SOS);
	}

	public RequestSendSOS(String sid, int oid, String content) {
		super(BaseProtocolFrame.SEND_SOS, NetAddress.HOST + NetAddress.SEND_SOS, sid);
		this.oid = oid;
		this.content = content;
	}
	
	public void setOid(int oid) {
		this.oid = oid;
	}

	public int getOid() {
		return this.oid;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}
	
	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		try {
			result.put("sid", sid);
			result.put("oid", oid);
			result.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
}
