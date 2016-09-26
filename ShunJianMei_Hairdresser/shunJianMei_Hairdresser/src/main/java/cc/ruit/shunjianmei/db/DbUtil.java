package cc.ruit.shunjianmei.db;

import java.util.List;

import android.content.Context;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
/**
 * @ClassName: DbUtil
 * @Description: 数据库操作工具类
 * @author: lee
 * @date: 2015年8月31日 下午3:00:38
 */
public class DbUtil {
	private static final String dbName = "ggz.db";
	private static final int dbVersion = 1;//数据库初始化创建

	public static void initDb(final Context ctx) {
		getDefaultDbUtils(ctx).configDebug(false);
	}
	
	public static synchronized DbUtils getDefaultDbUtils(Context ctx) {
		return DbUtils.create(ctx, dbName,dbVersion, new DbUpgradeListener() {

			@Override
			public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
				LogUtils.i("dbVersion:" + oldVersion + "/" + oldVersion);
			}
		});
	}

	public static void saveOrUpdate(Context ctx, Object obj) {
		try {
			DbUtils dbUtils = getDefaultDbUtils(ctx);
			if (NetWorkUtils.isDebug) {
				dbUtils.configDebug(false);
			}
			dbUtils.saveOrUpdate(obj);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void saveOrUpdateAll(Context ctx, List<?> obj) {
		try {
			DbUtils dbUtils = getDefaultDbUtils(ctx);
			if (NetWorkUtils.isDebug) {
				dbUtils.configDebug(false);
			}
			dbUtils.saveOrUpdateAll(obj);;
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public static void save(Context ctx, Object obj) {
		try {
			DbUtils dbUtils = getDefaultDbUtils(ctx);
			if (NetWorkUtils.isDebug) {
				dbUtils.configDebug(false);
			}
			dbUtils.save(obj);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean saveBindingId(Context ctx, Object obj) {
		try {
			DbUtils dbUtils = getDefaultDbUtils(ctx);
			if (NetWorkUtils.isDebug) {
				dbUtils.configDebug(false);
			}
			boolean ifsave = dbUtils.saveBindingId(obj);
			return ifsave;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 删除一条数据 例如：mUser
	 * @param ctx
	 * @param obj
	 * @return
	 */
	public static boolean deleteObj(Context ctx, Object obj) {
		try {
			DbUtils dbUtils = getDefaultDbUtils(ctx);
			if (NetWorkUtils.isDebug) {
				dbUtils.configDebug(false);
			}
			dbUtils.delete(obj);
			return true;
		} catch (DbException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 清空所有一个类的所有对象  例如：User.class
	 * @param ctx
	 * @param entityType
	 * @return
	 */
	public static boolean deleteAllObj(Context ctx, Class<?> entityType) {
		try {
			DbUtils dbUtils = getDefaultDbUtils(ctx);
			if (NetWorkUtils.isDebug) {
				dbUtils.configDebug(false);
			}
			dbUtils.deleteAll(entityType);

			return true;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
	/**
	 * 清空所有一个类的所有对象  例如：User.class
	 * @param ctx
	 * @param entityType
	 * @return
	 */
	public static Object findById(Context ctx, Class<?> entityType,Object idValue) {
		try {
			DbUtils dbUtils = getDefaultDbUtils(ctx);
			if (NetWorkUtils.isDebug) {
				dbUtils.configDebug(false);
			}
			return dbUtils.findById(entityType, idValue);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	
}
