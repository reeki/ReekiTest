package com.forchild.data.imple;

import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestLogoutChild;
import com.forchild.data.RequestRegisterSenior;

public class AnalysisRegisterSenior implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestRegisterSenior) {
			RequestRegisterSenior rlc = (RequestRegisterSenior) protocol;
			rlc.setReq(json.optInt("req", -1));
			rlc.setIsResponse(true);
			return rlc;
		}
		return null;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.REGISTER_SENIOR;
	}

}
