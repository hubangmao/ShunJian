package cc.ruit.shunjianmei.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import cc.ruit.utils.sdk.http.NetWorkUtils;

public class Util {

//
//	/**
//	 * 
//	 * @Title: getIpAddress
//	 * @Description: 获取手机的IP地址
//	 * @author: Johnny
//	 * @return
//	 * @return: String
//	 */
//	private String getIpAddress() {
//		
//		String ip = "";
//		// 如果当前网络连接类型为WIFI连接
//		if (NetWorkUtils.getNetworkType(activity) == 1) {
//			ip = getWIFIIPAddress();
//		} else{
//			ip = getNetIPAddress();
//		}
//		return ip;
//	}
//
//	/**
//	 * 
//	 * @Title: getWIFIIPAddress
//	 * @Description:获取本机WIFI IP
//	 * @author: Johnny
//	 * @return: void
//	 */
//	private String getWIFIIPAddress() {
//
//		WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
//		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//		// 获取32位整型IP地址
//		int ipAddress = wifiInfo.getIpAddress();
//
//		// 返回整型地址转换成“*.*.*.*”地址
//		return String.format("%d.%d.%d.%d", (ipAddress & 0xff),
//				(ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
//				(ipAddress >> 24 & 0xff));
//	}

}
