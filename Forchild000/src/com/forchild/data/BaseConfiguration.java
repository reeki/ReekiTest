package com.forchild.data;

public class BaseConfiguration {
	public static final int WARNING_TYPE_ONLY_SURE = 1;
	public static final int WARNING_TYPE_ONLY_CANCEL = 2;
	public static final int WARNING_TYPE_NORMAL_BUTTON = 0;
	public static final boolean FALL_WARNING_RINGING = true;
	public static final boolean FALL_WARNING_SHAKING = true;

	public static final int HANDLER_CLICK_MSG = 0;
	public static final int HANDLER_CLICK_COUNT = 1;
	public static final int ENSURE = 1;
	public static final int CANCEL = 0;

	public static final int RESPONSE_INITIATION = -100;
	public static final int INT_INITIATION = -100;
	public static final double DOUBLE_INITIATION = -100d;
	public static final long LONG_INITIATION = -100l;

	public static final int LOGIN_FAULT_TOLERATE = 5;

	public static final int NETWORK_TASK_LIMIT = 100;
	public static final int NETWORK_CONNECTION_TIMEOUT = 30000;
	public static final int NETWORK_SO_TIMEOUT = 30000;
	public static final long TIMEOUT_NO_NETWORK_REMIND = 3600000;

}
