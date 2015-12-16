package com.forchild.data;

public class MessageAccident extends MessageFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8928265260559538930L;
	protected int acc = BaseProtocolFrame.INT_INITIATION;
	protected double lo = BaseProtocolFrame.DOUBLE_INITIATION;
	protected double la = BaseProtocolFrame.DOUBLE_INITIATION;
	protected String address;
	protected int sosId;

	public MessageAccident() {
		super.type = MessageFrame.SENIORS_ACCIDENT_MESSAGE;
	}

	public MessageAccident(int type, long date, int from, String uname) {
		super(MessageFrame.SENIORS_ACCIDENT_MESSAGE, date, from, uname);
	}

	public MessageAccident(int type, long date, int from, String uname, int oldid, int acc, double lo, double la, String address, int sosId) {
		super(MessageFrame.SENIORS_ACCIDENT_MESSAGE, date, from, uname);
		super.setOid(oldid);
		this.acc = acc;
		this.lo = lo;
		this.la = la;
		this.address = address;
		this.sosId = sosId;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setOldid(int oldid) {
		super.setOid(oldid);
	}

	public int getOldid() {
		return super.getOid();
	}

	public void setAcc(int acc) {
		this.acc = acc;
	}

	public int getAcc() {
		return this.acc;
	}

	public void setLo(double lo) {
		this.lo = lo;
	}

	public double getLo() {
		return this.lo;
	}

	public void setLa(double la) {
		this.la = la;
	}

	public double getLa() {
		return this.la;
	}

	public int getSosId() {
		return sosId;
	}

	public void setSosId(int sosId) {
		this.sosId = sosId;
	}

}
