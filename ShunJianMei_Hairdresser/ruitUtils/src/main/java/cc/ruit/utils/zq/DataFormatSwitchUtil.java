package cc.ruit.utils.zq;

import android.text.TextUtils;

/**
 * 
 * @ClassName: DataFormatSwitchUtil
 * @Description: 数据类型转换工具
 * @author: Johnny
 * @date: 2015年12月10日 下午4:01:08
 */
public class DataFormatSwitchUtil {
	
	public static int string2int(String str) {
		if (str == null || TextUtils.isEmpty(str.trim())) {
			return 0;
		} else {
			return Integer.parseInt(str);
		}
	}
	
	public static long string2long(String str) {
		if (str == null || TextUtils.isEmpty(str.trim())) {
			return (long)0;
		} else {
			return Long.parseLong(str);
		}
	}
	
	public static Float string2float(String str) {
		if (str == null || TextUtils.isEmpty(str.trim())) {
			return (float) 0.00;
		} else {
			return Float.parseFloat(str);
		}
	}

	public static Double string2Double(String str) {
		if (str == null || TextUtils.isEmpty(str.trim())) {
			return (Double) 0.00;
		} else {
			return Double.parseDouble(str);
		}
	}
	
	
	
}
