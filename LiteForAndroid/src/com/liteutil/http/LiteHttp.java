package com.liteutil.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.liteutil.http.task.TaskControllerImpl;

import android.content.Context;

/**
 * @ClassName: 
 * @Description: 
 * @author: 
 * @date: 
 */
public class LiteHttp {

	private static HttpManagerImpl httpManagerImpl;
	private static TaskControllerImpl taskController;
	private static Context mContext;

	public LiteHttp(Context context) {
		mContext = context;
		httpManagerImpl = HttpManagerImpl.registerInstance();
		taskController = TaskControllerImpl.registerInstance();
		// 默认信任所有https域名
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
	}
	
	public static Context getContext(){
		return mContext;
	}

	public static TaskControllerImpl task() {
		if (taskController == null) {
			taskController = TaskControllerImpl.registerInstance();
		}
		return taskController;
	}

	public static HttpManagerImpl http() {
		if (httpManagerImpl == null) {
			httpManagerImpl = HttpManagerImpl.registerInstance();
		}
		return httpManagerImpl;
	}
}
