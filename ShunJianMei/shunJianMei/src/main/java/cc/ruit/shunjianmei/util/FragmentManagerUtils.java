package cc.ruit.shunjianmei.util;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * @ClassName: FragmentManagerUtils
 * @Description: fragment管理类
 * @author: lee
 * @date: 2015年9月1日 下午7:00:55
 */
public class FragmentManagerUtils {

	public FragmentManagerUtils() {
		super();
	}
	
	public static void replaceFramgment(FragmentActivity activity,
			int containerRes, Fragment fragment, Fragment origin)
	{
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		manager.popBackStack();
		ft.add(containerRes, fragment);
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
	}

	/**
	 * @Title: add
	 * @Description: 前往某个fragment
	 * @author: lee
	 * @param activity  当前依赖的FragmentActivity
	 * @param containerRes   空间
	 * @param fragment 要前往的fragment
	 * @param saveCurrentFragment  是否保留当前的fragment(如果保留之后可以返回回来)
	 * @return: void
	 */
	public static void add(FragmentActivity activity,
			int containerRes, Fragment fragment, boolean saveCurrentFragment) {
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		if (!saveCurrentFragment) {
			ft.replace(containerRes, fragment);
		}else {
			ft.add(containerRes, fragment);
			ft.addToBackStack(null);
		}
		ft.commitAllowingStateLoss();
	}
	/**
	 * @Title: back
	 * @Description: 返回上一个fragment
	 * @author: lee
	 * @param activity 依赖的activity
	 * @param containerRes 空间
	 * @return: boolean
	 */
	public static boolean back(FragmentActivity activity, int containerRes) {
		FragmentManager manager = activity.getSupportFragmentManager();
		if (manager.getBackStackEntryCount()>1) {
			manager.popBackStack();
			return true;
		}
		return false;
	}
	
	/**
	 * @Title: celar
	 * @Description: 清空回退栈
	 * @author: lee
	 * @param containerRes
	 * @return: void
	 */
	public static void celar(FragmentActivity activity, int containerRes) {
		int backStackEntryCount = activity.getSupportFragmentManager()
				.getBackStackEntryCount();
		for (int i = 0; i < backStackEntryCount; i++) {
			activity.getSupportFragmentManager().popBackStack();
		}
	}
	
//	 /**
//	 * @Title: addOrAttach
//	 * @Description: 添加或者关联fragment（如果当前fragment已经存在过，则会直接attach，否则会add）
//	 * @author: lee
//	 * @param activity当前依赖的FragmentActivity
//	 * @param fragment要添加的fragment
//	 * @param tag fragment的tag
//	 * @return: void
//	 */
//	public static void addOrAttach(FragmentActivity activity,
//			Fragment fragment, String tag) {
//		FragmentManager manager = activity.getSupportFragmentManager();
//		FragmentTransaction ft = manager.beginTransaction();
//		detachAll(manager, ft,null);
//		if (manager.findFragmentByTag(tag) != null) {
//			ft.attach(manager.findFragmentByTag(tag));
//		} else {
//			ft.add(fragment, tag);
//		}
//		ft.commitAllowingStateLoss();
//	}
//	/**
//	 * @Title: addOrAttach
//	 * @Description: 添加或者关联fragment（如果当前fragment已经存在过，则会直接attach，否则会add）
//	 * @author: lee
//	 * @param activity当前依赖的FragmentActivity
//	 * @param fragment要添加的fragment
//	 * @param tag fragment的tag
//	 * @param butTag 次tag的fragment不被销毁
//	 * @return: void
//	 */
//	public static void addOrAttach(FragmentActivity activity,
//			Fragment fragment, String tag,String butTag) {
//		FragmentManager manager = activity.getSupportFragmentManager();
//		FragmentTransaction ft = manager.beginTransaction();
//		detachAll(manager, ft, butTag);
//		if (manager.findFragmentByTag(tag) != null) {
//			ft.attach(manager.findFragmentByTag(tag));
//		} else {
//			ft.add(fragment, tag);
//		}
//		ft.commitAllowingStateLoss();
//	}
	
	/**
	 * @Title: addOrAttach
	 * @Description: 添加或者关联fragment（如果当前fragment已经存在过，则会直接attach，否则会add）
	 * @author: lee
	 * @param activity当前依赖的FragmentActivity
	 * @param fragment要添加的fragment
	 * @param tag fragment的tag
	 * @return: void
	 */
	public static void addOrAttach(FragmentActivity activity,
			String className,int containerRes) {
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
//		detachAll(manager, ft);
		if (manager.findFragmentByTag(className) != null) {
			ft.attach(manager.findFragmentByTag(className));
		} else {
			try {
				Fragment fragment;
				fragment = getFragment(activity, className);
				ft.add(containerRes, fragment, className);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		ft.commitAllowingStateLoss();
	}
	/**
	 * @Title: detach
	 * @Description: detach一个fragment
	 * @author: lee
	 * @param activity 当前依赖的FragmentActivity
	 * @param fragment 
	 * @param tag
	 * @return: void
	 */
	public static void detachByTag(FragmentActivity activity,
			String... tag) {
		if (tag.length<=0) {
			return;
		}
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		for (int i = 0; i < tag.length; i++) {
			Fragment fragment = manager.findFragmentByTag(tag[i]);
			if (fragment!=null) {
				ft.detach(fragment);
			}
		}
		ft.commitAllowingStateLoss();
	}
	/**
	 * @Title: detach
	 * @Description: detach一个fragment
	 * @author: lee
	 * @param butTag 此tag的fragment不被detach
	 * @param activity 当前依赖的FragmentActivity
	 * @param fragment 
	 * @param tag
	 * @return: void
	 */
	private static void detachAll(FragmentManager manager,FragmentTransaction ft) {
		List<Fragment> fragments = manager.getFragments();
//		Fragment butFragment = null;
//		if (butTag!=null) {
//			butFragment = manager.findFragmentByTag(butTag);
//		}
		for (int i = 0;fragments!=null&& i < fragments.size(); i++) {
//			if (butFragment==fragments.get(i)) {
//				continue;
//			}
			ft.detach(fragments.get(i));
		}
	}
	/**
	 * @Title: replace
	 * @Description: 替换
	 * @author: lee
	 * @param activity
	 * @param className
	 * @param containerRes
	 * @return: void
	 */
	public static void replace(FragmentActivity activity,
			String className,int containerRes){
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		if (manager.findFragmentByTag(className) != null) {
			ft.replace(containerRes, manager.findFragmentByTag(className), className);
		} else {
			try {
				Fragment fragment;
				fragment = getFragment(activity, className);
				ft.replace(containerRes, fragment, className);
				ft.addToBackStack(className);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		ft.commitAllowingStateLoss();
	}
	/**
	 * @Title: getFragment
	 * @Description: 反射获取Fragment
	 * @author: lee
	 * @param activity
	 * @param className
	 * @return
	 * @return: Fragment
	 */
	public static Fragment getFragment(FragmentActivity activity,String className){
		Fragment base = null;
		try {
			Class<?> cls = Class.forName(className);
//			Constructor<?> con = cls.getConstructor(FragmentActivity.class);
			base = (Fragment) cls.newInstance();  //BatcherBase 为自定义类
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base;
	}
	
	/**
	 * @Title: show
	 * @Description: 显示fragment
	 * @author: lee
	 * @param activity
	 * @param className
	 * @return: void
	 */
	public static void show(FragmentActivity activity,String className){
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		Fragment fragment = manager.findFragmentByTag(className);
		if (fragment== null) {
			fragment = getFragment(activity, className);
		}
		ft.show(fragment);
		ft.commitAllowingStateLoss();
	}
	
	/**
	 * @Title: hide
	 * @Description: 隐藏fragment
	 * @author: lee
	 * @param activity
	 * @param className
	 * @return: void
	 */
	public static void hide(FragmentActivity activity,String... className){
		if (className.length<=0) {
			return;
		}
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		for (int i = 0; i < className.length; i++) {
			Fragment fragment = manager.findFragmentByTag(className[i]);
			if (fragment!= null) {
				ft.hide(fragment);
			}
		}
		ft.commitAllowingStateLoss();
	}
	/**
	 * @Title: show
	 * @Description: 显示fragment
	 * @author: lee
	 * @param activity
	 * @param className
	 * @return: void
	 */
	public static void show(FragmentActivity activity,Fragment fragment){
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.show(fragment);
		ft.commitAllowingStateLoss();
	}
	/**
	 * @Title: hide
	 * @Description: 隐藏fragment
	 * @author: lee
	 * @param activity
	 * @param className
	 * @return: void
	 */
	public static void hide(FragmentActivity activity,Fragment... fragment){
		if (fragment.length<=0) {
			return;
		}
		FragmentManager manager = activity.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		for (int i = 0; i < fragment.length; i++) {
			if (fragment[i]!= null) {
				ft.hide(fragment[i]);
			}
		}
		ft.commitAllowingStateLoss();
	}
}
