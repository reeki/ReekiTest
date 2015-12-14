package com.forchild.data;

public class MessageSystem extends MessageFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9083185329870431869L;
	
	public MessageSystem() {
		super.type = MessageFrame.SYSTEM_MESSAGE;
	}
	
	public MessageSystem(long date, int from, String uname, String content) {
		super(MessageFrame.SYSTEM_MESSAGE, date, from, uname);
		super.con = content;
	}
	
	public void setContent(String content) {
		super.setCon(content);
	}
	
	public String getContent() {
		return super.getCon();
	}
}
