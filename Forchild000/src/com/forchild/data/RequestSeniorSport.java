package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestSeniorSport extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7555717994434412133L;

	protected int oid = BaseProtocolFrame.INT_INITIATION;
	protected long date;
	protected int run, walk, stand, unknown, cal;

	public RequestSeniorSport() {
		super(BaseProtocolFrame.REQUEST_SENIOR_SPORT, NetAddress.HOST + NetAddress.REQUEST_SENIOR_SPORT);
	}

	public RequestSeniorSport(String sid, int oid, long date) {
		super(BaseProtocolFrame.REQUEST_SENIOR_SPORT, NetAddress.HOST + NetAddress.REQUEST_SENIOR_SPORT, sid);
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

	public void setRun(int run) {
		this.run = run;
	}

	public int getRun() {
		return this.run;
	}

	public void setWalk(int walk) {
		this.walk = walk;
	}

	public int getWalk() {
		return this.walk;
	}

	public void setStand(int stand) {
		this.stand = stand;
	}

	public int getStand() {
		return this.stand;
	}

	public void setUnknown(int unknown) {
		this.unknown = unknown;
	}

	public int getUnknown() {
		return this.unknown;
	}

	public void setCal(int cal) {
		this.cal = cal;
	}

	public int getCal() {
		return this.cal;
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
}
