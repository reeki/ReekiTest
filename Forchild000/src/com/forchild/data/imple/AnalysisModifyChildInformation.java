package com.forchild.data.imple;

import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestAddSenior;
import com.forchild.data.RequestModifyChildInformation;

public class AnalysisModifyChildInformation implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestModifyChildInformation) {
			RequestModifyChildInformation rlc = (RequestModifyChildInformation) protocol;
			rlc.setReq(json.optInt("req", -1));
			rlc.setIsResponse(true);
			return rlc;
		}
		return null;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.MODIFY_CHILD_INFORMATION;
	}

}
