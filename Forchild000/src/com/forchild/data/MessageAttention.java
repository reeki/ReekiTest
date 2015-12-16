package com.forchild.data;

public class MessageAttention extends MessageFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4845667748011268023L;
	protected String phone = new String();
	protected String nick = new String();
	protected int followid = BaseProtocolFrame.INT_INITIATION;

	public MessageAttention() {
		super.type = MessageFrame.ACCIDENT_HELP_MESSAGE;
	}

	public MessageAttention(int type, long date, int from, String uname) {
		super(MessageFrame.SENIORS_ACCIDENT_MESSAGE, date, from, uname);
	}

	public MessageAttention(int type, long date, int from, String uname, String phone, String nick, int oid, int followid) {
		super(MessageFrame.SENIORS_ACCIDENT_MESSAGE, date, from, uname);
		this.phone = phone;
		this.nick = nick;
		super.setOid(oid);
		this.followid = followid;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return this.nick;
	}

	public void setFollowid(int followid) {
		this.followid = followid;
	}

	public int getFollowid() {
		return this.followid;
	}

	@Override
	public String toString() {
		return "MessageAttention [phone=" + phone + ", nick=" + nick + ", followid=" + followid + ", type=" + type + ", date=" + date
				+ ", lastShowTime=" + lastShowTime + ", state=" + state + ", from=" + from + ", uname=" + uname + ", loginId=" + loginId
				+ ", isComMeg=" + isComMeg + ", con=" + con + ", userMsgType=" + userMsgType + ", oid=" + oid + "]";
	}
}
