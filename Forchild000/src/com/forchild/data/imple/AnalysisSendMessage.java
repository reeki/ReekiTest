package com.forchild.data.imple;

import org.json.JSONException;
import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.MessageFrame;
import com.forchild.data.RequestLogoutChild;
import com.forchild.data.RequestSendMessage;

public class AnalysisSendMessage implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestSendMessage) {
			RequestSendMessage rsm = (RequestSendMessage) protocol;
			rsm.getMsgEntity().setState(MessageFrame.SENDSTATE_FAULT);
			try {
				rsm.setReq(json.getInt("req"));
				rsm.setIsResponse(true);
				if(rsm.getReq() == BaseProtocolFrame.RESPONSE_TYPE_OKAY) {
					rsm.getMsgEntity().setState(MessageFrame.SENDSTATE_FINISHED);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return protocol;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.SEND_MESSAGE;
	}

}
