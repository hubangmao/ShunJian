package cc.ruit.shunjianmei.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
/**
 * 
 * @ClassName: ScreenUtils
 * @Description: TODO
 * @author: Johnny
 * @date: 2015年10月16日 下午4:24:07
 */
public class ScreenUtils {
	/**
	 * @Title: getScreenWidth
	 * @Description: TODO
	 * @author: Johnny
	 * @param context
	 * @return
	 * @return: int
	 */
	public static int getScreenWidth(Context context){
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		return width;
	}
	 /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    } 
    
    /**
	 * 获取设备宽度
	 * 
	 * @return
	 */
	public static int getDeviceWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(mDisplayMetrics);
		int w = mDisplayMetrics.widthPixels;
		return w;
	}

	/**
	 * 获取设备高度
	 * 
	 * @return
	 */
	public static int getDeviceHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(mDisplayMetrics);
		int h = mDisplayMetrics.heightPixels;
		return h;
	}
}
