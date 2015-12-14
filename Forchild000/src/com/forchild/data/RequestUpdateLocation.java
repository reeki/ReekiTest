package com.forchild.data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestUpdateLocation extends RequestBaseSidFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4844913467845313866L;
	// for request
	protected double lo;
	protected double la;
	protected long date;
	protected int state;

	// for response
	protected List<MessageFrame> msg = new ArrayList<MessageFrame>();

	public RequestUpdateLocation() {
		super(BaseProtocolFrame.UPDATE_LOCATION);
		super.url = NetAddress.HOST + NetAddress.UPDATE_LOCATION;
		date = 0l;
	}

	public RequestUpdateLocation(String id, long date, double latitude, double longitude) {
		super(BaseProtocolFrame.UPDATE_LOCATION);
		super.url = NetAddress.HOST + NetAddress.UPDATE_LOCATION;
		this.lo = longitude;
		this.la = latitude;
		super.sid = id;
		this.date = date;
	}
	
	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}
	
	public void setLongitude(double longitude) {
		this.lo = longitude;
	}

	public void setLatitude(double latitude) {
		this.la = latitude;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public void parseDate(long time) {
		this.date = time;
	}

	public double getLatitude() {
		return this.la;
	}

	public double getLongitude() {
		return this.lo;
	}

	public long getDate() {
		return this.date;
	}

	public String getDateString() {
		Timestamp ts = new Timestamp(date);
		return ts.toString();
	}

	public void addMessage(MessageFrame message) {
		this.msg.add(message);
	}

	public List<MessageFrame> getMessage() {
		return this.msg;
	}

	@Override
	public JSONObject toRequestJson() {
		JSONObject result = super.toRequestJson();
		if (sid != null) {
			try {
				result.put("sid", sid);
				result.put("date", date);
				result.put("lo", lo);
				result.put("la", la);
			} catch (JSONException e) {
				e.printStackTrace();
				result = null;
			}
		} else {
			result = null;
		}
		return result;
	}
}
