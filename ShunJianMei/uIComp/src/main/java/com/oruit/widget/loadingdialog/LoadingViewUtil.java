package com.oruit.widget.loadingdialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zachary.hodge.uicomp.R;
/**
 * @ClassName: LoadingViewUtil
 * @Description: 加载页Util
 * @author: lee
 * @date: 2015年9月4日 上午11:06:42
 */
public class LoadingViewUtil {
	public ImageView iv_left,iv_right;
	public TextView tv_message;
	public RelativeLayout rl_container;
	public LoadingViewUtil(Activity activity) {
		super();
		initTitleView(activity);
	}
	public LoadingViewUtil(View view) {
		super();
		initTitleView(view);
	}
	private void initTitleView(Activity activity) {
		tv_message =(TextView) activity.findViewById(R.id.textView1);
		rl_container = (RelativeLayout) activity.findViewById(R.id.loading_view);
	}
	private void initTitleView(View view) {
		rl_container =(RelativeLayout) view.findViewById(R.id.loading_view);
		tv_message =(TextView) view.findViewById(R.id.textView1);
	}
	public void show() {
		if (rl_container!=null) {
			rl_container.setVisibility(View.VISIBLE);
		}
	}
	public void hint(){
		if (rl_container!=null) {
			rl_container.setVisibility(View.GONE);
		}
	}
}
