package com.forchild.server;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayDeque;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import android.content.Context;
import android.util.Log;
import com.forchild.data.BaseConfiguration;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.frame.NetworkHelperProcess;
import com.forchild.frame.NetworkHelperResult;
import com.forchild.frame.imple.NetworkHandler;
import com.forchild000.surface.ServiceCore;

public class NetworkHelper extends Thread {
	public static final int WAITREASON_UNKNOW = 0;
	public static final int WAITREASON_NONETWORK = 1;
	public static final int WAITREASON_NOTASK = 2;

	protected Context context;
	protected ArrayDeque<BaseProtocolFrame> taskDeque = new ArrayDeque<BaseProtocolFrame>();
	protected BaseProtocolFrame nowTask;
	protected boolean isAvailable = false;
	protected boolean isWaiting = false;
	protected int waitReason = WAITREASON_UNKNOW;

	protected Scheme scheme = null;
	protected HttpClient httpClient = null;

	protected NetworkHelperProcess networkHelper;
	protected NetworkHelperResult helperResult;

	protected int connectionTimeOut = BaseConfiguration.NETWORK_CONNECTION_TIMEOUT;
	protected int soTimeOut = BaseConfiguration.NETWORK_SO_TIMEOUT;
	protected int maxTaskSize = ServiceCore.getTaskLimited();

	public NetworkHelper(Context context, ArrayDeque<BaseProtocolFrame> taskList, NetworkHandler netHandler, NetworkHelperResult helperResult) {
		this.context = context;
		this.taskDeque = taskList;
		this.networkHelper = netHandler;
		this.helperResult = helperResult;
		init();
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void init() {
		this.getCertificate(helperResult);
		this.getClient(helperResult);
	}

	public BaseProtocolFrame getNowTask() {
		return nowTask;
	}

	public NetworkHelperResult getNetHelperResult() {
		if (helperResult == null) {
			helperResult = new NetworkHelperResult();
		}
		return helperResult;
	}

	public void setConnectionTimeOut(int connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

	public void setSoTimeOut(int soTimeOut) {
		this.soTimeOut = soTimeOut;
	}

	public void addTask(BaseProtocolFrame task) {
		if (task != null) {
			Log.e("NetworkHelper.addTask", "正在执行addtask, type == " + task.getType());
			synchronized (taskDeque) {
				if (taskDeque.size() > maxTaskSize) {
					task.setIsResponse(false, BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED);
					task.distributes();
					for (int i = 0; i < maxTaskSize - taskDeque.size(); ++i) {
						BaseProtocolFrame taskBuffer = taskDeque.pollLast();
						if (taskBuffer != null) {
							taskBuffer.setIsResponse(false, BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED);
							taskBuffer.distributes();
						}
					}
				}

				try {
					this.taskDeque.add(task);
				} catch (NullPointerException e) {
					return;
				}
			}
			this.toNotify();
		}
	}

	public void addFirstTask(BaseProtocolFrame task) {
		if (task != null) {
			Log.e("NetworkHelper.addFirstTask", "正在执行addFirstTask, type == " + task.getType());
		}
		synchronized (taskDeque) {
			if (taskDeque.size() > maxTaskSize) {
				task.setIsResponse(false, BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED);
				task.distributes();
				for (int i = 0; i < maxTaskSize - taskDeque.size(); ++i) {
					BaseProtocolFrame taskBuffer = taskDeque.pollLast();
					taskBuffer.setIsResponse(false, BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED);
					taskBuffer.distributes();
				}
			}

			try {
				this.taskDeque.addFirst(task);
			} catch (NullPointerException e) {
				Log.e("NetworkHelper.addTask", "task is null");
				return;
			}
		}
		this.toNotify();
	}

	private void toWait() {
		synchronized (this) {
			try {
				isWaiting = true;
				this.wait();
				Log.e("NetworkHelper.run", "to wait, cause: " + waitReason);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean toNotify() {
		if (isAvailable && isWaiting && ServiceCore.isNetworkAvailable) {
			synchronized (this) {
				this.notify();
			}
			waitReason = WAITREASON_UNKNOW;
			return true;
		} else {
			Log.e("NetworkHelper.toNotify", "to notify fail, cause: isAvailable = " + isAvailable + ", isWaiting = " + isWaiting
					+ ", network state = " + ServiceCore.isNetworkAvailable);
			return false;
		}
	}

	public void setOperation(int operation) {
		synchronized (helperResult) {
			if (this.helperResult != null) {
				this.helperResult = new NetworkHelperResult();
			}
			this.helperResult.setOperation(operation);
		}
	}

	public void addOperation(int operation) {
		synchronized (helperResult) {
			if (this.helperResult != null) {
				this.helperResult = new NetworkHelperResult();
			}
			this.helperResult.addOperation(operation);
		}
	}

	public void addOperationOverride(int[] operations) {
		synchronized (helperResult) {
			if (this.helperResult != null) {
				this.helperResult = new NetworkHelperResult();
			}
			this.helperResult.setOperation(NetworkHelperResult.OPERATION_GOON);
			for (int i : operations) {
				this.helperResult.addOperation(i);
			}
		}
	}

	public void removeOperation(int operation) {
		synchronized (helperResult) {
			if (this.helperResult != null) {
				this.helperResult = new NetworkHelperResult();
			}
			this.helperResult.reduceOperation(operation);
		}
	}

	public boolean isWaiting() {
		return this.isWaiting;
	}

	public boolean isRunning() {
		return this.isAvailable;
	}

	public void setWaitReason(int reason) {
		this.waitReason = reason;
	}

	public int getWaitReason() {
		return this.waitReason;
	}

	/**
	 * 
	 * @return 已捕获的异常 <br/>
	 *         IOException： 1. 发生于打开输入流时，2。KeyStore载入时 <br/>
	 *         CertificateException： 1 证书工厂获得时， 2生成证书时， 3。KeyStore载入时 <br/>
	 *         KeyStoreException：1. KeyStore创建时 <br/>
	 *         NoSuchProviderException： 1. KeyStore创建时 <br/>
	 *         NoSuchAlgorithmException： 1。KeyStore载入时 ；2.SSLSocketFactory构造时 <br/>
	 *         KeyManagementException ：1. SSLSocketFactory构造时 <br/>
	 *         UnrecoverableKeyException： 1. SSLSocketFactory构造时 <br/>
	 */
	public void getCertificate(NetworkHelperResult helperResult) {
		try {
			InputStream ins = context.getAssets().open("test_cer.cer"); // 下载的证书放到项目中的assets目录中
			CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
			Certificate cer = cerFactory.generateCertificate(ins);
			KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
			keyStore.load(null, null);
			keyStore.setCertificateEntry("trust", cer);
			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
			socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			scheme = new Scheme("https", socketFactory, 443);
		} catch (Exception e) {
			e.printStackTrace();
			this.scheme = null;
			networkHelper.onCertificateGettingExceptionOccur(e, helperResult, context);
		}
	}

	public void getClient(NetworkHelperResult helperResult) {
		if (scheme == null) {
			this.getCertificate(helperResult);
		}

		if (scheme == null) {
			httpClient = null;
			return;
		}

		httpClient = new DefaultHttpClient();
		httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeOut)
				.setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeOut);
		return;
	}

	protected NetworkHelperResult execute(BaseProtocolFrame task, NetworkHelperResult helperResult) {
		if (httpClient == null) {
			return networkHelper.onNullClient(helperResult, context);
		}

		if (task == null) {
			return helperResult;
		}

		HttpPost httpPost = new HttpPost(task.getUrl());
		try {
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setEntity(new StringEntity(task.toJsonString(), HTTP.UTF_8));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			return helperResult.setHttpResponse(httpResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return networkHelper.onExecuteExceptionOccur(e, helperResult, context);
		}

	}

	@Override
	public void run() {

		boolean isDequeEmpty = false;

		if (this.httpClient == null) {
			this.getClient(helperResult);
		}

		if (httpClient == null) {
			isAvailable = false;
			Log.e("NetworkHelper.run", "Cannot get the HttpClient, the thread will be terminated");
		} else {
			isAvailable = true;
		}

		while (isAvailable) {
			Log.e("NetworkHelper", "线程开始运行 && isAvailable：" + isAvailable);
			synchronized (taskDeque) {
				if (!(helperResult != null && (helperResult.getOperation() & NetworkHelperResult.OPERATION_ADDTASK) == 0)) {
					helperResult = networkHelper.onAddTask(context, nowTask, taskDeque, helperResult);
				}
//				Log.e("NetworkHelper", "onAddTask执行完成");

				if (!(helperResult != null && !helperResult.getAddTaskPermission())) {
					if (taskDeque.isEmpty()) {
						isDequeEmpty = true;
					} else {
						nowTask = taskDeque.pollFirst();
//						Log.e("NetworkHelper", "nowTask == " + nowTask.getType());

						if (helperResult != null) {
							helperResult.setTask(nowTask);
						}

//						Log.e("NetworkHelper.run", "task deque length is " + taskDeque.size());
					}
				} else {
					if (helperResult != null) {
						helperResult.setAddTaskPermission(true);
					}
				}

			}

			if (helperResult != null) {
				helperResult.clean();
			}

			if (isDequeEmpty) {
				isDequeEmpty = false;
				this.setWaitReason(WAITREASON_NOTASK);
				this.toWait();
			} else {
				if (!(helperResult != null && (helperResult.getOperation() & NetworkHelperResult.OPERATION_PREEXECUTE) == 0)) {
					helperResult = networkHelper.onPreExecute(nowTask, taskDeque, context, helperResult);
//					Log.e("NetworkHelper", "预处理完成");
				}

				if (!(helperResult != null && (helperResult.getOperation() & NetworkHelperResult.OPERATION_EXECUTE) == 0)) {
					helperResult = this.execute(nowTask, helperResult);
//					Log.e("NetworkHelper", "执行完成");
				}

				if (!(helperResult != null && (helperResult.getOperation() & NetworkHelperResult.OPERATION_PREPROCESS) == 0)) {
					helperResult = networkHelper.onPreProcess(nowTask, taskDeque, context, helperResult);
//					Log.e("NetworkHelper", "后处理1理完成");
				}

				// 任务恢复
				if (helperResult != null && (helperResult.getOperation() & NetworkHelperResult.OPERATION_CLEAR) == 0
						&& helperResult.getTaskResult() == NetworkHelperResult.TASKRESULT_RETRANSMISSION) {
					if (nowTask != null) {
						synchronized (taskDeque) {
							taskDeque.addFirst(nowTask);
						}
					}
				}

				if (!(helperResult != null && (helperResult.getOperation() & NetworkHelperResult.OPERATION_PROCESS) == 0)) {
					helperResult = networkHelper.onDoProcess(nowTask, taskDeque, context, helperResult);
//					Log.e("NetworkHelper", "后处理2完成");
				}

				if (!(helperResult != null && (helperResult.getOperation() & NetworkHelperResult.OPERATION_SYNCPROCESS) > 0)) {
					if (context instanceof ServiceCore) {
						ServiceCore serv = (ServiceCore) context;
						serv.doSyncProcess(nowTask);
					}
				}
			}

			if (helperResult != null) {
				if ((helperResult.getOperation() & NetworkHelperResult.OPERATION_CLEAR) > 0) {
					synchronized (taskDeque) {
						if (taskDeque.size() > 0) {
							taskDeque.clear();
						}
					}
				}

				if ((helperResult.getOperation() & NetworkHelperResult.OPERATION_WAIT) > 0) {
					this.setWaitReason(helperResult.getWaitReason());
					this.toWait();
				}
			} else {
				helperResult = new NetworkHelperResult();
			}

			nowTask = null;
		}
	}
}
