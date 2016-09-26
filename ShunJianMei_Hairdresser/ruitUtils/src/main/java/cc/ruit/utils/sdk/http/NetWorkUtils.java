package cc.ruit.utils.sdk.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetWorkUtils {

	private static Context mApp = null;
	public static boolean isDebug = true; // 调试模式

	public static ExecutorService executorService = Executors
			.newCachedThreadPool();

	NetWorkUtils(Context context) {
		this.mApp = context;
	}

	/**
	 * 判断网络连接
	 * 
	 * @param inContext
	 * @return
	 */
	public static boolean isConnectInternet(Activity inContext) {
		Context incontext = inContext.getApplicationContext();
		ConnectivityManager conManager = (ConnectivityManager) incontext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;

	}

	public static String getwebVersion(Context app) {
		String typeName = null;
		ConnectivityManager cm = (ConnectivityManager) app
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			typeName = info.getTypeName();
		}
		return typeName;
	}

	private static final String NET_TYPE_WIFI = "WIFI";
	public static final int NET_NOT_AVAILABLE = 0;
	public static final int NET_WIFI = 1;
	public static final int NET_PROXY = 2;
	public static final int NET_NORMAL = 3;

	/**
	 * 网络类型
	 */
	private volatile static int networkType = NET_NOT_AVAILABLE;

	/**
	 * 判断网络连接是否可用
	 */
	public static boolean checkNetworkAvailable1(Context inContext) {
		int type = getNetworkType(inContext);
		if (type == NET_NOT_AVAILABLE) {
			return false;
		}
		return true;
	}

	/**
	 * 获取网络连接类型
	 */
	public synchronized static int getNetworkType(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			// 当前网络不可用
			networkType = NET_NOT_AVAILABLE;
		} else {
			// 如果当前是WIFI连接
			if (NET_TYPE_WIFI.equals(networkinfo.getTypeName())) {
				networkType = NET_WIFI;
			}
			// 非WIFI联网
			else {
				String proxyHost = android.net.Proxy.getDefaultHost();
				// 代理模式
				if (proxyHost != null) {
					networkType = NET_PROXY;
				}
				// 直连模式
				else {
					networkType = NET_NORMAL;
				}
			}
		}
		return networkType;
	}

}