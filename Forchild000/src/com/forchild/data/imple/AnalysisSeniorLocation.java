package com.forchild.data.imple;

import org.json.JSONException;
import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestModifyChildInformation;
import com.forchild.data.RequestSeniorLocation;

public class AnalysisSeniorLocation implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestSeniorLocation) {
			RequestSeniorLocation rlc = (RequestSeniorLocation) protocol;
			rlc.setReq(json.optInt("req", -1));

			if (rlc.getReq() == 0) {
				try {
					rlc.setLatitude(json.getDouble("la"));
					rlc.setLongitude(json.getDouble("lo"));
					rlc.setDate(json.getLong("date"));
					rlc.setState(json.optInt("state"));
				} catch (JSONException e) {
					e.printStackTrace();
					return rlc;
				}
			}

			rlc.setIsResponse(true);
			return rlc;
		}
		return null;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.REQUEST_SENIORS_LOCATION;
	}

}
