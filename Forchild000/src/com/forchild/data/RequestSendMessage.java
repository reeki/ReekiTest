package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestSendMessage extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3108137344852965186L;

	protected int to = BaseProtocolFrame.INT_INITIATION;
	protected String uname, content;
	protected MessageFrame msgObj = null;

	public RequestSendMessage() {
		super(BaseProtocolFrame.SEND_MESSAGE, NetAddress.HOST + NetAddress.SEND_MESSAGE);
	}

	public RequestSendMessage(String sid, int to, String uname, String content) {
		super(BaseProtocolFrame.SEND_MESSAGE, NetAddress.HOST + NetAddress.SEND_MESSAGE, sid);
		this.to = to;
		this.uname = uname;
		this.content = content;
	}

	public RequestSendMessage(String sid, MessageFrame msg) {
		super(BaseProtocolFrame.SEND_MESSAGE, NetAddress.HOST + NetAddress.SEND_MESSAGE, sid);
		msgObj = msg;
		this.to = msg.getOid();
		this.uname = msg.getUname();
		this.content = msg.getCon();
	}

	public void setMsgEntity(MessageFrame msg) {
		this.msgObj = msg;
	}

	public MessageFrame getMsgEntity() {
		return this.msgObj;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public int getTo() {
		return this.to;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUname() {
		return this.uname;
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
			result.put("to", to);
			result.put("uname", uname);
			result.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
}
