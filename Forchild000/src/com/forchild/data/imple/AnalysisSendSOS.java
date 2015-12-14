package com.forchild.data.imple;

import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestLogoutChild;
import com.forchild.data.RequestSendSOS;

public class AnalysisSendSOS implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestSendSOS) {
			protocol.setReq(json.optInt("req", -1));
			protocol.setIsResponse(true);
			return protocol;
		}
		return null;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.SEND_SOS;
	}

}
