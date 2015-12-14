package com.forchild.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestSeniorTrack extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7647162438799202458L;

	protected int oid = BaseProtocolFrame.INT_INITIATION;
	protected long date;
	protected List<LocationInfo> locationList = new ArrayList<LocationInfo>();

	public RequestSeniorTrack() {
		super(BaseProtocolFrame.REQUEST_SENIOR_TRACK, NetAddress.HOST + NetAddress.REQUEST_SENIOR_TRACK);
	}

	public RequestSeniorTrack(String sid, int oid, long date) {
		super(BaseProtocolFrame.REQUEST_SENIOR_TRACK, NetAddress.HOST + NetAddress.REQUEST_SENIOR_TRACK, sid);
		this.oid = oid;
		this.date = date;
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
	
	public void setLocationList(List<LocationInfo> locationList) {
		this.locationList = locationList;
	}
	
	public List<LocationInfo> getLocationList() {
		return this.locationList;
	}
	
	public void addLocation(LocationInfo location) {
		this.locationList.add(location);
	}

	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		try {
			result.put("sid", sid);
			result.put("oid", oid);
			result.put("date", date);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	@Override
	public String toString() {
		return "RequestSeniorTrack [oid=" + oid + ", date=" + date + ", locationList=" + locationList + "]";
	}
	
	

}
