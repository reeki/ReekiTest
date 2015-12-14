package com.forchild.data.imple;

import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestSendSOS;
import com.forchild.data.RequestSendValidCode;

public class AnalysisSendValid implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestSendValidCode) {
			protocol.setReq(json.optInt("req", -1));
			protocol.setIsResponse(true);
			return protocol;
		}
		return null;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.SEND_VALID_CODE;
	}

}
