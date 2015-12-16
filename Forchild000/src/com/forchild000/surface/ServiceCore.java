package com.forchild000.surface;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.forchild.data.BaseConfiguration;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.MessageFrame;
import com.forchild.data.MessageUser;
import com.forchild.data.RequestLoginChild;
import com.forchild.data.RequestLogoutChild;
import com.forchild.data.RequestSendAutoMessage;
import com.forchild.data.imple.AnalysisAddSenior;
import com.forchild.data.imple.AnalysisAllowAddSenior;
import com.forchild.data.imple.AnalysisChildInformation;
import com.forchild.data.imple.AnalysisDeleteSenior;
import com.forchild.data.imple.AnalysisLoginChild;
import com.forchild.data.imple.AnalysisLogoutChild;
import com.forchild.data.imple.AnalysisModifyChildInformation;
import com.forchild.data.imple.AnalysisModifySeniorPhone;
import com.forchild.data.imple.AnalysisRegisterChild;
import com.forchild.data.imple.AnalysisRegisterSenior;
import com.forchild.data.imple.AnalysisRenwe;
import com.forchild.data.imple.AnalysisSendAutoMessage;
import com.forchild.data.imple.AnalysisSendMessage;
import com.forchild.data.imple.AnalysisSendSOS;
import com.forchild.data.imple.AnalysisSendValid;
import com.forchild.data.imple.AnalysisSeniorLocation;
import com.forchild.data.imple.AnalysisSeniorSport;
import com.forchild.data.imple.AnalysisSeniorTrack;
import com.forchild.data.imple.AnalysisUpdateLocation;
import com.forchild.frame.NetworkHelperProcess;
import com.forchild.frame.NetworkHelperResult;
import com.forchild.frame.imple.NetworkHandler;
import com.forchild.frame.servicenet.imple.ServiceAddSeniorProcess;
import com.forchild.frame.servicenet.imple.ServiceAllowAddSeniorProcess;
import com.forchild.frame.servicenet.imple.ServiceChildInformationProcess;
import com.forchild.frame.servicenet.imple.ServiceCommonProcess;
import com.forchild.frame.servicenet.imple.ServiceDeleteSeniorProcess;
import com.forchild.frame.servicenet.imple.ServiceLoginProcess;
import com.forchild.frame.servicenet.imple.ServiceModifyChindInformationProcess;
import com.forchild.frame.servicenet.imple.ServiceModifySeniorInformationProcess;
import com.forchild.frame.servicenet.imple.ServiceModifySeniorPhoneProcess;
import com.forchild.frame.servicenet.imple.ServiceNetworkResultHandler;
import com.forchild.frame.servicenet.imple.ServiceRegisterChildProcess;
import com.forchild.frame.servicenet.imple.ServiceRegisterSeniorProcess;
import com.forchild.frame.servicenet.imple.ServiceRenewProcess;
import com.forchild.frame.servicenet.imple.ServiceSendAutoMessageProcess;
import com.forchild.frame.servicenet.imple.ServiceSendMessageProcess;
import com.forchild.frame.servicenet.imple.ServiceSendSOS;
import com.forchild.frame.servicenet.imple.ServiceSendValidProcess;
import com.forchild.frame.servicenet.imple.ServiceSeniorLocationProcess;
import com.forchild.frame.servicenet.imple.ServiceSeniorSport;
import com.forchild.frame.servicenet.imple.ServiceSeniorTrackProcess;
import com.forchild.frame.servicenet.imple.ServiceUpdateLocationProcess;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.LocationServer;
import com.forchild.server.NetworkHelper;
import com.forchild.server.Preferences;

public class ServiceCore extends Service {

	public static final String REQUEST_FLAGS_AUTO_SEND_MESSAGE = "com.forchild.request.auto.message";
	public static final String REQUEST_FLAGS_LOGOUT = "com.forchild.logout.activity";
	public final String BROADCAST_FLAGS_SEND_AUTO_MESSAGE = "com.forchild.msg.SEND_AUTO_MESSAGE";

	public static final int NETWORK_RESPONSE = 1000;
	public static final int LOCATION_CONTROL = 1001;
	public static final int NETWORK_EXCEPTION = 1002;
	public static final int CERTIFICATE_EXCEPTION = 1003;

	public static final int START_LOCATION = 1;
	public static final int STOP_LOCATION = -1;

	public static final int NETWORKEXCEPTION_NO_NETWORK = 1;
	public static final int NETWORKEXCEPTION_NO_RESPONSE = 2;
	public static final int NO_SID = 3;

	public static long TIMEOUT_NO_NETWORK_REMIND = BaseConfiguration.TIMEOUT_NO_NETWORK_REMIND;

	public static List<AliveBaseActivity> aliveActivityList = new ArrayList<AliveBaseActivity>();
	public static List<MapBaseActivity> mapActivityList = new ArrayList<MapBaseActivity>();
	public static MainActivity aliveMainActivity;
	public static AMapLocation aMapLocation;

	private LocationServer ls;
	private Preferences preference;
	private DatabaseHelper dbHelper;
	private static NetworkHelper netHelper;
	protected NetworkHandler netHandler;
	protected NetworkHelperResult helperResult;
	private static ArrayDeque<BaseProtocolFrame> taskDeque = new ArrayDeque<BaseProtocolFrame>();
	private static String sid;
	private static boolean loginState = false;
	private static String loginId;
	private static int networdTaskLimited = BaseConfiguration.NETWORK_TASK_LIMIT;
	private Handler msgHandler;
	public static long lastNoNetworkRemindTime = 0;
	public static boolean isNetworkAvailable = false;
	private static int msgActivityOid = -1;

	private Map<Integer, ServiceNetworkResultHandler> processerMap = new HashMap<Integer, ServiceNetworkResultHandler>();
	private ServiceNetworkResultHandler commonProcesser;

	protected NetworkHelperProcess helperProcess;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		ls = new LocationServer(this);
		preference = new Preferences(this);
		msgHandler = new Handler(msgCallback);

		sid = preference.getSid();
		loginState = preference.getLoginState();
		loginId = preference.getLoginId();

		// 验证当前是否有网
		ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			this.setIsNetworkAvailable(mNetworkInfo.isAvailable());
		}

		// 需要匹配函数
		netHandler = new NetworkHandler();
		helperResult = new NetworkHelperResult();
		netHandler.setReturnHandler(msgHandler);
		this.addAllProcess();
		this.addParseProcess();

		netHelper = new NetworkHelper(this, taskDeque, netHandler, helperResult);
		netHelper.start();

		dbHelper = new DatabaseHelper(this);

		IntentFilter serviceFilter = new IntentFilter();
		serviceFilter.addAction(REQUEST_FLAGS_LOGOUT);
		serviceFilter.addAction(REQUEST_FLAGS_AUTO_SEND_MESSAGE);
		this.registerReceiver(serviceReceiver, serviceFilter);
		this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) ServiceCore.this.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				ServiceCore.this.setIsNetworkAvailable(mNetworkInfo.isAvailable());
				if (mNetworkInfo.isAvailable()) {
					if (netHelper != null && netHelper.isAvailable()) {
						netHelper.removeOperation(NetworkHelperResult.OPERATION_WAIT);
						netHelper.toNotify();
					}
				} else {
					synchronized (helperResult) {
						helperResult.setOperation(NetworkHelperResult.OPERATION_WAIT);
						helperResult.setWaitReason(NetworkHelper.WAITREASON_NONETWORK);
					}
				}
			} else {
				ServiceCore.this.setIsNetworkAvailable(false);
				synchronized (helperResult) {
					helperResult.setOperation(NetworkHelperResult.OPERATION_WAIT);
					helperResult.setWaitReason(NetworkHelper.WAITREASON_NONETWORK);
				}
			}

		}

	};

	private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(REQUEST_FLAGS_AUTO_SEND_MESSAGE)) {
				Log.e("ServiceCore.receiver", "接收到发送自动消息广播");
				int id = intent.getIntExtra("id", -1);
				if (id >= 0) {
					Cursor cursor = dbHelper.getAutoMessage(new String[] { "nick", "name", "content", "oid", "login_id", "type" }, "id = ?",
							new String[] { id + "" });
					if (cursor.moveToNext()) {
						String loginId = cursor.getString(cursor.getColumnIndex("login_id"));

						if (!loginId.equals(ServiceCore.getLoginId())) {
							return;
						}

						int oid = cursor.getInt(cursor.getColumnIndex("oid"));
						String nick = preference.getNick();
						String name = preference.getUserName();
						String content = cursor.getString(cursor.getColumnIndex("content"));
						String nameTo = new String();
						int type = cursor.getInt(cursor.getColumnIndex("type"));
						if (nick != null && nick.length() > 0) {
							nameTo = nick;
						} else if (name != null) {
							nameTo = name;
						}
						cursor.close();
						dbHelper.close();

						MessageUser entity = new MessageUser();
						entity.setDate(System.currentTimeMillis());
						entity.setCon(content);
						entity.setLoginId(ServiceCore.getLoginId());
						entity.setState(MessageFrame.SENDSTATE_SENDING);
						entity.setType(MessageFrame.USER_MESSAGE);
						entity.setUname(nameTo);
						entity.setUserMsgType(type);
						entity.setOid(oid);
						entity.setLoginId(loginId);

						if (ServiceCore.getMsgActivityOid() == oid) {
							Intent msgNotice = new Intent();
							msgNotice.setAction(BROADCAST_FLAGS_SEND_AUTO_MESSAGE);
							msgNotice.putExtra("msg", entity);
							ServiceCore.this.sendBroadcast(msgNotice);
						}

						addNetTask(new RequestSendAutoMessage(preference.getSid(), entity));
						dbHelper.addMessage(entity);
						Log.e("ServiceCore.receiver", "已发送自动消息");

					}
				}
			}

			if (intent.getAction().equals(REQUEST_FLAGS_LOGOUT)) {
				ServiceCore.this.toLogout();
			}

		}

	};

	public void startLocation() {
		if (ls == null) {
			ls = new LocationServer(this);
		} else if (!ls.isAlive()) {
			ls.start();
		}
	}

	public void stopLocation() {
		if (ls != null) {
			ls.stop();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(networkStateReceiver);
		this.unregisterReceiver(serviceReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return START_STICKY;
	}

	private void setIsNetworkAvailable(boolean option) {
		isNetworkAvailable = option;
	}

	public boolean getIsNetworkAvailable() {
		return isNetworkAvailable;
	}

	private Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case NETWORK_RESPONSE:
				if (msg.obj != null && msg.obj instanceof BaseProtocolFrame) {
					BaseProtocolFrame source = (BaseProtocolFrame) msg.obj;
					Log.e("ServiceCore.msgCallback", "get request result:" + source.getClass().getName() + ", type ==" + source.getType());
					ServiceNetworkResultHandler processer = processerMap.get(source.getType());
					if (processer == null && commonProcesser != null) {
						Log.e("ServiceCore.msgCallback", "commonProcesser is processing");
						commonProcesser.process(source);
					} else if (processer != null) {
						Log.e("ServiceCore.msgCallback", "processer is processing");
						processer.process(source);
					}
				}
				break;
			case CERTIFICATE_EXCEPTION:
				sid = null;
				loginState = false;
				Toast.makeText(ServiceCore.this, getText(R.string.servicecore_no_certificate_error), Toast.LENGTH_SHORT).show();
				Intent loginIntent = new Intent(ServiceCore.this, LoginActivity.class);
				ServiceCore.this.startActivity(loginIntent);
				break;
			case NO_SID:
				ServiceCore.this.toLogin();
				break;
			}

			return true;
		}
	};

	public void toLogin() {
		if (preference == null) {
			preference = new Preferences(this);
		}

		if (loginState) {
			if (sid == null && preference != null) {
				if (preference.getSid() != null) {
					sid = preference.getSid();
				} else if (preference.getLoginId() != null && preference.getPassword() != null) {
					addPriorNetTask(new RequestLoginChild(preference.getLoginId(), preference.getPassword()));
				} else {
					Intent intent = new Intent(this, LoginActivity.class);
					this.startActivity(intent);
				}
			}
		} else {
			if (netHelper != null) {
				// netHelper.setOperation(NetworkHelperResult.OPERATION_SKIP_ONLY_PROCESSRESULT);
			}
		}
	}

	public void toLogout() {
		if (sid == null) {
			loginState = false;
			return;
		} else {
			synchronized (helperResult) {
				helperResult.setTaskBuffer(new RequestLogoutChild(sid));
			}
			sid = null;
			loginState = false;
		}

		preference.setLoginState(false);
		preference.setSid(null);
		ls.stop();

		boolean isResume = false;
		for (AliveBaseActivity aliveAct : aliveActivityList) {
			if (aliveAct != null) {
				if (aliveAct.isAlive) {
					isResume = true;
				}
				aliveAct.finish();
			}
		}
		if (aliveMainActivity != null) {
			if (aliveMainActivity.isAlive) {
				isResume = true;
			}
			aliveMainActivity.finish();
		}

		if (isResume) {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(intent);
		}
	}

	public static boolean getLoginState() {
		return loginState;
	}

	public static void setLoginState(boolean state) {
		loginState = state;
	}

	public static String getLoginId() {
		return loginId;
	}

	public static void addNetTask(BaseProtocolFrame task) {
		if (netHelper != null) {
			netHelper.addTask(task);
		} else {
			synchronized (taskDeque) {
				taskDeque.add(task);
			}
		}
	}

	public static void addPriorNetTask(BaseProtocolFrame task) {
		if (netHelper != null) {
			netHelper.addFirstTask(task);
		} else {
			synchronized (taskDeque) {
				taskDeque.addFirst(task);
			}
		}
	}

	public void addProcess(ServiceNetworkResultHandler processer) {
		this.processerMap.put(processer.getType(), processer);
	}

	public static void setAMapLocation(AMapLocation aMapLocation) {
		ServiceCore.aMapLocation = aMapLocation;
		for (MapBaseActivity mAct : mapActivityList) {
			if (mAct != null) {
				mAct.setAMapLocation(aMapLocation);
			}
		}
	}

	public static AMapLocation getAMapLocation() {
		return ServiceCore.aMapLocation;
	}

	public void addAllProcess() {
		this.addProcess(new ServiceAddSeniorProcess(this, preference));
		this.addProcess(new ServiceAllowAddSeniorProcess(this, preference));
		this.addProcess(new ServiceChildInformationProcess(this, preference));
		this.addProcess(new ServiceDeleteSeniorProcess(this, preference));
		this.addProcess(new ServiceLoginProcess(this, preference));
		this.addProcess(new ServiceModifyChindInformationProcess(this, preference));
		this.addProcess(new ServiceModifySeniorInformationProcess(this, preference));
		this.addProcess(new ServiceModifySeniorPhoneProcess(this, preference));
		this.addProcess(new ServiceRegisterChildProcess(this, preference));
		this.addProcess(new ServiceRegisterSeniorProcess(this, preference));
		this.addProcess(new ServiceRenewProcess(this, preference));
		this.addProcess(new ServiceSendMessageProcess(this, preference));
		this.addProcess(new ServiceSendSOS(this, preference));
		this.addProcess(new ServiceSendValidProcess(this, preference));
		this.addProcess(new ServiceSeniorLocationProcess(this, preference));
		this.addProcess(new ServiceSeniorSport(this, preference));
		this.addProcess(new ServiceSeniorTrackProcess(this, preference));
		this.addProcess(new ServiceUpdateLocationProcess(this, preference));
		this.addProcess(new ServiceSendAutoMessageProcess(this, preference));
		commonProcesser = new ServiceCommonProcess(this, preference);
	}

	public void addParseProcess() {
		if (netHandler == null) {
			netHandler = new NetworkHandler();
			netHandler.setReturnHandler(msgHandler);
		}

		netHandler.addParseProcess(new AnalysisAddSenior());
		netHandler.addParseProcess(new AnalysisAllowAddSenior());
		netHandler.addParseProcess(new AnalysisChildInformation());
		netHandler.addParseProcess(new AnalysisDeleteSenior());
		netHandler.addParseProcess(new AnalysisLoginChild());
		netHandler.addParseProcess(new AnalysisLogoutChild());
		netHandler.addParseProcess(new AnalysisModifyChildInformation());
		netHandler.addParseProcess(new AnalysisModifySeniorPhone());
		netHandler.addParseProcess(new AnalysisRegisterChild());
		netHandler.addParseProcess(new AnalysisRegisterSenior());
		netHandler.addParseProcess(new AnalysisRenwe());
		netHandler.addParseProcess(new AnalysisSendMessage());
		netHandler.addParseProcess(new AnalysisSendSOS());
		netHandler.addParseProcess(new AnalysisSendValid());
		netHandler.addParseProcess(new AnalysisSeniorLocation());
		netHandler.addParseProcess(new AnalysisSeniorSport());
		netHandler.addParseProcess(new AnalysisSeniorTrack());
		netHandler.addParseProcess(new AnalysisUpdateLocation());
		netHandler.addParseProcess(new AnalysisSendAutoMessage());
	}

	public void doSyncProcess(BaseProtocolFrame nowTask) {
		if (helperProcess != null) {
			helperProcess.onDoSyncProcess(nowTask, this);
		}
	}

	public static void setSid(String sid) {
		ServiceCore.sid = sid;
	}

	public static String getSid() {
		return ServiceCore.sid;
	}

	public static void setTaskLimited(int limited) {
		ServiceCore.networdTaskLimited = limited;
	}

	public static int getTaskLimited() {
		return ServiceCore.networdTaskLimited;
	}

	public static void setMsgActivityOid(int oid) {
		msgActivityOid = oid;
	}

	public static int getMsgActivityOid() {
		return msgActivityOid;
	}

	public static void setLoginId(String phoneNumber) {
		ServiceCore.loginId = phoneNumber;
	}

}
