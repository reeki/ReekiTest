package com.forchild.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.forchild000.surface.ServiceCore;

public class BaseProtocolFrame implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7128509615013874075L;

	public static final int UNKNOWN = 10000;
	public static final int UPDATE_LOCATION = 2;
	public static final int UPDATE_SPORT = 3;
	public static final int UPDATE_ACCIDENT = 4;
	public static final int LOGOUT_SENIORS = 5;
	public static final int REQUEST_SENIORS_INFOMATION = 6;
	public static final int REQUEST_RENEW_SID = 7;
	public static final int ALLOW_ADD_SENIOR = 8;
	public static final int REQUEST_CHILD_INFORMATION = 9;
	public static final int MODIFY_CHILD_INFORMATION = 10;
	public static final int MODIFY_SENIOR_INFORMATION = 11;
	public static final int MODIFY_CHILD_PHONE = 12;
	public static final int MODIFY_SENIOR_PHONE = 13;
	public static final int REQUEST_SENIORS_LOCATION = 14;
	public static final int REQUEST_SENIOR_TRACK = 15;
	public static final int REQUEST_SENIOR_SPORT = 16;
	public static final int SEND_MESSAGE = 17;
	public static final int SEND_SOS = 18;
	public static final int SEND_VALID_CODE = 19;
	public static final int REGISTER_SENIOR = 20;
	public static final int LOGIN_SENIORS = 21;
	public static final int DELETE_SENIOR = 22;
	public static final int ADD_SENIOR = 23;
	public static final int REQUEST_SEND_AUTO_MESSAGE = 24;
	public static final int LOGIN_CHILD = 31;
	public static final int LOGOUT_CHILD = 32;
	public static final int REGISTER_CHILD = 30;

	public static final int RESPONSE_INITIATION = -100;
	public static final int INT_INITIATION = -100;
	public static final double DOUBLE_INITIATION = -100d;
	public static final long LONG_INITIATION = -100l;

	public static final int RESPONSE_TYPE_OKAY = 0;
	public static final int RESPONSE_TYPE_UNKNOW = 1;
	public static final int RESPONSE_TYPE_ACCESS_DENIED = 2;
	public static final int RESPONSE_TYPE_FORMAT_ERROR = 3;
	public static final int RESPONSE_TYPE_NO_LOGIN = 4;
	public static final int RESPONSE_TYPE_OTHER_LOGIN = 5;
	public static final int RESPONSE_TYPE_RENEW = 6;
	public static final int RESPONSE_TYPE_NULL_DATA = 7;

	// 非返回结果的原因
	public static final int REASON_NORMALITY = 0;
	public static final int REASON_EXCEEDS_TASKNUMBER_LIMITED = 1;
	public static final int REASON_NO_RESPONSE = 2;
	public static final int REASON_LOGOUT = 3;



	// public static final int RESPONSE_UPDATE_LOCATION = -2;
	// public static final int RESPONSE_UPDATE_SPORT = -3;
	// public static final int RESPONSE_UPDATE_ACCIDENT = -4;
	// public static final int RESPONSE_LOGOUT_SENIORS = -5;
	// public static final int RESPONSE_SENIORS_INFOMATION = -6;
	// public static final int RESPONSE_LOGIN_SENIORS = -21;

	protected String url;
	protected int type = 0;
	protected int req = RESPONSE_INITIATION;
	protected boolean isResponse = false;
	protected List<Handler> handlerList = new ArrayList<Handler>();

	private int reason = REASON_NORMALITY;

	public BaseProtocolFrame() {

	}

	public BaseProtocolFrame(int type) {
		this.type = type;
	}

	public void addHandler(Handler handler) {
		this.handlerList.add(handler);
	}

	public List<Handler> getHandler() {
		return this.handlerList;
	}

	public void clearHandler() {
		handlerList.clear();
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public void setReq(int req) {
		this.req = req;
	}

	public int getReq() {
		return this.req;
	}

	public void setIsResponse(boolean option) {
		this.isResponse = option;
	}

	public boolean isResponse() {
		return this.isResponse;
	}

	public static BaseProtocolFrame parse(BaseProtocolFrame origin, JSONObject source) {
		if (source == null || origin == null) {
			return null;
		}

		try {
			origin.setReq(source.getInt("req"));
			origin.setIsResponse(true);
		} catch (JSONException e2) {
			e2.printStackTrace();
			Log.e("Forolder001 BaseProtocolFrame Json converts exception", "can not get REQ in response json.");
			return null;
		}

		if (origin.req == 0) {
			switch (origin.getType()) {
			case BaseProtocolFrame.LOGIN_SENIORS:
			case BaseProtocolFrame.LOGOUT_SENIORS:
				return origin;
			case BaseProtocolFrame.REQUEST_SENIORS_INFOMATION:
			case BaseProtocolFrame.UPDATE_ACCIDENT:
				return origin;
			case BaseProtocolFrame.UPDATE_LOCATION:
				RequestUpdateLocation rul = null;
				try {
					rul = (RequestUpdateLocation) origin;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

				JSONArray msgt = new JSONArray();
				try {
					msgt = source.getJSONArray("msgt");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				for (int i = 0; i < msgt.length(); ++i) {
					int type = msgt.optInt(i);
					if (type == 0)
						continue;
					try {
						rul.addMessage(MessageFrame.parse(type, source.getJSONArray("content").getJSONObject(i)));
					} catch (JSONException e) {
						e.printStackTrace();
						continue;
					}
				}
				return rul;
			case BaseProtocolFrame.UPDATE_SPORT:
				return origin;
			default:
				return null;
			}
		}
		return origin;
	}

	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		return result;
	}

	public String toJsonString() {
		return this.toRequestJson().toString();
	}

	public void distributes() {
		Log.e("BaseProtocolFrame", "ready to send bean");
		for (Handler handler : handlerList) {
			if (handler != null) {
				Message msg = handler.obtainMessage();
				msg.what = ServiceCore.NETWORK_RESPONSE;
				// msg.arg1 = this.getType();
				msg.obj = this;
				msg.sendToTarget();
			}
		}
	}

	public void setIsResponse(boolean isResponse, int reason) {
		this.setIsResponse(isResponse);
		this.reason = reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

	public int getReason() {
		return this.reason;
	}

}
