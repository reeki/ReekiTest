package com.forchild.data;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateInfo extends Timestamp {
	private static final long serialVersionUID = 8610754842435810618L;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public DateInfo(long time) {
		super(time);
	}

	public DateInfo parseTS(Object sourse) throws ParseException {
		Date time = sdf.parse(sourse.toString());
		this.setTime(time.getTime());
		return this;
	}

	public DateInfo setNowTime() {
		this.setTime(System.currentTimeMillis());
		return this;
	}

	public boolean isToday(DateInfo objDate) {
		if (this.getYear() == objDate.getYear() && this.getMonth() == objDate.getMonth() && this.getDay() == objDate.getDay()) {
			return true;
		}
		return false;
	}

	public String getYYYYMMDD() {
		int month = this.getMonth() + 1;
		return this.getYear() + "-" + month + "-" + this.getDate();
	}

	public String getRTime() {
		return sdf.format(this);
	}

	public boolean isToday(long currentTimeMillis) {
		DateInfo objDate = new DateInfo(currentTimeMillis);
		return this.isToday(objDate);
	}

	@Override
	public int getYear() {
		return super.getYear() + 1900;
	}

	@Override
	public void setYear(int year) {
		super.setYear(year - 1900);
	}
}
