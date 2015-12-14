package com.forchild.data;

public class SeniorInfoCommunication extends SeniorInfoSimple {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3277777109381914027L;

	protected int sex, height, weight;
	protected String medical, allergic, blood, phone, address, code;
	private long birth;

	public SeniorInfoCommunication() {

	}

	public SeniorInfoCommunication(int id, int uid, int sex, String name, String medical, String allergic, int height, int weight, String blood,
			String phone, String nick, String address, long birth, String code) {
		super(id, name, uid);
		this.sex = sex;
		this.height = height;
		this.weight = weight;
		this.medical = medical;
		this.allergic = allergic;
		if (blood != null) {
			this.blood = blood.toUpperCase();
		}
		this.phone = phone;
		this.nick = nick;
		this.address = address;
		this.birth = birth;
		this.code = code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
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

	public long getBirth() {
		return this.birth;
	}

	public void setBirth(long birth) {
		this.birth = birth;
	}

	@Override
	public String toString() {
		return "SeniorInfoCommunication [oid=" + oid + ", id=" + id + ", sex=" + sex + ", height=" + height + ", weight=" + weight + ", medical="
				+ medical + ", allergic=" + allergic + ", blood=" + blood + ", phone=" + phone + ", nick=" + nick + ", address=" + address
				+ ", birth=" + birth + ", code=" + code + "]";
	}

}
