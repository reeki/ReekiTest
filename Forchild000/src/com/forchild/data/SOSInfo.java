package com.forchild.data;

public class SOSInfo extends AccidentInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1666533730199277151L;

	protected String content;

	public SOSInfo() {

	}

	public SOSInfo(int id, double lo, double la, long date, String name, String content, int sosId) {
		super(id, lo, la, date, name, 0, sosId);
		this.content = content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}
}
