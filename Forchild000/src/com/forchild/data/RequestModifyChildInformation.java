package com.forchild.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestModifyChildInformation extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4283139675513907665L;

	protected int sex, height, weight;
	protected long birth;
	protected String allergic, medical, blood, nick, address;

	public RequestModifyChildInformation() {
		super(BaseProtocolFrame.MODIFY_CHILD_INFORMATION, NetAddress.HOST + NetAddress.MODIFY_CHILD_INFORMATION);
	}

	public RequestModifyChildInformation(String sid, int sex, long birth, String allergic, String medical, int height, int weight, String blood,
			String nick, String address) {
		super(BaseProtocolFrame.MODIFY_CHILD_INFORMATION, NetAddress.HOST + NetAddress.MODIFY_CHILD_INFORMATION, sid);
		this.nick = nick;
		this.allergic = allergic;
		this.medical = medical;
		if (blood != null) {
			this.blood = blood.toUpperCase().trim();
		}
		this.address = address;
		this.sex = sex;
		this.height = height;
		this.weight = weight;
		this.birth = birth;
	}

	// public void setName(String name) {
	// this.name = name;
	// }
	//
	// public String getName() {
	// return this.name;
	// }

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return this.nick;
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
			if (sex == 1 || sex == 2) {
				result.put("sex", sex);
			}
			if (nick != null) {
				result.put("nick", nick);
			}
			result.put("birth", birth);
			if (allergic != null) {
				result.put("allergic", allergic);
			}
			if (medical != null) {
				result.put("medical", medical);
			}
			if (height != BaseProtocolFrame.INT_INITIATION) {
				result.put("height", height);
			}
			if (weight != BaseProtocolFrame.INT_INITIATION) {
				result.put("weight", weight);
			}
			if (blood != null) {
				result.put("blood", blood);
			}
			if (address != null) {
				result.put("address", address);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	@Override
	public String toString() {
		return "RequestModifyChildInformation [sex=" + sex + ", height=" + height + ", weight=" + weight + ", birth=" + birth + ", allergic="
				+ allergic + ", medical=" + medical + ", blood=" + blood + ", nick=" + nick + ", address=" + address + "]";
	}

}
