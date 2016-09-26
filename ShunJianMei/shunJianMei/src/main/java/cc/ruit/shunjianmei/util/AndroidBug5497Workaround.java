package cc.ruit.shunjianmei.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ScrollView;
import cc.ruit.shunjianmei.R;

import com.lidroid.xutils.util.LogUtils;
/**
 *  
 * @author tianjm
 * 解决安卓全屏“FLAG_FULLSCREEN”状态下“adjustResize”失效，全屏状态下EditText的输入框被软键盘挡住的问题
 *
 */
public class AndroidBug5497Workaround {
	private static AndroidBug5497Workaround instance;
	private AndroidBug5497Workaround() {
		super();
	}

	public static AndroidBug5497Workaround getInstance() {
		if(instance == null){
			return new AndroidBug5497Workaround();
		}else{
			return instance;
		}
		
	}
	/**
	 * 
	 * @Title: assistActivity
	 * @Description: activity直接调用方法直接全局调整
	 * @author: lee
	 * @param activity
	 * @return: void
	 */
	public static void assistActivity(Activity activity) {
		getInstance().assistResizeActivity(activity);
	}
   
    private View mChildOfContent;  
    private int usableHeightPrevious;  
    private FrameLayout.LayoutParams frameLayoutParams;  
    
   
    /**
     * 
     * @Title: resizeActivity
     * @Description: activity的大小重新计算，解决安卓全屏“FLAG_FULLSCREEN”状态下“adjustResize”失效
     * @author: lee
     * @param activity 需要调整的activity
     * @return: void
     */
	private void assistResizeActivity(Activity activity) {  
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);  
        content.setBackgroundColor(activity.getResources().getColor(R.color.white));
        mChildOfContent = content.getChildAt(0);  
        mChildOfContent.setBackgroundColor(activity.getResources().getColor(R.color.white));
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();  
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {  
            public void onGlobalLayout() {  
                possiblyResizeChildOfContent(mChildOfContent,frameLayoutParams);  
            }  
        });  
    }  
	 /**
     * 
     * @Title: resizeActivity
     * @Description: activity的大小重新计算，解决安卓全屏“FLAG_FULLSCREEN”状态下“adjustResize”失效
     * @author: lee
     * @param activity 需要调整的activity
     * @return: void
     */
	public void assistResizeActivity(Activity activity,final ScrollView view) {          
        frameLayoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();  
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {  
            public void onGlobalLayout() {  
                possiblyResizeChildOfContent(view,frameLayoutParams);  
            }  
        });  
    }  
    /**
     * 
     * @Title: possiblyResizeChildOfContent
     * @Description: 重新resize view大小
     * @author: lee
     * @param contentView 需要调整的view
     * @param oldFrameLayoutParams 调整前的LayoutParams
     * @return: void
     */
    public void possiblyResizeChildOfContent(View contentView, LayoutParams oldFrameLayoutParams) {
    	LogUtils.i("possiblyResizeChildOfContent");
        int usableHeightNow = computeUsableHeight(contentView);//得到指定view的可现实区域高度
        if (usableHeightNow != usableHeightPrevious) {  
            int usableHeightSansKeyboard = contentView.getRootView().getHeight(); //得到view的原始高度 
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;//得到原有高度和现在可显示高度差
            if (heightDifference > (usableHeightSansKeyboard/4)) {//高度差大于4分之一，认为键盘弹出
                // 这种情况 键盘就是出来了
                oldFrameLayoutParams.height = usableHeightSansKeyboard - heightDifference;  
            } else {  
                // 这种情况就是关闭了 
                oldFrameLayoutParams.height = usableHeightSansKeyboard;  
            }
            contentView.requestLayout();//重新适应自身大小 
            if (contentView instanceof ScrollView) {
//				((ScrollView) contentView).fullScroll(ScrollView.FOCUS_DOWN);
            	((ScrollView) contentView).scrollTo(0, usableHeightNow);
			}
            usableHeightPrevious = usableHeightNow;  
        }  
    }
    /**
     * 
     * @Title: computeUsableHeight
     * @Description: 得到指定view的可现实区域高度
     * @author: lee
     * @param view
     * @return
     * @return: int
     */
    private int computeUsableHeight(View view) {  
        Rect r = new Rect();  
        view.getWindowVisibleDisplayFrame(r);//得到可现实区域大小
        return (r.bottom - r.top);  
    }  
  
}  
