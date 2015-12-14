package com.forchild.data;

import java.io.Serializable;

public class LocationFrame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4990396509863678769L;
	
	protected double lo = BaseProtocolFrame.DOUBLE_INITIATION;
	protected double la = BaseProtocolFrame.DOUBLE_INITIATION;

	public LocationFrame() {

	}

	public LocationFrame(double lo, double la) {
		this.lo = lo;
		this.la = la;
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
}
