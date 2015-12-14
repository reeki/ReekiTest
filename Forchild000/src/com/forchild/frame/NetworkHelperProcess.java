package com.forchild.frame;

import java.util.ArrayDeque;

import android.content.Context;
import android.os.Handler;

import com.forchild.data.BaseProtocolFrame;
import com.forchild000.surface.ServiceCore;

public interface NetworkHelperProcess {

	/**
	 * 添加任务前执行的方法
	 * 
	 * @param task
	 * @param taskDeque
	 * @return
	 */
	public NetworkHelperResult onAddTask(Context context, BaseProtocolFrame nowTask, ArrayDeque<BaseProtocolFrame> taskDeque, NetworkHelperResult helperResult);

	/**
	 * 获取HttpClient过程中抛出的异常
	 * 
	 * @param e
	 * @return
	 */
	// public NetworkHelperResult onClientGettingExceptionOccur(Exception e,
	// NetworkHelperResult helperResult);

	/**
	 * 获取证书过程中抛出的异常
	 * 
	 * @param e
	 * @return
	 */
	public NetworkHelperResult onCertificateGettingExceptionOccur(Exception e, NetworkHelperResult helperResult, Context context);

	/**
	 * 在任务执行之前 执行的方法
	 * 
	 * @param task
	 * @param taskDeque
	 * @param hc
	 * @return
	 */
	public NetworkHelperResult onPreExecute(BaseProtocolFrame task, ArrayDeque<BaseProtocolFrame> taskDeque, Context context, NetworkHelperResult helperResult);

	/**
	 * 在任务执行过程中 抛出异常的处理方法
	 * 
	 * @param e
	 * @return
	 */
	public NetworkHelperResult onExecuteExceptionOccur(Exception e, NetworkHelperResult helperResult, Context context);

	/**
	 * 在任务执行之后,得到回复时执行的方法
	 * 
	 * @param hr
	 * @param task
	 * @return
	 */
	public NetworkHelperResult onPreProcess(BaseProtocolFrame task, ArrayDeque<BaseProtocolFrame> taskDeque, Context context, NetworkHelperResult helperResult);

	/**
	 * 最终执行的方法
	 * 
	 * @param preProcessResult
	 * @return
	 */
	public NetworkHelperResult onDoProcess(BaseProtocolFrame nowTask, ArrayDeque<BaseProtocolFrame> taskDeque, Context context, NetworkHelperResult helperResult);

	public NetworkHelperResult onPostCreateClient(NetworkHelperResult helperResult);

	public NetworkHelperResult onPostGetClient(NetworkHelperResult helperResult);

	public NetworkHelperResult onPostCreateCertificate(NetworkHelperResult helperResult);

	public NetworkHelperResult onPostGetCertificate(NetworkHelperResult helperResult);
	
	public void onDoSyncProcess(BaseProtocolFrame task, ServiceCore serv);

	/**
	 * 只会发生在已经尝试获取client以后。 不必再次尝试获取client。
	 * 
	 * @param helperResult
	 * @return
	 */
	public NetworkHelperResult onNullClient(NetworkHelperResult helperResult, Context context);

}