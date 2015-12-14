package com.forchild.server;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class Preferences extends SharedPreferencesHelper {

	public Preferences(Context context) {
		super(context);
	}

	public void setLoginState(boolean option) {
		super.putBoolean("login_state", option);
	}

	public boolean getLoginState() {
		return super.getBoolean("login_state", false);
	}

	public void setUserName(String userName) {
		super.putString("user_name", userName);
	}

	public String getUserName() {
		return super.getString("user_name", null);
	}

	public void setTodayWeather(String dayWeather, String dayTemp, String nightTemp) {
		super.putString("today_weather", dayWeather);
		super.putString("today_day_temp", dayTemp);
		super.putString("today_night_temp", nightTemp);
	}

	public void setTomorrowWeather(String dayWeather, String dayTemp, String nightTemp) {
		super.putString("tomorrow_weather", dayWeather);
		super.putString("tomorrow_day_temp", dayTemp);
		super.putString("tomorrow_night_temp", nightTemp);
	}

	public void setThirdDayWeather(String dayWeather, String dayTemp, String nightTemp) {
		super.putString("third_day_weather", dayWeather);
		super.putString("third_day_day_temp", dayTemp);
		super.putString("third_day_night_temp", nightTemp);
	}

	public Map<String, String> getTodayWeather() {
		String tw = super.getString("today_weather", null);
		String dt = super.getString("today_day_temp", null);
		String nt = super.getString("today_night_temp", null);
		if (tw != null && dt != null && nt != null) {
			Map<String, String> result = new HashMap<String, String>();
			result.put("weather", tw);
			result.put("dayTemp", dt);
			result.put("nightTemp", nt);
			return result;
		} else {
			return null;
		}
	}

	public Map<String, String> getTomorrowWeather() {
		String tw = super.getString("tomorrow_weather", null);
		String dt = super.getString("tomorrow_day_temp", null);
		String nt = super.getString("tomorrow_night_temp", null);
		if (tw != null && dt != null && nt != null) {
			Map<String, String> result = new HashMap<String, String>();
			result.put("weather", tw);
			result.put("dayTemp", dt);
			result.put("nightTemp", nt);
			return result;
		} else {
			return null;
		}
	}

	public Map<String, String> getThirdDayWeather() {
		String tw = super.getString("third_day_weather", null);
		String dt = super.getString("third_day_day_temp", null);
		String nt = super.getString("third_day_night_temp", null);
		if (tw != null && dt != null && nt != null) {
			Map<String, String> result = new HashMap<String, String>();
			result.put("weather", tw);
			result.put("dayTemp", dt);
			result.put("nightTemp", nt);
			return result;
		} else {
			return null;
		}
	}

	public void setSex(int sex) {
		super.putInt("sex", sex);
	}

	public int getSex() {
		return super.getInt("sex", 0);
	}

	public void setAge(int age) {
		super.putInt("age", age);
	}

	public int getAge() {
		return super.getInt("age", 0);
	}

	public void setMedicalInfo(String medicalInfo) {
		super.putString("medical_info", medicalInfo);
	}

	public String getMedicalInfo() {
		return super.getString("medical_info", null);
	}

	public void setAllergic(String allergic) {
		super.putString("allergic", allergic);
	}

	public String getAllergic() {
		return super.getString("allergic", null);
	}

	public void setHeight(int height) {
		super.putInt("height", height);

	}

	public int getHeight() {
		return super.getInt("height", 0);
	}

	public void setWeight(int weight) {
		super.putInt("weight", weight);

	}

	public int getWeight() {
		return super.getInt("weight", 0);
	}

	public void setBlood(String blood) {
		super.putString("blood", blood);
	}

	public String getBlood() {
		return super.getString("blood", null);
	}

	public void setAddress(String address) {
		super.putString("address", address);
	}

	public String getAddress() {
		return super.getString("address", null);
	}

	public void setSid(String sid) {
		this.putString("sid", sid);
	}

	public String getSid() {
		return super.getString("sid", null);
	}

	public void setNick(String nick) {
		Log.e("Preferences.setNick", "has been used");
		this.putString("nick_name", nick);
	}

	public String getNick() {
		return this.getString("nick_name", null);
	}

	public void setBirth(long birth) {
		this.putLong("birth", birth);
	}

	public long getBirth() {
		return this.getLong("birth", 0l);
	}

	public String getPhoneNumber() {
		return this.getString("phone_number", null);
	}

	public String getPassword() {
		return this.getString("password", null);
	}

	public void setPhoneNumber(String phoneNumber) {
		this.putString("phone_number", phoneNumber);
	}

	public void setPassword(String password) {
		this.putString("password", password);
	}

	/**
	 * 默认显示的老人的id
	 * 
	 * @return 默认显示的老人的id
	 */
	public int getDefaultSenior() {
		return this.getInt("default_senior", -1);
	}

	/**
	 * 设置显示老人的id
	 * 
	 * @param 默认显示老人的id
	 */
	public void setDefaultSenior(int arg1) {
		this.putInt("default_senior", arg1);
	}

	public void setLastSeniorId(int id) {
		this.putInt("last_senior_id", id);
	}

	public int getLastSeniorId() {
		return this.getInt("last_senior_id", 0);
	}

	public void setEmergencyInfo(String content) {
		this.putString("emergency_information", content);
	}

	public String getEmergencyInfo() {
		return this.getString("emergency_information", null);
	}

	public String getLoginId() {
		return this.getString("login_id", null);
	}

	public void setLoginId(String loginId) {
		this.putString("login_id", loginId);
	}

	public int getNextAlarmId() {
		return getInt("next_alarm_id", 0);
	}
	
	public void setNextAlarmId(int id) {
		this.putInt("next_alarm_id", id);
	}
}
