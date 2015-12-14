package com.forchild.data.imple;

import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestLoginChild;
import com.forchild.data.RequestLogoutChild;

public class AnalysisLogoutChild implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestLogoutChild) {
			RequestLogoutChild rlc = (RequestLogoutChild) protocol;
			rlc.setReq(json.optInt("req", -1));
			rlc.setIsResponse(true);
			return rlc;
		}
		return null;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.LOGOUT_SENIORS;
	}

}
