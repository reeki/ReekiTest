package com.forchild.frame;

import java.util.ArrayDeque;

import android.content.Context;
import android.os.Handler;

import com.forchild.data.BaseProtocolFrame;
import com.forchild000.surface.ServiceCore;

public interface NetworkHelperProcess {

	/**
	 * �������ǰִ�еķ���
	 * 
	 * @param task
	 * @param taskDeque
	 * @return
	 */
	public NetworkHelperResult onAddTask(Context context, BaseProtocolFrame nowTask, ArrayDeque<BaseProtocolFrame> taskDeque, NetworkHelperResult helperResult);

	/**
	 * ��ȡHttpClient�������׳����쳣
	 * 
	 * @param e
	 * @return
	 */
	// public NetworkHelperResult onClientGettingExceptionOccur(Exception e,
	// NetworkHelperResult helperResult);

	/**
	 * ��ȡ֤��������׳����쳣
	 * 
	 * @param e
	 * @return
	 */
	public NetworkHelperResult onCertificateGettingExceptionOccur(Exception e, NetworkHelperResult helperResult, Context context);

	/**
	 * ������ִ��֮ǰ ִ�еķ���
	 * 
	 * @param task
	 * @param taskDeque
	 * @param hc
	 * @return
	 */
	public NetworkHelperResult onPreExecute(BaseProtocolFrame task, ArrayDeque<BaseProtocolFrame> taskDeque, Context context, NetworkHelperResult helperResult);

	/**
	 * ������ִ�й����� �׳��쳣�Ĵ�����
	 * 
	 * @param e
	 * @return
	 */
	public NetworkHelperResult onExecuteExceptionOccur(Exception e, NetworkHelperResult helperResult, Context context);

	/**
	 * ������ִ��֮��,�õ��ظ�ʱִ�еķ���
	 * 
	 * @param hr
	 * @param task
	 * @return
	 */
	public NetworkHelperResult onPreProcess(BaseProtocolFrame task, ArrayDeque<BaseProtocolFrame> taskDeque, Context context, NetworkHelperResult helperResult);

	/**
	 * ����ִ�еķ���
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
	 * ֻ�ᷢ�����Ѿ����Ի�ȡclient�Ժ� �����ٴγ��Ի�ȡclient��
	 * 
	 * @param helperResult
	 * @return
	 */
	public NetworkHelperResult onNullClient(NetworkHelperResult helperResult, Context context);

}