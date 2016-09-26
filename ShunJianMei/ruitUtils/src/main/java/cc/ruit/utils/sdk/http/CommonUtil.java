package cc.ruit.utils.sdk.http;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.lidroid.xutils.util.LogUtils;

/**
 * 公用工具类
 * 
 * @author zhangxuaninfo
 */
public class CommonUtil {

//	private static final String TAG = "CommonUtil";

	/**
	 * 取得sim卡号
	 * 
	 * @param context
	 * @return
	 */
	public static String getSimSeriaNumber(Context context) {

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimSerialNumber();

	}

	/**
	 * @Description: 获取设备ID
	 * @author liliwei
	 * @create 2013-12-2 上午10:10:20
	 * @updateTime 2013-12-2 上午10:10:20
	 * @param context
	 * @return
	 */
	public static String getDeviceID(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 获取应用的appKey appKey为开发者申请所得
	 * 
	 * @param context
	 * @return
	 */
	public static Object getMetaData(Context context, String flag) {

		Bundle metaData = null;
		Object appKey = null;

		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);

			if (null != ai) {

				metaData = ai.metaData;
			}
			if (null != metaData) {
				appKey = metaData.get(flag);
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return appKey;

	}
//	public static String getAppID(Context context){
//		Object obj = getMetaData(context, Constants.DataKey.JD_CLOUD_APP_ID_KEY);
//		if (null==obj) {
//			Toast.makeText(context, "没有配置云推送AppID，请检查Manifest配置项", Toast.LENGTH_LONG).show();
//			return null;
//		}
//		return obj.toString();
//	}
	/**
	 * 获取系统版本
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {

		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			return null;
		}
	}

	/**
	 * 判断服务是否正在运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isServiceRunning(Context context,Class<?> cls) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> services = am
				.getRunningServices(Integer.MAX_VALUE);
		for (RunningServiceInfo runningServiceInfo : services) {
			if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @author YangHuijun Time: 2011-3-4 下午03:25:46 Name: 取得utf-8编码字符串的长度
	 * @return: Description: 取得字符串的长度，中文3个长度，中文以外1个长度
	 */
	public static int getLength(String checkStr) {
		char[] nameChars = checkStr.toCharArray();
		int length = 0;
		for (int i = 0; i < nameChars.length; i++) {
			if (isChinese(nameChars[i])) {
				length = length + 3;
			} else {
				length = length + 1;
			}
		}
		return length;
	}

	/**
	 * @author YangHuijun Time: 2011-3-4 下午03:27:42 Name: 是否中文
	 * @return: true-中文 false-非中文 Description: 判断给定字符是否中文
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	static long previousId = System.nanoTime();

	public static synchronized String nextUniqueId() {
		long current = System.nanoTime();
		if (current == previousId) {
			try {
				Thread.sleep(0, 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			current = System.nanoTime();
		}
		previousId = current;
		return String.valueOf(current);
	}

	/**
	 * @Description: 获取包名
	 * @author liliwei
	 * @create 2013-9-7 上午10:47:27
	 * @updateTime 2013-9-7 上午10:47:27
	 * @param context
	 * @return
	 */
	public static String getPackageName(Context context) {
		String packageName = "0";
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			packageName = packInfo.packageName;

			// Log.i("test", "packageName is=="+packageName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageName;
	}

	/**
	 * 获取设备IP地址
	 * 
	 * @author dengk
	 * @CrateTime 2013-4-27
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			LogUtils.e("getLocalIpAddress err: " + ex.toString());
		}
		return null;
	}

	// String-->Date
	public static Date getDateFromString(String format, String time) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = sdf.parse(time);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获取应用名
	public static String getAppName(Context context) {
		return context.getApplicationInfo()
				.loadLabel(context.getPackageManager()).toString();
	}

	/**
	 * 检测网络状态
	 */
	public static boolean isNetworkAvailable(Context con) {
		ConnectivityManager cm = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		if (netinfo.isConnected()) {
			return true;
		}
		return false;
	}
	/**是否存在某个应用
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean hasApp(Context context, String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return false;
		}
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packageName, 0);

		} catch (NameNotFoundException e) {
			LogUtils.e( "The package of "+packageName+" Not Found");
			packageInfo = null;
		}
		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}
	}
//	/**
//	 * 获取公用服务所在的应用包名
//	 * @param context
//	 * @return
//	 */
//	public static String getPushServicePackageName(Context context){
//		return SPUtils.getString(context, Constants.SDKMsg.JD_CLOUD_SERVIC_PACKAGENAME, "");
//	}
}
