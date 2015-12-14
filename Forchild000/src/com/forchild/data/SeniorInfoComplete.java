package com.forchild.data;

import android.content.ContentValues;
import android.database.Cursor;

public class SeniorInfoComplete extends SeniorInfoSimple {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1862606334402833017L;

	protected String medical;
	protected String blood;
	protected String phone;
	protected String address;
	protected String code;
	protected String allergic;
	protected String cardId;
	protected int height;
	protected int weight;
	protected int sex;
	protected long birth;

	public SeniorInfoComplete() {

	}

	public SeniorInfoComplete(int id, int oid, String name, String nick, int sex, int height, int weight, long birth, String blood, String address,
			String phone, String allergic, String medical, String code) {
		super(id, name, oid);
		this.nick = nick;
		this.medical = medical;
		this.blood = blood;
		this.phone = phone;
		this.address = address;
		this.code = code;
		this.allergic = allergic;
		this.height = height;
		this.weight = weight;
		this.sex = sex;
		this.birth = birth;
		// this.cardId = cardId;
	}

	public SeniorInfoComplete(Cursor cursor) {
		super(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")), cursor
				.getInt(cursor.getColumnIndex("oid")));
		this.nick = cursor.getString(cursor.getColumnIndex("nick"));
		this.medical = cursor.getString(cursor.getColumnIndex("medical"));
		this.blood = cursor.getString(cursor.getColumnIndex("blood"));
		// this.cardId = cursor.getString(cursor.getColumnIndex("cardid"));
		this.phone = cursor.getString(cursor.getColumnIndex("phone"));
		this.address = cursor.getString(cursor.getColumnIndex("address"));
		this.code = cursor.getString(cursor.getColumnIndex("code"));
		this.allergic = cursor.getString(cursor.getColumnIndex("allergic"));
		this.height = cursor.getInt(cursor.getColumnIndex("height"));
		this.weight = cursor.getInt(cursor.getColumnIndex("weight"));
		this.sex = cursor.getInt(cursor.getColumnIndex("sex"));
		this.birth = cursor.getLong(cursor.getColumnIndex("birth"));
	}

	public void setMedical(String medical) {
		this.medical = medical;
	}

	public String getMedical() {
		return this.medical;
	}

	public void setBlood(String blood) {
		if (blood != null)
			this.blood = blood.toUpperCase();
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

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getSex() {
		return this.sex;
	}

	public void setBirth(long birth) {
		this.birth = birth;
	}

	public long getBirth() {
		return this.birth;
	}

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("oid", oid);
		values.put("sex", sex);
		values.put("name", name);
		values.put("nick", nick);
		values.put("medical", medical);
		values.put("height", height);
		values.put("weight", weight);
		values.put("blood", blood);
		values.put("birth", birth);
		values.put("allergic", allergic);
		values.put("code", code);
		values.put("address", address);
		values.put("phone", phone);
		// values.put("cardid", cardId);
		return values;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	// public String getCardId() {
	// return this.cardId;
	// }
	//
	// public void setCardId(String cardId) {
	// this.cardId = cardId;
	// }

	public void setAllergic(String allergic) {
		this.allergic = allergic;
	}

	public String getAllergic() {
		return this.allergic;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((allergic == null) ? 0 : allergic.hashCode());
		result = prime * result + (int) (birth ^ (birth >>> 32));
		result = prime * result + ((blood == null) ? 0 : blood.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + height;
		result = prime * result + ((medical == null) ? 0 : medical.hashCode());
		result = prime * result + ((nick == null) ? 0 : nick.hashCode());
		result = prime * result + oid;
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + sex;
		result = prime * result + weight;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeniorInfoComplete other = (SeniorInfoComplete) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (allergic == null) {
			if (other.allergic != null)
				return false;
		} else if (!allergic.equals(other.allergic))
			return false;
		if (birth != other.birth)
			return false;
		if (blood == null) {
			if (other.blood != null)
				return false;
		} else if (!blood.toUpperCase().equals(other.blood.toUpperCase()))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (height != other.height)
			return false;
		if (medical == null) {
			if (other.medical != null)
				return false;
		} else if (!medical.equals(other.medical))
			return false;
		if (nick == null) {
			if (other.nick != null)
				return false;
		} else if (!nick.equals(other.nick))
			return false;
		if (oid != other.oid)
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (sex != other.sex)
			return false;
		if (weight != other.weight)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

}
