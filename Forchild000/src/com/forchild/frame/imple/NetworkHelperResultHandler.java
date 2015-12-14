package com.forchild.frame.imple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.forchild.data.BaseProtocolFrame;
import com.forchild.frame.NetworkHelperProcess;
import com.forchild.frame.NetworkHelperResult;
import com.forchild000.surface.ServiceCore;

public abstract class NetworkHelperResultHandler implements NetworkHelperProcess {

	@Override
	public NetworkHelperResult onPreProcess(BaseProtocolFrame task, ArrayDeque<BaseProtocolFrame> taskDeque, Context context, NetworkHelperResult helperResult) {
		if (helperResult.getHttpResponse() == null) {
			Log.e("NetworkHelperResultHandler.onPreProcess", " httpResponse is null");
			return helperResult;
		}
		InputStream is = null;
		try {
			is = helperResult.getHttpResponse().getEntity().getContent();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String result = new String();
		String line = new String();
		if (is != null) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					result = result + line;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.e("NetworkHelperResultHandler.onPreProcess", " result:" + result + ", request:" + task.toJsonString());
			JSONObject json = null;
			try {
				json = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
				json = null;
			}
			return helperResult.setResponseResult(json);
		} else {
			Log.e("NetworkHelper:", "InputStreamReader is null");
		}
		return helperResult;
	}

	@Override
	public NetworkHelperResult onPreExecute(BaseProtocolFrame task, ArrayDeque<BaseProtocolFrame> taskDeque, Context context,
			NetworkHelperResult helperResult) {
		return helperResult;
	}

	@Override
	public NetworkHelperResult onExecuteExceptionOccur(Exception e, NetworkHelperResult helperResult, Context context) {
		return helperResult;
	}

	@Override
	public NetworkHelperResult onDoProcess(BaseProtocolFrame nowTask, ArrayDeque<BaseProtocolFrame> taskDeque, Context context, NetworkHelperResult helperResult) {
		return helperResult;
	}

	@Override
	public NetworkHelperResult onCertificateGettingExceptionOccur(Exception e, NetworkHelperResult helperResult, Context context) {
		return helperResult;
	}

	@Override
	public NetworkHelperResult onPostCreateClient(NetworkHelperResult helperResult) {
		return helperResult;
	}

	@Override
	public NetworkHelperResult onPostGetClient(NetworkHelperResult helperResult) {
		return helperResult;
	}

	@Override
	public NetworkHelperResult onPostCreateCertificate(NetworkHelperResult helperResult) {
		return helperResult;
	}

	@Override
	public NetworkHelperResult onPostGetCertificate(NetworkHelperResult helperResult) {
		return helperResult;
	}

	@Override
	public NetworkHelperResult onNullClient(NetworkHelperResult helperResult, Context context) {
		return helperResult;
	}

	@Override
	public void onDoSyncProcess(BaseProtocolFrame task, ServiceCore serv) {
		// TODO Auto-generated method stub

	}

	@Override
	public NetworkHelperResult onAddTask(Context context, BaseProtocolFrame nowTask, ArrayDeque<BaseProtocolFrame> taskDeque, NetworkHelperResult helperResult) {
		return helperResult;
	}


}
