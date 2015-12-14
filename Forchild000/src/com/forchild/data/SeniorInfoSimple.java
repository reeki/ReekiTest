package com.forchild.data;

import java.io.Serializable;

public class SeniorInfoSimple implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6664121448173543561L;

	protected int id = -1;
	protected String name = new String();
	protected int oid = BaseProtocolFrame.INT_INITIATION;
	protected String nick;

	public SeniorInfoSimple() {

	}

	public SeniorInfoSimple(int id, String name, int oid) {
		this.id = id;
		this.oid = oid;
		this.name = name;
	}

	public SeniorInfoSimple(int id, String name, int oid, String nick) {
		this.id = id;
		this.oid = oid;
		this.name = name;
		this.nick = nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return this.nick;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public int getOid() {
		return this.oid;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
