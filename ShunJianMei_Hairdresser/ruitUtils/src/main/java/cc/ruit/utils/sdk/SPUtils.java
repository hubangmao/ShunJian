package cc.ruit.utils.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @ClassName: UserSP
 * @Description: SharedPreferences的操作类，保存和获取更简单，本类单例
 * @author liliwei
 * @create 2013-8-20 下午4:51:43
 */
public class SPUtils {
	// private static UserSP instance;
	public static SharedPreferences sp;
	public static Editor ed;
	private final static String name = "SharedPreferences_ruit";// 表明
	public static void init(Context context) {
		sp = context.getApplicationContext()
				.getSharedPreferences(name, Context.MODE_PRIVATE);
		ed = sp.edit();
	}

	private SPUtils() {
		super();
	}

	/**
	 * @Description: 添加boolean
	 * @author liliwei
	 * @create 2013-8-20 下午4:49:07
	 * @updateTime 2013-8-20 下午4:49:07
	 * @param key
	 * @param value
	 * @return 添加成功返回true，否则false�?
	 */
	public static boolean putBoolean(String key, boolean value) {
		try {
			ed.putBoolean(key, value);
			ed.commit();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * @Description: TODO
	 * @author liliwei
	 * @create 2013-8-20 下午4:55:51
	 * @updateTime 2013-8-20 下午4:55:51
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean getBoolean(String key, boolean value) {
		return sp.getBoolean(key, value);
	}

	public static boolean putFloat(String key, float value) {
		try {
			ed.putFloat(key, value);
			ed.commit();

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;

	}

	public static float getFloat(String key, float value) {
		return sp.getFloat(key, value);

	}

	public static boolean putInt(String key, int value) {
		try {

			ed.putInt(key, value);
			ed.commit();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;

	}

	public static int getInt(String key, int value) {
		return sp.getInt(key, value);

	}

	public static boolean putLong(String key, Long value) {
		try {
			ed.putFloat(key, value);
			ed.commit();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;

	}

	public static long getLong(String key, Long value) {
		return sp.getLong(key, value);

	}

	public static boolean putString(String key, String value) {
		try {
			ed.putString(key, value);
			ed.commit();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;

	}

	public static String getString(String key, String value) {
		return sp.getString(key, value);

	}

	public static void removeShare(String key) {
		ed.remove(key);
		ed.commit();
	}

}
