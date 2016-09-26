package cc.ruit.utils.sdk;

import android.content.Context;
import android.widget.Toast;
/**
 * Toast工具类
 */
public class ToastUtils {
	
	public static Toast myToast = null;
	public static Context mContext;
	public static Toast getInstace(Context context){
		if(myToast == null){
			myToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		}
		mContext = context;
		return myToast;
	}
	/**
	 * 短时间显示指定内容  字符串型，不指定位置
	 * @param msg
	 */
	public static void showShort(String msg) {
		if(myToast == null&&mContext!=null){
			myToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		}
		if (myToast != null) {
			myToast.setDuration(Toast.LENGTH_SHORT);
			myToast.setText(msg);
			myToast.show();
		}
	}
	
	/**
	 * 短时间显示指定内容  字符串型，指定位置
	 * @param msg  内容
	 * @param gravity   位置
	 */
	public static  void showShort(String msg,int gravity){
		if(myToast == null&&mContext!=null){
			myToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		}
		if (myToast != null) {
			myToast.setDuration(Toast.LENGTH_SHORT);
			myToast.setText(msg);
			myToast.setGravity(gravity, 0, 0);
			myToast.show();
		}
	}
	
	/**
	 * 短时间显示指定内容  字符串型，不指定位置
	 * @param msg
	 */
	public static void showLong(String msg) {
		if(myToast == null&&mContext!=null){
			myToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		}
		if (myToast != null) {
			myToast.setDuration(Toast.LENGTH_LONG);
			myToast.setText(msg);
			myToast.show();
		}
	}
	
	/**
	 * 短时间显示指定内容  字符串型，指定位置
	 * @param msg  内容
	 * @param gravity   位置
	 */
	public static void showLong(String msg,int gravity) {
		if(myToast == null&&mContext!=null){
			myToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		}
		if (myToast != null) {
			myToast.setDuration(Toast.LENGTH_LONG);
			myToast.setText(msg);
			myToast.setGravity(gravity, 0, 0);
			myToast.show();
		}
	}
	
	/**
	 * 显示int型内容  短时间
	 * @param msg_id
	 */
	public static void showShort(int msg_id) {
		if(myToast == null&&mContext!=null){
			myToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		}
		if (myToast != null) {
			myToast.setDuration(Toast.LENGTH_LONG);
			myToast.setText(msg_id);
			myToast.show();
		}
	}
	
	/**
	 * 显示int型内容  长时间
	 * @param msg_id
	 */
	public static void showLong(int msg_id) {
		if(myToast == null&&mContext!=null){
			myToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		}
		if (myToast != null) {
			myToast.setDuration(Toast.LENGTH_LONG);
			myToast.setText(msg_id);
			myToast.show();
		}
	}
}
