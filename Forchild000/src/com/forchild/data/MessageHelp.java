package com.forchild.data;

public class MessageHelp extends MessageFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4601770214447275733L;
	protected double lo = BaseProtocolFrame.DOUBLE_INITIATION;
	protected double la = BaseProtocolFrame.DOUBLE_INITIATION;
	protected int acc = BaseProtocolFrame.INT_INITIATION;
	
	public MessageHelp() {
		super.type = MessageFrame.ACCIDENT_HELP_MESSAGE;
	}
	
	public MessageHelp(int type, long date, int from, String uname) {
		super(MessageFrame.SENIORS_ACCIDENT_MESSAGE, date, from, uname);
	}
	
	public MessageHelp(int type, long date, int from, String uname, int acc, double lo, double la) {
		super(MessageFrame.SENIORS_ACCIDENT_MESSAGE, date, from, uname);
		this.acc = acc;
		this.lo = lo;
		this.la = la;
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
}
