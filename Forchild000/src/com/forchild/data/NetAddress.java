package com.forchild.data;

/**
 * 用于公共存储网络地址的类
 * 
 * @author SONY
 * 
 */
public class NetAddress {
	/** 主机地址 */
//	public static final String HOST = "https://114.215.201.229:8080/";
//	public static final String HOST = "https://114.215.201.229:8080/";
	public static final String HOST = "https://192.168.1.101/odcser/"; //台式机

	public static final String LOGIN = "user/login";
	public static final String LOGOUT = "user/logout";
	public static final String GET_INFO = "user/getinfo";
	public static final String UPDATE_LOCATION = "user/position";
	public static final String UPDATE_SPORT = "user/activities";
	public static final String UPDATE_ACCIDENT = "user/sos";
	public static final String REGISTER_CHILD = "user/regist";
	public static final String ADD_SENIOR = "user/follow";
	public static final String DELETE_SENIOR = "user/delold";
	public static final String REGISTER_SENIOR = "user/addold";
	public static final String ALLOW_ADD_SENIOR = "user/agreefollow";
	public static final String REQUEST_CHILD_INFORMATION = "user/getinfo";
	public static final String MODIFY_CHILD_INFORMATION = "user/setinfo";
	public static final String MODIFY_SENIOR_INFORMATION = "user/setinfo";
	public static final String MODIFY_CHILD_PHONE = "user/phone";
	public static final String MODIFY_SENIOR_PHONE = "user/phone";
	public static final String REQUEST_SENIORS_LOCATION = "user/currposition";
	public static final String REQUEST_SENIOR_TRACK = "user/track";
	public static final String REQUEST_SENIOR_SPORT = "user/curractivities";
	public static final String SEND_MESSAGE = "msg/usermessage";
	public static final String SEND_SOS = "msg/sosmessage";
	public static final String SEND_VALID_CODE = "validcode";
	public static final String RENEW = "session";
	public static final String MODIFY_CHILD_PASSWORD = "user/password";
}
