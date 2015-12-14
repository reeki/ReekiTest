package com.forchild.data.imple;

import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestModifyChildInformation;
import com.forchild.data.RequestSeniorSport;

public class AnalysisSeniorSport implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestSeniorSport) {
			RequestSeniorSport rlc = (RequestSeniorSport) protocol;
			rlc.setReq(json.optInt("req", -1));

			if (rlc.getReq() == 0) {
				rlc.setRun(json.optInt("run"));
				rlc.setWalk(json.optInt("walk"));
				rlc.setStand(json.optInt("stand"));
				rlc.setUnknown(json.optInt("unknown"));
				rlc.setCal(json.optInt("cal"));
			}

			rlc.setIsResponse(true);
			return rlc;
		}
		return null;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.REQUEST_SENIOR_SPORT;
	}

}
