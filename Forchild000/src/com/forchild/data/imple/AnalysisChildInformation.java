package com.forchild.data.imple;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestChildInformation;
import com.forchild.data.SeniorInfoCommunication;

public class AnalysisChildInformation implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject source) {
		if (protocol != null && protocol instanceof RequestChildInformation) {
			RequestChildInformation rsi = (RequestChildInformation) protocol;
			rsi.setReq(source.optInt("req", -1));

			if (rsi.getReq() == 0) {
				rsi.setSex(source.optInt("sex"));
				rsi.setBirth(source.optLong("birth"));
				rsi.setAddress(source.optString("address", null));
				rsi.setAllergic(source.optString("allergic", null));
				rsi.setBlood(source.optString("blood", null));
				rsi.setMedical(source.optString("medical", null));
				rsi.setNick(source.optString("nick", null));
				rsi.setName(source.optString("name", null));
				rsi.setPhone(source.optString("phone", null));
				rsi.setHeight(source.optInt("height", BaseProtocolFrame.INT_INITIATION));
				rsi.setWeight(source.optInt("weight", BaseProtocolFrame.INT_INITIATION));

				JSONArray communit = source.optJSONArray("communit");
				if (communit != null) {
					int uid = BaseProtocolFrame.INT_INITIATION;
					int sex = 0;
					int height = BaseProtocolFrame.INT_INITIATION;
					int weight = BaseProtocolFrame.INT_INITIATION;
					String name = null;
					String medical = null;
					String allergic = null;
					String blood = null;
					String phone = null;
					String nick = null;
					String address = null;
					long birth = 0;
					for (int i = 0; i < communit.length(); ++i) {
						JSONObject seniorInfo = communit.optJSONObject(i);
						if (seniorInfo != null) {
							uid = seniorInfo.optInt("uid", BaseProtocolFrame.INT_INITIATION);
							height = seniorInfo.optInt("height", BaseProtocolFrame.INT_INITIATION);
							weight = seniorInfo.optInt("weight", BaseProtocolFrame.INT_INITIATION);
							sex = seniorInfo.optInt("sex");
							name = seniorInfo.optString("name", null);
							medical = seniorInfo.optString("medical", null);
							allergic = seniorInfo.optString("allergic", null);
							blood = seniorInfo.optString("blood", null);
							phone = seniorInfo.optString("phone", null);
							nick = seniorInfo.optString("nick", null);
							address = seniorInfo.optString("address", null);
							birth = seniorInfo.optLong("birth");
							String code = seniorInfo.optString("code");
							if (name != null && uid != BaseProtocolFrame.INT_INITIATION) {
								SeniorInfoCommunication seniorInfoCommunication = new SeniorInfoCommunication(i, uid, sex, name, medical, allergic,
										height, weight, blood, phone, nick, address, birth, code);
								Log.e("AnalysisChildInformation.process", seniorInfoCommunication.toString());
								rsi.addSenior(seniorInfoCommunication);
							}
						}
					}
				}
			}

			rsi.setIsResponse(true);

			return rsi;
		}
		return null;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.REQUEST_CHILD_INFORMATION;
	}

}
