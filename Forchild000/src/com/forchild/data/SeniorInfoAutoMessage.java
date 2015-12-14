package com.forchild.data;

public class SeniorInfoAutoMessage extends SeniorInfoSimple {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1946211652960764143L;

	protected String content;
	protected int hour;
	protected int minute;
	protected int year;
	protected int month;
	protected int day;
	protected int type;
	protected int sex;
	protected String loginId;

	public SeniorInfoAutoMessage() {
		super();
	}

	public SeniorInfoAutoMessage(int id, int to, String name, String nick, String content, int hour, int minute) {
		super(id, name, to);

		this.nick = nick;
		this.content = content;
		this.hour = hour;
		this.minute = minute;
	}

	public SeniorInfoAutoMessage(int id, int to, int sex, String name, String nick, String content, int year, int month, int day, int hour,
			int minute, int type) {
		super(id, name, to);

		this.nick = nick;
		this.content = content;
		this.hour = hour;
		this.minute = minute;
		this.year = year;
		this.month = month;
		this.day = day;
		this.type = type;
		this.sex = sex;
	}

	public SeniorInfoAutoMessage(int id, int to, int sex, String name, String nick, String content, int year, int month, int day, int hour,
			int minute, int type, String loginId) {
		super(id, name, to);
		
		this.loginId = loginId;
		this.nick = nick;
		this.content = content;
		this.hour = hour;
		this.minute = minute;
		this.year = year;
		this.month = month;
		this.day = day;
		this.type = type;
		this.sex = sex;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginId() {
		return this.loginId;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getSex() {
		return this.sex;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return this.year;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMonth() {
		return this.month;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return this.day;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public boolean setContent(String content) {
		if (content.length() > 256) {
			return false;
		}
		this.content = content;
		return true;
	}

	public String getContent() {
		return this.content;
	}

	public boolean setHour(int hour) {
		if (hour > 23 || hour <= 0) {
			return false;
		}

		this.hour = hour;
		return true;
	}

	public int getHour() {
		return this.hour;
	}

	public boolean setMinute(int minute) {
		if (minute > 59 || minute <= 0) {
			return false;
		}

		this.minute = minute;
		return true;
	}

	public int getMinute() {
		return this.minute;
	}

	@Override
	public String toString() {
		return "SeniorInfoAutoMessage [content=" + content + ", hour=" + hour + ", minute=" + minute + ", year=" + year + ", month=" + month
				+ ", day=" + day + ", type=" + type + ", sex=" + sex + ", id=" + id + ", name=" + name + ", oid=" + oid + ", nick=" + nick + "]";
	}

}
