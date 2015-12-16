package com.forchild.data.imple;

import org.json.JSONArray;
import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.MessageFrame;
import com.forchild.data.RequestUpdateLocation;

public class AnalysisUpdateLocation implements BaseProtocolFrameProcess {

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestUpdateLocation) {
			RequestUpdateLocation rlc = (RequestUpdateLocation) protocol;
			rlc.setReq(json.optInt("req", -1));

			if (rlc.getReq() == 0) {
				JSONArray msgt = json.optJSONArray("msgt");
				if (msgt != null) {
					for (int i = 0; i < msgt.length(); ++i) {
						int type = msgt.optInt(i);
						if (type == 0) {
							continue;
						}
						JSONArray contentArray = json.optJSONArray("msg");
						if (contentArray != null) {
							JSONObject content = contentArray.optJSONObject(i);
							if (content != null) {
								// 这是我懒了，架构改成现在这样以后，应该在这里重新写匹配函数，但因为message的匹配以前谢过了，就先不复写了，先用这个
								MessageFrame msg = MessageFrame.parse(type, content);
								if(msg != null) {
									msg.setUserMsgType(MessageFrame.USERMESSAGETYPE_RECEIVED_MESSAGE);
									rlc.addMessage(msg);
								}
							}
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
		return BaseProtocolFrame.UPDATE_LOCATION;
	}

}
