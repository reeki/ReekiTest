package com.forchild.data.imple;

import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestAllowAddSenior;
import com.forchild.data.RequestLoginChild;

public class AnalysisAllowAddSenior implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestAllowAddSenior) {
			RequestAllowAddSenior rlc = (RequestAllowAddSenior) protocol;
			rlc.setReq(json.optInt("req", -1));
			rlc.setIsResponse(true);
			return rlc;
		}
		
		return null;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.ALLOW_ADD_SENIOR;
	}

}
