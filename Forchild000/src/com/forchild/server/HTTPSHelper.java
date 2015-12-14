package com.forchild.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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

import com.forchild.data.BaseProtocolFrame;
import com.forchild.frame.NetworkHelperProcess;

/***
 * @deprecated
 * @author Kang
 *
 */
public class HTTPSHelper {
	protected Context context;
	protected HttpClient httpClient;
	protected NetworkHelperProcess nhp;

	public HTTPSHelper(Context context) {
		this.context = context;
		httpClient = this.getClient();
	}

	public HTTPSHelper(Context context, NetworkHelperProcess nhp) {
		this(context);
		this.nhp = nhp;
	}

	public HTTPSHelper setNetworkHelperProcess(NetworkHelperProcess nhp) {
		this.nhp = nhp;
		return this;
	}

	public NetworkHelperProcess getNetworkHelperProcess() {
		return this.nhp;
	}

	public HttpClient getClient() {
		try {
			InputStream ins = context.getAssets().open("test_cer.cer"); // 下载的证书放到项目中的assets目录中
			CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
			Certificate cer = cerFactory.generateCertificate(ins);
			KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
			keyStore.load(null, null);
			keyStore.setCertificateEntry("trust", cer);
			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
			socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme sch = new Scheme("https", socketFactory, 443);
			httpClient = new DefaultHttpClient();
			httpClient.getConnectionManager().getSchemeRegistry().register(sch);
			return httpClient;
		} catch (KeyStoreException e) {
			e.printStackTrace();
			Log.e("HTTPSHelper KeyStoreException", e.toString());
			if (nhp != null) {
//				nhp.onClientGettingExceptionOccur(e);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("HTTPSHelper IOException", e.toString());
			if (nhp != null) {
//				nhp.onClientGettingExceptionOccur(e);
			}
		} catch (CertificateException e) {
			e.printStackTrace();
			Log.e("HTTPSHelper CertificateException", e.toString());
			if (nhp != null) {
//				nhp.onClientGettingExceptionOccur(e);
			}
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			Log.e("HTTPSHelper NoSuchProviderException", e.toString());
			if (nhp != null) {
//				nhp.onClientGettingExceptionOccur(e);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			Log.e("HTTPSHelper NoSuchAlgorithmException", e.toString());
			if (nhp != null) {
//				nhp.onClientGettingExceptionOccur(e);
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
			Log.e("HTTPSHelper KeyManagementException", e.toString());
			if (nhp != null) {
//				nhp.onClientGettingExceptionOccur(e);
			}
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
			Log.e("HTTPSHelper UnrecoverableKeyException", e.toString());
			if (nhp != null) {
//				nhp.onClientGettingExceptionOccur(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("HTTPSHelper Exception", e.toString());
			if (nhp != null) {
//				nhp.onClientGettingExceptionOccur(e);
			}
		}
		return null;
	}

	public HttpResponse execute(BaseProtocolFrame task) {
		if (httpClient == null) {
			httpClient = this.getClient();
		}
		if (httpClient == null) {
			Log.e("forolder001 HTTPSHelper", "httpClient is null");
			return null;
		}
		HttpPost httpPost = new HttpPost(task.getUrl());
		try {
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setEntity(new StringEntity(task.toJsonString(), HTTP.UTF_8));
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30 * 1000).setParameter(CoreConnectionPNames.SO_TIMEOUT, 30 * 1000);
			HttpResponse httpResponse = httpClient.execute(httpPost);

			return httpResponse;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO 添加to servicecore。java的广播 说明网络不可用
		}
		return null;
	}

	public HttpResponse execute(BaseProtocolFrame task, HttpClient httpClient) {
		this.httpClient = httpClient;
		return this.execute(task);
	}

}
