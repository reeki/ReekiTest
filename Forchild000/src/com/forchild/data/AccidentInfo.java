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
	protected int sosId;
	protected int oid;

	public AccidentInfo() {

	}

	public AccidentInfo(int id, double lo, double la, long date, String name, int oid, int sosId) {
		this.id = id;
		this.lo = lo;
		this.la = la;
		this.date = date;
		this.name = name;
		this.oid = oid;
		this.sosId = sosId;
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

	public int getSosId() {
		return sosId;
	}

	public void setSosId(int sosId) {
		this.sosId = sosId;
	}

	@Override
	public String toString() {
		return "AccidentInfo [id=" + id + ", lo=" + lo + ", la=" + la + ", date=" + date + ", name=" + name + ", address=" + address + ", sosId="
				+ sosId + "]";
	}

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}
}
