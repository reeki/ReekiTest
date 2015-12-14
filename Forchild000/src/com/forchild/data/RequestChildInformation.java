package com.forchild.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestChildInformation extends RequestBaseSidFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7882548767845631491L;

	protected String nick, name, allergic, medical, blood, phone, address;
	protected int sex, height, weight;
	protected long birth;
	protected List<SeniorInfoCommunication> seniorList = new ArrayList<SeniorInfoCommunication>();

	public RequestChildInformation() {
		super(BaseProtocolFrame.REQUEST_CHILD_INFORMATION, NetAddress.HOST + NetAddress.REQUEST_CHILD_INFORMATION);
	}
	
	public RequestChildInformation(String sid) {
		super(BaseProtocolFrame.REQUEST_CHILD_INFORMATION, NetAddress.HOST + NetAddress.REQUEST_CHILD_INFORMATION, sid);
	}

	public RequestChildInformation(String sid, String name, String nick, String allergic, String medical, String blood, String phone, String address,
			int sex, int height, int weight, long birth, List<SeniorInfoCommunication> seniorList) {
		super(BaseProtocolFrame.REQUEST_CHILD_INFORMATION, NetAddress.HOST + NetAddress.REQUEST_CHILD_INFORMATION, sid);
		this.nick = nick;
		this.name = name;
		this.allergic = allergic;
		this.medical = medical;
		if (blood != null)
			this.blood = blood.toUpperCase().trim();
		this.phone = phone;
		this.address = address;
		this.sex = sex;
		this.height = height;
		this.weight = weight;
		this.birth = birth;
		this.seniorList = seniorList;
	}

	public void setSeniorList(List<SeniorInfoCommunication> seniorList) {
		this.seniorList = seniorList;
	}

	public List<SeniorInfoCommunication> getSeniorList() {
		return this.seniorList;
	}

	public List<SeniorInfoCommunication> addSenior(SeniorInfoCommunication seniorInfo) {
		this.seniorList.add(seniorInfo);
		return this.seniorList;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return this.nick;
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
		return super.toRequestJson();
	}

}
