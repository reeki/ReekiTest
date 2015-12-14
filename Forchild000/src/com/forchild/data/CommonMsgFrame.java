package com.forchild.data;

import java.io.Serializable;

public class CommonMsgFrame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 570829160124120509L;
	protected int id = 0;
	protected String content;

	public CommonMsgFrame() {

	}

	public CommonMsgFrame(int id, String content) {
		this.id = id;
		this.content = content;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

}
