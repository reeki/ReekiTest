package com.forchild.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MessageFrame implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8051907218265953507L;

	// Message type define
	public static final int SYSTEM_MESSAGE = 1;
	public static final int USER_MESSAGE = 2;
	public static final int SENIORS_ACCIDENT_MESSAGE = 3;
	public static final int ACCIDENT_HELP_MESSAGE = 4;
	public static final int USER_INFO_CHANGED_MESSAGE = 5;
	public static final int REQUEST_ATTENTION = 6;

	public static final int SENDSTATE_SENDING = 2;
	public static final int SENDSTATE_FAULT = 1;
	public static final int SENDSTATE_FINISHED = 3;

	public static final int USERMESSAGETYPE_AUTO_CIRCLE_MESSAGE = 1;
	public static final int USERMESSAGETYPE_AUTO_ONEOFF_MESSAGE = 2;
	public static final int USERMESSAGETYPE_AUTO_MEDICAL_MESSAGE = 3;
	public static final int USERMESSAGETYPE_MANUAL_MESSAGE = 4;
	public static final int USERMESSAGETYPE_RECEIVED_MESSAGE = 11;

	protected int type = BaseProtocolFrame.INT_INITIATION;
	protected long date = 0l;
	protected long lastShowTime = 0l;
	protected int state = 0;
	protected int from = BaseProtocolFrame.INT_INITIATION;
	protected String uname = new String();
	protected String loginId;
	protected boolean isComMeg;
	protected String con = new String();
	protected int userMsgType = 0; // 标示用户消息的发送类型，如自动消息，手动消息
	protected int oid = BaseProtocolFrame.INT_INITIATION;

	public MessageFrame() {

	}

	public MessageFrame(int type, long date, int from, String uname) {
		this.type = type;
		this.date = date;
		this.from = from;
		this.uname = uname;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public int getOid() {
		return this.oid;
	}

	public void setUserMsgType(int type) {
		this.userMsgType = type;
	}

	public int getUserMsgType() {
		return this.userMsgType;
	}

	public void setCon(String con) {
		this.con = con;
	}

	public String getCon() {
		return this.con;
	}

	public boolean getMsgType() {
		return userMsgType > 10 ? true : false;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginId() {
		return this.loginId;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public void setLastTime(long time) {
		this.lastShowTime = time;
	}

	public long getLastTime() {
		return this.lastShowTime;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public void setDate(long time) {
		this.date = time;
	}

	public long getDate() {
		return this.date;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getFrom() {
		return this.from;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUname() {
		return this.uname;
	}

	public static MessageFrame parse(int type, JSONObject source) {
		switch (type) {
		case MessageFrame.SYSTEM_MESSAGE:
			MessageSystem ms = new MessageSystem();
			try {
				ms.setContent(source.getString("content"));
				ms.setDate(source.getLong("date"));
				ms.setFrom(source.getInt("from"));
				ms.setUname(source.getString("uname"));
				ms.setType(type);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return ms;
		case MessageFrame.USER_MESSAGE:
			MessageUser mu = new MessageUser();
			try {
				mu.setContent(source.getString("content"));
				mu.setDate(source.getLong("date"));
				mu.setFrom(source.getInt("from"));
				mu.setUname(source.getString("uname"));
				mu.setType(type);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return mu;
		case MessageFrame.SENIORS_ACCIDENT_MESSAGE:
			MessageAccident ma = new MessageAccident();
			JSONObject content = source.optJSONObject("content");
			if (content != null) {
				// TODO sosid 出错时 进行处理
				try {
					ma.setSosId(content.getInt("sosid"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				int oldId = content.optInt("oldid", BaseProtocolFrame.INT_INITIATION);
				int acc = content.optInt("acc");
				double lo = content.optDouble("lo", BaseProtocolFrame.DOUBLE_INITIATION);
				double la = content.optDouble("la", BaseProtocolFrame.DOUBLE_INITIATION);
				ma.setAcc(acc);
				ma.setOldid(oldId);
				ma.setLa(la);
				ma.setLo(lo);
				ma.setAddress(content.optString("adr"));
				ma.setType(type);
			}
			ma.setDate(source.optLong("date"));
			ma.setFrom(source.optInt("from"));
			ma.setUname(source.optString("uname"));
			return ma;
		case MessageFrame.ACCIDENT_HELP_MESSAGE:
			MessageHelp mg = new MessageHelp();
			JSONObject SOSContent = source.optJSONObject("content");
			if (SOSContent != null) {
				String con = SOSContent.optString("con");
				int acc = SOSContent.optInt("acc");
				double lo = SOSContent.optDouble("lo", BaseProtocolFrame.DOUBLE_INITIATION);
				double la = SOSContent.optDouble("la", BaseProtocolFrame.DOUBLE_INITIATION);
				try {
					int sosId = SOSContent.getInt("sosid");
					mg.setAcc(acc);
					mg.setCon(con);
					mg.setLa(la);
					mg.setLo(lo);
					mg.setSosId(sosId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			mg.setDate(source.optLong("date"));
			mg.setFrom(source.optInt("from"));
			mg.setUname(source.optString("uname"));
			mg.setType(type);
			return mg;
		case MessageFrame.USER_INFO_CHANGED_MESSAGE:
			return new MessageUserInfoChanged();
		case MessageFrame.REQUEST_ATTENTION:
			MessageAttention msgAttention = new MessageAttention();
			JSONObject attentionContent = source.optJSONObject("content");
			if (attentionContent != null) {
				msgAttention.setPhone(attentionContent.optString("phone"));
				msgAttention.setNick(attentionContent.optString("nick"));
				msgAttention.setOid(attentionContent.optInt("oid"));
				msgAttention.setFollowid(attentionContent.optInt("followid"));
				Log.e("MessageFrame.拼接。关注的详细信息拼接",
						"oid:" + source.optInt("oid") + ", followid:" + source.optInt("followid") + ", source:" + source.toString() + ", content:"
								+ attentionContent.toString());
			} else {
				Log.e("MessageFrame", "content is null");
			}
			msgAttention.setDate(source.optLong("date"));
			msgAttention.setFrom(source.optInt("from"));
			msgAttention.setUname(source.optString("uname"));
			msgAttention.setType(type);
			Log.e("MessageFrame", msgAttention.toString());
			return msgAttention;
		}
		return null;
	}

	@Override
	public String toString() {
		return "MessageFrame [type=" + type + ", date=" + date + ", lastShowTime=" + lastShowTime + ", state=" + state + ", from=" + from
				+ ", uname=" + uname + ", loginId=" + loginId + ", isComMeg=" + isComMeg + ", con=" + con + ", userMsgType=" + userMsgType + ", oid="
				+ oid + "]";
	}

}
