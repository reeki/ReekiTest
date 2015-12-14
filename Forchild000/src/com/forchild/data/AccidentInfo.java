package com.forchild.data;

import java.io.Serializable;

public class AccidentInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8955556862867875629L;

	protected int id;
	protected double lo, la;
	protected long date;
	protected String name, address;

	public AccidentInfo() {

	}

	public AccidentInfo(int id, double lo, double la, long date, String name) {
		this.id = id;
		this.lo = lo;
		this.la = la;
		this.date = date;
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setLongitude(double lo) {
		this.lo = lo;
	}

	public double getLongitude() {
		return this.lo;
	}

	public void setLatitude(double la) {
		this.la = la;
	}

	public double getLatitude() {
		return this.la;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getDate() {
		return this.date;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
