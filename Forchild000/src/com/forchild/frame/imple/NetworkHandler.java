package com.forchild.frame.imple;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.RequestBaseSidFrame;
import com.forchild.data.RequestLoginChild;
import com.forchild.data.RequestLogoutChild;
import com.forchild.data.RequestRenew;
import com.forchild.data.imple.AnalysisCommon;
import com.forchild.data.imple.BaseProtocolFrameProcess;
import com.forchild.frame.NetworkHelperResult;
import com.forchild.server.NetworkHelper;
import com.forchild.server.Preferences;
import com.forchild000.surface.AliveBaseActivity;
import com.forchild000.surface.LoginActivity;
import com.forchild000.surface.ServiceCore;

public class NetworkHandler extends NetworkHelperResultHandler {

	protected Handler handler;
	protected Map<Integer, BaseProtocolFrameProcess> parseMap = new HashMap<Integer, BaseProtocolFrameProcess>();

	public NetworkHandler() {

	}

	public NetworkHandler(Map<Integer, BaseProtocolFrameProcess> parseMap) {
		this.parseMap = parseMap;
	}

	public void setParseProcess(Map<Integer, BaseProtocolFrameProcess> parseMap) {
		this.parseMap = parseMap;
	}

	public Map<Integer, BaseProtocolFrameProcess> getParseProcess() {
		return this.parseMap;
	}

	public void addParseProcess(BaseProtocolFrameProcess process) {
		this.parseMap.put(process.getType(), process);
	}

	public void setReturnHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	public NetworkHelperResult onAddTask(Context context, BaseProtocolFrame nowTask, ArrayDeque<BaseProtocolFrame> taskDeque,
			NetworkHelperResult helperResult) {

		synchronized (helperResult) {
			if (helperResult.getTaskBuffer() != null) {
				BaseProtocolFrame taskBuffer = helperResult.getTaskBuffer();
				helperResult.setAddTaskPermission(false);
				if (taskBuffer instanceof RequestLogoutChild) {
					for (BaseProtocolFrame task : taskDeque) {
						task.setIsResponse(false, BaseProtocolFrame.REASON_LOGOUT);
						task.addHandler(handler);
						task.distributes();
						taskDeque.remove(task);
					}
				}
				nowTask = taskBuffer;
			}
		}

		return helperResult;
	}

	@Override
	public NetworkHelperResult onPreExecute(BaseProtocolFrame task, ArrayDeque<BaseProtocolFrame> taskDeque, Context context,
			NetworkHelperResult helperResult) {

		if (task == null) {
			return helperResult;
		}

		if (task instanceof RequestLoginChild) {
			synchronized (taskDeque) {
				for (BaseProtocolFrame taskBuff : taskDeque) {
					if (taskBuff instanceof RequestLoginChild || taskBuff instanceof RequestRenew) {
						taskDeque.remove(taskBuff);
					}
				}
			}
		}

		if (task instanceof RequestRenew) {
			synchronized (taskDeque) {
				for (BaseProtocolFrame taskBuff : taskDeque) {
					if (taskBuff instanceof RequestRenew) {
						taskDeque.remove(taskBuff);
					}
				}
			}
		}

		synchronized (taskDeque) {
			if (taskDeque.size() > ServiceCore.getTaskLimited()) {
				int flowNumber = taskDeque.size() - ServiceCore.getTaskLimited();
				for (int i = 0; i < flowNumber; ++i) {
					BaseProtocolFrame taskBuff = taskDeque.poll();
					if (task != null) {
						taskBuff.setIsResponse(false, BaseProtocolFrame.REASON_EXCEEDS_TASKNUMBER_LIMITED);
						taskBuff.addHandler(handler);
						task.distributes();
					}
				}
			}
		}

		if (task instanceof RequestBaseSidFrame) {
			if (ServiceCore.getSid() != null) {
				((RequestBaseSidFrame) task).setSid(ServiceCore.getSid());
			} else {
				Message msg = handler.obtainMessage();
				msg.what = ServiceCore.NO_SID;
				msg.sendToTarget();
				task.setReason(BaseProtocolFrame.REASON_LOGOUT);
			}
		}
		return helperResult;
	}

	@Override
	public NetworkHelperResult onExecuteExceptionOccur(Exception e, NetworkHelperResult helperResult, Context context) {
		if (!ServiceCore.isNetworkAvailable) {
			synchronized (helperResult) {
				helperResult.addOperation(NetworkHelperResult.OPERATION_WAIT);
				helperResult.setWaitReason(NetworkHelper.WAITREASON_NONETWORK);
				helperResult.setTaskResult(NetworkHelperResult.TASKRESULT_RETRANSMISSION);
			}
		}
		return helperResult;
	}

	@Override
	public NetworkHelperResult onCertificateGettingExceptionOccur(Exception e, NetworkHelperResult helperResult, Context context) {
		Message msg = handler.obtainMessage();
		msg.what = ServiceCore.CERTIFICATE_EXCEPTION;
		msg.obj = e;
		return helperResult;
	}

	@Override
	public NetworkHelperResult onPreProcess(BaseProtocolFrame task, ArrayDeque<BaseProtocolFrame> taskDeque, Context context,
			NetworkHelperResult helperResult) {
		if (task == null) {
			return helperResult;
		}

		synchronized (helperResult) {
			helperResult = super.onPreProcess(task, taskDeque, context, helperResult);

			if (helperResult.getResponseResult() != null) {
				BaseProtocolFrameProcess bpfp = parseMap.get(task.getType());
				if (bpfp == null) {
					bpfp = new AnalysisCommon();
				}
				bpfp.parse(task, helperResult.getResponseResult());
			} else {
				task.setIsResponse(false, BaseProtocolFrame.REASON_NO_RESPONSE);
			}
			helperResult.setTask(task);
		}
		Log.e("NetworkHandler.onPreProcess", "task type = " + task.getType() + ", content = " + task.toJsonString());
		return helperResult;
	}

	@Override
	public NetworkHelperResult onDoProcess(BaseProtocolFrame nowTask, ArrayDeque<BaseProtocolFrame> taskDeque, Context context,
			NetworkHelperResult helperResult) {

		if (nowTask == null) {
			return helperResult;
		}

		boolean resendOption = false;

		switch (nowTask.getReq()) {
		case BaseProtocolFrame.RESPONSE_INITIATION:
			nowTask.setIsResponse(false);
			break;
		case 4: // 当前用户未登录
			Log.e("NetworkHandler.onDoProcess", "正在进行自动重新登录");
			Preferences preference = new Preferences(context);
			if (preference.getLoginId() != null && preference.getPassword() != null) {
				synchronized (taskDeque) {
					taskDeque.addFirst(nowTask);
					Log.e("NetworkHandler.onDoProcess", "正在进行自动重新登录,已还原任务");
				}
				taskDeque.addFirst(new RequestLoginChild(preference.getLoginId(), preference.getPassword()));
				Log.e("NetworkHandler.onDoProcess", "正在进行自动重新登录,已插入重新登录任务");
				resendOption = true;
			} else {
				Log.e("NetworkHandler.onDoProcess", "正在进行自动重新登录,无法自动重新登录，缺少用户名密码，正在自动登出");
				preference.setLoginState(false);
				ServiceCore.setLoginState(false);
				boolean isResume = false;
				for (AliveBaseActivity aliveAct : ServiceCore.aliveActivityList) {
					if (aliveAct.isFrontActivity()) {
						isResume = true;
					}
					aliveAct.finish();
				}
				if (isResume) {
					Intent intent = new Intent(context, LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}

				synchronized (taskDeque) {
					for (BaseProtocolFrame task : taskDeque) {
						if (task instanceof RequestBaseSidFrame) {
							task.addHandler(handler);
							task.distributes();
							taskDeque.remove(task);
						}
					}
				}
			}

			break;
		case 6: // 需要续约
			Log.e("NetworkHandler.onDoProcess", "正在进行续约");
			synchronized (taskDeque) {
				taskDeque.addFirst(nowTask);
				Log.e("NetworkHandler.onDoProcess", "正在进行续约,已还原任务");
			}
			if (ServiceCore.getSid() != null) {
				taskDeque.addFirst(new RequestRenew(ServiceCore.getSid()));
				Log.e("NetworkHandler.onDoProcess", "正在进行续约, 已插入续约任务已还原任务");
			} else {
				Log.e("NetworkHandler.onDoProcess", "正在进行续约, 无sid 无法插入续约任务");
				Message msg = handler.obtainMessage();
				msg.what = ServiceCore.NO_SID;
				msg.sendToTarget();
			}
			resendOption = true;
			break;
		default:
			break;
		}

		synchronized (helperResult) {
			if (nowTask.isResponse() && nowTask.getReq() == BaseProtocolFrame.RESPONSE_TYPE_OKAY) {
				if (helperResult.getTask() instanceof RequestLoginChild) {
					RequestLoginChild rlc = (RequestLoginChild) nowTask;
					ServiceCore.setSid(rlc.getSid());
					ServiceCore.setLoginState(true);
				}
			}

			if (!resendOption && helperResult.getTaskResult() != NetworkHelperResult.TASKRESULT_RETRANSMISSION) {
				nowTask.addHandler(handler);
				nowTask.distributes();
			}
		}

		return helperResult;
	}
}
