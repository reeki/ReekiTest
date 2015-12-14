package com.forchild.data.imple;

import org.json.JSONArray;
import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.LocationInfo;
import com.forchild.data.RequestSeniorTrack;

public class AnalysisSeniorTrack implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestSeniorTrack) {
			RequestSeniorTrack rlc = (RequestSeniorTrack) protocol;
			rlc.setReq(json.optInt("req", -1));

			if (rlc.getReq() == 0) {
				JSONArray locationArray = json.optJSONArray("location");
				if (locationArray != null) {
					for (int i = 0; i < locationArray.length(); ++i) {
						JSONObject location = locationArray.optJSONObject(i);
						if (location != null) {
							rlc.addLocation(new LocationInfo(location.optDouble("lo", BaseProtocolFrame.DOUBLE_INITIATION), location.optDouble("la",
									BaseProtocolFrame.DOUBLE_INITIATION), location.optLong("date")));
						}
					}
				}
			}

			rlc.setIsResponse(true);
			return rlc;
		}
		return null;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.REQUEST_SENIOR_TRACK;
	}

}
