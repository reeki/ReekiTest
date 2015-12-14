package com.forchild.frame.servicenet.imple;

import com.forchild.data.BaseProtocolFrame;

public interface ServiceNetworkResultHandler {

	public int getType();

	public int process(BaseProtocolFrame source);
}
