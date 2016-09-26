package cc.ruit.shunjianmei.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import cc.ruit.shunjianmei.MainApplication;
import cc.ruit.shunjianmei.R;
/**
 * @ClassName: BaseFragment
 * @Description: fragment处理基类
 * @author: tianjm lee 
 * @date: 2015年9月1日 上午10:27:00
 */
public abstract class BaseFragment extends Fragment {
	public View view;
	public FragmentActivity activity;//getActivity()方法会报null，使用构造方式获取这个
	public int statusHeight;//状态栏高度
	private boolean isEnable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		statusHeight = getStatusHeight(getActivity());
//		MainApplication.fragments.add(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view == null){
			view = initView(inflater);
		}
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		ViewGroup parent = (ViewGroup) view.getParent();
		if (null != parent) {
			parent.removeView(view);
		}
//		initTransparentTitle();
		return view;
	}
	/**
	 * @Title: initTransparentTitle
	 * @Description: 处理顶部状态栏背景色
	 * @author: lee
	 * @return: void
	 */
	public void initTransparentTitle() {
		if (!isEnable) {
			return;
		}
		// 自己写的title，上面加一个textview
		View statusTitle = view.findViewById(R.id.title_top_view);
		if (statusTitle==null) {
			return;
		}
		statusTitle.getLayoutParams().height = 0;
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			((Activity) activity).getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, statusHeight);
			statusTitle.setLayoutParams(lParams);
		}
	}
	/**
	 * @Title: initView
	 * @Description: onCreatView时需要返回的view
	 * @author: lee
	 * @param inflater
	 * @return: View fragment加载的view
	 */
	public abstract View initView(LayoutInflater inflater);
	
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		if (getView() != null) {
			getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
		}
		super.setMenuVisibility(menuVisible);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
//		MainApplication.fragments.remove(this);
	}
	
	public void onBackPressed() {
		
	}
	
	/**
	 * @Title: getStatusHeight
	 * @Description: 获取状态栏高度
	 * @author: lee
	 * @param activity
	 * @return: int > 0 success; <= 0 fail
	 */
	public static int getStatusHeight(Activity activity){
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
    	return statusHeight;
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
}
