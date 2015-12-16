package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestSendSOS extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5334839155969632155L;
	protected int sosid = BaseProtocolFrame.INT_INITIATION;
	protected String content;

	public RequestSendSOS() {
		super(BaseProtocolFrame.SEND_SOS, NetAddress.HOST + NetAddress.SEND_SOS);
	}

	public RequestSendSOS(String sid, int sosid, String content) {
		super(BaseProtocolFrame.SEND_SOS, NetAddress.HOST + NetAddress.SEND_SOS, sid);
		this.sosid = sosid;
		this.content = content;
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
			result.put("sosid", sosid);
			result.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public int getSosid() {
		return sosid;
	}

	public void setSosid(int sosid) {
		this.sosid = sosid;
	}
}
