package com.forchild.data.imple;

import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestModifyChildInformation;
import com.forchild.data.RequestModifyChildPhone;

public class AnalysisModifySeniorPhone implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestModifyChildPhone) {
			RequestModifyChildPhone rlc = (RequestModifyChildPhone) protocol;
			rlc.setReq(json.optInt("req", -1));
			rlc.setIsResponse(true);
			return rlc;
		}
		return null;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.MODIFY_SENIOR_PHONE;
	}

}
