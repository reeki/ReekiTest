package com.forchild.data;

public class RequestSendAutoMessage extends RequestSendMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7543004774372042316L;

	public RequestSendAutoMessage() {
		super();
		super.setType(BaseProtocolFrame.REQUEST_SEND_AUTO_MESSAGE);
	}

	public RequestSendAutoMessage(String sid, int to, String uname, String content) {
		super(sid, to, uname, content);
		super.setType(BaseProtocolFrame.REQUEST_SEND_AUTO_MESSAGE);
	}

	public RequestSendAutoMessage(String sid, MessageFrame msg) {
		super(sid, msg);
		super.setType(BaseProtocolFrame.REQUEST_SEND_AUTO_MESSAGE);
	}

	
	
}
