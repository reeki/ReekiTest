package com.forchild.data;

public class MessageUser extends MessageFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3650028461178584272L;

	public MessageUser() {
		super.type = MessageFrame.USER_MESSAGE;
	}

	public MessageUser(long date, int from, String uname, String content) {
		super(MessageFrame.USER_MESSAGE, date, from, uname);
		super.setCon(content);
	}

	public void setContent(String content) {
		super.setCon(content);
	}

	public String getContent() {
		return super.getCon();
	}
}
