package com.oruit.widget.loadingdialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @ClassName: LoadingDailog
 * @Description: TODO 加载弹框
 * @author 李利伟
 * @date 2013-9-20 下午2:05:13
 * @version V1.0
 */
public class LoadingDailog {
	private static ProgressDialog pd;

	/**
	 * @Title: show
	 * @Description: TODO 显示加载框
	 * @author 李利伟
	 * @date 2013-9-20 下午2:06:37
	 * @param context
	 * @param msg
	 *            显示内容
	 */
	public static void show(Context context, String msg) {
		show(context, msg, true);
	}

	/**
	 * @Title: show
	 * @Description: TODO 显示加载框
	 * @author 李利伟
	 * @date 2013-9-20 下午2:19:15
	 * @param context
	 * @param msg
	 *            显示内容
	 * @param cancelable
	 *            是否可被取消
	 */
	public static void show(Context context, String msg, boolean cancelable) {
		try {
			dismiss();
			pd = new ProgressDialog(context);
			pd.setMessage(msg);
			pd.setCancelable(cancelable);
			pd.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	/**
	 * @Title: show
	 * @Description: TODO 显示加载框
	 * @author 李利伟
	 * @date 2013-9-20 下午2:07:14
	 * @param context
	 * @param rid
	 *            显示内容资源ID；
	 */
	public static void show(Context context, int rid) {
		show(context, context.getResources().getString(rid), true);
	}

	/**
	 * @Title: dismiss
	 * @Description: TODO 关闭弹框
	 * @author 李利伟
	 * @date 2013-9-20 下午2:07:46
	 */
	public static void dismiss() {
		try {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			pd = null;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			pd = null;
		}
	}
	/**
	 * @Title: isShowing
	 * @Description: 判断当前弹框是否处于弹出状态
	 * @author: lee
	 * @return
	 * @return: boolean
	 */
	public static boolean isShowing(){
		try {
			if (pd != null) {
				return pd.isShowing();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
