package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestRegisterSenior extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6251497497726394701L;
	protected String code, nick, cardId, name, allergic, medical, blood, phone, valid, address;
	protected int sex, height, weight;
	protected long birth;

	public RequestRegisterSenior() {
		super(BaseProtocolFrame.REGISTER_SENIOR);
		super.url = NetAddress.HOST + NetAddress.REGISTER_SENIOR;
	}

	public RequestRegisterSenior(String sid, String code, String name, String nick, String cardId, String allergic, String medical, String blood,
			String phone, String valid, String address, int sex, int height, int weight, long birth) {
		this();
		super.sid = sid;
		this.code = code;
		this.nick = nick;
		this.cardId = cardId;
		this.name = name;
		this.allergic = allergic;
		this.medical = medical;
		if (blood != null)
			this.blood = blood.toUpperCase().trim();
		this.phone = phone;
		this.valid = valid;
		this.address = address;
		this.sex = sex;
		this.height = height;
		this.weight = weight;
		this.birth = birth;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return this.nick;
	}

	public void setCardid(String cardId) {
		this.cardId = cardId;
	}

	public String getCardid() {
		return this.cardId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setAllergic(String allergic) {
		this.allergic = allergic;
	}

	public String getAllergic() {
		return this.allergic;
	}

	public void setMedical(String medical) {
		this.medical = medical;
	}

	public String getMedical() {
		return this.medical;
	}

	public void setBlood(String blood) {
		if (blood != null)
			this.blood = blood.toUpperCase().trim();
	}

	public String getBlood() {
		return this.blood;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getValid() {
		return this.valid;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getSex() {
		return this.sex;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return this.height;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return this.weight;
	}

	public void setBirth(long birth) {
		this.birth = birth;
	}

	public long getBirth() {
		return this.birth;
	}

	@Override
	public JSONObject toRequestJson() {
		JSONObject result = new JSONObject();
		try {
			result.put("sid", sid);
			result.put("code", code);
			result.put("sex", sex);
			result.put("nick", nick);
			result.put("cardid", cardId);
			result.put("name", name);
			result.put("birth", birth);
			result.put("allergic", allergic);
			result.put("medical", medical);
			result.put("height", height);
			result.put("weight", weight);
			result.put("blood", blood);
			result.put("phone", phone);
			result.put("valid", valid);
			result.put("address", address);
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
}
