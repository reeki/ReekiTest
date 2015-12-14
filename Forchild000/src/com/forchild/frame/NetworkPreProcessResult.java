package com.forchild.frame;

import com.forchild.data.BaseProtocolFrame;

public class NetworkPreProcessResult {
	public static final int PROCESS_FINISH = 0;
	public static final int PROCESS_ERROR = -1;
	public static final int PROCESS_RETRANSMISSION = 1;
	public static final int PROCESS_RENEW = 2;

	

	protected int result = PROCESS_FINISH;
	protected BaseProtocolFrame task;

	public NetworkPreProcessResult(int result, BaseProtocolFrame task) {
		this.result = result;
		this.task = task;
	}

	public NetworkPreProcessResult() {

	}

	public NetworkPreProcessResult setResult(int result) {
		this.result = result;
		return this;
	}

	public NetworkPreProcessResult setBean(BaseProtocolFrame task) {
		this.task = task;
		return this;
	}

	public int getResult() {
		return this.result;
	}

	public BaseProtocolFrame getBean() {
		return this.task;
	}
}
