package com.forchild.data.imple;

import org.json.JSONObject;

import android.util.Log;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestLoginChild;

public class AnalysisLoginChild implements BaseProtocolFrameProcess {

	@Override
	public int getType() {
		return BaseProtocolFrame.LOGIN_CHILD;
	}

	@Override
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json) {
		if (protocol != null && protocol instanceof RequestLoginChild) {
			RequestLoginChild rlc = (RequestLoginChild) protocol;
			rlc.setReq(json.optInt("req", -1));
			if (rlc.getReq() == 0) {
				rlc.setSid(json.optString("sid", null));
			}
			
			if (rlc.getSid() != null) {
				Log.e("AnalysisLoginChild.parse", "rlc.getReq() == " + rlc.getReq());
			} else {
				Log.e("AnalysisLoginChild.parse", "rlc.getReq() == null");
			}
			rlc.setIsResponse(true);
			return rlc;
		}
		return null;
	}

}
