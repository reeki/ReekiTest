package com.forchild.data;

public class LocationInfo extends LocationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4541933429214899271L;
	
	protected long date;
	
	public LocationInfo() {
		
	}
	
	public LocationInfo(double lo, double la, long date) {
		super(lo, la);
		this.date = date;
	}
	
	public void setDate(long date) {
		this.date = date;
	}

	public long getDate() {
		return this.date;
	}

}
