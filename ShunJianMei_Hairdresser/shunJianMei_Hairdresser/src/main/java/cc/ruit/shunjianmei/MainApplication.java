package cc.ruit.shunjianmei;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.db.DbUtil;
import cc.ruit.utils.sdk.SPUtils;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.file.FileConstant;
import cc.ruit.utils.sdk.file.FileHelper;
import cc.ruit.utils.sdk.http.FileUploadHttpHelper;
import cc.ruit.utils.sdk.http.HttpHelper;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
/**
 * @ClassName: MainApplication
 * @Description: 程序入口
 * @author: lee
 * @date: 2015年8月31日 下午3:00:15
 */
public class MainApplication extends Application {
	/**
	 * 
	 */
	public float density;

	/**
	 * 
	 */
	public String deviceId;
	/**
	 * 屏宽
	 */
	public int screenWidth;

	/**
	 * 屏高
	 */
	public int screenHeight;
	/**
	 * 全局资源对象
	 */
	public Resources res;
	/**
	 * 客户端是否是首次启动
	 */
	public boolean isFirstStartUp = true;

	/**
	 * APP对象
	 */
	private static MainApplication instance;

	public static MainApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		initJPush();
		// Toast初始化
		ToastUtils.getInstace(this);
		init(null, null);
//		initYouMi();
		MobclickAgent.openActivityDurationTrack(false);//关闭默认的友盟页面统计
		// initRongLianVoip();
		// mLocationClient = new LocationClient(this.getApplicationContext());
	}
	/**
	 * @Title: init
	 * @Description: 初始化程序相关内容
	 * @author: lee
	 * @param activity
	 * @param handler
	 * @return: void
	 */
	public void init(Activity activity, Handler handler) {
		res = getResources();

		FileConstant constant = new FileConstant(
				"/data/data/cc.ruit.shunjianmeihairdresser/first/", "/shunjianmeihairdresser",
				"/shunjianmeihairdresser/download/", "/shunjianmeihairdresser/temp/",
				"/data/data/cc.ruit.shunjianmeihairdresser/temp/", "",
				"/shunjianmeihairdresser/upload/", "", "");

		initSystemParams(activity);
		constant.isFirstStartUp = !FileHelper.isFileExist(new File(
				FileConstant.RES_FIRST_DATA));
		isFirstStartUp = constant.isFirstStartUp;
		initFileSystem(constant);
		initUserSystem();
		SPUtils.init(instance);
		DbUtil.initDb(instance);
		HttpHelper.init(instance);
		FileUploadHttpHelper.init(instance);

		if (handler != null) {
			// 同时删除 upload文件夹
			handler.sendEmptyMessageDelayed(0, 1000);
		}

	}
	/**
	 * @Title: initJPush
	 * @Description: 推送初始化
	 * @author: lee
	 * @return: void
	 */
	private void initJPush() {
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
	}
	/**
	 * 系统参数初始化
	 * 
	 * @param activity
	 */
	public void initSystemParams(Activity activity) {
		if (activity == null) {
			return;
		}
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		density = dm.density;

		TelephonyManager tm = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		deviceId = tm.getDeviceId();

	}

	/**
	 * 文件系统初始化
	 */
	@SuppressWarnings("static-access")
	public void initFileSystem(FileConstant constant) {
		constant.sdCardIsExist = FileHelper.isExistSD(instance);
		FileHelper.makeDir();
	}

	/**
	 * 用户系统初始化
	 */
	public void initUserSystem() {

	}
	
//	/**
//	 * @Title: initYouMi
//	 * @Description: 初始化有米
//	 * @author: lee
//	 * @return: void
//	 */
//	private void initYouMi() {
//		// 有米Android SDK v4.10之后的sdk还需要配置下面代码，以告诉sdk使用了服务器回调
//		AdManager.getInstance(this.getApplicationContext()).init(Constant.YOUMI_APPID,Constant.YOUMI_APPSECRET,false);
//		OffersManager.setUsingServerCallBack(true);
//	}
	
	//-------------------------类管理----begin------------------------
	public static ArrayList<Activity> activities = new ArrayList<Activity>();
	public static ArrayList<FragmentActivity> factivities = new ArrayList<FragmentActivity>();
	/**
	 * 添加Activity
	 * @param activity
	 */
	public static void addActivity(Object activity) {
		if (activity instanceof Activity) {
			activities.add((Activity) activity);
		} else if (activity instanceof FragmentActivity) {
			factivities.add((FragmentActivity) activity);
		} 
	}
	/**
	 * 移除Activity
	 * @param activity
	 */
	public static void removeActivity(Object activity) {
		if (activity instanceof Activity) {
			activities.remove((Activity)activity);
		} else if (activity instanceof FragmentActivity) {
			factivities.remove((FragmentActivity) activity);
		} 
	}

	/**
	 * 关闭Activity
	 */
	public static void closeAllActivity() {
		try {
			for (int i = 0; i < activities.size(); i++) {
				if (activities.get(i) != null) {
					activities.get(i).finish();
				}
			}
			for (int i = 0; i < factivities.size(); i++) {
				if (factivities.get(i) != null) {
					factivities.get(i).finish();
				}
			}
			
		} catch (Exception e) {
		}
	}
	public static ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	public static void addFragment(Object fragment){
		if (fragment instanceof Fragment) {
			fragments.add((Fragment) fragment);
		}
	}
	public static void removeFragment(Object fragment){
		if (fragment instanceof Fragment) {
			fragments.remove((Fragment) fragment);
			
		}
	}
	public static void colseFragment(){
		for(int i = 0;i < fragments.size();i++){
			fragments.get(i).onDestroy();
		}
	}
	//---------------------类管理end

	/**
	 * 判断当前应用程序处于前台还是后台
	 */
	public static boolean isApplicationFront() {
		try {
			ActivityManager am = (ActivityManager) MainApplication.getInstance().getApplicationContext()
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> tasks = am.getRunningTasks(1);
			if (!tasks.isEmpty()) {
				ComponentName topActivity = tasks.get(0).topActivity;
				if (topActivity.getPackageName().equals( MainApplication.getInstance().getApplicationContext().getPackageName())) {
					return true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

}
