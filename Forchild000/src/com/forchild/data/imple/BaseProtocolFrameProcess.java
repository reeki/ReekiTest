package com.forchild.data.imple;

import org.json.JSONObject;

import com.forchild.data.BaseProtocolFrame;

public interface BaseProtocolFrameProcess {
	
	public BaseProtocolFrame parse(BaseProtocolFrame protocol, JSONObject json);

	public int getType();
	
}
