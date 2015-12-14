package com.forchild.frame;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.util.Log;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.server.NetworkHelper;

public class NetworkHelperResult {
	public static final int TASKRESULT_FINISH = 0;
	public static final int TASKRESULT_ERROR = -1;
	public static final int TASKRESULT_RETRANSMISSION = 1;

	public static final int OPERATION_GOON = 0;
	public static final int OPERATION_WAIT = 1;

	public static final int OPERATION_NORMAL = 252;

	public static final int OPERATION_CLEAR = 2;
	public static final int OPERATION_PREEXECUTE = 4;
	public static final int OPERATION_PREPROCESS = 8;
	public static final int OPERATION_PROCESS = 16;
	public static final int OPERATION_SYNCPROCESS = 32;
	public static final int OPERATION_ADDTASK = 64;
	public static final int OPERATION_EXECUTE = 128;

	protected int operation = OPERATION_NORMAL;
	protected boolean networkServiceAlive = true;

	protected JSONObject json = null;
	protected HttpResponse httpResponse = null;
	protected BaseProtocolFrame task = null;
	protected BaseProtocolFrame taskBuffer = null;
	protected int taskResult = TASKRESULT_FINISH;
	protected boolean addTaskPermission = true;
	protected int waitReason = NetworkHelper.WAITREASON_UNKNOW;

	public NetworkHelperResult setOperation(int operation) {
		this.operation = operation;
		return this;
	}

	public int getOperation() {
		return operation;
	}

	public NetworkHelperResult addOperation(int operation) {
		this.operation = (this.operation | operation);
		return this;
	}

	public NetworkHelperResult reduceOperation(int operation) {
		this.operation = (this.operation & ~operation);
		return this;
	}

	public NetworkHelperResult setResponseResult(JSONObject json) {
		this.json = json;
		return this;
	}

	public JSONObject getResponseResult() {
		return this.json;
	}

	public NetworkHelperResult setServiceAlive(boolean isAlive) {
		this.networkServiceAlive = isAlive;
		return this;
	}

	public boolean getServiceAlive() {
		return this.networkServiceAlive;
	}

	public NetworkHelperResult setHttpResponse(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
		return this;
	}

	public HttpResponse getHttpResponse() {
		return this.httpResponse;
	}

	public NetworkHelperResult setTask(BaseProtocolFrame task) {
		this.task = task;
		return this;
	}

	public BaseProtocolFrame getTask() {
		return this.task;
	}

	public void clean() {
		json = null;
		httpResponse = null;
		task = null;
		taskBuffer = null;
		addTaskPermission = true;
		operation = OPERATION_NORMAL;
		taskResult = TASKRESULT_FINISH;
	}

	public int getTaskResult() {
		return this.taskResult;
	}

	public NetworkHelperResult setTaskResult(int taskResult) {
		this.taskResult = taskResult;
		return this;
	}

	public boolean getAddTaskPermission() {
		return this.addTaskPermission;
	}

	public void setAddTaskPermission(boolean permission) {
		this.addTaskPermission = permission;
	}

	public BaseProtocolFrame getTaskBuffer() {
		return this.taskBuffer;
	}

	public NetworkHelperResult setTaskBuffer(BaseProtocolFrame taskBuffer) {
		if (this.taskBuffer != null) {
			synchronized (this.taskBuffer) {
				this.taskBuffer = taskBuffer;
				Log.e("NetworkHelperResult.setTaskBuffer", taskBuffer == null ? "taskBuffer is null" : taskBuffer.toJsonString());
			}
		} else {
			this.taskBuffer = taskBuffer;
		}
		return this;
	}

	public NetworkHelperResult setWaitReason(int reason) {
		this.waitReason = reason;
		return this;
	}

	public int getWaitReason() {
		return this.waitReason;
	}

}
