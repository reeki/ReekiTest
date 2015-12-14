package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestSeniorLocation extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6799807752973927497L;

	public static final int STATE_RUN = 1;
	public static final int STATE_WALK = 2;
	public static final int STATE_STAND = 3;
	public static final int STATE_UNKNOWN = 4;

	protected int oid = BaseProtocolFrame.INT_INITIATION;

	protected long date;
	protected double lo = BaseProtocolFrame.DOUBLE_INITIATION;
	protected double la = BaseProtocolFrame.DOUBLE_INITIATION;
	protected int state;

	public RequestSeniorLocation() {
		super(BaseProtocolFrame.REQUEST_SENIORS_LOCATION, NetAddress.HOST + NetAddress.REQUEST_SENIORS_LOCATION);
	}

	public RequestSeniorLocation(String sid, int oid) {
		super(BaseProtocolFrame.REQUEST_SENIORS_LOCATION, NetAddress.HOST + NetAddress.REQUEST_SENIORS_LOCATION, sid);
		this.oid = oid;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return this.state;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public int getOid() {
		return this.oid;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getDate() {
		return this.date;
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

	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		try {
			result.put("sid", super.sid);
			result.put("oid", oid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

}
